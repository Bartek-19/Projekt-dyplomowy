package pl.pollub.android.powerstrongapp.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import pl.pollub.android.powerstrongapp.model.dao.EffortTypeDao;
import pl.pollub.android.powerstrongapp.model.dao.ExecutedSetDao;
import pl.pollub.android.powerstrongapp.model.dao.ExerciseCategoryDao;
import pl.pollub.android.powerstrongapp.model.dao.ExerciseDao;
import pl.pollub.android.powerstrongapp.model.dao.MovementPatternDao;
import pl.pollub.android.powerstrongapp.model.dao.TargetMuscleGroupDao;
import pl.pollub.android.powerstrongapp.model.dao.TrainingDayDao;
import pl.pollub.android.powerstrongapp.model.dao.TrainingPlanDao;
import pl.pollub.android.powerstrongapp.model.dao.UserDao;
import pl.pollub.android.powerstrongapp.model.entity.EffortType;
import pl.pollub.android.powerstrongapp.model.entity.ExecutedSet;
import pl.pollub.android.powerstrongapp.model.entity.Exercise;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseCategory;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseHasMovementPattern;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseTargetMuscleGroup;
import pl.pollub.android.powerstrongapp.model.entity.MovementPattern;
import pl.pollub.android.powerstrongapp.model.entity.PlannedExercise;
import pl.pollub.android.powerstrongapp.model.entity.TargetMuscleGroup;
import pl.pollub.android.powerstrongapp.model.entity.TrainingDay;
import pl.pollub.android.powerstrongapp.model.entity.TrainingMethod;
import pl.pollub.android.powerstrongapp.model.entity.TrainingPlan;
import pl.pollub.android.powerstrongapp.model.entity.User;
import pl.pollub.android.powerstrongapp.model.entity.UserTrainingPlan;
import pl.pollub.android.powerstrongapp.utils.DateConverter;

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
@TypeConverters({DateConverter.class})
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