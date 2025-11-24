package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "planned_exercises",
        foreignKeys = @ForeignKey(entity = TrainingDayEntity.class,
                parentColumns = "id",
                childColumns = "trainingDayId",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("trainingDayId")
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@Ignore}))
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
    private Double targetWeight;
    private String suggestionType;
    private Double suggestionValue;
    private String effortType;
    private Long lastSyncTimestamp;
}