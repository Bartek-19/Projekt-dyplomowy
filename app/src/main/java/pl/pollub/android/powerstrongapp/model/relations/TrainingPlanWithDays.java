package pl.pollub.android.powerstrongapp.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

import pl.pollub.android.powerstrongapp.model.entity.TrainingDay;
import pl.pollub.android.powerstrongapp.model.entity.TrainingPlan;

public class TrainingPlanWithDays {
    @Embedded
    public TrainingPlan plan;

    @Relation(
            parentColumn = "id",
            entityColumn = "training_plan_id"
    )
    public List<TrainingDay> trainingDays;
}