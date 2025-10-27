package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_category")
public class ExerciseCategory {
    @PrimaryKey
    public int id;
    public String categoryName;
}