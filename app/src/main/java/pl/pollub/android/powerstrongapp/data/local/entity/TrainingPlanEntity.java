package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "training_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlanEntity {

    @PrimaryKey
    private int id;

    private String name;
    private int durationOfCycle;
    private String startDate;
    private boolean isActive;
    private String effortType;
}