package pl.pollub.android.powerstrongapp.model;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.pollub.android.powerstrongapp.utils.DateConverter;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExecutedSetDao_Impl implements ExecutedSetDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExecutedSet> __insertAdapterOfExecutedSet;

  private final EntityDeleteOrUpdateAdapter<ExecutedSet> __deleteAdapterOfExecutedSet;

  private final EntityDeleteOrUpdateAdapter<ExecutedSet> __updateAdapterOfExecutedSet;

  public ExecutedSetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExecutedSet = new EntityInsertAdapter<ExecutedSet>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `executed_set` (`id`,`setNumber`,`executedReps`,`weightUsed`,`executionDate`,`user_training_plan_user_id`,`user_training_plan_plan_id`,`planned_exercise_id`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExecutedSet entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSetNumber());
        statement.bindLong(3, entity.getExecutedReps());
        statement.bindDouble(4, entity.getWeightUsed());
        final Long _tmp = DateConverter.fromDate(entity.getExecutionDate());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        statement.bindLong(6, entity.getUser_training_plan_user_id());
        statement.bindLong(7, entity.getUser_training_plan_plan_id());
        statement.bindLong(8, entity.getPlanned_exercise_id());
      }
    };
    this.__deleteAdapterOfExecutedSet = new EntityDeleteOrUpdateAdapter<ExecutedSet>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `executed_set` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExecutedSet entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExecutedSet = new EntityDeleteOrUpdateAdapter<ExecutedSet>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `executed_set` SET `id` = ?,`setNumber` = ?,`executedReps` = ?,`weightUsed` = ?,`executionDate` = ?,`user_training_plan_user_id` = ?,`user_training_plan_plan_id` = ?,`planned_exercise_id` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExecutedSet entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSetNumber());
        statement.bindLong(3, entity.getExecutedReps());
        statement.bindDouble(4, entity.getWeightUsed());
        final Long _tmp = DateConverter.fromDate(entity.getExecutionDate());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        statement.bindLong(6, entity.getUser_training_plan_user_id());
        statement.bindLong(7, entity.getUser_training_plan_plan_id());
        statement.bindLong(8, entity.getPlanned_exercise_id());
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public void insert(final ExecutedSet executedSet) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExecutedSet.insert(_connection, executedSet);
      return null;
    });
  }

  @Override
  public void delete(final ExecutedSet executedSet) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfExecutedSet.handle(_connection, executedSet);
      return null;
    });
  }

  @Override
  public void update(final ExecutedSet executedSet) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfExecutedSet.handle(_connection, executedSet);
      return null;
    });
  }

  @Override
  public List<ExecutedSet> getExecutedSetForId(final int id) {
    final String _sql = "SELECT * FROM executed_set WHERE id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSetNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "setNumber");
        final int _columnIndexOfExecutedReps = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executedReps");
        final int _columnIndexOfWeightUsed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "weightUsed");
        final int _columnIndexOfExecutionDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executionDate");
        final int _columnIndexOfUserTrainingPlanUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "user_training_plan_user_id");
        final int _columnIndexOfUserTrainingPlanPlanId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "user_training_plan_plan_id");
        final int _columnIndexOfPlannedExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "planned_exercise_id");
        final List<ExecutedSet> _result = new ArrayList<ExecutedSet>();
        while (_stmt.step()) {
          final ExecutedSet _item;
          _item = new ExecutedSet();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final int _tmpSetNumber;
          _tmpSetNumber = (int) (_stmt.getLong(_columnIndexOfSetNumber));
          _item.setSetNumber(_tmpSetNumber);
          final int _tmpExecutedReps;
          _tmpExecutedReps = (int) (_stmt.getLong(_columnIndexOfExecutedReps));
          _item.setExecutedReps(_tmpExecutedReps);
          final double _tmpWeightUsed;
          _tmpWeightUsed = _stmt.getDouble(_columnIndexOfWeightUsed);
          _item.setWeightUsed(_tmpWeightUsed);
          final Date _tmpExecutionDate;
          final Long _tmp;
          if (_stmt.isNull(_columnIndexOfExecutionDate)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(_columnIndexOfExecutionDate);
          }
          _tmpExecutionDate = DateConverter.toDate(_tmp);
          _item.setExecutionDate(_tmpExecutionDate);
          final int _tmpUser_training_plan_user_id;
          _tmpUser_training_plan_user_id = (int) (_stmt.getLong(_columnIndexOfUserTrainingPlanUserId));
          _item.setUser_training_plan_user_id(_tmpUser_training_plan_user_id);
          final int _tmpUser_training_plan_plan_id;
          _tmpUser_training_plan_plan_id = (int) (_stmt.getLong(_columnIndexOfUserTrainingPlanPlanId));
          _item.setUser_training_plan_plan_id(_tmpUser_training_plan_plan_id);
          final int _tmpPlanned_exercise_id;
          _tmpPlanned_exercise_id = (int) (_stmt.getLong(_columnIndexOfPlannedExerciseId));
          _item.setPlanned_exercise_id(_tmpPlanned_exercise_id);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExecutedSet> getExecutedSetForIdAndUserId(final int id, final int userId) {
    final String _sql = "SELECT * FROM executed_set WHERE id = ? AND user_training_plan_user_id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, userId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSetNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "setNumber");
        final int _columnIndexOfExecutedReps = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executedReps");
        final int _columnIndexOfWeightUsed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "weightUsed");
        final int _columnIndexOfExecutionDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executionDate");
        final int _columnIndexOfUserTrainingPlanUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "user_training_plan_user_id");
        final int _columnIndexOfUserTrainingPlanPlanId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "user_training_plan_plan_id");
        final int _columnIndexOfPlannedExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "planned_exercise_id");
        final List<ExecutedSet> _result = new ArrayList<ExecutedSet>();
        while (_stmt.step()) {
          final ExecutedSet _item;
          _item = new ExecutedSet();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final int _tmpSetNumber;
          _tmpSetNumber = (int) (_stmt.getLong(_columnIndexOfSetNumber));
          _item.setSetNumber(_tmpSetNumber);
          final int _tmpExecutedReps;
          _tmpExecutedReps = (int) (_stmt.getLong(_columnIndexOfExecutedReps));
          _item.setExecutedReps(_tmpExecutedReps);
          final double _tmpWeightUsed;
          _tmpWeightUsed = _stmt.getDouble(_columnIndexOfWeightUsed);
          _item.setWeightUsed(_tmpWeightUsed);
          final Date _tmpExecutionDate;
          final Long _tmp;
          if (_stmt.isNull(_columnIndexOfExecutionDate)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(_columnIndexOfExecutionDate);
          }
          _tmpExecutionDate = DateConverter.toDate(_tmp);
          _item.setExecutionDate(_tmpExecutionDate);
          final int _tmpUser_training_plan_user_id;
          _tmpUser_training_plan_user_id = (int) (_stmt.getLong(_columnIndexOfUserTrainingPlanUserId));
          _item.setUser_training_plan_user_id(_tmpUser_training_plan_user_id);
          final int _tmpUser_training_plan_plan_id;
          _tmpUser_training_plan_plan_id = (int) (_stmt.getLong(_columnIndexOfUserTrainingPlanPlanId));
          _item.setUser_training_plan_plan_id(_tmpUser_training_plan_plan_id);
          final int _tmpPlanned_exercise_id;
          _tmpPlanned_exercise_id = (int) (_stmt.getLong(_columnIndexOfPlannedExerciseId));
          _item.setPlanned_exercise_id(_tmpPlanned_exercise_id);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExecutedSet> getExecutedSetsForPlannedExerciseId(final int plannedExerciseId,
      final int userId) {
    final String _sql = "SELECT * FROM executed_set WHERE planned_exercise_id = ? AND user_training_plan_user_id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, plannedExerciseId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, userId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSetNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "setNumber");
        final int _columnIndexOfExecutedReps = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executedReps");
        final int _columnIndexOfWeightUsed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "weightUsed");
        final int _columnIndexOfExecutionDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executionDate");
        final int _columnIndexOfUserTrainingPlanUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "user_training_plan_user_id");
        final int _columnIndexOfUserTrainingPlanPlanId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "user_training_plan_plan_id");
        final int _columnIndexOfPlannedExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "planned_exercise_id");
        final List<ExecutedSet> _result = new ArrayList<ExecutedSet>();
        while (_stmt.step()) {
          final ExecutedSet _item;
          _item = new ExecutedSet();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final int _tmpSetNumber;
          _tmpSetNumber = (int) (_stmt.getLong(_columnIndexOfSetNumber));
          _item.setSetNumber(_tmpSetNumber);
          final int _tmpExecutedReps;
          _tmpExecutedReps = (int) (_stmt.getLong(_columnIndexOfExecutedReps));
          _item.setExecutedReps(_tmpExecutedReps);
          final double _tmpWeightUsed;
          _tmpWeightUsed = _stmt.getDouble(_columnIndexOfWeightUsed);
          _item.setWeightUsed(_tmpWeightUsed);
          final Date _tmpExecutionDate;
          final Long _tmp;
          if (_stmt.isNull(_columnIndexOfExecutionDate)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(_columnIndexOfExecutionDate);
          }
          _tmpExecutionDate = DateConverter.toDate(_tmp);
          _item.setExecutionDate(_tmpExecutionDate);
          final int _tmpUser_training_plan_user_id;
          _tmpUser_training_plan_user_id = (int) (_stmt.getLong(_columnIndexOfUserTrainingPlanUserId));
          _item.setUser_training_plan_user_id(_tmpUser_training_plan_user_id);
          final int _tmpUser_training_plan_plan_id;
          _tmpUser_training_plan_plan_id = (int) (_stmt.getLong(_columnIndexOfUserTrainingPlanPlanId));
          _item.setUser_training_plan_plan_id(_tmpUser_training_plan_plan_id);
          final int _tmpPlanned_exercise_id;
          _tmpPlanned_exercise_id = (int) (_stmt.getLong(_columnIndexOfPlannedExerciseId));
          _item.setPlanned_exercise_id(_tmpPlanned_exercise_id);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExecutedSet> getExecutedSetsForUsersTrainingPlan(final int userId, final int planId) {
    final String _sql = "SELECT * FROM executed_set WHERE user_training_plan_user_id = ? AND user_training_plan_plan_id = ? ORDER BY id";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, planId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSetNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "setNumber");
        final int _columnIndexOfExecutedReps = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executedReps");
        final int _columnIndexOfWeightUsed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "weightUsed");
        final int _columnIndexOfExecutionDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "executionDate");
        final int _columnIndexOfUserTrainingPlanUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "user_training_plan_user_id");
        final int _columnIndexOfUserTrainingPlanPlanId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "user_training_plan_plan_id");
        final int _columnIndexOfPlannedExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "planned_exercise_id");
        final List<ExecutedSet> _result = new ArrayList<ExecutedSet>();
        while (_stmt.step()) {
          final ExecutedSet _item;
          _item = new ExecutedSet();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final int _tmpSetNumber;
          _tmpSetNumber = (int) (_stmt.getLong(_columnIndexOfSetNumber));
          _item.setSetNumber(_tmpSetNumber);
          final int _tmpExecutedReps;
          _tmpExecutedReps = (int) (_stmt.getLong(_columnIndexOfExecutedReps));
          _item.setExecutedReps(_tmpExecutedReps);
          final double _tmpWeightUsed;
          _tmpWeightUsed = _stmt.getDouble(_columnIndexOfWeightUsed);
          _item.setWeightUsed(_tmpWeightUsed);
          final Date _tmpExecutionDate;
          final Long _tmp;
          if (_stmt.isNull(_columnIndexOfExecutionDate)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(_columnIndexOfExecutionDate);
          }
          _tmpExecutionDate = DateConverter.toDate(_tmp);
          _item.setExecutionDate(_tmpExecutionDate);
          final int _tmpUser_training_plan_user_id;
          _tmpUser_training_plan_user_id = (int) (_stmt.getLong(_columnIndexOfUserTrainingPlanUserId));
          _item.setUser_training_plan_user_id(_tmpUser_training_plan_user_id);
          final int _tmpUser_training_plan_plan_id;
          _tmpUser_training_plan_plan_id = (int) (_stmt.getLong(_columnIndexOfUserTrainingPlanPlanId));
          _item.setUser_training_plan_plan_id(_tmpUser_training_plan_plan_id);
          final int _tmpPlanned_exercise_id;
          _tmpPlanned_exercise_id = (int) (_stmt.getLong(_columnIndexOfPlannedExerciseId));
          _item.setPlanned_exercise_id(_tmpPlanned_exercise_id);
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
