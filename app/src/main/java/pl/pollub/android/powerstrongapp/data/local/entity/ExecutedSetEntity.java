package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pollub.android.powerstrongapp.data.local.entity.enums.SyncStatus;

@Entity(tableName = "executed_sets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@Ignore}))
public class ExecutedSetEntity {
    @PrimaryKey(autoGenerate = true)
    private Integer localId;
    private int plannedExerciseId;
    @NonNull
    private int setNumber;
    private int executedReps;
    private double weightUsed;
    @NonNull
    private Long executionTimestamp;
    @Builder.Default
    private SyncStatus syncStatus = SyncStatus.NOT_SYNCED;
}