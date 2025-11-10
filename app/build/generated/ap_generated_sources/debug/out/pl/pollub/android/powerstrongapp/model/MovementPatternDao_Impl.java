package pl.pollub.android.powerstrongapp.model;

import androidx.annotation.NonNull;
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

import pl.pollub.android.powerstrongapp.model.dao.MovementPatternDao;
import pl.pollub.android.powerstrongapp.model.entity.Exercise;
import pl.pollub.android.powerstrongapp.model.entity.MovementPattern;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class MovementPatternDao_Impl implements MovementPatternDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<MovementPattern> __insertAdapterOfMovementPattern;

  public MovementPatternDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfMovementPattern = new EntityInsertAdapter<MovementPattern>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `movement_pattern` (`id`,`name`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final MovementPattern entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
      }
    };
  }

  @Override
  public void insert(final MovementPattern movementPattern) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfMovementPattern.insert(_connection, movementPattern);
      return null;
    });
  }

  @Override
  public List<MovementPattern> getAllMovementPatterns() {
    final String _sql = "SELECT * FROM movement_pattern";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final List<MovementPattern> _result = new ArrayList<MovementPattern>();
        while (_stmt.step()) {
          final MovementPattern _item;
          _item = new MovementPattern();
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
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<MovementPattern> getMovementPatternById(final int id) {
    final String _sql = "SELECT * FROM movement_pattern WHERE id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final List<MovementPattern> _result = new ArrayList<MovementPattern>();
        while (_stmt.step()) {
          final MovementPattern _item;
          _item = new MovementPattern();
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
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<MovementPattern> getMovementPatternsByExerciseId(final int exerciseId) {
    final String _sql = "SELECT mp.* FROM movement_pattern mp INNER JOIN exercise_has_movement_pattern ehmp ON mp.id = ehmp.movement_pattern_id INNER JOIN exercise e ON ehmp.exercise_id = e.id WHERE e.id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, exerciseId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final List<MovementPattern> _result = new ArrayList<MovementPattern>();
        while (_stmt.step()) {
          final MovementPattern _item;
          _item = new MovementPattern();
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
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Exercise> getExercisesByMovementPatternId(final int movementPatternId) {
    final String _sql = "SELECT e.* FROM exercise e INNER JOIN exercise_has_movement_pattern ehmp ON e.id = ehmp.exercise_id INNER JOIN movement_pattern mp ON ehmp.movement_pattern_id = mp.id WHERE mp.id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, movementPatternId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfExerciseCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exercise_category_id");
        final List<Exercise> _result = new ArrayList<Exercise>();
        while (_stmt.step()) {
          final Exercise _item;
          _item = new Exercise();
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
          final int _tmpExercise_category_id;
          _tmpExercise_category_id = (int) (_stmt.getLong(_columnIndexOfExerciseCategoryId));
          _item.setExercise_category_id(_tmpExercise_category_id);
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
