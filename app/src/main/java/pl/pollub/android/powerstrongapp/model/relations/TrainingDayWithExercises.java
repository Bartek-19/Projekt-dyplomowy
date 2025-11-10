package pl.pollub.android.powerstrongapp.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

import pl.pollub.android.powerstrongapp.model.entity.PlannedExercise;
import pl.pollub.android.powerstrongapp.model.entity.TrainingDay;

public class TrainingDayWithExercises {
    @Embedded
    public TrainingDay trainingDay;

    @Relation(
            parentColumn = "id",
            entityColumn = "training_day_id"
    )
    public List<PlannedExercise> plannedExercises;
}