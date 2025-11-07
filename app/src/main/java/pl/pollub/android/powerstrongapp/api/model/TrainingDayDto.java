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
public class TrainingDayDto implements Serializable {

    private Integer id; // ID TrainingDay
    private Integer trainingPlanId;
    private String dayName;
    private Integer dayOrder; // Dzień 1, Dzień 2, itd.
    private Integer weekNumber;

    // Lista zaplanowanych ćwiczeń na ten konkretny dzień
    private List<PlannedExerciseDto> plannedExercises;
}