package pl.pollub.android.powerstrongapp.model;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class TrainingPlanWithDays {
    @Embedded
    public TrainingPlan plan;

    @Relation(
            parentColumn = "id",
            entityColumn = "training_plan_id"
    )
    public List<TrainingDay> trainingDays;
}