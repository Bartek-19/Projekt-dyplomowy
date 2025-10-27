package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movement_pattern")
public class MovementPattern {
    @PrimaryKey
    public int id;
    public String name;
}
