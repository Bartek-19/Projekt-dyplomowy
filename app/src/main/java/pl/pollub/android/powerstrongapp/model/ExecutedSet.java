package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "executed_set",
        foreignKeys = {
                @ForeignKey(entity = PlannedExercise.class, parentColumns = "id", childColumns = "planned_exercise_id"),
                @ForeignKey(entity = UserTrainingPlan.class, parentColumns = "user_id, training_plan_id",
                        childColumns = "user_training_plan_user_id, user_training_plan_plan_id")
        }
)
public class ExecutedSet {
    @PrimaryKey
    private int id;
    private int setNumber;
    private int executedReps;
    private double weightUsed;
    private Date executionDate;
    private int user_training_plan_user_id;
    private int user_training_plan_plan_id;
    private int planned_exercise_id;
}
