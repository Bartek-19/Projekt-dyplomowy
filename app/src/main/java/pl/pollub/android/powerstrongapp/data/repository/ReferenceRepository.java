package pl.pollub.android.powerstrongapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pollub.android.powerstrongapp.api.model.*;
import pl.pollub.android.powerstrongapp.api.service.ReferenceService;
import pl.pollub.android.powerstrongapp.data.local.AppDatabase;
import pl.pollub.android.powerstrongapp.data.local.dao.*;
import pl.pollub.android.powerstrongapp.data.local.entity.*;
import pl.pollub.android.powerstrongapp.utils.DtoMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferenceRepository {
    private final ExerciseDao exerciseDao;
    private final MovementPatternDao movementPatternDao;
    private final TargetMuscleGroupDao targetMuscleGroupDao;
    private final ExerciseCategoryDao exerciseCategoryDao;
    private final EffortTypeDao effortTypeDao;
    private final TrainingMethodDao trainingMethodDao;

    private final ReferenceService referenceService;

    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService networkExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public ReferenceRepository(Application application, ReferenceService referenceService) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.exerciseDao = db.exerciseDao();
        this.movementPatternDao = db.movementPatternDao();
        this.targetMuscleGroupDao = db.targetMuscleGroupDao();
        this.exerciseCategoryDao = db.exerciseCategoryDao();
        this.effortTypeDao = db.effortTypeDao();
        this.trainingMethodDao = db.trainingMethodDao();

        this.referenceService = referenceService;
    }
    public void syncDictionaries() {
        syncExercises();
        syncMovementPatterns();
        syncTargetMuscleGroups();
        syncCategories();
        syncEffortTypes();
        syncTrainingMethods();
    }
    public LiveData<List<ExerciseEntity>> getAllExercises() {
        return exerciseDao.getAllExercises();
    }
    public LiveData<List<TrainingMethodEntity>> getAllTrainingMethods() {
        return trainingMethodDao.getAllTrainingMethods();
    }
    public void syncExercises() {
        networkExecutor.execute(() -> {
            referenceService.getAllExercises().enqueue(new Callback<List<ExerciseDto>>() {
                @Override
                public void onResponse(Call<List<ExerciseDto>> call, Response<List<ExerciseDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ExerciseEntity> entities = DtoMapper.toExerciseEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> exerciseDao.insertAllExercises(entities));
                    }
                }
                @Override public void onFailure(Call<List<ExerciseDto>> call, Throwable t) { }
            });
        });
    }
    private void syncMovementPatterns() {
        networkExecutor.execute(() -> {
            referenceService.getAllMovementPatterns().enqueue(new Callback<List<MovementPatternDto>>() {
                @Override
                public void onResponse(Call<List<MovementPatternDto>> call, Response<List<MovementPatternDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MovementPatternEntity> entities = DtoMapper.toMovementPatternEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> movementPatternDao.insertAll(entities));
                    }
                }
                @Override public void onFailure(Call<List<MovementPatternDto>> call, Throwable t) { }
            });
        });
    }
    private void syncTargetMuscleGroups() {
        networkExecutor.execute(() -> {
            referenceService.getAllTargetMuscleGroups().enqueue(new Callback<List<TargetMuscleGroupDto>>() {
                @Override
                public void onResponse(Call<List<TargetMuscleGroupDto>> call, Response<List<TargetMuscleGroupDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<TargetMuscleGroupEntity> entities = DtoMapper.toTargetMuscleGroupEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> targetMuscleGroupDao.insertAll(entities));
                    }
                }
                @Override public void onFailure(Call<List<TargetMuscleGroupDto>> call, Throwable t) { }
            });
        });
    }
    private void syncCategories() {
        networkExecutor.execute(() -> {
            referenceService.getAllCategories().enqueue(new Callback<List<ExerciseCategoryDto>>() {
                @Override
                public void onResponse(Call<List<ExerciseCategoryDto>> call, Response<List<ExerciseCategoryDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ExerciseCategoryEntity> entities = DtoMapper.toExerciseCategoryEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> exerciseCategoryDao.insertAll(entities));
                    }
                }
                @Override public void onFailure(Call<List<ExerciseCategoryDto>> call, Throwable t) { }
            });
        });
    }
    private void syncEffortTypes() {
        networkExecutor.execute(() -> {
            referenceService.getAllEffortTypes().enqueue(new Callback<List<EffortTypeDto>>() {
                @Override
                public void onResponse(Call<List<EffortTypeDto>> call, Response<List<EffortTypeDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<EffortTypeEntity> entities = DtoMapper.toEffortTypeEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> effortTypeDao.insertAll(entities));
                    }
                }
                @Override public void onFailure(Call<List<EffortTypeDto>> call, Throwable t) { }
            });
        });
    }
    private void syncTrainingMethods() {
        networkExecutor.execute(() -> {
            referenceService.getAllTrainingMethods().enqueue(new Callback<List<TrainingMethodDto>>() {
                @Override
                public void onResponse(Call<List<TrainingMethodDto>> call, Response<List<TrainingMethodDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<TrainingMethodEntity> entities = DtoMapper.toTrainingMethodEntityList(response.body());
                        AppDatabase.databaseWriteExecutor.execute(() -> trainingMethodDao.insertAll(entities));
                    }
                }
                @Override public void onFailure(Call<List<TrainingMethodDto>> call, Throwable t) { }
            });
        });
    }
}