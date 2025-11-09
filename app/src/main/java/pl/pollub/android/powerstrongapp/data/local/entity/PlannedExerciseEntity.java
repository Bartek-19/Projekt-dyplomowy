package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "planned_exercises",
        foreignKeys = @ForeignKey(entity = TrainingDayEntity.class,
                parentColumns = "id",
                childColumns = "trainingDayId",
                onDelete = ForeignKey.CASCADE)
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlannedExerciseEntity {

    @PrimaryKey
    private int id;
    private int trainingDayId;
    @NonNull
    private String exerciseName;
    private String exerciseDescription;
    private Integer exerciseOrder;
    private Integer plannedSets;
    private Integer plannedReps;
    private Double plannedWeight;
    private Long lastSyncTimestamp;
}