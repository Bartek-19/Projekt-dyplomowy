package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "target_muscle_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TargetMuscleGroupEntity {
    @PrimaryKey
    private int id;
    @NonNull
    private String name;
}