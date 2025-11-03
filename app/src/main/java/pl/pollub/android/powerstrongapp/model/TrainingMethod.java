package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "training_method")
public class TrainingMethod {
    @PrimaryKey
    public int id;
    public String name;
    public int durationOfCycle;
}
