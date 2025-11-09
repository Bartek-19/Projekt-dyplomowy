package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;

@Dao
public interface PlannedExerciseDao {
    @Query("SELECT * FROM planned_exercises WHERE trainingDayId = :dayId ORDER BY exerciseOrder ASC")
    LiveData<List<PlannedExerciseEntity>> getPlannedExercisesForDay(int dayId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PlannedExerciseEntity> exercises);
    @Query("DELETE FROM planned_exercises")
    void deleteAll();
}