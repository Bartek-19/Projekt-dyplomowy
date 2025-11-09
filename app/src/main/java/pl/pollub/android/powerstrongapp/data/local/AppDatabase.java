package pl.pollub.android.powerstrongapp.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.MovementPatternDao;
import pl.pollub.android.powerstrongapp.data.local.dao.PlannedExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TargetMuscleGroupDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao;
import pl.pollub.android.powerstrongapp.data.local.dao.UserDao;
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.MovementPatternEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TargetMuscleGroupEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.UserEntity;

@Database(entities = {
        UserEntity.class,
        TrainingDayEntity.class,
        TrainingPlanEntity.class,
        ExerciseEntity.class,
        ExecutedSetEntity.class,
        PlannedExerciseEntity.class,
        MovementPatternEntity.class,
        TargetMuscleGroupEntity.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract TrainingPlanAndDayDao trainingPlanAndDayDao();
    public abstract ExerciseDao exerciseDao();
    public abstract PlannedExerciseDao plannedExerciseDao();
    public abstract ExecutedSetDao executedSetDao();
    public abstract MovementPatternDao movementPatternDao();
    public abstract TargetMuscleGroupDao targetMuscleGroupDao();
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "powerstrong_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}