package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.enums.SyncStatus;
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetWithExercise; // Import klasy pośredniej

@Dao
public interface ExecutedSetDao {
    @Insert
    void insert(ExecutedSetEntity executedSet);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExecutedSetEntity> sets);
    @Query("DELETE FROM executed_sets")
    void deleteAllExecutedSets();
    @Query("SELECT COUNT(DISTINCT executionTimestamp) FROM executed_sets " +
            "JOIN planned_exercises ON executed_sets.plannedExerciseId = planned_exercises.id " +
            "JOIN training_days ON planned_exercises.trainingDayId = training_days.id " +
            "WHERE training_days.trainingPlanId = :planId")
    public abstract LiveData<Integer> getCompletedSessionsCount(int planId);
    @Query("SELECT COUNT(DISTINCT executionTimestamp) FROM executed_sets " +
            "JOIN planned_exercises ON executed_sets.plannedExerciseId = planned_exercises.id " +
            "JOIN training_days ON planned_exercises.trainingDayId = training_days.id " +
            "WHERE training_days.trainingPlanId = :planId")
    public abstract int getCompletedSessionsCountSync(int planId);

    @Query("SELECT DISTINCT executionTimestamp FROM executed_sets ORDER BY executionTimestamp ASC")
    public abstract LiveData<List<Long>> getAllWorkoutDates();
    @Query("SELECT DISTINCT executionTimestamp FROM executed_sets ORDER BY executionTimestamp ASC")
    public abstract List<Long> getAllWorkoutDatesSync();
    @Query("SELECT es.setNumber, es.executedReps, es.weightUsed, es.executionTimestamp, ex.name AS exerciseName " +
            "FROM executed_sets es " +
            "JOIN planned_exercises pe ON es.plannedExerciseId = pe.id " +
            "JOIN exercises ex ON pe.id = ex.id " +
            "JOIN training_days td ON pe.trainingDayId = td.id " +
            "WHERE td.trainingPlanId = :planId " +
            "ORDER BY es.executionTimestamp DESC")
    LiveData<List<ExecutedSetWithExercise>> getExecutedSetsHistoryForPlan(int planId);
    @Query("SELECT * FROM executed_sets WHERE syncStatus = :status")
    List<ExecutedSetEntity> getSetsBySyncStatus(SyncStatus status);
    @Update
    void update(List<ExecutedSetEntity> sets);
}