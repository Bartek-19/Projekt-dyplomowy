package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "exercise_target_muscle_group",
        primaryKeys = {"exercise_id", "target_muscle_group_id"},
        foreignKeys = {
                @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id"),
                @ForeignKey(entity = TargetMuscleGroup.class, parentColumns = "id", childColumns = "target_muscle_group_id")
        }
)
public class ExerciseTargetMuscleGroup {
    private int exercise_id;
    private int target_muscle_group_id;
}