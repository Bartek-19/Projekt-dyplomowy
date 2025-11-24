package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseEntity;

@Dao
public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllExercises(List<ExerciseEntity> exercises);
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    LiveData<List<ExerciseEntity>> getAllExercises();
}