package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "training_day",
        foreignKeys = @ForeignKey(
                entity = TrainingPlan.class,
                parentColumns = "id",
                childColumns = "training_plan_id"
        )
)
public class TrainingDay {
    @PrimaryKey
    public int id;
    public int training_plan_id;
    public String weekDay;
    public String week;
}

