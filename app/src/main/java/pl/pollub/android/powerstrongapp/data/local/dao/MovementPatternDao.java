package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import pl.pollub.android.powerstrongapp.data.local.entity.MovementPatternEntity;

@Dao
public interface MovementPatternDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MovementPatternEntity> patterns);

    @Query("SELECT * FROM movement_patterns")
    List<MovementPatternEntity> getAllPatterns();
}