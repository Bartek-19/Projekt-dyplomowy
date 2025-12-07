package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TrainingPlanAndDayDao_Impl extends TrainingPlanAndDayDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TrainingPlanEntity> __insertAdapterOfTrainingPlanEntity;

  private final EntityInsertAdapter<TrainingDayEntity> __insertAdapterOfTrainingDayEntity;

  private final EntityInsertAdapter<PlannedExerciseEntity> __insertAdapterOfPlannedExerciseEntity;

  public TrainingPlanAndDayDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTrainingPlanEntity = new EntityInsertAdapter<TrainingPlanEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `training_plans` (`id`,`name`,`durationOfCycle`,`startDate`,`status`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final TrainingPlanEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        statement.bindLong(3, entity.getDurationOfCycle());
        if (entity.getStartDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getStartDate());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getStatus());
        }
      }
    };
    this.__insertAdapterOfTrainingDayEntity = new EntityInsertAdapter<TrainingDayEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `training_days` (`id`,`trainingPlanId`,`dayName`,`dayOrder`,`daysGap`,`weekNumber`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final TrainingDayEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTrainingPlanId());
        if (entity.getDayName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDayName());
        }
        statement.bindLong(4, entity.getDayOrder());
        statement.bindLong(5, entity.getDaysGap());
        statement.bindLong(6, entity.getWeekNumber());
      }
    };
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
  public void insertPlan(final TrainingPlanEntity plan) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTrainingPlanEntity.insert(_connection, plan);
      return null;
    });
  }

  @Override
  void insertDays(final List<TrainingDayEntity> days) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTrainingDayEntity.insert(_connection, days);
      return null;
    });
  }

  @Override
  void insertExercises(final List<PlannedExerciseEntity> exercises) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfPlannedExerciseEntity.insert(_connection, exercises);
      return null;
    });
  }

  @Override
  public void updateFullTrainingPlan(final TrainingPlanEntity plan,
      final List<TrainingDayEntity> days, final List<PlannedExerciseEntity> exercises) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      TrainingPlanAndDayDao_Impl.super.updateFullTrainingPlan(plan, days, exercises);
      return Unit.INSTANCE;
    });
  }

  @Override
  public void clearTrainingPlan() {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      TrainingPlanAndDayDao_Impl.super.clearTrainingPlan();
      return Unit.INSTANCE;
    });
  }

  @Override
  public LiveData<List<TrainingPlanEntity>> getAllTrainingPlans() {
    final String _sql = "SELECT * FROM training_plans";
    return __db.getInvalidationTracker().createLiveData(new String[] {"training_plans"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDurationOfCycle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationOfCycle");
        final int _columnIndexOfStartDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startDate");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "status");
        final List<TrainingPlanEntity> _result = new ArrayList<TrainingPlanEntity>();
        while (_stmt.step()) {
          final TrainingPlanEntity _item;
          _item = new TrainingPlanEntity();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          _item.setName(_tmpName);
          final int _tmpDurationOfCycle;
          _tmpDurationOfCycle = (int) (_stmt.getLong(_columnIndexOfDurationOfCycle));
          _item.setDurationOfCycle(_tmpDurationOfCycle);
          final String _tmpStartDate;
          if (_stmt.isNull(_columnIndexOfStartDate)) {
            _tmpStartDate = null;
          } else {
            _tmpStartDate = _stmt.getText(_columnIndexOfStartDate);
          }
          _item.setStartDate(_tmpStartDate);
          final String _tmpStatus;
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmpStatus = null;
          } else {
            _tmpStatus = _stmt.getText(_columnIndexOfStatus);
          }
          _item.setStatus(_tmpStatus);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<TrainingPlanEntity> getActiveTrainingPlan() {
    final String _sql = "SELECT * FROM training_plans WHERE status = 'ACTIVE' LIMIT 1";
    return __db.getInvalidationTracker().createLiveData(new String[] {"training_plans"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDurationOfCycle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationOfCycle");
        final int _columnIndexOfStartDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startDate");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "status");
        final TrainingPlanEntity _result;
        if (_stmt.step()) {
          _result = new TrainingPlanEntity();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _result.setId(_tmpId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          _result.setName(_tmpName);
          final int _tmpDurationOfCycle;
          _tmpDurationOfCycle = (int) (_stmt.getLong(_columnIndexOfDurationOfCycle));
          _result.setDurationOfCycle(_tmpDurationOfCycle);
          final String _tmpStartDate;
          if (_stmt.isNull(_columnIndexOfStartDate)) {
            _tmpStartDate = null;
          } else {
            _tmpStartDate = _stmt.getText(_columnIndexOfStartDate);
          }
          _result.setStartDate(_tmpStartDate);
          final String _tmpStatus;
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmpStatus = null;
          } else {
            _tmpStatus = _stmt.getText(_columnIndexOfStatus);
          }
          _result.setStatus(_tmpStatus);
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
  public TrainingPlanEntity getActiveTrainingPlanSync() {
    final String _sql = "SELECT * FROM training_plans WHERE status = 'ACTIVE' LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDurationOfCycle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationOfCycle");
        final int _columnIndexOfStartDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startDate");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "status");
        final TrainingPlanEntity _result;
        if (_stmt.step()) {
          _result = new TrainingPlanEntity();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _result.setId(_tmpId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          _result.setName(_tmpName);
          final int _tmpDurationOfCycle;
          _tmpDurationOfCycle = (int) (_stmt.getLong(_columnIndexOfDurationOfCycle));
          _result.setDurationOfCycle(_tmpDurationOfCycle);
          final String _tmpStartDate;
          if (_stmt.isNull(_columnIndexOfStartDate)) {
            _tmpStartDate = null;
          } else {
            _tmpStartDate = _stmt.getText(_columnIndexOfStartDate);
          }
          _result.setStartDate(_tmpStartDate);
          final String _tmpStatus;
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmpStatus = null;
          } else {
            _tmpStatus = _stmt.getText(_columnIndexOfStatus);
          }
          _result.setStatus(_tmpStatus);
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
  public LiveData<List<TrainingDayEntity>> getDaysForPlan(final int planId) {
    final String _sql = "SELECT * FROM training_days WHERE trainingPlanId = ? ORDER BY dayOrder ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"training_days"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, planId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTrainingPlanId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "trainingPlanId");
        final int _columnIndexOfDayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dayName");
        final int _columnIndexOfDayOrder = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dayOrder");
        final int _columnIndexOfDaysGap = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "daysGap");
        final int _columnIndexOfWeekNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "weekNumber");
        final List<TrainingDayEntity> _result = new ArrayList<TrainingDayEntity>();
        while (_stmt.step()) {
          final TrainingDayEntity _item;
          _item = new TrainingDayEntity();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final int _tmpTrainingPlanId;
          _tmpTrainingPlanId = (int) (_stmt.getLong(_columnIndexOfTrainingPlanId));
          _item.setTrainingPlanId(_tmpTrainingPlanId);
          final String _tmpDayName;
          if (_stmt.isNull(_columnIndexOfDayName)) {
            _tmpDayName = null;
          } else {
            _tmpDayName = _stmt.getText(_columnIndexOfDayName);
          }
          _item.setDayName(_tmpDayName);
          final int _tmpDayOrder;
          _tmpDayOrder = (int) (_stmt.getLong(_columnIndexOfDayOrder));
          _item.setDayOrder(_tmpDayOrder);
          final int _tmpDaysGap;
          _tmpDaysGap = (int) (_stmt.getLong(_columnIndexOfDaysGap));
          _item.setDaysGap(_tmpDaysGap);
          final int _tmpWeekNumber;
          _tmpWeekNumber = (int) (_stmt.getLong(_columnIndexOfWeekNumber));
          _item.setWeekNumber(_tmpWeekNumber);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<TrainingDayEntity> getDaysForPlanSync(final int planId) {
    final String _sql = "SELECT * FROM training_days WHERE trainingPlanId = ? ORDER BY dayOrder ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, planId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTrainingPlanId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "trainingPlanId");
        final int _columnIndexOfDayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dayName");
        final int _columnIndexOfDayOrder = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dayOrder");
        final int _columnIndexOfDaysGap = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "daysGap");
        final int _columnIndexOfWeekNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "weekNumber");
        final List<TrainingDayEntity> _result = new ArrayList<TrainingDayEntity>();
        while (_stmt.step()) {
          final TrainingDayEntity _item;
          _item = new TrainingDayEntity();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final int _tmpTrainingPlanId;
          _tmpTrainingPlanId = (int) (_stmt.getLong(_columnIndexOfTrainingPlanId));
          _item.setTrainingPlanId(_tmpTrainingPlanId);
          final String _tmpDayName;
          if (_stmt.isNull(_columnIndexOfDayName)) {
            _tmpDayName = null;
          } else {
            _tmpDayName = _stmt.getText(_columnIndexOfDayName);
          }
          _item.setDayName(_tmpDayName);
          final int _tmpDayOrder;
          _tmpDayOrder = (int) (_stmt.getLong(_columnIndexOfDayOrder));
          _item.setDayOrder(_tmpDayOrder);
          final int _tmpDaysGap;
          _tmpDaysGap = (int) (_stmt.getLong(_columnIndexOfDaysGap));
          _item.setDaysGap(_tmpDaysGap);
          final int _tmpWeekNumber;
          _tmpWeekNumber = (int) (_stmt.getLong(_columnIndexOfWeekNumber));
          _item.setWeekNumber(_tmpWeekNumber);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<Integer> getCompletedPlansCount() {
    final String _sql = "SELECT COUNT(*) FROM training_plans WHERE status = 'COMPLETED'";
    return __db.getInvalidationTracker().createLiveData(new String[] {"training_plans"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
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
  void deleteAllPlans() {
    final String _sql = "DELETE FROM training_plans";
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

  @Override
  void deleteAllDays() {
    final String _sql = "DELETE FROM training_days";
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

  @Override
  void deleteAllExercises() {
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

  @Override
  public void markActivePlanAsCompleted() {
    final String _sql = "UPDATE training_plans SET status = 'COMPLETED' WHERE status = 'ACTIVE'";
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
