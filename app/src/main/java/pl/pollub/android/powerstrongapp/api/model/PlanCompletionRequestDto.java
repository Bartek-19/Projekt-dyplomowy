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
public class PlanCompletionRequestDto implements Serializable {
    private boolean trackingNutrition;
    private boolean trackingSleep;
    private Double averageHoursOfSleep;
    private Integer personalEvaluation;
}
