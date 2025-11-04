package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ExerciseCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExerciseCategory exerciseCategory);

    @Query("SELECT * FROM exercise_category")
    List<ExerciseCategory> getAllExerciseCategories();

    @Query("SELECT * FROM exercise_category WHERE id = :id")
    List<ExerciseCategory> getExerciseCategory(int id);
}
