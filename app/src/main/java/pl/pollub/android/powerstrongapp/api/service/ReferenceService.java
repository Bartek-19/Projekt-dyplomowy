package pl.pollub.android.powerstrongapp.api.service;

import java.util.List;

import pl.pollub.android.powerstrongapp.api.model.EffortTypeDto;
import pl.pollub.android.powerstrongapp.api.model.ExerciseCategoryDto;
import pl.pollub.android.powerstrongapp.api.model.ExerciseDto;
import pl.pollub.android.powerstrongapp.api.model.MovementPatternDto;
import pl.pollub.android.powerstrongapp.api.model.TargetMuscleGroupDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingMethodDto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ReferenceService {

    @GET("/api/reference/exercises")
    Call<List<ExerciseDto>> getAllExercises();

    @GET("/api/reference/categories")
    Call<List<ExerciseCategoryDto>> getAllCategories();

    @GET("/api/reference/movement-patterns")
    Call<List<MovementPatternDto>> getAllMovementPatterns();

    @GET("/api/reference/target-muscle-groups")
    Call<List<TargetMuscleGroupDto>> getAllTargetMuscleGroups();
    @GET("/api/reference/effort-types")
    Call<List<EffortTypeDto>> getAllEffortTypes();

    @GET("/api/reference/training-methods")
    Call<List<TrainingMethodDto>> getAllTrainingMethods();
}