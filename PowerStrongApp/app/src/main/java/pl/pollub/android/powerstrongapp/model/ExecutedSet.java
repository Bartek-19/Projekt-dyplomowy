package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "executed_sets",
        foreignKeys = {
                @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id"),
                @ForeignKey(entity = TrainingDay.class, parentColumns = "id", childColumns = "training_day_id")
        }
)
public class ExecutedSet {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int executedRepetitions;
    public float weightUsed;
    public float weightPlanned;
    public int exercise_id;
    public int training_day_id;
    public int effort_type_id;
    public int user_id;
}
