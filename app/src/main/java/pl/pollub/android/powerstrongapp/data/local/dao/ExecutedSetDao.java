package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity;

@Dao
public interface ExecutedSetDao {
    @Insert
    void insert(ExecutedSetEntity executedSet);
    @Query("SELECT * FROM executed_sets WHERE syncStatus = 0")
    List<ExecutedSetEntity> getUnsyncedSets();
    @Query("UPDATE executed_sets SET syncStatus = 1 WHERE localId = :id")
    void markAsSynced(int id);
    @Query("DELETE FROM executed_sets WHERE syncStatus = 1")
    void deleteSyncedSets();
}