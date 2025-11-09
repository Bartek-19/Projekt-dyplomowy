package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import pl.pollub.android.powerstrongapp.data.local.entity.UserEntity;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    LiveData<UserEntity> getUser();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);
    @Query("DELETE FROM users")
    void deleteAllUsers();
}