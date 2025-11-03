package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "target_muscle_group")
public class TargetMuscleGroup {
    @PrimaryKey
    private int id;
    private String name;
}
