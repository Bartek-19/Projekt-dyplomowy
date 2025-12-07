package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.pollub.android.powerstrongapp.data.local.entity.UserRecordEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserRecordDao_Impl implements UserRecordDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<UserRecordEntity> __insertAdapterOfUserRecordEntity;

  public UserRecordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUserRecordEntity = new EntityInsertAdapter<UserRecordEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_records` (`exerciseId`,`exerciseName`,`currentOneRepMax`,`isBodyweight`,`lastUpdatedDate`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final UserRecordEntity entity) {
        statement.bindLong(1, entity.getExerciseId());
        if (entity.getExerciseName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getExerciseName());
        }
        if (entity.getCurrentOneRepMax() == null) {
          statement.bindNull(3);
        } else {
          statement.bindDouble(3, entity.getCurrentOneRepMax());
        }
        final int _tmp = entity.isBodyweight() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getLastUpdatedDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getLastUpdatedDate());
        }
      }
    };
  }

  @Override
  public void insertAll(final List<UserRecordEntity> records) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfUserRecordEntity.insert(_connection, records);
      return null;
    });
  }

  @Override
  public LiveData<List<UserRecordEntity>> getAllRecords() {
    final String _sql = "SELECT * FROM user_records";
    return __db.getInvalidationTracker().createLiveData(new String[] {"user_records"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseId");
        final int _columnIndexOfExerciseName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseName");
        final int _columnIndexOfCurrentOneRepMax = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "currentOneRepMax");
        final int _columnIndexOfIsBodyweight = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isBodyweight");
        final int _columnIndexOfLastUpdatedDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastUpdatedDate");
        final List<UserRecordEntity> _result = new ArrayList<UserRecordEntity>();
        while (_stmt.step()) {
          final UserRecordEntity _item;
          _item = new UserRecordEntity();
          final int _tmpExerciseId;
          _tmpExerciseId = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _item.setExerciseId(_tmpExerciseId);
          final String _tmpExerciseName;
          if (_stmt.isNull(_columnIndexOfExerciseName)) {
            _tmpExerciseName = null;
          } else {
            _tmpExerciseName = _stmt.getText(_columnIndexOfExerciseName);
          }
          _item.setExerciseName(_tmpExerciseName);
          final Double _tmpCurrentOneRepMax;
          if (_stmt.isNull(_columnIndexOfCurrentOneRepMax)) {
            _tmpCurrentOneRepMax = null;
          } else {
            _tmpCurrentOneRepMax = _stmt.getDouble(_columnIndexOfCurrentOneRepMax);
          }
          _item.setCurrentOneRepMax(_tmpCurrentOneRepMax);
          final boolean _tmpIsBodyweight;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsBodyweight));
          _tmpIsBodyweight = _tmp != 0;
          _item.setBodyweight(_tmpIsBodyweight);
          final String _tmpLastUpdatedDate;
          if (_stmt.isNull(_columnIndexOfLastUpdatedDate)) {
            _tmpLastUpdatedDate = null;
          } else {
            _tmpLastUpdatedDate = _stmt.getText(_columnIndexOfLastUpdatedDate);
          }
          _item.setLastUpdatedDate(_tmpLastUpdatedDate);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<UserRecordEntity> getLatestRecord() {
    final String _sql = "SELECT * FROM user_records ORDER BY lastUpdatedDate DESC LIMIT 1";
    return __db.getInvalidationTracker().createLiveData(new String[] {"user_records"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseId");
        final int _columnIndexOfExerciseName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseName");
        final int _columnIndexOfCurrentOneRepMax = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "currentOneRepMax");
        final int _columnIndexOfIsBodyweight = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isBodyweight");
        final int _columnIndexOfLastUpdatedDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastUpdatedDate");
        final UserRecordEntity _result;
        if (_stmt.step()) {
          _result = new UserRecordEntity();
          final int _tmpExerciseId;
          _tmpExerciseId = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _result.setExerciseId(_tmpExerciseId);
          final String _tmpExerciseName;
          if (_stmt.isNull(_columnIndexOfExerciseName)) {
            _tmpExerciseName = null;
          } else {
            _tmpExerciseName = _stmt.getText(_columnIndexOfExerciseName);
          }
          _result.setExerciseName(_tmpExerciseName);
          final Double _tmpCurrentOneRepMax;
          if (_stmt.isNull(_columnIndexOfCurrentOneRepMax)) {
            _tmpCurrentOneRepMax = null;
          } else {
            _tmpCurrentOneRepMax = _stmt.getDouble(_columnIndexOfCurrentOneRepMax);
          }
          _result.setCurrentOneRepMax(_tmpCurrentOneRepMax);
          final boolean _tmpIsBodyweight;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsBodyweight));
          _tmpIsBodyweight = _tmp != 0;
          _result.setBodyweight(_tmpIsBodyweight);
          final String _tmpLastUpdatedDate;
          if (_stmt.isNull(_columnIndexOfLastUpdatedDate)) {
            _tmpLastUpdatedDate = null;
          } else {
            _tmpLastUpdatedDate = _stmt.getText(_columnIndexOfLastUpdatedDate);
          }
          _result.setLastUpdatedDate(_tmpLastUpdatedDate);
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
  public void clearAll() {
    final String _sql = "DELETE FROM user_records";
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
