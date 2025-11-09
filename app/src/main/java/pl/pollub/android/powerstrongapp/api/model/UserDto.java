package pl.pollub.android.powerstrongapp.api.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private Integer status;
    @SerializedName("createDate")
    private String createDate;
    private Integer completedTrainingPlansCount;
    private Integer createdTrainingPlansCount;
}