package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExecutedSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExecutedSet executedSet);

    @Query("SELECT * FROM executed_sets WHERE user_id = :userId")
    List<ExecutedSet> getExecutedSetsForU(int userId);

    @Query("SELECT * FROM executed_sets WHERE exercise_id = :exerciseId AND user_id = :userId")
    List<ExecutedSet> getExecutedSetsForEU(int exerciseId, int userId);

    @Query("SELECT * FROM executed_sets WHERE exercise_id = :exerciseId AND training_day_id = :dayId AND user_id = :userId")
    List<ExecutedSet> getExecutedSetsForEDU(int exerciseId, int dayId, int userId);

    @Query("SELECT * FROM executed_sets WHERE exercise_id = :exerciseId AND training_day_id = :dayId AND user_id = :userId AND effort_type_id = :effortTypeId")
    List<ExecutedSet> getExecutedSetsForEDUEF(int exerciseId, int dayId, int userId, int effortTypeId);
}
