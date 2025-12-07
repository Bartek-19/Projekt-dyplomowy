package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import pl.pollub.android.powerstrongapp.data.local.entity.EffortTypeEntity;

@Dao
public interface EffortTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EffortTypeEntity> list);
}