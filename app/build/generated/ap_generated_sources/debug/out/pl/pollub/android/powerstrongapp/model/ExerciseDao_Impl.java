package pl.pollub.android.powerstrongapp.model;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import pl.pollub.android.powerstrongapp.model.dao.ExerciseDao;
import pl.pollub.android.powerstrongapp.model.entity.Exercise;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseCategory;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseHasMovementPattern;
import pl.pollub.android.powerstrongapp.model.entity.ExerciseTargetMuscleGroup;
import pl.pollub.android.powerstrongapp.model.relations.ExerciseWithDetails;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExerciseDao_Impl implements ExerciseDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Exercise> __insertAdapterOfExercise;

  private final EntityDeleteOrUpdateAdapter<Exercise> __deleteAdapterOfExercise;

  private final EntityDeleteOrUpdateAdapter<Exercise> __updateAdapterOfExercise;

  public ExerciseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExercise = new EntityInsertAdapter<Exercise>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercise` (`id`,`name`,`description`,`exercise_category_id`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Exercise entity) {
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
        statement.bindLong(4, entity.getExercise_category_id());
      }
    };
    this.__deleteAdapterOfExercise = new EntityDeleteOrUpdateAdapter<Exercise>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exercise` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Exercise entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExercise = new EntityDeleteOrUpdateAdapter<Exercise>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exercise` SET `id` = ?,`name` = ?,`description` = ?,`exercise_category_id` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Exercise entity) {
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
        statement.bindLong(4, entity.getExercise_category_id());
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public void insert(final Exercise exercise) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExercise.insert(_connection, exercise);
      return null;
    });
  }

  @Override
  public void delete(final Exercise exercise) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfExercise.handle(_connection, exercise);
      return null;
    });
  }

  @Override
  public void update(final Exercise exercise) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfExercise.handle(_connection, exercise);
      return null;
    });
  }

  @Override
  public List<Exercise> getExerciseById(final int id) {
    final String _sql = "SELECT * FROM exercise WHERE id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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

  @Override
  public List<Exercise> getExercisesByName(final String name) {
    final String _sql = "SELECT * FROM exercise WHERE name LIKE ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (name == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, name);
        }
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

  @Override
  public List<Exercise> getExercisesByPhrase(final String phrase) {
    final String _sql = "SELECT * FROM exercise WHERE name LIKE ? OR description LIKE ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (phrase == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, phrase);
        }
        _argIndex = 2;
        if (phrase == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, phrase);
        }
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

  @Override
  public List<Exercise> getExercisesByExerciseCategoryId(final int exerciseCategoryId) {
    final String _sql = "SELECT * FROM exercise WHERE exercise_category_id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, exerciseCategoryId);
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

  @Override
  public ExerciseWithDetails getExerciseDetails(final int id) {
    final String _sql = "SELECT * FROM exercise WHERE id = ?";
    return DBUtil.performBlocking(__db, true, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfExerciseCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exercise_category_id");
        final LongSparseArray<ArrayList<ExerciseTargetMuscleGroup>> _collectionTargetMuscleGroups = new LongSparseArray<ArrayList<ExerciseTargetMuscleGroup>>();
        final LongSparseArray<ArrayList<ExerciseHasMovementPattern>> _collectionMovementPatterns = new LongSparseArray<ArrayList<ExerciseHasMovementPattern>>();
        final LongSparseArray<ExerciseCategory> _collectionCategory = new LongSparseArray<ExerciseCategory>();
        while (_stmt.step()) {
          final Long _tmpKey;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _stmt.getLong(_columnIndexOfId);
          }
          if (_tmpKey != null) {
            if (!_collectionTargetMuscleGroups.containsKey(_tmpKey)) {
              _collectionTargetMuscleGroups.put(_tmpKey, new ArrayList<ExerciseTargetMuscleGroup>());
            }
          }
          final Long _tmpKey_1;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _stmt.getLong(_columnIndexOfId);
          }
          if (_tmpKey_1 != null) {
            if (!_collectionMovementPatterns.containsKey(_tmpKey_1)) {
              _collectionMovementPatterns.put(_tmpKey_1, new ArrayList<ExerciseHasMovementPattern>());
            }
          }
          final Long _tmpKey_2;
          if (_stmt.isNull(_columnIndexOfExerciseCategoryId)) {
            _tmpKey_2 = null;
          } else {
            _tmpKey_2 = _stmt.getLong(_columnIndexOfExerciseCategoryId);
          }
          if (_tmpKey_2 != null) {
            _collectionCategory.put(_tmpKey_2, null);
          }
        }
        _stmt.reset();
        __fetchRelationshipexerciseTargetMuscleGroupAsplPollubAndroidPowerstrongappModelExerciseTargetMuscleGroup(_connection, _collectionTargetMuscleGroups);
        __fetchRelationshipexerciseHasMovementPatternAsplPollubAndroidPowerstrongappModelExerciseHasMovementPattern(_connection, _collectionMovementPatterns);
        __fetchRelationshipexerciseCategoryAsplPollubAndroidPowerstrongappModelExerciseCategory(_connection, _collectionCategory);
        final ExerciseWithDetails _result;
        if (_stmt.step()) {
          final Exercise _tmpExercise;
          if (!(_stmt.isNull(_columnIndexOfId) && _stmt.isNull(_columnIndexOfName) && _stmt.isNull(_columnIndexOfDescription) && _stmt.isNull(_columnIndexOfExerciseCategoryId))) {
            _tmpExercise = new Exercise();
            final int _tmpId;
            _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
            _tmpExercise.setId(_tmpId);
            final String _tmpName;
            if (_stmt.isNull(_columnIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _stmt.getText(_columnIndexOfName);
            }
            _tmpExercise.setName(_tmpName);
            final String _tmpDescription;
            if (_stmt.isNull(_columnIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _stmt.getText(_columnIndexOfDescription);
            }
            _tmpExercise.setDescription(_tmpDescription);
            final int _tmpExercise_category_id;
            _tmpExercise_category_id = (int) (_stmt.getLong(_columnIndexOfExerciseCategoryId));
            _tmpExercise.setExercise_category_id(_tmpExercise_category_id);
          } else {
            _tmpExercise = null;
          }
          final ArrayList<ExerciseTargetMuscleGroup> _tmpTargetMuscleGroupsCollection;
          final Long _tmpKey_3;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpKey_3 = null;
          } else {
            _tmpKey_3 = _stmt.getLong(_columnIndexOfId);
          }
          if (_tmpKey_3 != null) {
            _tmpTargetMuscleGroupsCollection = _collectionTargetMuscleGroups.get(_tmpKey_3);
          } else {
            _tmpTargetMuscleGroupsCollection = new ArrayList<ExerciseTargetMuscleGroup>();
          }
          final ArrayList<ExerciseHasMovementPattern> _tmpMovementPatternsCollection;
          final Long _tmpKey_4;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpKey_4 = null;
          } else {
            _tmpKey_4 = _stmt.getLong(_columnIndexOfId);
          }
          if (_tmpKey_4 != null) {
            _tmpMovementPatternsCollection = _collectionMovementPatterns.get(_tmpKey_4);
          } else {
            _tmpMovementPatternsCollection = new ArrayList<ExerciseHasMovementPattern>();
          }
          final ExerciseCategory _tmpCategory;
          final Long _tmpKey_5;
          if (_stmt.isNull(_columnIndexOfExerciseCategoryId)) {
            _tmpKey_5 = null;
          } else {
            _tmpKey_5 = _stmt.getLong(_columnIndexOfExerciseCategoryId);
          }
          if (_tmpKey_5 != null) {
            _tmpCategory = _collectionCategory.get(_tmpKey_5);
          } else {
            _tmpCategory = null;
          }
          _result = new ExerciseWithDetails();
          _result.exercise = _tmpExercise;
          _result.targetMuscleGroups = _tmpTargetMuscleGroupsCollection;
          _result.movementPatterns = _tmpMovementPatternsCollection;
          _result.category = _tmpCategory;
        } else {
          _result = null;
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

  private void __fetchRelationshipexerciseTargetMuscleGroupAsplPollubAndroidPowerstrongappModelExerciseTargetMuscleGroup(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<ExerciseTargetMuscleGroup>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshipexerciseTargetMuscleGroupAsplPollubAndroidPowerstrongappModelExerciseTargetMuscleGroup(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `exercise_id`,`target_muscle_group_id` FROM `exercise_target_muscle_group` WHERE `exercise_id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      final int _itemKeyIndex = SQLiteStatementUtil.getColumnIndex(_stmt, "exercise_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfExerciseId = 0;
      final int _columnIndexOfTargetMuscleGroupId = 1;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<ExerciseTargetMuscleGroup> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ExerciseTargetMuscleGroup _item_1;
          _item_1 = new ExerciseTargetMuscleGroup();
          final int _tmpExercise_id;
          _tmpExercise_id = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _item_1.setExercise_id(_tmpExercise_id);
          final int _tmpTarget_muscle_group_id;
          _tmpTarget_muscle_group_id = (int) (_stmt.getLong(_columnIndexOfTargetMuscleGroupId));
          _item_1.setTarget_muscle_group_id(_tmpTarget_muscle_group_id);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }

  private void __fetchRelationshipexerciseHasMovementPatternAsplPollubAndroidPowerstrongappModelExerciseHasMovementPattern(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<ExerciseHasMovementPattern>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshipexerciseHasMovementPatternAsplPollubAndroidPowerstrongappModelExerciseHasMovementPattern(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `exercise_id`,`movement_pattern_id` FROM `exercise_has_movement_pattern` WHERE `exercise_id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      final int _itemKeyIndex = SQLiteStatementUtil.getColumnIndex(_stmt, "exercise_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfExerciseId = 0;
      final int _columnIndexOfMovementPatternId = 1;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<ExerciseHasMovementPattern> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ExerciseHasMovementPattern _item_1;
          _item_1 = new ExerciseHasMovementPattern();
          final int _tmpExercise_id;
          _tmpExercise_id = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _item_1.setExercise_id(_tmpExercise_id);
          final int _tmpMovement_pattern_id;
          _tmpMovement_pattern_id = (int) (_stmt.getLong(_columnIndexOfMovementPatternId));
          _item_1.setMovement_pattern_id(_tmpMovement_pattern_id);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }

  private void __fetchRelationshipexerciseCategoryAsplPollubAndroidPowerstrongappModelExerciseCategory(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ExerciseCategory> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (_tmpMap) -> {
        __fetchRelationshipexerciseCategoryAsplPollubAndroidPowerstrongappModelExerciseCategory(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `id`,`categoryName` FROM `exercise_category` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      final int _itemKeyIndex = SQLiteStatementUtil.getColumnIndex(_stmt, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfId = 0;
      final int _columnIndexOfCategoryName = 1;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final ExerciseCategory _item_1;
          _item_1 = new ExerciseCategory();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item_1.setId(_tmpId);
          final String _tmpCategoryName;
          if (_stmt.isNull(_columnIndexOfCategoryName)) {
            _tmpCategoryName = null;
          } else {
            _tmpCategoryName = _stmt.getText(_columnIndexOfCategoryName);
          }
          _item_1.setCategoryName(_tmpCategoryName);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }
}
