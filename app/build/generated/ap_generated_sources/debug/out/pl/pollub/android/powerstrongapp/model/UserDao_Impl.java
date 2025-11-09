package pl.pollub.android.powerstrongapp.model;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.pollub.android.powerstrongapp.utils.DateConverter;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<User> __insertAdapterOfUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUser = new EntityInsertAdapter<User>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user` (`id`,`username`,`password`,`email`,`activeStatus`,`createDate`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getUsername());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getPassword());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getEmail());
        }
        final int _tmp = entity.isActiveStatus() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final Long _tmp_1 = DateConverter.fromDate(entity.getCreateDate());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp_1);
        }
      }
    };
  }

  @Override
  public void insert(final User user) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfUser.insert(_connection, user);
      return null;
    });
  }

  @Override
  public User getUserById(final int id) {
    final String _sql = "SELECT * FROM user WHERE id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfUsername = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "username");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "password");
        final int _columnIndexOfEmail = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "email");
        final int _columnIndexOfActiveStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "activeStatus");
        final int _columnIndexOfCreateDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createDate");
        final User _result;
        if (_stmt.step()) {
          _result = new User();
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
          final String _tmpPassword;
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _tmpPassword = null;
          } else {
            _tmpPassword = _stmt.getText(_columnIndexOfPassword);
          }
          _result.setPassword(_tmpPassword);
          final String _tmpEmail;
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _stmt.getText(_columnIndexOfEmail);
          }
          _result.setEmail(_tmpEmail);
          final boolean _tmpActiveStatus;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfActiveStatus));
          _tmpActiveStatus = _tmp != 0;
          _result.setActiveStatus(_tmpActiveStatus);
          final Date _tmpCreateDate;
          final Long _tmp_1;
          if (_stmt.isNull(_columnIndexOfCreateDate)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfCreateDate);
          }
          _tmpCreateDate = DateConverter.toDate(_tmp_1);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
