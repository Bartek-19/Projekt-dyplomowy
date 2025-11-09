package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TargetMuscleGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TargetMuscleGroup targetMuscleGroup);

    @Query("SELECT * FROM target_muscle_group WHERE id = :id")
    List<TargetMuscleGroup> getTargetMuscleGroup(int id);
}