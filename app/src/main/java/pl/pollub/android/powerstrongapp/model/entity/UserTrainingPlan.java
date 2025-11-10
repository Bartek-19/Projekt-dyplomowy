package pl.pollub.android.powerstrongapp.model.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "user_training_plan",
        primaryKeys = {"user_id", "training_plan_id"},
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"),
                @ForeignKey(entity = TrainingPlan.class, parentColumns = "id", childColumns = "training_plan_id")
        }
)
public class UserTrainingPlan {
    private int user_id;
    private int training_plan_id;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private boolean wasTrackingNutrition;
    private boolean wasTrackingSleep;
    private int averageHoursOfSleep;
    private int personalEvaluation;
}