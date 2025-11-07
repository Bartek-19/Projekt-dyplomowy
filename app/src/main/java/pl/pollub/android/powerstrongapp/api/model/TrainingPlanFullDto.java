package pl.pollub.android.powerstrongapp.api.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlanFullDto implements Serializable {

    private Integer id; // ID TrainingPlan
    private String name;
    private Integer durationOfCycle; // Z TrainingMethod
    private String startDate; // Data rozpoczęcia (z UserTrainingPlan)
    private String effortType;

    // Lista dni, które zawierają wszystkie ćwiczenia w cyklu
    private List<TrainingDayDto> trainingDays;
}