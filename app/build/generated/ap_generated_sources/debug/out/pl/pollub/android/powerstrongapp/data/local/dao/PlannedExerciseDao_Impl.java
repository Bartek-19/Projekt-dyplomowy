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
        return "INSERT OR REPLACE INTO `planned_exercises` (`id`,`trainingDayId`,`exerciseName`,`exerciseDescription`,`exerciseOrder`,`plannedSets`,`plannedReps`,`targetWeight`,`suggestionType`,`suggestionValue`,`effortType`,`lastSyncTimestamp`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
        if (entity.getTargetWeight() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getTargetWeight());
        }
        if (entity.getSuggestionType() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getSuggestionType());
        }
        if (entity.getSuggestionValue() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getSuggestionValue());
        }
        if (entity.getEffortType() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getEffortType());
        }
        if (entity.getLastSyncTimestamp() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getLastSyncTimestamp());
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
        final int _columnIndexOfTargetWeight = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetWeight");
        final int _columnIndexOfSuggestionType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "suggestionType");
        final int _columnIndexOfSuggestionValue = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "suggestionValue");
        final int _columnIndexOfEffortType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "effortType");
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
          final Double _tmpTargetWeight;
          if (_stmt.isNull(_columnIndexOfTargetWeight)) {
            _tmpTargetWeight = null;
          } else {
            _tmpTargetWeight = _stmt.getDouble(_columnIndexOfTargetWeight);
          }
          _item.setTargetWeight(_tmpTargetWeight);
          final String _tmpSuggestionType;
          if (_stmt.isNull(_columnIndexOfSuggestionType)) {
            _tmpSuggestionType = null;
          } else {
            _tmpSuggestionType = _stmt.getText(_columnIndexOfSuggestionType);
          }
          _item.setSuggestionType(_tmpSuggestionType);
          final Double _tmpSuggestionValue;
          if (_stmt.isNull(_columnIndexOfSuggestionValue)) {
            _tmpSuggestionValue = null;
          } else {
            _tmpSuggestionValue = _stmt.getDouble(_columnIndexOfSuggestionValue);
          }
          _item.setSuggestionValue(_tmpSuggestionValue);
          final String _tmpEffortType;
          if (_stmt.isNull(_columnIndexOfEffortType)) {
            _tmpEffortType = null;
          } else {
            _tmpEffortType = _stmt.getText(_columnIndexOfEffortType);
          }
          _item.setEffortType(_tmpEffortType);
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
  public LiveData<Integer> getExercisesCountForDay(final int dayId) {
    final String _sql = "SELECT COUNT(*) FROM planned_exercises WHERE trainingDayId = ?";
    return __db.getInvalidationTracker().createLiveData(new String[] {"planned_exercises"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, dayId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
