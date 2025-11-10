package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.annotation.NonNull;
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
        return "INSERT OR REPLACE INTO `exercises` (`id`,`name`,`description`,`exerciseCategory`,`movementPatternId`,`targetMuscleGroupId`) VALUES (?,?,?,?,?,?)";
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
        if (entity.getMovementPatternId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getMovementPatternId());
        }
        if (entity.getTargetMuscleGroupId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getTargetMuscleGroupId());
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
  public ExerciseEntity getExerciseById(final int exerciseId) {
    final String _sql = "SELECT * FROM exercises WHERE id = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, exerciseId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfExerciseCategory = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseCategory");
        final int _columnIndexOfMovementPatternId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "movementPatternId");
        final int _columnIndexOfTargetMuscleGroupId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetMuscleGroupId");
        final ExerciseEntity _result;
        if (_stmt.step()) {
          _result = new ExerciseEntity();
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
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          _result.setDescription(_tmpDescription);
          final String _tmpExerciseCategory;
          if (_stmt.isNull(_columnIndexOfExerciseCategory)) {
            _tmpExerciseCategory = null;
          } else {
            _tmpExerciseCategory = _stmt.getText(_columnIndexOfExerciseCategory);
          }
          _result.setExerciseCategory(_tmpExerciseCategory);
          final Integer _tmpMovementPatternId;
          if (_stmt.isNull(_columnIndexOfMovementPatternId)) {
            _tmpMovementPatternId = null;
          } else {
            _tmpMovementPatternId = (int) (_stmt.getLong(_columnIndexOfMovementPatternId));
          }
          _result.setMovementPatternId(_tmpMovementPatternId);
          final Integer _tmpTargetMuscleGroupId;
          if (_stmt.isNull(_columnIndexOfTargetMuscleGroupId)) {
            _tmpTargetMuscleGroupId = null;
          } else {
            _tmpTargetMuscleGroupId = (int) (_stmt.getLong(_columnIndexOfTargetMuscleGroupId));
          }
          _result.setTargetMuscleGroupId(_tmpTargetMuscleGroupId);
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
  public List<ExerciseEntity> getAllExercises() {
    final String _sql = "SELECT * FROM exercises ORDER BY name ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfExerciseCategory = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseCategory");
        final int _columnIndexOfMovementPatternId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "movementPatternId");
        final int _columnIndexOfTargetMuscleGroupId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetMuscleGroupId");
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
          final Integer _tmpMovementPatternId;
          if (_stmt.isNull(_columnIndexOfMovementPatternId)) {
            _tmpMovementPatternId = null;
          } else {
            _tmpMovementPatternId = (int) (_stmt.getLong(_columnIndexOfMovementPatternId));
          }
          _item.setMovementPatternId(_tmpMovementPatternId);
          final Integer _tmpTargetMuscleGroupId;
          if (_stmt.isNull(_columnIndexOfTargetMuscleGroupId)) {
            _tmpTargetMuscleGroupId = null;
          } else {
            _tmpTargetMuscleGroupId = (int) (_stmt.getLong(_columnIndexOfTargetMuscleGroupId));
          }
          _item.setTargetMuscleGroupId(_tmpTargetMuscleGroupId);
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
