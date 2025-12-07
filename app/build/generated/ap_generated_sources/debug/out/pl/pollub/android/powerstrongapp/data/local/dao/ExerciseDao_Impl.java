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
import pl.pollub.android.powerstrongapp.data.local.Converters;
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExerciseDao_Impl implements ExerciseDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExerciseEntity> __insertAdapterOfExerciseEntity;

  public ExerciseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExerciseEntity = new EntityInsertAdapter<ExerciseEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercises` (`id`,`name`,`description`,`exerciseCategory`,`isBodyweight`,`movementPatternIds`,`targetMuscleGroupIds`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDescription());
        }
        if (entity.getExerciseCategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getExerciseCategory());
        }
        final int _tmp = entity.isBodyweight() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final String _tmp_1 = Converters.fromIntegerList(entity.getMovementPatternIds());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, _tmp_1);
        }
        final String _tmp_2 = Converters.fromIntegerList(entity.getTargetMuscleGroupIds());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, _tmp_2);
        }
      }
    };
  }

  @Override
  public void insertAllExercises(final List<ExerciseEntity> exercises) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExerciseEntity.insert(_connection, exercises);
      return null;
    });
  }

  @Override
  public LiveData<List<ExerciseEntity>> getAllExercises() {
    final String _sql = "SELECT * FROM exercises ORDER BY name ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"exercises"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfExerciseCategory = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseCategory");
        final int _columnIndexOfIsBodyweight = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isBodyweight");
        final int _columnIndexOfMovementPatternIds = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "movementPatternIds");
        final int _columnIndexOfTargetMuscleGroupIds = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetMuscleGroupIds");
        final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>();
        while (_stmt.step()) {
          final ExerciseEntity _item;
          _item = new ExerciseEntity();
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
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          _item.setDescription(_tmpDescription);
          final String _tmpExerciseCategory;
          if (_stmt.isNull(_columnIndexOfExerciseCategory)) {
            _tmpExerciseCategory = null;
          } else {
            _tmpExerciseCategory = _stmt.getText(_columnIndexOfExerciseCategory);
          }
          _item.setExerciseCategory(_tmpExerciseCategory);
          final boolean _tmpIsBodyweight;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsBodyweight));
          _tmpIsBodyweight = _tmp != 0;
          _item.setBodyweight(_tmpIsBodyweight);
          final List<Integer> _tmpMovementPatternIds;
          final String _tmp_1;
          if (_stmt.isNull(_columnIndexOfMovementPatternIds)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfMovementPatternIds);
          }
          _tmpMovementPatternIds = Converters.toIntegerList(_tmp_1);
          _item.setMovementPatternIds(_tmpMovementPatternIds);
          final List<Integer> _tmpTargetMuscleGroupIds;
          final String _tmp_2;
          if (_stmt.isNull(_columnIndexOfTargetMuscleGroupIds)) {
            _tmp_2 = null;
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfTargetMuscleGroupIds);
          }
          _tmpTargetMuscleGroupIds = Converters.toIntegerList(_tmp_2);
          _item.setTargetMuscleGroupIds(_tmpTargetMuscleGroupIds);
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
