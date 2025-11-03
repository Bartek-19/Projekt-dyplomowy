package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "exercise",
        foreignKeys = @ForeignKey(
                entity = ExerciseCategory.class,
                parentColumns = "id",
                childColumns = "exercise_category_id"
        )
)
public class Exercise {
    @PrimaryKey
    public int id;
    public String name;
    public String description;
    public int exercise_category_id;
}

