package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "exercise_has_movement_pattern",
        primaryKeys = {"exercise_id", "movement_pattern_id"},
        foreignKeys = {
                @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id"),
                @ForeignKey(entity = MovementPattern.class, parentColumns = "id", childColumns = "movement_pattern_id")
        }
)
public class ExerciseHasMovementPattern {
    public int exercise_id;
    public int movement_pattern_id;
}
