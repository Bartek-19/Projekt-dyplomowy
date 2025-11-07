package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;

@Dao
public interface TrainingPlanAndDayDao {
    // --- TrainingPlanEntity ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrainingPlan(TrainingPlanEntity plan);

    @Query("SELECT * FROM training_plans WHERE isActive = 1 LIMIT 1")
    LiveData<TrainingPlanEntity> getActiveTrainingPlan();

    // --- TrainingDayEntity ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrainingDays(List<TrainingDayEntity> days);

    @Query("SELECT * FROM training_days WHERE trainingPlanId = :planId ORDER BY weekNumber, dayOrder ASC")
    LiveData<List<TrainingDayEntity>> getDaysForPlan(int planId);
}