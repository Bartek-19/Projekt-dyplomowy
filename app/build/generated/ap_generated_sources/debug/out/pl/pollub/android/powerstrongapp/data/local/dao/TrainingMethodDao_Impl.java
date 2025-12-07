package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingMethodEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TrainingMethodDao_Impl implements TrainingMethodDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TrainingMethodEntity> __insertAdapterOfTrainingMethodEntity;

  public TrainingMethodDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTrainingMethodEntity = new EntityInsertAdapter<TrainingMethodEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `training_methods` (`id`,`name`,`durationOfCycle`,`description`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final TrainingMethodEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        statement.bindLong(3, entity.getDurationOfCycle());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getDescription());
        }
      }
    };
  }

  @Override
  public void insertAll(final List<TrainingMethodEntity> list) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTrainingMethodEntity.insert(_connection, list);
      return null;
    });
  }

  @Override
  public LiveData<List<TrainingMethodEntity>> getAllTrainingMethods() {
    final String _sql = "SELECT * FROM training_methods ORDER BY id ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"training_methods"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDurationOfCycle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationOfCycle");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final List<TrainingMethodEntity> _result = new ArrayList<TrainingMethodEntity>();
        while (_stmt.step()) {
          final TrainingMethodEntity _item;
          _item = new TrainingMethodEntity();
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
          final int _tmpDurationOfCycle;
          _tmpDurationOfCycle = (int) (_stmt.getLong(_columnIndexOfDurationOfCycle));
          _item.setDurationOfCycle(_tmpDurationOfCycle);
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
