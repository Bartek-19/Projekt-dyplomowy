package pl.pollub.android.powerstrongapp.model.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "planned_exercise",
        foreignKeys = {
                @ForeignKey(entity = TrainingDay.class, parentColumns = "id", childColumns = "training_day_id"),
                @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id"),
                @ForeignKey(entity = EffortType.class, parentColumns = "id", childColumns = "effort_type_id")
        }
)
public class PlannedExercise {
    @PrimaryKey
    private int id;
    private int exerciseOrder;
    private int plannedReps;
    private int plannedSets;
    private float plannedWeight;
    private int exercise_id;
    private int training_day_id;
    private int effort_type_id;
}