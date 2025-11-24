package pl.pollub.android.powerstrongapp.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutedSetDto {
    private Integer plannedExerciseId;
    private Integer setNumber;
    private Integer executedReps;
    private Double weightUsed;
}