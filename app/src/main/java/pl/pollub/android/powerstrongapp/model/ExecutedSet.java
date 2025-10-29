package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "executed_sets",
        foreignKeys = {
                @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id"),
                @ForeignKey(entity = TrainingDay.class, parentColumns = "id", childColumns = "training_day_id"),
                @ForeignKey(entity = UsersTrainingPlans.class, parentColumns = "user_id, training_plan_id", childColumns = "user_id, training_plan_id")
        }
)

public class ExecutedSet {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int setNumber;
    public int executedRepetitions;
    public float weightUsed;
    public float weightPlanned;
    public Date executionDate;
    public int exercise_id;
    public int training_day_id;
    public int effort_type_id;
    public int user_id;
    public int training_plan_id;
}
