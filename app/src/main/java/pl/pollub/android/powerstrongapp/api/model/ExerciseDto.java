package pl.pollub.android.powerstrongapp.api.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {
    private Integer id;
    private String name;
    private String description;
    @SerializedName(value = "isBodyweight", alternate = {"bodyweight"})
    private boolean isBodyweight;
    private String categoryName;
    private List<Integer> movementPatternIds;
    private List<Integer> targetMuscleGroupIds;
}