package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Date;

@Entity(
        tableName = "users_training_plans",
        primaryKeys = {"user_id", "plan_id"},
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"),
                @ForeignKey(entity = TrainingPlan.class, parentColumns = "id", childColumns = "plan_id")
        }
)
public class UsersTrainingPlans {
    public int user_id;
    public int plan_id;
    public Date beginDate;
    public Date endDate;
    public boolean wasTrackingSleep;
    public boolean wasTrackingNutrition;
    public int personalEvaluation;
    public int amountOfSleep;
}
