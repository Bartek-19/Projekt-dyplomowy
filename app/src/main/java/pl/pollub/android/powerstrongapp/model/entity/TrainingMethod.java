package pl.pollub.android.powerstrongapp.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "training_method")
public class TrainingMethod {
    @PrimaryKey
    private int id;
    private String name;
    private int durationOfCycle;
    private String description;
}