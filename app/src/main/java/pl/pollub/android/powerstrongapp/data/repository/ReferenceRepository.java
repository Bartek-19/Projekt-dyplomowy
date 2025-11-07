package pl.pollub.android.powerstrongapp.data.repository;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executors;

import pl.pollub.android.powerstrongapp.api.ReferenceService;
import pl.pollub.android.powerstrongapp.api.model.ExerciseDto;
import pl.pollub.android.powerstrongapp.api.model.MovementPatternDto;
import pl.pollub.android.powerstrongapp.api.model.TargetMuscleGroupDto;
import pl.pollub.android.powerstrongapp.data.local.AppDatabase;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.MovementPatternDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TargetMuscleGroupDao;
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.MovementPatternEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TargetMuscleGroupEntity;
import pl.pollub.android.powerstrongapp.utils.DtoMapper;

public class ReferenceRepository {

    private final ExerciseDao exerciseDao;
    private final MovementPatternDao movementPatternDao;
    private final TargetMuscleGroupDao targetMuscleGroupDao;
    private final ReferenceService referenceService;

    private static final int NUMBER_OF_THREADS = 2;
    private static final java.util.concurrent.ExecutorService networkExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public ReferenceRepository(Application application, ReferenceService referenceService) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.exerciseDao = db.exerciseDao();
        this.movementPatternDao = db.movementPatternDao();
        this.targetMuscleGroupDao = db.targetMuscleGroupDao();
        this.referenceService = referenceService;
    }

    /**
     * Inicjuje synchronizację wszystkich danych referencyjnych (wywoływane raz po starcie/logowaniu).
     */
    public void syncAllReferenceData() {
        // Uruchamiamy wszystkie synchronizacje w tle
        syncExercises();
        syncMovementPatterns();
        syncTargetMuscleGroups();
    }

    // Zapewniamy metody do pobierania z lokalnej bazy (synchroniczne lub asynchroniczne)
    public List<ExerciseEntity> getAllExercises() {
        return exerciseDao.getAllExercises(); // Zazwyczaj wymaga asynchronicznego wykonania poza UI thread
    }

    // --- Metody do synchronizacji pojedynczych encji ---

    private void syncExercises() {
        networkExecutor.execute(() -> {
            referenceService.getAllExercises().enqueue(new retrofit2.Callback<List<ExerciseDto>>() {
                @Override
                public void onResponse(retrofit2.Call<List<ExerciseDto>> call, retrofit2.Response<List<ExerciseDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ExerciseEntity> entities = DtoMapper.toExerciseEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> exerciseDao.insertAllExercises(entities));
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<List<ExerciseDto>> call, Throwable t) { /* Obsługa błędu */ }
            });
        });
    }

    private void syncMovementPatterns() {
        networkExecutor.execute(() -> {
            referenceService.getAllMovementPatterns().enqueue(new retrofit2.Callback<List<MovementPatternDto>>() {
                @Override
                public void onResponse(retrofit2.Call<List<MovementPatternDto>> call, retrofit2.Response<List<MovementPatternDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MovementPatternEntity> entities = DtoMapper.toMovementPatternEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> movementPatternDao.insertAll(entities));
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<List<MovementPatternDto>> call, Throwable t) { /* Obsługa błędu */ }
            });
        });
    }

    private void syncTargetMuscleGroups() {
        networkExecutor.execute(() -> {
            referenceService.getAllTargetMuscleGroups().enqueue(new retrofit2.Callback<List<TargetMuscleGroupDto>>() {
                @Override
                public void onResponse(retrofit2.Call<List<TargetMuscleGroupDto>> call, retrofit2.Response<List<TargetMuscleGroupDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<TargetMuscleGroupEntity> entities = DtoMapper.toTargetMuscleGroupEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> targetMuscleGroupDao.insertAll(entities));
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<List<TargetMuscleGroupDto>> call, Throwable t) { /* Obsługa błędu */ }
            });
        });
    }
}