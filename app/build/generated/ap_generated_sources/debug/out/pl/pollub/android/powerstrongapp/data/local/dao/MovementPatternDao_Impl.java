package pl.pollub.android.powerstrongapp.data.local.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
