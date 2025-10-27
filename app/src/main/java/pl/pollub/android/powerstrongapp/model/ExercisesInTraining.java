package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "exercises_in_training",
        primaryKeys = {"exercise_id", "training_day_id", "effort_type_id"},
        foreignKeys = {
                @ForeignKey(entity = TrainingDay.class, parentColumns = "id", childColumns = "training_day_id"),
                @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id"),
                @ForeignKey(entity = EffortType.class, parentColumns = "id", childColumns = "effort_type_id")
        }
)
public class ExercisesInTraining {
    public int exercise_id;
    public int repetitions;
    public int sets;
    public float baseline_weight;
    public int training_day_id;
    public int effort_type_id;
}
