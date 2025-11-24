package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseCategoryEntity;

@Dao
public interface ExerciseCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExerciseCategoryEntity> list);

    @Query("SELECT * FROM exercise_categories ORDER BY name ASC")
    LiveData<List<ExerciseCategoryEntity>> getAllCategories();
}