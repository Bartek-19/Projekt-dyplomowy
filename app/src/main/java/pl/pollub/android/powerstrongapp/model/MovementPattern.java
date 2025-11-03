package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "movement_pattern")
public class MovementPattern {
    @PrimaryKey
    private int id;
    private String name;
}
