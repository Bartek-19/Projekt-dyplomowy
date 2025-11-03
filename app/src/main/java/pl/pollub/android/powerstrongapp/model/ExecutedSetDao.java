package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExecutedSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExecutedSet executedSet);

    @Update
    void update(ExecutedSet executedSet);

    @Delete
    void delete(ExecutedSet executedSet);

    @Query("SELECT * FROM executed_set WHERE id = :id")
    List<ExecutedSet> getExecutedSet(int id);

    @Query("SELECT * FROM executed_set WHERE planned_exercise_id = :plannedExerciseId")
    List<ExecutedSet> getExecutedSetsForPlannedExercise(int plannedExerciseId);
}

