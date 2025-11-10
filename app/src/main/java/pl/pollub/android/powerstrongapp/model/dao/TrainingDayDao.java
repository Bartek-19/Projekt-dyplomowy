package pl.pollub.android.powerstrongapp.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import pl.pollub.android.powerstrongapp.model.relations.TrainingDayWithExercises;
import pl.pollub.android.powerstrongapp.model.entity.TrainingDay;

@Dao
public interface TrainingDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrainingDay day);

    @Transaction
    @Query("SELECT * FROM training_day WHERE id = :id")
    TrainingDayWithExercises getDayWithExercises(int id);
}