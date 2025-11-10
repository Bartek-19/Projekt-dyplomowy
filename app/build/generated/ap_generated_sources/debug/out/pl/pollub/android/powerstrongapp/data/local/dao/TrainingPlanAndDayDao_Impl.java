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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TrainingPlanAndDayDao_Impl implements TrainingPlanAndDayDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TrainingPlanEntity> __insertAdapterOfTrainingPlanEntity;

  private final EntityInsertAdapter<TrainingDayEntity> __insertAdapterOfTrainingDayEntity;

  public TrainingPlanAndDayDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTrainingPlanEntity = new EntityInsertAdapter<TrainingPlanEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `training_plans` (`id`,`name`,`durationOfCycle`,`startDate`,`isActive`,`effortType`) VALUES (?,?,?,?,?,?)";
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
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(5, _tmp);
        if (entity.getEffortType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getEffortType());
        }
      }
    };
    this.__insertAdapterOfTrainingDayEntity = new EntityInsertAdapter<TrainingDayEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `training_days` (`id`,`trainingPlanId`,`dayName`,`dayOrder`,`weekNumber`) VALUES (?,?,?,?,?)";
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
        statement.bindLong(5, entity.getWeekNumber());
      }
    };
  }

  @Override
  public void insertTrainingPlan(final TrainingPlanEntity plan) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTrainingPlanEntity.insert(_connection, plan);
      return null;
    });
  }

  @Override
  public void insertTrainingDays(final List<TrainingDayEntity> days) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTrainingDayEntity.insert(_connection, days);
      return null;
    });
  }

  @Override
  public LiveData<TrainingPlanEntity> getActiveTrainingPlan() {
    final String _sql = "SELECT * FROM training_plans WHERE isActive = 1 LIMIT 1";
    return __db.getInvalidationTracker().createLiveData(new String[] {"training_plans"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDurationOfCycle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationOfCycle");
        final int _columnIndexOfStartDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startDate");
        final int _columnIndexOfIsActive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isActive");
        final int _columnIndexOfEffortType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "effortType");
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
          final boolean _tmpIsActive;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsActive));
          _tmpIsActive = _tmp != 0;
          _result.setActive(_tmpIsActive);
          final String _tmpEffortType;
          if (_stmt.isNull(_columnIndexOfEffortType)) {
            _tmpEffortType = null;
          } else {
            _tmpEffortType = _stmt.getText(_columnIndexOfEffortType);
          }
          _result.setEffortType(_tmpEffortType);
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
    final String _sql = "SELECT * FROM training_days WHERE trainingPlanId = ? ORDER BY weekNumber, dayOrder ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"training_days"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, planId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTrainingPlanId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "trainingPlanId");
        final int _columnIndexOfDayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dayName");
        final int _columnIndexOfDayOrder = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dayOrder");
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
