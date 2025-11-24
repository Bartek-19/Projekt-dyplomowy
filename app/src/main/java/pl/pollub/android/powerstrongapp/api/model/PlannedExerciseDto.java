package pl.pollub.android.powerstrongapp.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlannedExerciseDto implements Serializable {
    private Integer id;
    private String exerciseName;
    private String exerciseDescription;
    private Integer plannedSets;
    private Integer plannedReps;
    private Double targetWeight;
    private String suggestionType;
    private Double suggestionValue;
    private Integer exerciseOrder;
    private String effortType;
}