package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.pollub.android.powerstrongapp.data.local.entity.UserEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<UserEntity> __insertAdapterOfUserEntity;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUserEntity = new EntityInsertAdapter<UserEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`id`,`username`,`email`,`createDate`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final UserEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getUsername());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getEmail());
        }
        if (entity.getCreateDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getCreateDate());
        }
      }
    };
  }

  @Override
  public void insertUser(final UserEntity user) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfUserEntity.insert(_connection, user);
      return null;
    });
  }

  @Override
  public LiveData<UserEntity> getUser() {
    final String _sql = "SELECT * FROM users LIMIT 1";
    return __db.getInvalidationTracker().createLiveData(new String[] {"users"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfUsername = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "username");
        final int _columnIndexOfEmail = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "email");
        final int _columnIndexOfCreateDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createDate");
        final UserEntity _result;
        if (_stmt.step()) {
          _result = new UserEntity();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _result.setId(_tmpId);
          final String _tmpUsername;
          if (_stmt.isNull(_columnIndexOfUsername)) {
            _tmpUsername = null;
          } else {
            _tmpUsername = _stmt.getText(_columnIndexOfUsername);
          }
          _result.setUsername(_tmpUsername);
          final String _tmpEmail;
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _stmt.getText(_columnIndexOfEmail);
          }
          _result.setEmail(_tmpEmail);
          final String _tmpCreateDate;
          if (_stmt.isNull(_columnIndexOfCreateDate)) {
            _tmpCreateDate = null;
          } else {
            _tmpCreateDate = _stmt.getText(_columnIndexOfCreateDate);
          }
          _result.setCreateDate(_tmpCreateDate);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteAllUsers() {
    final String _sql = "DELETE FROM users";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
