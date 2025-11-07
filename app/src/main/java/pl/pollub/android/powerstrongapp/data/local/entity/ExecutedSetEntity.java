package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "executed_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // Status synchronizacji: 0=Gotowe do wysyłki, 1=Wysłane
    private int syncStatus = 0;
}