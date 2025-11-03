package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("SELECT * FROM exercise WHERE id = :id")
    List<Exercise> getExerciseById(int id);

    @Query("SELECT * FROM exercise WHERE name LIKE :name")
    List<Exercise> getExercisesByName(String name);

    @Query("SELECT * FROM exercise WHERE name LIKE :phrase OR description LIKE :phrase")
    List<Exercise> getExercisesByPhrase(String phrase);

    @Query("SELECT * FROM exercise WHERE exercise_category_id = :exerciseCategoryId")
    List<Exercise> getExercisesByExerciseCategoryId(int exerciseCategoryId);

    @Transaction
    @Query("SELECT * FROM exercise WHERE id = :id")
    ExerciseWithDetails getExerciseDetails(int id);
}
