package pl.pollub.android.powerstrongapp.api;

import java.util.List;
import pl.pollub.android.powerstrongapp.api.model.ExerciseDto;
import pl.pollub.android.powerstrongapp.api.model.MovementPatternDto;
import pl.pollub.android.powerstrongapp.api.model.TargetMuscleGroupDto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ReferenceService {

    /**
     * Pobiera całą listę referencyjnych ćwiczeń.
     */
    @GET("/api/exercises")
    Call<List<ExerciseDto>> getAllExercises();

    /**
     * Pobiera listę wzorców ruchu (np. Pchanie, Ciągnięcie).
     */
    @GET("/api/movement-patterns")
    Call<List<MovementPatternDto>> getAllMovementPatterns();

    /**
     * Pobiera listę docelowych grup mięśniowych.
     */
    @GET("/api/target-muscle-groups")
    Call<List<TargetMuscleGroupDto>> getAllTargetMuscleGroups();
}