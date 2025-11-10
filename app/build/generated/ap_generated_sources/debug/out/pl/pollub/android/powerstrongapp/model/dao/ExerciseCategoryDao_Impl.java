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
import pl.pollub.android.powerstrongapp.model.entity.ExerciseCategory;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExerciseCategoryDao_Impl implements ExerciseCategoryDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExerciseCategory> __insertAdapterOfExerciseCategory;

  private final EntityDeleteOrUpdateAdapter<ExerciseCategory> __deleteAdapterOfExerciseCategory;

  public ExerciseCategoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExerciseCategory = new EntityInsertAdapter<ExerciseCategory>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercise_category` (`id`,`categoryName`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExerciseCategory entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCategoryName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCategoryName());
        }
      }
    };
    this.__deleteAdapterOfExerciseCategory = new EntityDeleteOrUpdateAdapter<ExerciseCategory>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exercise_category` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExerciseCategory entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public void insert(final ExerciseCategory exerciseCategory) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExerciseCategory.insert(_connection, exerciseCategory);
      return null;
    });
  }

  @Override
  public void delete(final ExerciseCategory exerciseCategory) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfExerciseCategory.handle(_connection, exerciseCategory);
      return null;
    });
  }

  @Override
  public List<ExerciseCategory> getAllExerciseCategories() {
    final String _sql = "SELECT * FROM exercise_category";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfCategoryName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryName");
        final List<ExerciseCategory> _result = new ArrayList<ExerciseCategory>();
        while (_stmt.step()) {
          final ExerciseCategory _item;
          _item = new ExerciseCategory();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final String _tmpCategoryName;
          if (_stmt.isNull(_columnIndexOfCategoryName)) {
            _tmpCategoryName = null;
          } else {
            _tmpCategoryName = _stmt.getText(_columnIndexOfCategoryName);
          }
          _item.setCategoryName(_tmpCategoryName);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExerciseCategory> getExerciseCategory(final int id) {
    final String _sql = "SELECT * FROM exercise_category WHERE id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfCategoryName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryName");
        final List<ExerciseCategory> _result = new ArrayList<ExerciseCategory>();
        while (_stmt.step()) {
          final ExerciseCategory _item;
          _item = new ExerciseCategory();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final String _tmpCategoryName;
          if (_stmt.isNull(_columnIndexOfCategoryName)) {
            _tmpCategoryName = null;
          } else {
            _tmpCategoryName = _stmt.getText(_columnIndexOfCategoryName);
          }
          _item.setCategoryName(_tmpCategoryName);
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
