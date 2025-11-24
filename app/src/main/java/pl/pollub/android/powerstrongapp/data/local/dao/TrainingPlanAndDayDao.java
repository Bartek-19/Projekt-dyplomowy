package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;

@Dao
public abstract class TrainingPlanAndDayDao {
    @Query("SELECT * FROM training_plans")
    public abstract LiveData<List<TrainingPlanEntity>> getAllTrainingPlans();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPlan(TrainingPlanEntity plan);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertDays(List<TrainingDayEntity> days);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertExercises(List<PlannedExerciseEntity> exercises);
    @Query("DELETE FROM training_plans")
    abstract void deleteAllPlans();

    @Query("DELETE FROM training_days")
    abstract void deleteAllDays();

    @Query("DELETE FROM planned_exercises")
    abstract void deleteAllExercises();
    @Query("SELECT * FROM training_plans WHERE status = 'ACTIVE' LIMIT 1")
    public abstract LiveData<TrainingPlanEntity> getActiveTrainingPlan();
    @Query("SELECT * FROM training_plans WHERE status = 'ACTIVE' LIMIT 1")
    public abstract TrainingPlanEntity getActiveTrainingPlanSync();
    @Query("SELECT * FROM training_days WHERE trainingPlanId = :planId ORDER BY dayOrder ASC")
    public abstract LiveData<List<TrainingDayEntity>> getDaysForPlan(int planId);
    @Query("SELECT * FROM training_days WHERE trainingPlanId = :planId ORDER BY dayOrder ASC")
    public abstract List<TrainingDayEntity> getDaysForPlanSync(int planId);
    @Query("UPDATE training_plans SET status = 'COMPLETED' WHERE status = 'ACTIVE'")
    public abstract void markActivePlanAsCompleted();
    @Query("SELECT COUNT(*) FROM training_plans WHERE status = 'COMPLETED'")
    public abstract LiveData<Integer> getCompletedPlansCount();
    @Transaction
    public void updateFullTrainingPlan(TrainingPlanEntity plan,
                                       List<TrainingDayEntity> days,
                                       List<PlannedExerciseEntity> exercises) {
        deleteAllPlans();
        deleteAllDays();
        deleteAllExercises();

        if (plan != null) {
            insertPlan(plan);
            if (days != null && !days.isEmpty()) {
                insertDays(days);
            }
            if (exercises != null && !exercises.isEmpty()) {
                insertExercises(exercises);
            }
        }
    }
    @Transaction
    public void clearTrainingPlan() {
        deleteAllExercises();
        deleteAllDays();
        deleteAllPlans();
    }
}