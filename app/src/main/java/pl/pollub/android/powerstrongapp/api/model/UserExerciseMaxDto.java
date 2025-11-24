package pl.pollub.android.powerstrongapp.api.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExerciseMaxDto {
    private Integer exerciseId;
    private String exerciseName;
    private Double currentOneRepMax;
    private String lastUpdatedDate;
    @SerializedName(value = "isBodyweight", alternate = {"bodyweight"})
    private boolean isBodyweight;
}
