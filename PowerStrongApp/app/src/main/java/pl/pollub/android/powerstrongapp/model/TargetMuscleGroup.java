package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "target_muscle_group")
public class TargetMuscleGroup {
    @PrimaryKey
    public int id;
    public String name;
}
