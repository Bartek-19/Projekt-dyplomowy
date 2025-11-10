package pl.pollub.android.powerstrongapp.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pl.pollub.android.powerstrongapp.model.entity.ExecutedSet;
import pl.pollub.android.powerstrongapp.model.entity.UserTrainingPlan;

public class UserTrainingPlanWithExecutedSets {
    @Embedded
    public UserTrainingPlan userTrainingPlan;

    @Relation(
            parentColumn = "user_id",
            entityColumn = "user_training_plan_user_id",
            entity = ExecutedSet.class
    )
    public List<ExecutedSet> executedSets;
}
