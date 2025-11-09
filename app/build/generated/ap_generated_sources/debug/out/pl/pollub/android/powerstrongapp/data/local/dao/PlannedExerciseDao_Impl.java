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
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class PlannedExerciseDao_Impl implements PlannedExerciseDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<PlannedExerciseEntity> __insertAdapterOfPlannedExerciseEntity;

  public PlannedExerciseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfPlannedExerciseEntity = new EntityInsertAdapter<PlannedExerciseEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `planned_exercises` (`id`,`trainingDayId`,`exerciseName`,`exerciseDescription`,`exerciseOrder`,`plannedSets`,`plannedReps`,`plannedWeight`,`lastSyncTimestamp`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final PlannedExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTrainingDayId());
        if (entity.getExerciseName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getExerciseName());
        }
        if (entity.getExerciseDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getExerciseDescription());
        }
        if (entity.getExerciseOrder() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getExerciseOrder());
        }
        if (entity.getPlannedSets() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getPlannedSets());
        }
        if (entity.getPlannedReps() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPlannedReps());
        }
        if (entity.getPlannedWeight() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getPlannedWeight());
        }
        if (entity.getLastSyncTimestamp() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getLastSyncTimestamp());
        }
      }
    };
  }

  @Override
  public void insertAll(final List<PlannedExerciseEntity> exercises) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfPlannedExerciseEntity.insert(_connection, exercises);
      return null;
    });
  }

  @Override
  public LiveData<List<PlannedExerciseEntity>> getPlannedExercisesForDay(final int dayId) {
    final String _sql = "SELECT * FROM planned_exercises WHERE trainingDayId = ? ORDER BY exerciseOrder ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"planned_exercises"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, dayId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTrainingDayId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "trainingDayId");
        final int _columnIndexOfExerciseName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseName");
        final int _columnIndexOfExerciseDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseDescription");
        final int _columnIndexOfExerciseOrder = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseOrder");
        final int _columnIndexOfPlannedSets = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "plannedSets");
        final int _columnIndexOfPlannedReps = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "plannedReps");
        final int _columnIndexOfPlannedWeight = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "plannedWeight");
        final int _columnIndexOfLastSyncTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastSyncTimestamp");
        final List<PlannedExerciseEntity> _result = new ArrayList<PlannedExerciseEntity>();
        while (_stmt.step()) {
          final PlannedExerciseEntity _item;
          _item = new PlannedExerciseEntity();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final int _tmpTrainingDayId;
          _tmpTrainingDayId = (int) (_stmt.getLong(_columnIndexOfTrainingDayId));
          _item.setTrainingDayId(_tmpTrainingDayId);
          final String _tmpExerciseName;
          if (_stmt.isNull(_columnIndexOfExerciseName)) {
            _tmpExerciseName = null;
          } else {
            _tmpExerciseName = _stmt.getText(_columnIndexOfExerciseName);
          }
          _item.setExerciseName(_tmpExerciseName);
          final String _tmpExerciseDescription;
          if (_stmt.isNull(_columnIndexOfExerciseDescription)) {
            _tmpExerciseDescription = null;
          } else {
            _tmpExerciseDescription = _stmt.getText(_columnIndexOfExerciseDescription);
          }
          _item.setExerciseDescription(_tmpExerciseDescription);
          final Integer _tmpExerciseOrder;
          if (_stmt.isNull(_columnIndexOfExerciseOrder)) {
            _tmpExerciseOrder = null;
          } else {
            _tmpExerciseOrder = (int) (_stmt.getLong(_columnIndexOfExerciseOrder));
          }
          _item.setExerciseOrder(_tmpExerciseOrder);
          final Integer _tmpPlannedSets;
          if (_stmt.isNull(_columnIndexOfPlannedSets)) {
            _tmpPlannedSets = null;
          } else {
            _tmpPlannedSets = (int) (_stmt.getLong(_columnIndexOfPlannedSets));
          }
          _item.setPlannedSets(_tmpPlannedSets);
          final Integer _tmpPlannedReps;
          if (_stmt.isNull(_columnIndexOfPlannedReps)) {
            _tmpPlannedReps = null;
          } else {
            _tmpPlannedReps = (int) (_stmt.getLong(_columnIndexOfPlannedReps));
          }
          _item.setPlannedReps(_tmpPlannedReps);
          final Double _tmpPlannedWeight;
          if (_stmt.isNull(_columnIndexOfPlannedWeight)) {
            _tmpPlannedWeight = null;
          } else {
            _tmpPlannedWeight = _stmt.getDouble(_columnIndexOfPlannedWeight);
          }
          _item.setPlannedWeight(_tmpPlannedWeight);
          final Long _tmpLastSyncTimestamp;
          if (_stmt.isNull(_columnIndexOfLastSyncTimestamp)) {
            _tmpLastSyncTimestamp = null;
          } else {
            _tmpLastSyncTimestamp = _stmt.getLong(_columnIndexOfLastSyncTimestamp);
          }
          _item.setLastSyncTimestamp(_tmpLastSyncTimestamp);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteAll() {
    final String _sql = "DELETE FROM planned_exercises";
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
