package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetWithExercise; // Klasa POJO
import pl.pollub.android.powerstrongapp.data.local.entity.enums.SyncStatus;

@Dao
public interface ExecutedSetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExecutedSetEntity> sets);

    @Insert
    void insert(ExecutedSetEntity executedSet);

    @Update
    void update(List<ExecutedSetEntity> sets);

    @Query("DELETE FROM executed_sets")
    void deleteAllExecutedSets();

    @Query("SELECT * FROM executed_sets WHERE syncStatus = :status")
    List<ExecutedSetEntity> getSetsBySyncStatus(SyncStatus status);
    @Query("SELECT COUNT(DISTINCT executed_sets.executionTimestamp) " +
            "FROM executed_sets " +
            "INNER JOIN planned_exercises ON executed_sets.plannedExerciseId = planned_exercises.id " +
            "INNER JOIN training_days ON planned_exercises.trainingDayId = training_days.id " +
            "WHERE training_days.trainingPlanId = :planId")
    LiveData<Integer> getCompletedSessionsCount(int planId);
    @Query("SELECT COUNT(DISTINCT executed_sets.executionTimestamp) " +
            "FROM executed_sets " +
            "INNER JOIN planned_exercises ON executed_sets.plannedExerciseId = planned_exercises.id " +
            "INNER JOIN training_days ON planned_exercises.trainingDayId = training_days.id " +
            "WHERE training_days.trainingPlanId = :planId")
    int getCompletedSessionsCountSync(int planId);
    @Query("SELECT DISTINCT executionTimestamp FROM executed_sets ORDER BY executionTimestamp DESC")
    LiveData<List<Long>> getAllWorkoutDates();
    @Query("SELECT es.setNumber, es.executedReps, es.weightUsed, es.executionTimestamp, pe.exerciseName AS exerciseName " +
            "FROM executed_sets es " +
            "JOIN planned_exercises pe ON es.plannedExerciseId = pe.id " +
            "JOIN training_days td ON pe.trainingDayId = td.id " +
            "WHERE td.trainingPlanId = :planId " +
            "ORDER BY es.executionTimestamp DESC")
    LiveData<List<ExecutedSetWithExercise>> getExecutedSetsHistoryForPlan(int planId);
}