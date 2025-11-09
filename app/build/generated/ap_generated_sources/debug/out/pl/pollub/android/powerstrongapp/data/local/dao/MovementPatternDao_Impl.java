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
import pl.pollub.android.powerstrongapp.data.local.entity.MovementPatternEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class MovementPatternDao_Impl implements MovementPatternDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<MovementPatternEntity> __insertAdapterOfMovementPatternEntity;

  public MovementPatternDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfMovementPatternEntity = new EntityInsertAdapter<MovementPatternEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `movement_patterns` (`id`,`name`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final MovementPatternEntity entity) {
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
  public void insertAll(final List<MovementPatternEntity> patterns) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfMovementPatternEntity.insert(_connection, patterns);
      return null;
    });
  }

  @Override
  public List<MovementPatternEntity> getAllPatterns() {
    final String _sql = "SELECT * FROM movement_patterns";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final List<MovementPatternEntity> _result = new ArrayList<MovementPatternEntity>();
        while (_stmt.step()) {
          final MovementPatternEntity _item;
          _item = new MovementPatternEntity();
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
