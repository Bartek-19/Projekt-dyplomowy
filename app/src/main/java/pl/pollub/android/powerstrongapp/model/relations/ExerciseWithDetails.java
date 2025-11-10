package pl.pollub.android.powerstrongapp.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pl.pollub.android.powerstrongapp.model.entity.Exercise;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseCategory;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseHasMovementPattern;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseTargetMuscleGroup;

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