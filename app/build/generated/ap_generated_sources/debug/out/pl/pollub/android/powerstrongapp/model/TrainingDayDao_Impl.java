package pl.pollub.android.powerstrongapp.model;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import pl.pollub.android.powerstrongapp.model.dao.TrainingDayDao;
import pl.pollub.android.powerstrongapp.model.entity.PlannedExercise;
import pl.pollub.android.powerstrongapp.model.entity.TrainingDay;
import pl.pollub.android.powerstrongapp.model.relations.TrainingDayWithExercises;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TrainingDayDao_Impl implements TrainingDayDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TrainingDay> __insertAdapterOfTrainingDay;

  public TrainingDayDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTrainingDay = new EntityInsertAdapter<TrainingDay>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `training_day` (`id`,`weekNumber`,`dayName`,`dayOrder`,`training_plan_id`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final TrainingDay entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWeekNumber());
        if (entity.getDayName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDayName());
        }
        statement.bindLong(4, entity.getDayOrder());
        statement.bindLong(5, entity.getTraining_plan_id());
      }
    };
  }

  @Override
  public void insert(final TrainingDay day) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTrainingDay.insert(_connection, day);
      return null;
    });
  }

  @Override
  public TrainingDayWithExercises getDayWithExercises(final int id) {
    final String _sql = "SELECT * FROM training_day WHERE id = ?";
    return DBUtil.performBlocking(__db, true, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfWeekNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "weekNumber");
        final int _columnIndexOfDayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dayName");
        final int _columnIndexOfDayOrder = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dayOrder");
        final int _columnIndexOfTrainingPlanId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "training_plan_id");
        final LongSparseArray<ArrayList<PlannedExercise>> _collectionPlannedExercises = new LongSparseArray<ArrayList<PlannedExercise>>();
        while (_stmt.step()) {
          final Long _tmpKey;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _stmt.getLong(_columnIndexOfId);
          }
          if (_tmpKey != null) {
            if (!_collectionPlannedExercises.containsKey(_tmpKey)) {
              _collectionPlannedExercises.put(_tmpKey, new ArrayList<PlannedExercise>());
            }
          }
        }
        _stmt.reset();
        __fetchRelationshipplannedExerciseAsplPollubAndroidPowerstrongappModelPlannedExercise(_connection, _collectionPlannedExercises);
        final TrainingDayWithExercises _result;
        if (_stmt.step()) {
          final TrainingDay _tmpTrainingDay;
          if (!(_stmt.isNull(_columnIndexOfId) && _stmt.isNull(_columnIndexOfWeekNumber) && _stmt.isNull(_columnIndexOfDayName) && _stmt.isNull(_columnIndexOfDayOrder) && _stmt.isNull(_columnIndexOfTrainingPlanId))) {
            _tmpTrainingDay = new TrainingDay();
            final int _tmpId;
            _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
            _tmpTrainingDay.setId(_tmpId);
            final int _tmpWeekNumber;
            _tmpWeekNumber = (int) (_stmt.getLong(_columnIndexOfWeekNumber));
            _tmpTrainingDay.setWeekNumber(_tmpWeekNumber);
            final String _tmpDayName;
            if (_stmt.isNull(_columnIndexOfDayName)) {
              _tmpDayName = null;
            } else {
              _tmpDayName = _stmt.getText(_columnIndexOfDayName);
            }
            _tmpTrainingDay.setDayName(_tmpDayName);
            final int _tmpDayOrder;
            _tmpDayOrder = (int) (_stmt.getLong(_columnIndexOfDayOrder));
            _tmpTrainingDay.setDayOrder(_tmpDayOrder);
            final int _tmpTraining_plan_id;
            _tmpTraining_plan_id = (int) (_stmt.getLong(_columnIndexOfTrainingPlanId));
            _tmpTrainingDay.setTraining_plan_id(_tmpTraining_plan_id);
          } else {
            _tmpTrainingDay = null;
          }
          final ArrayList<PlannedExercise> _tmpPlannedExercisesCollection;
          final Long _tmpKey_1;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _stmt.getLong(_columnIndexOfId);
          }
          if (_tmpKey_1 != null) {
            _tmpPlannedExercisesCollection = _collectionPlannedExercises.get(_tmpKey_1);
          } else {
            _tmpPlannedExercisesCollection = new ArrayList<PlannedExercise>();
          }
          _result = new TrainingDayWithExercises();
          _result.trainingDay = _tmpTrainingDay;
          _result.plannedExercises = _tmpPlannedExercisesCollection;
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

  private void __fetchRelationshipplannedExerciseAsplPollubAndroidPowerstrongappModelPlannedExercise(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<PlannedExercise>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshipplannedExerciseAsplPollubAndroidPowerstrongappModelPlannedExercise(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `id`,`exerciseOrder`,`plannedReps`,`plannedSets`,`plannedWeight`,`exercise_id`,`training_day_id`,`effort_type_id` FROM `planned_exercise` WHERE `training_day_id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      final int _itemKeyIndex = SQLiteStatementUtil.getColumnIndex(_stmt, "training_day_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfId = 0;
      final int _columnIndexOfExerciseOrder = 1;
      final int _columnIndexOfPlannedReps = 2;
      final int _columnIndexOfPlannedSets = 3;
      final int _columnIndexOfPlannedWeight = 4;
      final int _columnIndexOfExerciseId = 5;
      final int _columnIndexOfTrainingDayId = 6;
      final int _columnIndexOfEffortTypeId = 7;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<PlannedExercise> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final PlannedExercise _item_1;
          _item_1 = new PlannedExercise();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item_1.setId(_tmpId);
          final int _tmpExerciseOrder;
          _tmpExerciseOrder = (int) (_stmt.getLong(_columnIndexOfExerciseOrder));
          _item_1.setExerciseOrder(_tmpExerciseOrder);
          final int _tmpPlannedReps;
          _tmpPlannedReps = (int) (_stmt.getLong(_columnIndexOfPlannedReps));
          _item_1.setPlannedReps(_tmpPlannedReps);
          final int _tmpPlannedSets;
          _tmpPlannedSets = (int) (_stmt.getLong(_columnIndexOfPlannedSets));
          _item_1.setPlannedSets(_tmpPlannedSets);
          final float _tmpPlannedWeight;
          _tmpPlannedWeight = (float) (_stmt.getDouble(_columnIndexOfPlannedWeight));
          _item_1.setPlannedWeight(_tmpPlannedWeight);
          final int _tmpExercise_id;
          _tmpExercise_id = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _item_1.setExercise_id(_tmpExercise_id);
          final int _tmpTraining_day_id;
          _tmpTraining_day_id = (int) (_stmt.getLong(_columnIndexOfTrainingDayId));
          _item_1.setTraining_day_id(_tmpTraining_day_id);
          final int _tmpEffort_type_id;
          _tmpEffort_type_id = (int) (_stmt.getLong(_columnIndexOfEffortTypeId));
          _item_1.setEffort_type_id(_tmpEffort_type_id);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }
}
