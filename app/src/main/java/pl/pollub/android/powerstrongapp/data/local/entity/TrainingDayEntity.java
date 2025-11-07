package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "training_days",
        foreignKeys = @ForeignKey(entity = TrainingPlanEntity.class,
                parentColumns = "id",
                childColumns = "trainingPlanId",
                onDelete = ForeignKey.CASCADE))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDayEntity {

    @PrimaryKey
    private int id;

    private int trainingPlanId;
    private String dayName;
    private int dayOrder;
    private int weekNumber;
}