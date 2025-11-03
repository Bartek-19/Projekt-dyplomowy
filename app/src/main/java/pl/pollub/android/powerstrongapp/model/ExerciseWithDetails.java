package pl.pollub.android.powerstrongapp.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ExerciseWithDetails {
    @Embedded
    public Exercise exercise;

    @Relation(
            parentColumn = "id",
            entityColumn = "exercise_id",
            entity = ExerciseTargetMuscleGroup.class
    )
    public List<ExerciseTargetMuscleGroup> targetMuscleGroups;

    @Relation(
            parentColumn = "id",
            entityColumn = "exercise_id",
            entity = ExerciseHasMovementPattern.class
    )
    public List<ExerciseHasMovementPattern> movementPatterns;

    @Relation(
            parentColumn = "exercise_category_id",
            entityColumn = "id"
    )
    public ExerciseCategory category;
}
