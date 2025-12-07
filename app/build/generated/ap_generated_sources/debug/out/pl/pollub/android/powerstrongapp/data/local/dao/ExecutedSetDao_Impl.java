package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeleteOrUpdateAdapter;
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
import pl.pollub.android.powerstrongapp.data.local.Converters;
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetWithExercise;
import pl.pollub.android.powerstrongapp.data.local.entity.enums.SyncStatus;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExecutedSetDao_Impl implements ExecutedSetDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExecutedSetEntity> __insertAdapterOfExecutedSetEntity;

  private final EntityInsertAdapter<ExecutedSetEntity> __insertAdapterOfExecutedSetEntity_1;

  private final EntityDeleteOrUpdateAdapter<ExecutedSetEntity> __updateAdapterOfExecutedSetEntity;

  public ExecutedSetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExecutedSetEntity = new EntityInsertAdapter<ExecutedSetEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `executed_sets` (`localId`,`plannedExerciseId`,`setNumber`,`executedReps`,`weightUsed`,`executionTimestamp`,`syncStatus`) VALUES (?,?,?,?,?,?,?)";
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
        final Integer _tmp = Converters.fromSyncStatus(entity.getSyncStatus());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp);
        }
      }
    };
    this.__insertAdapterOfExecutedSetEntity_1 = new EntityInsertAdapter<ExecutedSetEntity>() {
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
        final Integer _tmp = Converters.fromSyncStatus(entity.getSyncStatus());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp);
        }
      }
    };
    this.__updateAdapterOfExecutedSetEntity = new EntityDeleteOrUpdateAdapter<ExecutedSetEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `executed_sets` SET `localId` = ?,`plannedExerciseId` = ?,`setNumber` = ?,`executedReps` = ?,`weightUsed` = ?,`executionTimestamp` = ?,`syncStatus` = ? WHERE `localId` = ?";
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
        final Integer _tmp = Converters.fromSyncStatus(entity.getSyncStatus());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp);
        }
        if (entity.getLocalId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getLocalId());
        }
      }
    };
  }

  @Override
  public void insertAll(final List<ExecutedSetEntity> sets) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExecutedSetEntity.insert(_connection, sets);
      return null;
    });
  }

  @Override
  public void insert(final ExecutedSetEntity executedSet) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExecutedSetEntity_1.insert(_connection, executedSet);
      return null;
    });
  }

  @Override
  public void update(final List<ExecutedSetEntity> sets) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfExecutedSetEntity.handleMultiple(_connection, sets);
      return null;
    });
  }

  @Override
  public List<ExecutedSetEntity> getSetsBySyncStatus(final SyncStatus status) {
    final String _sql = "SELECT * FROM executed_sets WHERE syncStatus = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        final Integer _tmp = Converters.fromSyncStatus(status);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
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
          final SyncStatus _tmpSyncStatus;
          final Integer _tmp_1;
          if (_stmt.isNull(_columnIndexOfSyncStatus)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = (int) (_stmt.getLong(_columnIndexOfSyncStatus));
          }
          _tmpSyncStatus = Converters.toSyncStatus(_tmp_1);
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
  public LiveData<Integer> getCompletedSessionsCount(final int planId) {
    final String _sql = "SELECT COUNT(DISTINCT executed_sets.executionTimestamp) FROM executed_sets INNER JOIN planned_exercises ON executed_sets.plannedExerciseId = planned_exercises.id INNER JOIN training_days ON planned_exercises.trainingDayId = training_days.id WHERE training_days.trainingPlanId = ?";
    return __db.getInvalidationTracker().createLiveData(new String[] {"executed_sets",
        "planned_exercises", "training_days"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, planId);
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
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
  public int getCompletedSessionsCountSync(final int planId) {
    final String _sql = "SELECT COUNT(DISTINCT executed_sets.executionTimestamp) FROM executed_sets INNER JOIN planned_exercises ON executed_sets.plannedExerciseId = planned_exercises.id INNER JOIN training_days ON planned_exercises.trainingDayId = training_days.id WHERE training_days.trainingPlanId = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, planId);
        final int _result;
        if (_stmt.step()) {
          _result = (int) (_stmt.getLong(0));
        } else {
          _result = 0;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<Long>> getAllWorkoutDates() {
    final String _sql = "SELECT DISTINCT executionTimestamp FROM executed_sets ORDER BY executionTimestamp DESC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"executed_sets"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final List<Long> _result = new ArrayList<Long>();
        while (_stmt.step()) {
          final Long _item;
          if (_stmt.isNull(0)) {
            _item = null;
          } else {
            _item = _stmt.getLong(0);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<ExecutedSetWithExercise>> getExecutedSetsHistoryForPlan(final int planId) {
    final String _sql = "SELECT es.setNumber, es.executedReps, es.weightUsed, es.executionTimestamp, pe.exerciseName AS exerciseName FROM executed_sets es JOIN planned_exercises pe ON es.plannedExerciseId = pe.id JOIN training_days td ON pe.trainingDayId = td.id WHERE td.trainingPlanId = ? ORDER BY es.executionTimestamp DESC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"executed_sets",
        "planned_exercises", "training_days"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, planId);
        final int _columnIndexOfSetNumber = 0;
        final int _columnIndexOfExecutedReps = 1;
        final int _columnIndexOfWeightUsed = 2;
        final int _columnIndexOfExecutionTimestamp = 3;
        final int _columnIndexOfExerciseName = 4;
        final List<ExecutedSetWithExercise> _result = new ArrayList<ExecutedSetWithExercise>();
        while (_stmt.step()) {
          final ExecutedSetWithExercise _item;
          _item = new ExecutedSetWithExercise();
          _item.setNumber = (int) (_stmt.getLong(_columnIndexOfSetNumber));
          _item.executedReps = (int) (_stmt.getLong(_columnIndexOfExecutedReps));
          _item.weightUsed = _stmt.getDouble(_columnIndexOfWeightUsed);
          if (_stmt.isNull(_columnIndexOfExecutionTimestamp)) {
            _item.executionTimestamp = null;
          } else {
            _item.executionTimestamp = _stmt.getLong(_columnIndexOfExecutionTimestamp);
          }
          if (_stmt.isNull(_columnIndexOfExerciseName)) {
            _item.exerciseName = null;
          } else {
            _item.exerciseName = _stmt.getText(_columnIndexOfExerciseName);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteAllExecutedSets() {
    final String _sql = "DELETE FROM executed_sets";
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
