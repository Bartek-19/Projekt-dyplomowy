package pl.pollub.android.powerstrongapp.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                User.class,
                UserTrainingPlan.class,
                TrainingPlan.class,
                TrainingMethod.class,
                TrainingDay.class,
                Exercise.class,
                ExerciseCategory.class,
                ExerciseHasMovementPattern.class,
                MovementPattern.class,
                ExerciseTargetMuscleGroup.class,
                TargetMuscleGroup.class,
                EffortType.class,
                PlannedExercise.class,
                ExecutedSet.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TrainingPlanDao trainingPlanDao();
    public abstract TrainingDayDao trainingDayDao();
    public abstract ExerciseDao exerciseDao();
    public abstract ExecutedSetDao executedSetDao();
    public abstract EffortTypeDao effortTypeDao();
    public abstract ExerciseCategoryDao exerciseCategoryDao();
    public abstract MovementPatternDao movementPatternDao();
    public abstract TargetMuscleGroupDao targetMuscleGroupDao();
}