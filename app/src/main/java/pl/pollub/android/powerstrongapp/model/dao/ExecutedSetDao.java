package pl.pollub.android.powerstrongapp.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.pollub.android.powerstrongapp.model.entity.ExecutedSet;

@Dao
public interface ExecutedSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExecutedSet executedSet);

    @Update
    void update(ExecutedSet executedSet);

    @Delete
    void delete(ExecutedSet executedSet);

    @Query("SELECT * FROM executed_set WHERE id = :id")
    List<ExecutedSet> getExecutedSetForId(int id);

    @Query("SELECT * FROM executed_set WHERE id = :id AND user_training_plan_user_id = :userId")
    List<ExecutedSet> getExecutedSetForIdAndUserId(int id, int userId);

    @Query("SELECT * FROM executed_set WHERE planned_exercise_id = :plannedExerciseId AND user_training_plan_user_id = :userId")
    List<ExecutedSet> getExecutedSetsForPlannedExerciseId(int plannedExerciseId, int userId);

    @Query("SELECT * FROM executed_set WHERE user_training_plan_user_id = :userId AND user_training_plan_plan_id = :planId ORDER BY id")
    List<ExecutedSet> getExecutedSetsForUsersTrainingPlan(int userId, int planId);
}