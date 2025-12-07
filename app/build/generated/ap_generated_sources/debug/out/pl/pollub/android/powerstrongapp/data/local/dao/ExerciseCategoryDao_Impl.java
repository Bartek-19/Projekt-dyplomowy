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
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseCategoryEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExerciseCategoryDao_Impl implements ExerciseCategoryDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExerciseCategoryEntity> __insertAdapterOfExerciseCategoryEntity;

  public ExerciseCategoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExerciseCategoryEntity = new EntityInsertAdapter<ExerciseCategoryEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercise_categories` (`id`,`name`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final ExerciseCategoryEntity entity) {
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
  public void insertAll(final List<ExerciseCategoryEntity> list) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExerciseCategoryEntity.insert(_connection, list);
      return null;
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
