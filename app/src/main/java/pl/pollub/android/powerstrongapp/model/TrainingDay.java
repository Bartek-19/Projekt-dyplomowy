package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "training_day",
        foreignKeys = @ForeignKey(
                entity = TrainingPlan.class, parentColumns = "id", childColumns = "training_plan_id"
        )
)
public class TrainingDay {
    @PrimaryKey
    private int id;
    private int weekNumber;
    private String dayName;
    private int dayOrder;
    private int training_plan_id;
}

