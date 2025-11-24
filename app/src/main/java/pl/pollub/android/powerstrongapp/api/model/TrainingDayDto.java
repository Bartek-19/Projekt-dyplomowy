package pl.pollub.android.powerstrongapp.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDayDto implements Serializable {
    private Integer id;
    private Integer trainingPlanId;
    private String dayName;
    private Integer dayOrder;
    private Integer daysGap;
    private Integer weekNumber;
    private List<PlannedExerciseDto> plannedExercises;
}