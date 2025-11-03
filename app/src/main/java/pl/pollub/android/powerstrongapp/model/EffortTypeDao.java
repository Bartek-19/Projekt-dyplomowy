package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EffortTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EffortType effortType);

    @Delete
    void delete(EffortType effortType);

    @Query("SELECT * FROM effort_type WHERE id = :id")
    List<EffortType> getEffortType(int id);

    @Query("SELECT * FROM effort_type")
    List<EffortType> getAllEffortTypes();
}
