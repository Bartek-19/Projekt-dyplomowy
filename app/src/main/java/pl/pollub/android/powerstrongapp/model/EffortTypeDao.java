package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EffortTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EffortType effortType);

    @Query("SELECT * FROM effort_type WHERE id = :id")
    List<EffortType> getEffortTypes(int id);
}
