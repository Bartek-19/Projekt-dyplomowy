package pl.pollub.android.powerstrongapp.model.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
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
import pl.pollub.android.powerstrongapp.model.entity.EffortType;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class EffortTypeDao_Impl implements EffortTypeDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<EffortType> __insertAdapterOfEffortType;

  private final EntityDeleteOrUpdateAdapter<EffortType> __deleteAdapterOfEffortType;

  public EffortTypeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfEffortType = new EntityInsertAdapter<EffortType>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `effort_type` (`id`,`effortTypeName`,`description`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final EffortType entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getEffortTypeName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getEffortTypeName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDescription());
        }
      }
    };
    this.__deleteAdapterOfEffortType = new EntityDeleteOrUpdateAdapter<EffortType>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `effort_type` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final EffortType entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public void insert(final EffortType effortType) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfEffortType.insert(_connection, effortType);
      return null;
    });
  }

  @Override
  public void delete(final EffortType effortType) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfEffortType.handle(_connection, effortType);
      return null;
    });
  }

  @Override
  public List<EffortType> getEffortType(final int id) {
    final String _sql = "SELECT * FROM effort_type WHERE id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfEffortTypeName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "effortTypeName");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final List<EffortType> _result = new ArrayList<EffortType>();
        while (_stmt.step()) {
          final EffortType _item;
          _item = new EffortType();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final String _tmpEffortTypeName;
          if (_stmt.isNull(_columnIndexOfEffortTypeName)) {
            _tmpEffortTypeName = null;
          } else {
            _tmpEffortTypeName = _stmt.getText(_columnIndexOfEffortTypeName);
          }
          _item.setEffortTypeName(_tmpEffortTypeName);
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          _item.setDescription(_tmpDescription);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<EffortType> getAllEffortTypes() {
    final String _sql = "SELECT * FROM effort_type";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfEffortTypeName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "effortTypeName");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final List<EffortType> _result = new ArrayList<EffortType>();
        while (_stmt.step()) {
          final EffortType _item;
          _item = new EffortType();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final String _tmpEffortTypeName;
          if (_stmt.isNull(_columnIndexOfEffortTypeName)) {
            _tmpEffortTypeName = null;
          } else {
            _tmpEffortTypeName = _stmt.getText(_columnIndexOfEffortTypeName);
          }
          _item.setEffortTypeName(_tmpEffortTypeName);
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          _item.setDescription(_tmpDescription);
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
