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
public class TrainingPlanFullDto implements Serializable {
    private Integer id;
    private String name;
    private String startDate;
    private Integer durationOfCycle;
    private String status;
    private Integer trainingMethodId;
    private List<TrainingDayDto> trainingDays;
}