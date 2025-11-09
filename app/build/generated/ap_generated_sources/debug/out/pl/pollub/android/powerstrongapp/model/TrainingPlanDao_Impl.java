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
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import pl.pollub.android.powerstrongapp.utils.DateConverter;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TrainingPlanDao_Impl implements TrainingPlanDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TrainingPlan> __insertAdapterOfTrainingPlan;

  public TrainingPlanDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTrainingPlan = new EntityInsertAdapter<TrainingPlan>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `training_plan` (`id`,`name`,`training_method_id`,`isPreset`,`created_by_user_id`,`createdDate`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final TrainingPlan entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        statement.bindLong(3, entity.getTraining_method_id());
        final int _tmp = entity.isPreset() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getCreated_by_user_id());
        final Long _tmp_1 = DateConverter.fromDate(entity.getCreatedDate());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp_1);
        }
      }
    };
  }

  @Override
  public void insert(final TrainingPlan plan) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTrainingPlan.insert(_connection, plan);
      return null;
    });
  }

  @Override
  public TrainingPlanWithDays getPlanWithDays(final int id) {
    final String _sql = "SELECT * FROM training_plan WHERE id = ?";
    return DBUtil.performBlocking(__db, true, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfTrainingMethodId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "training_method_id");
        final int _columnIndexOfIsPreset = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isPreset");
        final int _columnIndexOfCreatedByUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "created_by_user_id");
        final int _columnIndexOfCreatedDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdDate");
        final LongSparseArray<ArrayList<TrainingDay>> _collectionTrainingDays = new LongSparseArray<ArrayList<TrainingDay>>();
        while (_stmt.step()) {
          final Long _tmpKey;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _stmt.getLong(_columnIndexOfId);
          }
          if (_tmpKey != null) {
            if (!_collectionTrainingDays.containsKey(_tmpKey)) {
              _collectionTrainingDays.put(_tmpKey, new ArrayList<TrainingDay>());
            }
          }
        }
        _stmt.reset();
        __fetchRelationshiptrainingDayAsplPollubAndroidPowerstrongappModelTrainingDay(_connection, _collectionTrainingDays);
        final TrainingPlanWithDays _result;
        if (_stmt.step()) {
          final TrainingPlan _tmpPlan;
          if (!(_stmt.isNull(_columnIndexOfId) && _stmt.isNull(_columnIndexOfName) && _stmt.isNull(_columnIndexOfTrainingMethodId) && _stmt.isNull(_columnIndexOfIsPreset) && _stmt.isNull(_columnIndexOfCreatedByUserId) && _stmt.isNull(_columnIndexOfCreatedDate))) {
            _tmpPlan = new TrainingPlan();
            final int _tmpId;
            _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
            _tmpPlan.setId(_tmpId);
            final String _tmpName;
            if (_stmt.isNull(_columnIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _stmt.getText(_columnIndexOfName);
            }
            _tmpPlan.setName(_tmpName);
            final int _tmpTraining_method_id;
            _tmpTraining_method_id = (int) (_stmt.getLong(_columnIndexOfTrainingMethodId));
            _tmpPlan.setTraining_method_id(_tmpTraining_method_id);
            final boolean _tmpIsPreset;
            final int _tmp;
            _tmp = (int) (_stmt.getLong(_columnIndexOfIsPreset));
            _tmpIsPreset = _tmp != 0;
            _tmpPlan.setPreset(_tmpIsPreset);
            final int _tmpCreated_by_user_id;
            _tmpCreated_by_user_id = (int) (_stmt.getLong(_columnIndexOfCreatedByUserId));
            _tmpPlan.setCreated_by_user_id(_tmpCreated_by_user_id);
            final Date _tmpCreatedDate;
            final Long _tmp_1;
            if (_stmt.isNull(_columnIndexOfCreatedDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _stmt.getLong(_columnIndexOfCreatedDate);
            }
            _tmpCreatedDate = DateConverter.toDate(_tmp_1);
            _tmpPlan.setCreatedDate(_tmpCreatedDate);
          } else {
            _tmpPlan = null;
          }
          final ArrayList<TrainingDay> _tmpTrainingDaysCollection;
          final Long _tmpKey_1;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _stmt.getLong(_columnIndexOfId);
          }
          if (_tmpKey_1 != null) {
            _tmpTrainingDaysCollection = _collectionTrainingDays.get(_tmpKey_1);
          } else {
            _tmpTrainingDaysCollection = new ArrayList<TrainingDay>();
          }
          _result = new TrainingPlanWithDays();
          _result.plan = _tmpPlan;
          _result.trainingDays = _tmpTrainingDaysCollection;
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

  private void __fetchRelationshiptrainingDayAsplPollubAndroidPowerstrongappModelTrainingDay(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<TrainingDay>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshiptrainingDayAsplPollubAndroidPowerstrongappModelTrainingDay(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `id`,`weekNumber`,`dayName`,`dayOrder`,`training_plan_id` FROM `training_day` WHERE `training_plan_id` IN (");
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
      final int _itemKeyIndex = SQLiteStatementUtil.getColumnIndex(_stmt, "training_plan_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfId = 0;
      final int _columnIndexOfWeekNumber = 1;
      final int _columnIndexOfDayName = 2;
      final int _columnIndexOfDayOrder = 3;
      final int _columnIndexOfTrainingPlanId = 4;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<TrainingDay> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final TrainingDay _item_1;
          _item_1 = new TrainingDay();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item_1.setId(_tmpId);
          final int _tmpWeekNumber;
          _tmpWeekNumber = (int) (_stmt.getLong(_columnIndexOfWeekNumber));
          _item_1.setWeekNumber(_tmpWeekNumber);
          final String _tmpDayName;
          if (_stmt.isNull(_columnIndexOfDayName)) {
            _tmpDayName = null;
          } else {
            _tmpDayName = _stmt.getText(_columnIndexOfDayName);
          }
          _item_1.setDayName(_tmpDayName);
          final int _tmpDayOrder;
          _tmpDayOrder = (int) (_stmt.getLong(_columnIndexOfDayOrder));
          _item_1.setDayOrder(_tmpDayOrder);
          final int _tmpTraining_plan_id;
          _tmpTraining_plan_id = (int) (_stmt.getLong(_columnIndexOfTrainingPlanId));
          _item_1.setTraining_plan_id(_tmpTraining_plan_id);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }
}
