package pl.pollub.android.powerstrongapp.model.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "exercise",
        foreignKeys = @ForeignKey(
                entity = ExerciseCategory.class, parentColumns = "id", childColumns = "exercise_category_id"
        )
)
public class Exercise {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private int exercise_category_id;
}
