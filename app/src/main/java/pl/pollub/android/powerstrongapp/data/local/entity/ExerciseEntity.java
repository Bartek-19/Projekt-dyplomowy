package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseEntity {

    @PrimaryKey
    private int id;

    @NonNull
    private String name;
    private String description;
    private String exerciseCategory;
    private Integer movementPatternId;
    private Integer targetMuscleGroupId;
}