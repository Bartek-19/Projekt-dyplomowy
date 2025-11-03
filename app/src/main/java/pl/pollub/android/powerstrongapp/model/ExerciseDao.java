package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Exercise exercise);

    @Transaction
    @Query("SELECT * FROM exercise WHERE id = :id")
    ExerciseWithDetails getExerciseDetails(int id);
}
