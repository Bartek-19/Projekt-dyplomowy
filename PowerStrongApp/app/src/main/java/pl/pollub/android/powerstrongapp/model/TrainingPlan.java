package pl.pollub.android.powerstrongapp.model;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "training_plan",
        foreignKeys = @ForeignKey(
                entity = TrainingMethod.class,
                parentColumns = "id",
                childColumns = "training_method_id"
        )
)
public class TrainingPlan {
    @PrimaryKey
    public int id;
    public String name;
    public int training_method_id;
}
