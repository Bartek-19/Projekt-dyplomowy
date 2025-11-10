package pl.pollub.android.powerstrongapp.data.local;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao;
import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.MovementPatternDao;
import pl.pollub.android.powerstrongapp.data.local.dao.MovementPatternDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.PlannedExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.PlannedExerciseDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.TargetMuscleGroupDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TargetMuscleGroupDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.UserDao;
import pl.pollub.android.powerstrongapp.data.local.dao.UserDao_Impl;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile TrainingPlanAndDayDao _trainingPlanAndDayDao;

  private volatile ExerciseDao _exerciseDao;

  private volatile PlannedExerciseDao _plannedExerciseDao;

  private volatile ExecutedSetDao _executedSetDao;

  private volatile MovementPatternDao _movementPatternDao;

  private volatile TargetMuscleGroupDao _targetMuscleGroupDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "b1fc1ab7efb1db3add2c16d07b812438", "3108b414ef21f36d61c2180d279eee2c") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER NOT NULL, `username` TEXT NOT NULL, `email` TEXT, `createDate` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `training_days` (`id` INTEGER NOT NULL, `trainingPlanId` INTEGER NOT NULL, `dayName` TEXT, `dayOrder` INTEGER NOT NULL, `weekNumber` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`trainingPlanId`) REFERENCES `training_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `training_plans` (`id` INTEGER NOT NULL, `name` TEXT, `durationOfCycle` INTEGER NOT NULL, `startDate` TEXT, `isActive` INTEGER NOT NULL, `effortType` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exercises` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `exerciseCategory` TEXT, `movementPatternId` INTEGER, `targetMuscleGroupId` INTEGER, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `executed_sets` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `plannedExerciseId` INTEGER NOT NULL, `setNumber` INTEGER NOT NULL, `executedReps` INTEGER NOT NULL, `weightUsed` REAL NOT NULL, `executionTimestamp` INTEGER NOT NULL, `syncStatus` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `planned_exercises` (`id` INTEGER NOT NULL, `trainingDayId` INTEGER NOT NULL, `exerciseName` TEXT NOT NULL, `exerciseDescription` TEXT, `exerciseOrder` INTEGER, `plannedSets` INTEGER, `plannedReps` INTEGER, `plannedWeight` REAL, `lastSyncTimestamp` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`trainingDayId`) REFERENCES `training_days`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `movement_patterns` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `target_muscle_groups` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b1fc1ab7efb1db3add2c16d07b812438')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `users`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `training_days`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `training_plans`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `exercises`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `executed_sets`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `planned_exercises`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `movement_patterns`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `target_muscle_groups`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(4);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createDate", new TableInfo.Column("createDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(connection, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenDelegate.ValidationResult(false, "users(pl.pollub.android.powerstrongapp.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final Map<String, TableInfo.Column> _columnsTrainingDays = new HashMap<String, TableInfo.Column>(5);
        _columnsTrainingDays.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("trainingPlanId", new TableInfo.Column("trainingPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("dayName", new TableInfo.Column("dayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("dayOrder", new TableInfo.Column("dayOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("weekNumber", new TableInfo.Column("weekNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTrainingDays = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTrainingDays.add(new TableInfo.ForeignKey("training_plans", "CASCADE", "NO ACTION", Arrays.asList("trainingPlanId"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesTrainingDays = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrainingDays = new TableInfo("training_days", _columnsTrainingDays, _foreignKeysTrainingDays, _indicesTrainingDays);
        final TableInfo _existingTrainingDays = TableInfo.read(connection, "training_days");
        if (!_infoTrainingDays.equals(_existingTrainingDays)) {
          return new RoomOpenDelegate.ValidationResult(false, "training_days(pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity).\n"
                  + " Expected:\n" + _infoTrainingDays + "\n"
                  + " Found:\n" + _existingTrainingDays);
        }
        final Map<String, TableInfo.Column> _columnsTrainingPlans = new HashMap<String, TableInfo.Column>(6);
        _columnsTrainingPlans.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("durationOfCycle", new TableInfo.Column("durationOfCycle", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("startDate", new TableInfo.Column("startDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("effortType", new TableInfo.Column("effortType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTrainingPlans = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTrainingPlans = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrainingPlans = new TableInfo("training_plans", _columnsTrainingPlans, _foreignKeysTrainingPlans, _indicesTrainingPlans);
        final TableInfo _existingTrainingPlans = TableInfo.read(connection, "training_plans");
        if (!_infoTrainingPlans.equals(_existingTrainingPlans)) {
          return new RoomOpenDelegate.ValidationResult(false, "training_plans(pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity).\n"
                  + " Expected:\n" + _infoTrainingPlans + "\n"
                  + " Found:\n" + _existingTrainingPlans);
        }
        final Map<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(6);
        _columnsExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("exerciseCategory", new TableInfo.Column("exerciseCategory", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("movementPatternId", new TableInfo.Column("movementPatternId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("targetMuscleGroupId", new TableInfo.Column("targetMuscleGroupId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExercises = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExercises = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExercises = new TableInfo("exercises", _columnsExercises, _foreignKeysExercises, _indicesExercises);
        final TableInfo _existingExercises = TableInfo.read(connection, "exercises");
        if (!_infoExercises.equals(_existingExercises)) {
          return new RoomOpenDelegate.ValidationResult(false, "exercises(pl.pollub.android.powerstrongapp.data.local.entity.ExerciseEntity).\n"
                  + " Expected:\n" + _infoExercises + "\n"
                  + " Found:\n" + _existingExercises);
        }
        final Map<String, TableInfo.Column> _columnsExecutedSets = new HashMap<String, TableInfo.Column>(7);
        _columnsExecutedSets.put("localId", new TableInfo.Column("localId", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSets.put("plannedExerciseId", new TableInfo.Column("plannedExerciseId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSets.put("setNumber", new TableInfo.Column("setNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSets.put("executedReps", new TableInfo.Column("executedReps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSets.put("weightUsed", new TableInfo.Column("weightUsed", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSets.put("executionTimestamp", new TableInfo.Column("executionTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSets.put("syncStatus", new TableInfo.Column("syncStatus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExecutedSets = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExecutedSets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExecutedSets = new TableInfo("executed_sets", _columnsExecutedSets, _foreignKeysExecutedSets, _indicesExecutedSets);
        final TableInfo _existingExecutedSets = TableInfo.read(connection, "executed_sets");
        if (!_infoExecutedSets.equals(_existingExecutedSets)) {
          return new RoomOpenDelegate.ValidationResult(false, "executed_sets(pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity).\n"
                  + " Expected:\n" + _infoExecutedSets + "\n"
                  + " Found:\n" + _existingExecutedSets);
        }
        final Map<String, TableInfo.Column> _columnsPlannedExercises = new HashMap<String, TableInfo.Column>(9);
        _columnsPlannedExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("trainingDayId", new TableInfo.Column("trainingDayId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("exerciseName", new TableInfo.Column("exerciseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("exerciseDescription", new TableInfo.Column("exerciseDescription", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("exerciseOrder", new TableInfo.Column("exerciseOrder", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("plannedSets", new TableInfo.Column("plannedSets", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("plannedReps", new TableInfo.Column("plannedReps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("plannedWeight", new TableInfo.Column("plannedWeight", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("lastSyncTimestamp", new TableInfo.Column("lastSyncTimestamp", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysPlannedExercises = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPlannedExercises.add(new TableInfo.ForeignKey("training_days", "CASCADE", "NO ACTION", Arrays.asList("trainingDayId"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesPlannedExercises = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlannedExercises = new TableInfo("planned_exercises", _columnsPlannedExercises, _foreignKeysPlannedExercises, _indicesPlannedExercises);
        final TableInfo _existingPlannedExercises = TableInfo.read(connection, "planned_exercises");
        if (!_infoPlannedExercises.equals(_existingPlannedExercises)) {
          return new RoomOpenDelegate.ValidationResult(false, "planned_exercises(pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity).\n"
                  + " Expected:\n" + _infoPlannedExercises + "\n"
                  + " Found:\n" + _existingPlannedExercises);
        }
        final Map<String, TableInfo.Column> _columnsMovementPatterns = new HashMap<String, TableInfo.Column>(2);
        _columnsMovementPatterns.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovementPatterns.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysMovementPatterns = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesMovementPatterns = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovementPatterns = new TableInfo("movement_patterns", _columnsMovementPatterns, _foreignKeysMovementPatterns, _indicesMovementPatterns);
        final TableInfo _existingMovementPatterns = TableInfo.read(connection, "movement_patterns");
        if (!_infoMovementPatterns.equals(_existingMovementPatterns)) {
          return new RoomOpenDelegate.ValidationResult(false, "movement_patterns(pl.pollub.android.powerstrongapp.data.local.entity.MovementPatternEntity).\n"
                  + " Expected:\n" + _infoMovementPatterns + "\n"
                  + " Found:\n" + _existingMovementPatterns);
        }
        final Map<String, TableInfo.Column> _columnsTargetMuscleGroups = new HashMap<String, TableInfo.Column>(2);
        _columnsTargetMuscleGroups.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetMuscleGroups.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTargetMuscleGroups = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTargetMuscleGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTargetMuscleGroups = new TableInfo("target_muscle_groups", _columnsTargetMuscleGroups, _foreignKeysTargetMuscleGroups, _indicesTargetMuscleGroups);
        final TableInfo _existingTargetMuscleGroups = TableInfo.read(connection, "target_muscle_groups");
        if (!_infoTargetMuscleGroups.equals(_existingTargetMuscleGroups)) {
          return new RoomOpenDelegate.ValidationResult(false, "target_muscle_groups(pl.pollub.android.powerstrongapp.data.local.entity.TargetMuscleGroupEntity).\n"
                  + " Expected:\n" + _infoTargetMuscleGroups + "\n"
                  + " Found:\n" + _existingTargetMuscleGroups);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "training_days", "training_plans", "exercises", "executed_sets", "planned_exercises", "movement_patterns", "target_muscle_groups");
  }

  @Override
  public void clearAllTables() {
    super.performClear(true, "users", "training_days", "training_plans", "exercises", "executed_sets", "planned_exercises", "movement_patterns", "target_muscle_groups");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrainingPlanAndDayDao.class, TrainingPlanAndDayDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExerciseDao.class, ExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlannedExerciseDao.class, PlannedExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExecutedSetDao.class, ExecutedSetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MovementPatternDao.class, MovementPatternDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TargetMuscleGroupDao.class, TargetMuscleGroupDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public TrainingPlanAndDayDao trainingPlanAndDayDao() {
    if (_trainingPlanAndDayDao != null) {
      return _trainingPlanAndDayDao;
    } else {
      synchronized(this) {
        if(_trainingPlanAndDayDao == null) {
          _trainingPlanAndDayDao = new TrainingPlanAndDayDao_Impl(this);
        }
        return _trainingPlanAndDayDao;
      }
    }
  }

  @Override
  public ExerciseDao exerciseDao() {
    if (_exerciseDao != null) {
      return _exerciseDao;
    } else {
      synchronized(this) {
        if(_exerciseDao == null) {
          _exerciseDao = new ExerciseDao_Impl(this);
        }
        return _exerciseDao;
      }
    }
  }

  @Override
  public PlannedExerciseDao plannedExerciseDao() {
    if (_plannedExerciseDao != null) {
      return _plannedExerciseDao;
    } else {
      synchronized(this) {
        if(_plannedExerciseDao == null) {
          _plannedExerciseDao = new PlannedExerciseDao_Impl(this);
        }
        return _plannedExerciseDao;
      }
    }
  }

  @Override
  public ExecutedSetDao executedSetDao() {
    if (_executedSetDao != null) {
      return _executedSetDao;
    } else {
      synchronized(this) {
        if(_executedSetDao == null) {
          _executedSetDao = new ExecutedSetDao_Impl(this);
        }
        return _executedSetDao;
      }
    }
  }

  @Override
  public MovementPatternDao movementPatternDao() {
    if (_movementPatternDao != null) {
      return _movementPatternDao;
    } else {
      synchronized(this) {
        if(_movementPatternDao == null) {
          _movementPatternDao = new MovementPatternDao_Impl(this);
        }
        return _movementPatternDao;
      }
    }
  }

  @Override
  public TargetMuscleGroupDao targetMuscleGroupDao() {
    if (_targetMuscleGroupDao != null) {
      return _targetMuscleGroupDao;
    } else {
      synchronized(this) {
        if(_targetMuscleGroupDao == null) {
          _targetMuscleGroupDao = new TargetMuscleGroupDao_Impl(this);
        }
        return _targetMuscleGroupDao;
      }
    }
  }
}
