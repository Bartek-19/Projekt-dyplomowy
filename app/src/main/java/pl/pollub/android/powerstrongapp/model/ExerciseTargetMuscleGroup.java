package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "exercise_target_muscle_group",
        primaryKeys = {"exercise_id", "target_muscle_group_id"},
        foreignKeys = {
                @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id"),
                @ForeignKey(entity = TargetMuscleGroup.class, parentColumns = "id", childColumns = "target_muscle_group_id")
        }
)
public class ExerciseTargetMuscleGroup {
    public int exercise_id;
    public int target_muscle_group_id;
}
