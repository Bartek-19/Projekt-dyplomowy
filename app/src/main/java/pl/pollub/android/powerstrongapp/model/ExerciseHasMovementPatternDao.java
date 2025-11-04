package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ExerciseHasMovementPatternDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExerciseHasMovementPattern relation);

    @Delete
    void delete(ExerciseHasMovementPattern relation);

    @Query("SELECT * FROM exercise_has_movement_pattern WHERE exercise_id = :exerciseId")
    List<ExerciseHasMovementPattern> getMovementPatternIdsForExerciseId(int exerciseId);

    @Query("SELECT * FROM exercise_has_movement_pattern WHERE movement_pattern_id = :movementPatternId")
    List<ExerciseHasMovementPattern> getExerciseIdsForMovementPatternId(int movementPatternId);

    @Transaction
    @Query("SELECT mp.* FROM movement_pattern mp INNER JOIN exercise_has_movement_pattern ehmp ON mp.id = ehmp.movement_pattern_id WHERE ehmp.exercise_id = :exerciseId")
    List<MovementPattern> getMovementPatternsForExerciseId(int exerciseId);
}

