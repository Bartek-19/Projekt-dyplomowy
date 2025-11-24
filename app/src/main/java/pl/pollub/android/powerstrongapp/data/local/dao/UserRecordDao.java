package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pl.pollub.android.powerstrongapp.data.local.entity.UserRecordEntity;

@Dao
public interface UserRecordDao {
    @Query("SELECT * FROM user_records")
    LiveData<List<UserRecordEntity>> getAllRecords();
    @Query("SELECT * FROM user_records ORDER BY lastUpdatedDate DESC LIMIT 1")
    public abstract LiveData<UserRecordEntity> getLatestRecord();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserRecordEntity> records);
    @Query("DELETE FROM user_records")
    void clearAll();
}