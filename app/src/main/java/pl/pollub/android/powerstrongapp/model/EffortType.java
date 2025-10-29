package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "effort_type")
public class EffortType {
    @PrimaryKey
    public int id;
    public String effortTypeName;
    public String description;
}
