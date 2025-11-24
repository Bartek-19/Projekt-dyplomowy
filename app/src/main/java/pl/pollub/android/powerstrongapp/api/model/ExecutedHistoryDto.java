package pl.pollub.android.powerstrongapp.api.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutedHistoryDto implements Serializable {
    private int setNumber;
    private int executedReps;
    private double weightUsed;
    private Long executionTimestamp;
    private String exerciseName;
}