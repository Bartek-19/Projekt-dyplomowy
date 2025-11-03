package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "exercise_has_movement_pattern",
        primaryKeys = {"exercise_id", "movement_pattern_id"},
        foreignKeys = {
                @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id"),
                @ForeignKey(entity = MovementPattern.class, parentColumns = "id", childColumns = "movement_pattern_id")
        }
)
public class ExerciseHasMovementPattern {
    private int exercise_id;
    private int movement_pattern_id;
}
