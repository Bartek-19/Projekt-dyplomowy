package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.room.Ignore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@Ignore}))
public class ExecutedSetWithExercise {
    public int setNumber;
    public int executedReps;
    public double weightUsed;
    public Long executionTimestamp;
    public String exerciseName;
}
