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
import pl.pollub.android.powerstrongapp.data.local.dao.EffortTypeDao;
import pl.pollub.android.powerstrongapp.data.local.dao.EffortTypeDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao;
import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseCategoryDao;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseCategoryDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.MovementPatternDao;
import pl.pollub.android.powerstrongapp.data.local.dao.MovementPatternDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.PlannedExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.PlannedExerciseDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.TargetMuscleGroupDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TargetMuscleGroupDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingMethodDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingMethodDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.UserDao;
import pl.pollub.android.powerstrongapp.data.local.dao.UserDao_Impl;
import pl.pollub.android.powerstrongapp.data.local.dao.UserRecordDao;
import pl.pollub.android.powerstrongapp.data.local.dao.UserRecordDao_Impl;

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

  private volatile ExerciseCategoryDao _exerciseCategoryDao;

  private volatile EffortTypeDao _effortTypeDao;

  private volatile TrainingMethodDao _trainingMethodDao;

  private volatile UserRecordDao _userRecordDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "94d87f902c5aa8918e79f97996d77562", "791fbcf590a3ff871e841c47cb7d0d74") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER NOT NULL, `username` TEXT NOT NULL, `email` TEXT, `createDate` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `training_days` (`id` INTEGER NOT NULL, `trainingPlanId` INTEGER NOT NULL, `dayName` TEXT, `dayOrder` INTEGER NOT NULL, `daysGap` INTEGER NOT NULL, `weekNumber` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`trainingPlanId`) REFERENCES `training_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_training_days_trainingPlanId` ON `training_days` (`trainingPlanId`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `training_plans` (`id` INTEGER NOT NULL, `name` TEXT, `durationOfCycle` INTEGER NOT NULL, `startDate` TEXT, `status` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exercises` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `exerciseCategory` TEXT, `isBodyweight` INTEGER NOT NULL, `movementPatternIds` TEXT, `targetMuscleGroupIds` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `executed_sets` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `plannedExerciseId` INTEGER NOT NULL, `setNumber` INTEGER NOT NULL, `executedReps` INTEGER NOT NULL, `weightUsed` REAL NOT NULL, `executionTimestamp` INTEGER NOT NULL, `syncStatus` INTEGER)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `planned_exercises` (`id` INTEGER NOT NULL, `trainingDayId` INTEGER NOT NULL, `exerciseName` TEXT NOT NULL, `exerciseDescription` TEXT, `exerciseOrder` INTEGER, `plannedSets` INTEGER, `plannedReps` INTEGER, `targetWeight` REAL, `suggestionType` TEXT, `suggestionValue` REAL, `effortType` TEXT, `lastSyncTimestamp` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`trainingDayId`) REFERENCES `training_days`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_planned_exercises_trainingDayId` ON `planned_exercises` (`trainingDayId`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `movement_patterns` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `target_muscle_groups` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exercise_categories` (`id` INTEGER NOT NULL, `name` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `effort_types` (`id` INTEGER NOT NULL, `name` TEXT, `description` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `training_methods` (`id` INTEGER NOT NULL, `name` TEXT, `durationOfCycle` INTEGER NOT NULL, `description` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `user_records` (`exerciseId` INTEGER NOT NULL, `exerciseName` TEXT, `currentOneRepMax` REAL, `isBodyweight` INTEGER NOT NULL, `lastUpdatedDate` TEXT, PRIMARY KEY(`exerciseId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '94d87f902c5aa8918e79f97996d77562')");
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
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `exercise_categories`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `effort_types`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `training_methods`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `user_records`");
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
        final Map<String, TableInfo.Column> _columnsTrainingDays = new HashMap<String, TableInfo.Column>(6);
        _columnsTrainingDays.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("trainingPlanId", new TableInfo.Column("trainingPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("dayName", new TableInfo.Column("dayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("dayOrder", new TableInfo.Column("dayOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("daysGap", new TableInfo.Column("daysGap", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDays.put("weekNumber", new TableInfo.Column("weekNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTrainingDays = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTrainingDays.add(new TableInfo.ForeignKey("training_plans", "CASCADE", "NO ACTION", Arrays.asList("trainingPlanId"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesTrainingDays = new HashSet<TableInfo.Index>(1);
        _indicesTrainingDays.add(new TableInfo.Index("index_training_days_trainingPlanId", false, Arrays.asList("trainingPlanId"), Arrays.asList("ASC")));
        final TableInfo _infoTrainingDays = new TableInfo("training_days", _columnsTrainingDays, _foreignKeysTrainingDays, _indicesTrainingDays);
        final TableInfo _existingTrainingDays = TableInfo.read(connection, "training_days");
        if (!_infoTrainingDays.equals(_existingTrainingDays)) {
          return new RoomOpenDelegate.ValidationResult(false, "training_days(pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity).\n"
                  + " Expected:\n" + _infoTrainingDays + "\n"
                  + " Found:\n" + _existingTrainingDays);
        }
        final Map<String, TableInfo.Column> _columnsTrainingPlans = new HashMap<String, TableInfo.Column>(5);
        _columnsTrainingPlans.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("durationOfCycle", new TableInfo.Column("durationOfCycle", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("startDate", new TableInfo.Column("startDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlans.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTrainingPlans = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTrainingPlans = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrainingPlans = new TableInfo("training_plans", _columnsTrainingPlans, _foreignKeysTrainingPlans, _indicesTrainingPlans);
        final TableInfo _existingTrainingPlans = TableInfo.read(connection, "training_plans");
        if (!_infoTrainingPlans.equals(_existingTrainingPlans)) {
          return new RoomOpenDelegate.ValidationResult(false, "training_plans(pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity).\n"
                  + " Expected:\n" + _infoTrainingPlans + "\n"
                  + " Found:\n" + _existingTrainingPlans);
        }
        final Map<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(7);
        _columnsExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("exerciseCategory", new TableInfo.Column("exerciseCategory", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("isBodyweight", new TableInfo.Column("isBodyweight", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("movementPatternIds", new TableInfo.Column("movementPatternIds", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("targetMuscleGroupIds", new TableInfo.Column("targetMuscleGroupIds", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
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
        _columnsExecutedSets.put("syncStatus", new TableInfo.Column("syncStatus", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExecutedSets = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExecutedSets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExecutedSets = new TableInfo("executed_sets", _columnsExecutedSets, _foreignKeysExecutedSets, _indicesExecutedSets);
        final TableInfo _existingExecutedSets = TableInfo.read(connection, "executed_sets");
        if (!_infoExecutedSets.equals(_existingExecutedSets)) {
          return new RoomOpenDelegate.ValidationResult(false, "executed_sets(pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity).\n"
                  + " Expected:\n" + _infoExecutedSets + "\n"
                  + " Found:\n" + _existingExecutedSets);
        }
        final Map<String, TableInfo.Column> _columnsPlannedExercises = new HashMap<String, TableInfo.Column>(12);
        _columnsPlannedExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("trainingDayId", new TableInfo.Column("trainingDayId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("exerciseName", new TableInfo.Column("exerciseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("exerciseDescription", new TableInfo.Column("exerciseDescription", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("exerciseOrder", new TableInfo.Column("exerciseOrder", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("plannedSets", new TableInfo.Column("plannedSets", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("plannedReps", new TableInfo.Column("plannedReps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("targetWeight", new TableInfo.Column("targetWeight", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("suggestionType", new TableInfo.Column("suggestionType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("suggestionValue", new TableInfo.Column("suggestionValue", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("effortType", new TableInfo.Column("effortType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercises.put("lastSyncTimestamp", new TableInfo.Column("lastSyncTimestamp", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysPlannedExercises = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPlannedExercises.add(new TableInfo.ForeignKey("training_days", "CASCADE", "NO ACTION", Arrays.asList("trainingDayId"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesPlannedExercises = new HashSet<TableInfo.Index>(1);
        _indicesPlannedExercises.add(new TableInfo.Index("index_planned_exercises_trainingDayId", false, Arrays.asList("trainingDayId"), Arrays.asList("ASC")));
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
        final Map<String, TableInfo.Column> _columnsExerciseCategories = new HashMap<String, TableInfo.Column>(2);
        _columnsExerciseCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseCategories.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExerciseCategories = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExerciseCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExerciseCategories = new TableInfo("exercise_categories", _columnsExerciseCategories, _foreignKeysExerciseCategories, _indicesExerciseCategories);
        final TableInfo _existingExerciseCategories = TableInfo.read(connection, "exercise_categories");
        if (!_infoExerciseCategories.equals(_existingExerciseCategories)) {
          return new RoomOpenDelegate.ValidationResult(false, "exercise_categories(pl.pollub.android.powerstrongapp.data.local.entity.ExerciseCategoryEntity).\n"
                  + " Expected:\n" + _infoExerciseCategories + "\n"
                  + " Found:\n" + _existingExerciseCategories);
        }
        final Map<String, TableInfo.Column> _columnsEffortTypes = new HashMap<String, TableInfo.Column>(3);
        _columnsEffortTypes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEffortTypes.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEffortTypes.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysEffortTypes = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesEffortTypes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEffortTypes = new TableInfo("effort_types", _columnsEffortTypes, _foreignKeysEffortTypes, _indicesEffortTypes);
        final TableInfo _existingEffortTypes = TableInfo.read(connection, "effort_types");
        if (!_infoEffortTypes.equals(_existingEffortTypes)) {
          return new RoomOpenDelegate.ValidationResult(false, "effort_types(pl.pollub.android.powerstrongapp.data.local.entity.EffortTypeEntity).\n"
                  + " Expected:\n" + _infoEffortTypes + "\n"
                  + " Found:\n" + _existingEffortTypes);
        }
        final Map<String, TableInfo.Column> _columnsTrainingMethods = new HashMap<String, TableInfo.Column>(4);
        _columnsTrainingMethods.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingMethods.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingMethods.put("durationOfCycle", new TableInfo.Column("durationOfCycle", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingMethods.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTrainingMethods = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTrainingMethods = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrainingMethods = new TableInfo("training_methods", _columnsTrainingMethods, _foreignKeysTrainingMethods, _indicesTrainingMethods);
        final TableInfo _existingTrainingMethods = TableInfo.read(connection, "training_methods");
        if (!_infoTrainingMethods.equals(_existingTrainingMethods)) {
          return new RoomOpenDelegate.ValidationResult(false, "training_methods(pl.pollub.android.powerstrongapp.data.local.entity.TrainingMethodEntity).\n"
                  + " Expected:\n" + _infoTrainingMethods + "\n"
                  + " Found:\n" + _existingTrainingMethods);
        }
        final Map<String, TableInfo.Column> _columnsUserRecords = new HashMap<String, TableInfo.Column>(5);
        _columnsUserRecords.put("exerciseId", new TableInfo.Column("exerciseId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserRecords.put("exerciseName", new TableInfo.Column("exerciseName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserRecords.put("currentOneRepMax", new TableInfo.Column("currentOneRepMax", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserRecords.put("isBodyweight", new TableInfo.Column("isBodyweight", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserRecords.put("lastUpdatedDate", new TableInfo.Column("lastUpdatedDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysUserRecords = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesUserRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserRecords = new TableInfo("user_records", _columnsUserRecords, _foreignKeysUserRecords, _indicesUserRecords);
        final TableInfo _existingUserRecords = TableInfo.read(connection, "user_records");
        if (!_infoUserRecords.equals(_existingUserRecords)) {
          return new RoomOpenDelegate.ValidationResult(false, "user_records(pl.pollub.android.powerstrongapp.data.local.entity.UserRecordEntity).\n"
                  + " Expected:\n" + _infoUserRecords + "\n"
                  + " Found:\n" + _existingUserRecords);
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "training_days", "training_plans", "exercises", "executed_sets", "planned_exercises", "movement_patterns", "target_muscle_groups", "exercise_categories", "effort_types", "training_methods", "user_records");
  }

  @Override
  public void clearAllTables() {
    super.performClear(true, "users", "training_days", "training_plans", "exercises", "executed_sets", "planned_exercises", "movement_patterns", "target_muscle_groups", "exercise_categories", "effort_types", "training_methods", "user_records");
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
    _typeConvertersMap.put(ExerciseCategoryDao.class, ExerciseCategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EffortTypeDao.class, EffortTypeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrainingMethodDao.class, TrainingMethodDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserRecordDao.class, UserRecordDao_Impl.getRequiredConverters());
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

  @Override
  public ExerciseCategoryDao exerciseCategoryDao() {
    if (_exerciseCategoryDao != null) {
      return _exerciseCategoryDao;
    } else {
      synchronized(this) {
        if(_exerciseCategoryDao == null) {
          _exerciseCategoryDao = new ExerciseCategoryDao_Impl(this);
        }
        return _exerciseCategoryDao;
      }
    }
  }

  @Override
  public EffortTypeDao effortTypeDao() {
    if (_effortTypeDao != null) {
      return _effortTypeDao;
    } else {
      synchronized(this) {
        if(_effortTypeDao == null) {
          _effortTypeDao = new EffortTypeDao_Impl(this);
        }
        return _effortTypeDao;
      }
    }
  }

  @Override
  public TrainingMethodDao trainingMethodDao() {
    if (_trainingMethodDao != null) {
      return _trainingMethodDao;
    } else {
      synchronized(this) {
        if(_trainingMethodDao == null) {
          _trainingMethodDao = new TrainingMethodDao_Impl(this);
        }
        return _trainingMethodDao;
      }
    }
  }

  @Override
  public UserRecordDao userRecordDao() {
    if (_userRecordDao != null) {
      return _userRecordDao;
    } else {
      synchronized(this) {
        if(_userRecordDao == null) {
          _userRecordDao = new UserRecordDao_Impl(this);
        }
        return _userRecordDao;
      }
    }
  }
}
