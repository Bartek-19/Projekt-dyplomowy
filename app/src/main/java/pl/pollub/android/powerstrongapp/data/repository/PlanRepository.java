package pl.pollub.android.powerstrongapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pollub.android.powerstrongapp.api.model.PlanCompletionRequestDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingDayDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.api.service.TrainingService;
import pl.pollub.android.powerstrongapp.data.local.AppDatabase;
import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao;
import pl.pollub.android.powerstrongapp.data.local.dao.PlannedExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.utils.AuthManager;
import pl.pollub.android.powerstrongapp.utils.DtoMapper;
import pl.pollub.android.powerstrongapp.utils.TrainingCycleCalculator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanRepository {

    private final TrainingPlanAndDayDao trainingPlanAndDayDao;
    private final PlannedExerciseDao plannedExerciseDao;
    private final ExecutedSetDao executedSetDao;
    private final TrainingService trainingService;
    private final AuthManager authManager;
    private final TrainingCycleCalculator calculator;
    private static final ExecutorService networkExecutor = Executors.newFixedThreadPool(4);
    private static final ExecutorService diskIO = Executors.newSingleThreadExecutor();

    public PlanRepository(Application application, TrainingService trainingService) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.trainingPlanAndDayDao = db.trainingPlanAndDayDao();
        this.plannedExerciseDao = db.plannedExerciseDao();
        this.executedSetDao = db.executedSetDao();
        this.trainingService = trainingService;
        this.authManager = AuthManager.getInstance(application);
        this.calculator = new TrainingCycleCalculator();
    }
    public LiveData<TrainingPlanEntity> getActiveTrainingPlan() {
        return trainingPlanAndDayDao.getActiveTrainingPlan();
    }
    public LiveData<List<TrainingDayEntity>> getDaysForPlan(int planId) {
        return trainingPlanAndDayDao.getDaysForPlan(planId);
    }
    public LiveData<List<PlannedExerciseEntity>> getPlannedExercisesForDay(int dayId) {
        return plannedExerciseDao.getPlannedExercisesForDay(dayId);
    }
    public LiveData<Integer> getRemainingExercisesCount(int dayId) {
        return plannedExerciseDao.getExercisesCountForDay(dayId);
    }
    public LiveData<Integer> getTotalScheduledSessions(int planId) {
        MediatorLiveData<Integer> total = new MediatorLiveData<>();

        // Zróbmy prosty LiveData, który bierze Dni szablonu
        LiveData<List<TrainingDayEntity>> daysLive = trainingPlanAndDayDao.getDaysForPlan(planId);
        LiveData<TrainingPlanEntity> planLive = trainingPlanAndDayDao.getActiveTrainingPlan();

        // Mediator łączy oba źródła
        MediatorLiveData<Object> combinedSource = new MediatorLiveData<>();
        combinedSource.addSource(planLive, plan -> combinedSource.setValue(new Object()));
        combinedSource.addSource(daysLive, days -> combinedSource.setValue(new Object()));

        total.addSource(combinedSource, ignored -> {
            TrainingPlanEntity plan = planLive.getValue();
            List<TrainingDayEntity> days = daysLive.getValue();

            if (plan == null || days == null || days.isEmpty()) {
                total.setValue(0);
                return;
            }

            // --- Logika Obliczania Całkowitej Liczby Treningów ---

            // 1. Liczba unikalnych dni w szablonie (np. Pon, Śr, Pt = 3)
            int daysInTemplate = days.size();

            // 2. Długość planu w tygodniach
            int durationWeeks = plan.getDurationOfCycle();
            if (durationWeeks <= 0) durationWeeks = 1;

            // 3. Jak długi jest szablon w tygodniach? (np. A/B/C na 3 tygodnie)
            int maxTemplateWeek = 0;
            for (TrainingDayEntity d : days) {
                if (d.getWeekNumber() > maxTemplateWeek) maxTemplateWeek = d.getWeekNumber();
            }
            if (maxTemplateWeek == 0) maxTemplateWeek = 1;

            // 4. Obliczenie powtórzeń cyklu i całkowita liczba sesji
            double loops = (double) durationWeeks / maxTemplateWeek;
            int totalSessions = (int) (daysInTemplate * loops);

            total.setValue(totalSessions);
        });

        return total;
    }
    public LiveData<TrainingDayEntity> getNextTrainingDayInQueue() {
        MediatorLiveData<TrainingDayEntity> result = new MediatorLiveData<>();
        LiveData<TrainingPlanEntity> planLive = trainingPlanAndDayDao.getActiveTrainingPlan();

        result.addSource(planLive, plan -> {
            if (plan == null) { result.setValue(null); return; }
            LiveData<List<TrainingDayEntity>> daysLive = trainingPlanAndDayDao.getDaysForPlan(plan.getId());
            result.addSource(daysLive, days -> {
                if (days == null || days.isEmpty()) { result.setValue(null); return; }
                diskIO.execute(() -> {
                    int completedSessions = executedSetDao.getCompletedSessionsCountSync(plan.getId());
                    TrainingDayEntity nextDay = calculator.determineNextDayEntity(plan, days, completedSessions);
                    result.postValue(nextDay);
                });
            });
        });
        return result;
    }
    public LiveData<Long> getCalculatedNextTrainingDateTimestamp() {
        MediatorLiveData<Long> result = new MediatorLiveData<>();
        LiveData<TrainingPlanEntity> planLive = trainingPlanAndDayDao.getActiveTrainingPlan();

        result.addSource(planLive, plan -> {
            if (plan == null) { result.setValue(null); return; }
            diskIO.execute(() -> {
                List<TrainingDayEntity> days = trainingPlanAndDayDao.getDaysForPlanSync(plan.getId());
                int completedSessions = executedSetDao.getCompletedSessionsCountSync(plan.getId());
                Long nextDate = calculator.calculateNextTrainingDate(plan, days, completedSessions);
                result.postValue(nextDate);
            });
        });
        return result;
    }
    public List<Long> getTrainingDatesInRange(TrainingPlanEntity plan, List<TrainingDayEntity> days, long startMillis, long endMillis) {

        return calculator.getTrainingDatesInRange(plan, days, startMillis, endMillis);
    }
    public LiveData<Integer> getCompletedPlansCount() {
        return trainingPlanAndDayDao.getCompletedPlansCount();
    }
    public TrainingDayEntity getDayForDate(TrainingPlanEntity plan, List<TrainingDayEntity> templateDays, long dateMillis) {
        return calculator.findDayForDate(plan, templateDays, dateMillis);
    }
    public void syncFullTrainingPlan() {
        networkExecutor.execute(() -> {
            trainingService.getActiveTrainingPlan().enqueue(new Callback<TrainingPlanFullDto>() {
                @Override
                public void onResponse(Call<TrainingPlanFullDto> call, Response<TrainingPlanFullDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            TrainingPlanEntity plan = DtoMapper.toTrainingPlanEntity(response.body());
                            List<TrainingDayEntity> days = DtoMapper.toTrainingDayEntityList(response.body().getTrainingDays());
                            List<PlannedExerciseEntity> exercises = new ArrayList<>();
                            if (response.body().getTrainingDays() != null) {
                                for (TrainingDayDto d : response.body().getTrainingDays()) {
                                    exercises.addAll(DtoMapper.toPlannedExerciseEntityList(d.getPlannedExercises(), d.getId()));
                                }
                            }
                            trainingPlanAndDayDao.updateFullTrainingPlan(plan, days, exercises);
                        });
                    } else if (response.code() == 404) {
                        // Jeśli serwer mówi 404 (brak planu), czyścimy lokalnie
                        AppDatabase.databaseWriteExecutor.execute(trainingPlanAndDayDao::clearTrainingPlan);
                    }
                }
                @Override
                public void onFailure(Call<TrainingPlanFullDto> call, Throwable t) {}
            });
        });
    }
    public void activatePlan(int planId, String startDate, Callback<Void> callback) {
        Integer userId = authManager.getUserId();
        if (userId == null) {
            callback.onFailure(null, new Throwable("User not logged in"));
            return;
        }

        trainingService.assignPlanToUser(planId, startDate).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Czyścimy STARY plan i STARE wyniki
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        executedSetDao.deleteAllExecutedSets();
                        trainingPlanAndDayDao.clearTrainingPlan();
                    });
                    // Pobieramy NOWY plan
                    syncFullTrainingPlan();
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
    public void cancelActivePlan(Callback<Void> callback) {
        trainingService.cancelActivePlan().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    AppDatabase.databaseWriteExecutor.execute(trainingPlanAndDayDao::clearTrainingPlan);
                }
                callback.onResponse(call, response);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
    public void completePlan(PlanCompletionRequestDto dto, Callback<Void> callback) {
        trainingService.completeActivePlan(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    diskIO.execute(trainingPlanAndDayDao::markActivePlanAsCompleted);
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
    public LiveData<Integer> calculateCurrentStreak() {
        MediatorLiveData<Integer> streak = new MediatorLiveData<>();
        LiveData<TrainingPlanEntity> planLive = trainingPlanAndDayDao.getActiveTrainingPlan();

        streak.addSource(planLive, plan -> {
            if (plan == null) { streak.setValue(0); return; }

            diskIO.execute(() -> {
                List<TrainingDayEntity> days = trainingPlanAndDayDao.getDaysForPlanSync(plan.getId());
                List<Long> history = executedSetDao.getAllWorkoutDatesSync();

                if (days.isEmpty() || history.isEmpty()) {
                    streak.postValue(0);
                    return;
                }

                // 3. Oblicz daty, które POWINNY się odbyć do dzisiaj
                long startMillis = LocalDate.parse(plan.getStartDate()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long nowMillis = System.currentTimeMillis();

                List<Long> expectedDates = calculator.getTrainingDatesInRange(plan, days, startMillis, nowMillis);

                // 4. Porównaj od końca
                int currentStreak = 0;
                // Sortujemy historię malejąco dla wygody sprawdzania
                // (Ale tutaj wystarczy sprawdzić contains)

                for (int i = expectedDates.size() - 1; i >= 0; i--) {
                    Long expectedDate = expectedDates.get(i);

                    // Sprawdź czy ta data (lub bliska jej) jest w historii
                    boolean executed = isDateExecuted(expectedDate, history);

                    if (executed) {
                        currentStreak++;
                    } else {
                        // Jeśli treningu nie było, to KONIEC STREAKA.
                        // JEDYNY WYJĄTEK: Jeśli to jest DZISIAJ, a dzień się jeszcze nie skończył.
                        if (android.text.format.DateUtils.isToday(expectedDate)) {
                            continue; // Nie przerywamy, ale też nie dodajemy (chyba że chcesz)
                        }
                        break; // Przerwij pętlę
                    }
                }
                streak.postValue(currentStreak);
            });
        });
        return streak;
    }

    private boolean isDateExecuted(Long expectedMillis, List<Long> historyMillis) {
        LocalDate expected = Instant.ofEpochMilli(expectedMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        for (Long h : historyMillis) {
            LocalDate actual = Instant.ofEpochMilli(h).atZone(ZoneId.systemDefault()).toLocalDate();
            if (expected.isEqual(actual)) return true;
        }
        return false;
    }
    public void createCustomPlan(TrainingPlanFullDto planDto, Callback<Void> callback) {
        trainingService.createCustomPlan(planDto).enqueue(callback);
    }
    public void getAvailablePlans(Callback<List<TrainingPlanFullDto>> callback) {
        trainingService.getPlanTemplates().enqueue(callback);
    }
}