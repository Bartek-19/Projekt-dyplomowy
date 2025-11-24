package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import java.util.List; // Import listy

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "exercises")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@Ignore}))
public class ExerciseEntity {

    @PrimaryKey
    private int id;

    @NonNull
    private String name;
    private String description;
    private String exerciseCategory;
    private boolean isBodyweight;
    private List<Integer> movementPatternIds;
    private List<Integer> targetMuscleGroupIds;

    @Override
    public String toString() {
        return this.name;
    }
}