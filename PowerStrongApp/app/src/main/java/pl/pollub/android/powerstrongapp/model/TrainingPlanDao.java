package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface TrainingPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrainingPlan plan);

    @Transaction
    @Query("SELECT * FROM training_plan WHERE id = :id")
    TrainingPlanWithDays getPlanWithDays(int id);
}
