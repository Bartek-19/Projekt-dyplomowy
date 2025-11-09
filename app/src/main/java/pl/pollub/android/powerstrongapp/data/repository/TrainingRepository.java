package pl.pollub.android.powerstrongapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;

import pl.pollub.android.powerstrongapp.api.TrainingService;
import pl.pollub.android.powerstrongapp.api.model.ExecutedSetDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingDayDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.data.local.AppDatabase;
import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao;
import pl.pollub.android.powerstrongapp.data.local.dao.PlannedExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao; // Poprawna nazwa
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.utils.DtoMapper;

public class TrainingRepository {

    // Zmieniona nazwa DAO
    private final TrainingPlanAndDayDao trainingPlanAndDayDao;
    private final PlannedExerciseDao plannedExerciseDao;
    private final ExecutedSetDao executedSetDao;
    private final TrainingService trainingService;

    private static final int NUMBER_OF_THREADS = 4;
    private static final java.util.concurrent.ExecutorService networkExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public TrainingRepository(Application application, TrainingService trainingService) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.trainingPlanAndDayDao = db.trainingPlanAndDayDao(); // Zmieniona inicjalizacja
        this.plannedExerciseDao = db.plannedExerciseDao();
        this.executedSetDao = db.executedSetDao();
        this.trainingService = trainingService;
    }

    /**
     * ZWRACA DANE Z ROOM i inicjuje synchronizację z serwerem w tle.
     * @param dayId ID dnia treningowego, dla którego chcemy ćwiczenia
     * @return LiveData z listą zaplanowanych ćwiczeń dla tego dnia
     */
    public LiveData<List<PlannedExerciseEntity>> getPlannedExercisesForDay(int dayId) {
        return plannedExerciseDao.getPlannedExercisesForDay(dayId);
    }

    /**
     * ZWRACA dane o aktywnym harmonogramie z Room (SSOT).
     * @return LiveData z aktywnym planem
     */
    public LiveData<TrainingPlanEntity> getActiveTrainingPlan() {
        return trainingPlanAndDayDao.getActiveTrainingPlan();
    }

    /**
     * Krok 1 zapisu: Zapisuje wyniki serii do lokalnego Outbox (ExecutedSetEntity).
     * @param dtos Lista ExecutedSetDto z ExerciseExecutionFragment
     */
    public void saveExecutedSetsLocally(List<ExecutedSetDto> dtos) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long timestamp = Instant.now().toEpochMilli();

            for (ExecutedSetDto dto : dtos) {
                ExecutedSetEntity entity = DtoMapper.toExecutedSetEntity(dto, timestamp);
                executedSetDao.insert(entity);
            }

            // Po zapisaniu uruchamiamy natychmiast synchronizację z serwerem
            synchronizeExecutedSets();
        });
    }

    /**
     * Krok 2 zapisu: Wysyła niesynchronizowane serie na serwer (Outbox logic).
     */
    public void synchronizeExecutedSets() {
        networkExecutor.execute(() -> {
            List<ExecutedSetEntity> unsyncedSets = executedSetDao.getUnsyncedSets();

            if (unsyncedSets.isEmpty()) return;

            List<ExecutedSetDto> dtosToSend = DtoMapper.toExecutedSetDtoList(unsyncedSets);

            trainingService.sendExecutedSets(dtosToSend).enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            // Oznaczamy i czyścimy zsynchronizowane rekordy
                            for (ExecutedSetEntity entity : unsyncedSets) {
                                executedSetDao.markAsSynced(entity.getLocalId());
                            }
                            executedSetDao.deleteSyncedSets();
                        });
                    }
                    // W przeciwnym razie rekordy pozostają niezmienione w Outbox
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    // Brak internetu - rekordy zostają w Outbox
                }
            });
        });
    }

    /**
     * Synchronizuje cały aktywny plan treningowy z serwera do Room (SSOT).
     */
    public void syncFullTrainingPlan(int userId) {
        networkExecutor.execute(() -> {
            trainingService.getFullActiveTrainingPlan(userId).enqueue(new retrofit2.Callback<TrainingPlanFullDto>() {
                @Override
                public void onResponse(retrofit2.Call<TrainingPlanFullDto> call, retrofit2.Response<TrainingPlanFullDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        TrainingPlanFullDto fullPlanDto = response.body();
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            // 1. Zapis TrainingPlanEntity (zawiera teraz EffortType)
                            TrainingPlanEntity planEntity = DtoMapper.toTrainingPlanEntity(fullPlanDto);
                            trainingPlanAndDayDao.insertTrainingPlan(planEntity);

                            // 2. Zapis TrainingDayEntity
                            List<TrainingDayEntity> dayEntities = DtoMapper.toTrainingDayEntityList(fullPlanDto.getTrainingDays());
                            trainingPlanAndDayDao.insertTrainingDays(dayEntities);

                            // 3. Zapis PlannedExerciseEntity
                            // Musimy usunąć stare zaplanowane ćwiczenia, aby uniknąć duplikatów
                            // w przypadku modyfikacji planu przez serwer.
                            plannedExerciseDao.deleteAll();

                            for (TrainingDayDto dayDto : fullPlanDto.getTrainingDays()) {
                                List<PlannedExerciseEntity> exerciseEntities = DtoMapper.toPlannedExerciseEntityList(
                                        dayDto.getPlannedExercises(), dayDto.getId() /* trainingDayId */
                                );
                                plannedExerciseDao.insertAll(exerciseEntities);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<TrainingPlanFullDto> call, Throwable t) {
                    // Brak internetu - polegamy na danych z Room
                }
            });
        });
    }
}