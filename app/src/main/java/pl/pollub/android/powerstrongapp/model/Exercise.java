package pl.pollub.android.powerstrongapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

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
