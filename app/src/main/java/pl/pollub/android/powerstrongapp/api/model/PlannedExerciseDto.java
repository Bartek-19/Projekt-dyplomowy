package pl.pollub.android.powerstrongapp.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlannedExerciseDto implements Serializable {

    private Integer id;
    private String exerciseName; // Z exercise.name
    private String exerciseDescription; // Z exercise.description
    private Integer plannedSets;
    private Integer plannedReps;
    private Double plannedWeight;
    private Integer exerciseOrder;

    // Metoda pomocnicza do generowania tekstu dla widoku
    public List<String> getSetsInfo() {
        List<String> infoList = new ArrayList<>();
        if (plannedSets != null && plannedReps != null && plannedWeight != null) {
            String format = plannedReps + " powtórzeń, " + String.format("%.1f", plannedWeight) + " kg";
            for (int i = 0; i < plannedSets; i++) {
                infoList.add(format);
            }
        }
        return infoList;
    }
}