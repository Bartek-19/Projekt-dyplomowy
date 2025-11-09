package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import pl.pollub.android.powerstrongapp.data.local.entity.TargetMuscleGroupEntity;

@Dao
public interface TargetMuscleGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TargetMuscleGroupEntity> groups);

    @Query("SELECT * FROM target_muscle_groups")
    List<TargetMuscleGroupEntity> getAllMuscleGroups();
}