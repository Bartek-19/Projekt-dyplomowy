package pl.pollub.android.powerstrongapp.model.dao;

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
import pl.pollub.android.powerstrongapp.model.entity.TargetMuscleGroup;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TargetMuscleGroupDao_Impl implements TargetMuscleGroupDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TargetMuscleGroup> __insertAdapterOfTargetMuscleGroup;

  public TargetMuscleGroupDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTargetMuscleGroup = new EntityInsertAdapter<TargetMuscleGroup>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `target_muscle_group` (`id`,`name`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final TargetMuscleGroup entity) {
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
  public void insert(final TargetMuscleGroup targetMuscleGroup) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTargetMuscleGroup.insert(_connection, targetMuscleGroup);
      return null;
    });
  }

  @Override
  public List<TargetMuscleGroup> getTargetMuscleGroup(final int id) {
    final String _sql = "SELECT * FROM target_muscle_group WHERE id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final List<TargetMuscleGroup> _result = new ArrayList<TargetMuscleGroup>();
        while (_stmt.step()) {
          final TargetMuscleGroup _item;
          _item = new TargetMuscleGroup();
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
