package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface TrainingDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrainingDay day);

    @Transaction
    @Query("SELECT * FROM training_day WHERE id = :id")
    TrainingDayWithExercises getDayWithExercises(int id);
}