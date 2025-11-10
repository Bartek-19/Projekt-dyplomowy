package pl.pollub.android.powerstrongapp.model.entity;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "training_plan",
        foreignKeys = {
                @ForeignKey(entity = TrainingMethod.class, parentColumns = "id", childColumns = "training_method_id"),
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "created_by_user_id")
        }
)
public class TrainingPlan {
    @PrimaryKey
    private int id;
    private String name;
    private int training_method_id;
    private boolean isPreset;
    private int created_by_user_id;
    private Date createdDate;
}