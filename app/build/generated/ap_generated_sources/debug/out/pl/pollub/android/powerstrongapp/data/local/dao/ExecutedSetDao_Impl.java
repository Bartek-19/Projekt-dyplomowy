package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExecutedSetDao_Impl implements ExecutedSetDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExecutedSetEntity> __insertAdapterOfExecutedSetEntity;

  public ExecutedSetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExecutedSetEntity = new EntityInsertAdapter<ExecutedSetEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `executed_sets` (`localId`,`plannedExerciseId`,`setNumber`,`executedReps`,`weightUsed`,`executionTimestamp`,`syncStatus`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final ExecutedSetEntity entity) {
        if (entity.getLocalId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getLocalId());
        }
        statement.bindLong(2, entity.getPlannedExerciseId());
        statement.bindLong(3, entity.getSetNumber());
        statement.bindLong(4, entity.getExecutedReps());
        statement.bindDouble(5, entity.getWeightUsed());
        if (entity.getExecutionTimestamp() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getExecutionTimestamp());
        }
        statement.bindLong(7, entity.getSyncStatus());
      }
    };
  }

  @Override
  public void insert(final ExecutedSetEntity executedSet) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExecutedSetEntity.insert(_connection, executedSet);
      return null;
    });
  }

  @Override
  public List<ExecutedSetEntity> getUnsyncedSets() {
    final String _sql = "SELECT * FROM executed_sets WHERE syncStatus = 0";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfLocalId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "localId");
        final int _columnIndexOfPlannedExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "plannedExerciseId");
        final int _columnIndexOfSetNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "setNumber");
        final int _columnIndexOfExecutedReps = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executedReps");
        final int _columnIndexOfWeightUsed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "weightUsed");
        final int _columnIndexOfExecutionTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executionTimestamp");
        final int _columnIndexOfSyncStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "syncStatus");
        final List<ExecutedSetEntity> _result = new ArrayList<ExecutedSetEntity>();
        while (_stmt.step()) {
          final ExecutedSetEntity _item;
          _item = new ExecutedSetEntity();
          final Integer _tmpLocalId;
          if (_stmt.isNull(_columnIndexOfLocalId)) {
            _tmpLocalId = null;
          } else {
            _tmpLocalId = (int) (_stmt.getLong(_columnIndexOfLocalId));
          }
          _item.setLocalId(_tmpLocalId);
          final int _tmpPlannedExerciseId;
          _tmpPlannedExerciseId = (int) (_stmt.getLong(_columnIndexOfPlannedExerciseId));
          _item.setPlannedExerciseId(_tmpPlannedExerciseId);
          final int _tmpSetNumber;
          _tmpSetNumber = (int) (_stmt.getLong(_columnIndexOfSetNumber));
          _item.setSetNumber(_tmpSetNumber);
          final int _tmpExecutedReps;
          _tmpExecutedReps = (int) (_stmt.getLong(_columnIndexOfExecutedReps));
          _item.setExecutedReps(_tmpExecutedReps);
          final double _tmpWeightUsed;
          _tmpWeightUsed = _stmt.getDouble(_columnIndexOfWeightUsed);
          _item.setWeightUsed(_tmpWeightUsed);
          final Long _tmpExecutionTimestamp;
          if (_stmt.isNull(_columnIndexOfExecutionTimestamp)) {
            _tmpExecutionTimestamp = null;
          } else {
            _tmpExecutionTimestamp = _stmt.getLong(_columnIndexOfExecutionTimestamp);
          }
          _item.setExecutionTimestamp(_tmpExecutionTimestamp);
          final int _tmpSyncStatus;
          _tmpSyncStatus = (int) (_stmt.getLong(_columnIndexOfSyncStatus));
          _item.setSyncStatus(_tmpSyncStatus);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void markAsSynced(final int id) {
    final String _sql = "UPDATE executed_sets SET syncStatus = 1 WHERE localId = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteSyncedSets() {
    final String _sql = "DELETE FROM executed_sets WHERE syncStatus = 1";
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
