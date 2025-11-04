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
}
