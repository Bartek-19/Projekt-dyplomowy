package pl.pollub.android.powerstrongapp.data.local.dao;

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
import pl.pollub.android.powerstrongapp.data.local.entity.TargetMuscleGroupEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TargetMuscleGroupDao_Impl implements TargetMuscleGroupDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TargetMuscleGroupEntity> __insertAdapterOfTargetMuscleGroupEntity;

  public TargetMuscleGroupDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTargetMuscleGroupEntity = new EntityInsertAdapter<TargetMuscleGroupEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `target_muscle_groups` (`id`,`name`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final TargetMuscleGroupEntity entity) {
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
  public void insertAll(final List<TargetMuscleGroupEntity> groups) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTargetMuscleGroupEntity.insert(_connection, groups);
      return null;
    });
  }

  @Override
  public List<TargetMuscleGroupEntity> getAllMuscleGroups() {
    final String _sql = "SELECT * FROM target_muscle_groups";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final List<TargetMuscleGroupEntity> _result = new ArrayList<TargetMuscleGroupEntity>();
        while (_stmt.step()) {
          final TargetMuscleGroupEntity _item;
          _item = new TargetMuscleGroupEntity();
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
