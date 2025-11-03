package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "exercise_category")
public class ExerciseCategory {
    @PrimaryKey
    private int id;
    private String categoryName;
}