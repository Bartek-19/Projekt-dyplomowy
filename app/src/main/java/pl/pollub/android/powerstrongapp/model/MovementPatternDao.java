package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MovementPatternDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovementPattern movementPattern);

    @Query("SELECT * FROM movement_pattern")
    List<MovementPattern> getAllMovementPatterns();

    @Query("SELECT * FROM movement_pattern WHERE id = :id")
    List<MovementPattern> getMovementPatternById(int id);

    @Query("SELECT mp.* FROM movement_pattern mp INNER JOIN exercise_has_movement_pattern ehmp ON mp.id = ehmp.movement_pattern_id INNER JOIN exercise e ON ehmp.exercise_id = e.id WHERE e.id = :exerciseId")
    List<MovementPattern> getMovementPatternsByExerciseId(int exerciseId);

    @Query("SELECT e.* FROM exercise e INNER JOIN exercise_has_movement_pattern ehmp ON e.id = ehmp.exercise_id INNER JOIN movement_pattern mp ON ehmp.movement_pattern_id = mp.id WHERE mp.id = :movementPatternId")
    List<Exercise> getExercisesByMovementPatternId(int movementPatternId);
}
