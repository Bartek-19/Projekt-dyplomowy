package pl.pollub.android.powerstrongapp.model;

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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile TrainingPlanDao _trainingPlanDao;

  private volatile TrainingDayDao _trainingDayDao;

  private volatile ExerciseDao _exerciseDao;

  private volatile ExecutedSetDao _executedSetDao;

  private volatile EffortTypeDao _effortTypeDao;

  private volatile ExerciseCategoryDao _exerciseCategoryDao;

  private volatile MovementPatternDao _movementPatternDao;

  private volatile TargetMuscleGroupDao _targetMuscleGroupDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "6a4eff9b73c2445bf7ae5dc02f690443", "7ec1a65c1f3b771da132fab84c3f6bc8") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `user` (`id` INTEGER NOT NULL, `username` TEXT, `password` TEXT, `email` TEXT, `activeStatus` INTEGER NOT NULL, `createDate` INTEGER, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `user_training_plan` (`user_id` INTEGER NOT NULL, `training_plan_id` INTEGER NOT NULL, `startDate` INTEGER, `endDate` INTEGER, `isActive` INTEGER NOT NULL, `wasTrackingNutrition` INTEGER NOT NULL, `wasTrackingSleep` INTEGER NOT NULL, `averageHoursOfSleep` INTEGER NOT NULL, `personalEvaluation` INTEGER NOT NULL, PRIMARY KEY(`user_id`, `training_plan_id`), FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`training_plan_id`) REFERENCES `training_plan`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `training_plan` (`id` INTEGER NOT NULL, `name` TEXT, `training_method_id` INTEGER NOT NULL, `isPreset` INTEGER NOT NULL, `created_by_user_id` INTEGER NOT NULL, `createdDate` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`training_method_id`) REFERENCES `training_method`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`created_by_user_id`) REFERENCES `user`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `training_method` (`id` INTEGER NOT NULL, `name` TEXT, `durationOfCycle` INTEGER NOT NULL, `description` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `training_day` (`id` INTEGER NOT NULL, `weekNumber` INTEGER NOT NULL, `dayName` TEXT, `dayOrder` INTEGER NOT NULL, `training_plan_id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`training_plan_id`) REFERENCES `training_plan`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exercise` (`id` INTEGER NOT NULL, `name` TEXT, `description` TEXT, `exercise_category_id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`exercise_category_id`) REFERENCES `exercise_category`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exercise_category` (`id` INTEGER NOT NULL, `categoryName` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exercise_has_movement_pattern` (`exercise_id` INTEGER NOT NULL, `movement_pattern_id` INTEGER NOT NULL, PRIMARY KEY(`exercise_id`, `movement_pattern_id`), FOREIGN KEY(`exercise_id`) REFERENCES `exercise`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`movement_pattern_id`) REFERENCES `movement_pattern`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `movement_pattern` (`id` INTEGER NOT NULL, `name` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exercise_target_muscle_group` (`exercise_id` INTEGER NOT NULL, `target_muscle_group_id` INTEGER NOT NULL, PRIMARY KEY(`exercise_id`, `target_muscle_group_id`), FOREIGN KEY(`exercise_id`) REFERENCES `exercise`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`target_muscle_group_id`) REFERENCES `target_muscle_group`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `target_muscle_group` (`id` INTEGER NOT NULL, `name` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `effort_type` (`id` INTEGER NOT NULL, `effortTypeName` TEXT NOT NULL, `description` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `planned_exercise` (`id` INTEGER NOT NULL, `exerciseOrder` INTEGER NOT NULL, `plannedReps` INTEGER NOT NULL, `plannedSets` INTEGER NOT NULL, `plannedWeight` REAL NOT NULL, `exercise_id` INTEGER NOT NULL, `training_day_id` INTEGER NOT NULL, `effort_type_id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`training_day_id`) REFERENCES `training_day`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`exercise_id`) REFERENCES `exercise`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`effort_type_id`) REFERENCES `effort_type`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `executed_set` (`id` INTEGER NOT NULL, `setNumber` INTEGER NOT NULL, `executedReps` INTEGER NOT NULL, `weightUsed` REAL NOT NULL, `executionDate` INTEGER, `user_training_plan_user_id` INTEGER NOT NULL, `user_training_plan_plan_id` INTEGER NOT NULL, `planned_exercise_id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`planned_exercise_id`) REFERENCES `planned_exercise`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`user_training_plan_user_id`, `user_training_plan_plan_id`) REFERENCES `user_training_plan`(`user_id`, `training_plan_id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '6a4eff9b73c2445bf7ae5dc02f690443')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `user`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `user_training_plan`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `training_plan`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `training_method`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `training_day`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `exercise`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `exercise_category`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `exercise_has_movement_pattern`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `movement_pattern`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `exercise_target_muscle_group`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `target_muscle_group`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `effort_type`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `planned_exercise`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `executed_set`");
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
        final Map<String, TableInfo.Column> _columnsUser = new HashMap<String, TableInfo.Column>(6);
        _columnsUser.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("username", new TableInfo.Column("username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("password", new TableInfo.Column("password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("activeStatus", new TableInfo.Column("activeStatus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("createDate", new TableInfo.Column("createDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysUser = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesUser = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUser = new TableInfo("user", _columnsUser, _foreignKeysUser, _indicesUser);
        final TableInfo _existingUser = TableInfo.read(connection, "user");
        if (!_infoUser.equals(_existingUser)) {
          return new RoomOpenDelegate.ValidationResult(false, "user(pl.pollub.android.powerstrongapp.model.User).\n"
                  + " Expected:\n" + _infoUser + "\n"
                  + " Found:\n" + _existingUser);
        }
        final Map<String, TableInfo.Column> _columnsUserTrainingPlan = new HashMap<String, TableInfo.Column>(9);
        _columnsUserTrainingPlan.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTrainingPlan.put("training_plan_id", new TableInfo.Column("training_plan_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTrainingPlan.put("startDate", new TableInfo.Column("startDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTrainingPlan.put("endDate", new TableInfo.Column("endDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTrainingPlan.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTrainingPlan.put("wasTrackingNutrition", new TableInfo.Column("wasTrackingNutrition", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTrainingPlan.put("wasTrackingSleep", new TableInfo.Column("wasTrackingSleep", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTrainingPlan.put("averageHoursOfSleep", new TableInfo.Column("averageHoursOfSleep", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTrainingPlan.put("personalEvaluation", new TableInfo.Column("personalEvaluation", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysUserTrainingPlan = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysUserTrainingPlan.add(new TableInfo.ForeignKey("user", "NO ACTION", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("id")));
        _foreignKeysUserTrainingPlan.add(new TableInfo.ForeignKey("training_plan", "NO ACTION", "NO ACTION", Arrays.asList("training_plan_id"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesUserTrainingPlan = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserTrainingPlan = new TableInfo("user_training_plan", _columnsUserTrainingPlan, _foreignKeysUserTrainingPlan, _indicesUserTrainingPlan);
        final TableInfo _existingUserTrainingPlan = TableInfo.read(connection, "user_training_plan");
        if (!_infoUserTrainingPlan.equals(_existingUserTrainingPlan)) {
          return new RoomOpenDelegate.ValidationResult(false, "user_training_plan(pl.pollub.android.powerstrongapp.model.UserTrainingPlan).\n"
                  + " Expected:\n" + _infoUserTrainingPlan + "\n"
                  + " Found:\n" + _existingUserTrainingPlan);
        }
        final Map<String, TableInfo.Column> _columnsTrainingPlan = new HashMap<String, TableInfo.Column>(6);
        _columnsTrainingPlan.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlan.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlan.put("training_method_id", new TableInfo.Column("training_method_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlan.put("isPreset", new TableInfo.Column("isPreset", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlan.put("created_by_user_id", new TableInfo.Column("created_by_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingPlan.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTrainingPlan = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTrainingPlan.add(new TableInfo.ForeignKey("training_method", "NO ACTION", "NO ACTION", Arrays.asList("training_method_id"), Arrays.asList("id")));
        _foreignKeysTrainingPlan.add(new TableInfo.ForeignKey("user", "NO ACTION", "NO ACTION", Arrays.asList("created_by_user_id"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesTrainingPlan = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrainingPlan = new TableInfo("training_plan", _columnsTrainingPlan, _foreignKeysTrainingPlan, _indicesTrainingPlan);
        final TableInfo _existingTrainingPlan = TableInfo.read(connection, "training_plan");
        if (!_infoTrainingPlan.equals(_existingTrainingPlan)) {
          return new RoomOpenDelegate.ValidationResult(false, "training_plan(pl.pollub.android.powerstrongapp.model.TrainingPlan).\n"
                  + " Expected:\n" + _infoTrainingPlan + "\n"
                  + " Found:\n" + _existingTrainingPlan);
        }
        final Map<String, TableInfo.Column> _columnsTrainingMethod = new HashMap<String, TableInfo.Column>(4);
        _columnsTrainingMethod.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingMethod.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingMethod.put("durationOfCycle", new TableInfo.Column("durationOfCycle", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingMethod.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTrainingMethod = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTrainingMethod = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrainingMethod = new TableInfo("training_method", _columnsTrainingMethod, _foreignKeysTrainingMethod, _indicesTrainingMethod);
        final TableInfo _existingTrainingMethod = TableInfo.read(connection, "training_method");
        if (!_infoTrainingMethod.equals(_existingTrainingMethod)) {
          return new RoomOpenDelegate.ValidationResult(false, "training_method(pl.pollub.android.powerstrongapp.model.TrainingMethod).\n"
                  + " Expected:\n" + _infoTrainingMethod + "\n"
                  + " Found:\n" + _existingTrainingMethod);
        }
        final Map<String, TableInfo.Column> _columnsTrainingDay = new HashMap<String, TableInfo.Column>(5);
        _columnsTrainingDay.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDay.put("weekNumber", new TableInfo.Column("weekNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDay.put("dayName", new TableInfo.Column("dayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDay.put("dayOrder", new TableInfo.Column("dayOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrainingDay.put("training_plan_id", new TableInfo.Column("training_plan_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTrainingDay = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTrainingDay.add(new TableInfo.ForeignKey("training_plan", "NO ACTION", "NO ACTION", Arrays.asList("training_plan_id"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesTrainingDay = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrainingDay = new TableInfo("training_day", _columnsTrainingDay, _foreignKeysTrainingDay, _indicesTrainingDay);
        final TableInfo _existingTrainingDay = TableInfo.read(connection, "training_day");
        if (!_infoTrainingDay.equals(_existingTrainingDay)) {
          return new RoomOpenDelegate.ValidationResult(false, "training_day(pl.pollub.android.powerstrongapp.model.TrainingDay).\n"
                  + " Expected:\n" + _infoTrainingDay + "\n"
                  + " Found:\n" + _existingTrainingDay);
        }
        final Map<String, TableInfo.Column> _columnsExercise = new HashMap<String, TableInfo.Column>(4);
        _columnsExercise.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercise.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercise.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercise.put("exercise_category_id", new TableInfo.Column("exercise_category_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExercise = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExercise.add(new TableInfo.ForeignKey("exercise_category", "NO ACTION", "NO ACTION", Arrays.asList("exercise_category_id"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesExercise = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExercise = new TableInfo("exercise", _columnsExercise, _foreignKeysExercise, _indicesExercise);
        final TableInfo _existingExercise = TableInfo.read(connection, "exercise");
        if (!_infoExercise.equals(_existingExercise)) {
          return new RoomOpenDelegate.ValidationResult(false, "exercise(pl.pollub.android.powerstrongapp.model.Exercise).\n"
                  + " Expected:\n" + _infoExercise + "\n"
                  + " Found:\n" + _existingExercise);
        }
        final Map<String, TableInfo.Column> _columnsExerciseCategory = new HashMap<String, TableInfo.Column>(2);
        _columnsExerciseCategory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseCategory.put("categoryName", new TableInfo.Column("categoryName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExerciseCategory = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExerciseCategory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExerciseCategory = new TableInfo("exercise_category", _columnsExerciseCategory, _foreignKeysExerciseCategory, _indicesExerciseCategory);
        final TableInfo _existingExerciseCategory = TableInfo.read(connection, "exercise_category");
        if (!_infoExerciseCategory.equals(_existingExerciseCategory)) {
          return new RoomOpenDelegate.ValidationResult(false, "exercise_category(pl.pollub.android.powerstrongapp.model.ExerciseCategory).\n"
                  + " Expected:\n" + _infoExerciseCategory + "\n"
                  + " Found:\n" + _existingExerciseCategory);
        }
        final Map<String, TableInfo.Column> _columnsExerciseHasMovementPattern = new HashMap<String, TableInfo.Column>(2);
        _columnsExerciseHasMovementPattern.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseHasMovementPattern.put("movement_pattern_id", new TableInfo.Column("movement_pattern_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExerciseHasMovementPattern = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysExerciseHasMovementPattern.add(new TableInfo.ForeignKey("exercise", "NO ACTION", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        _foreignKeysExerciseHasMovementPattern.add(new TableInfo.ForeignKey("movement_pattern", "NO ACTION", "NO ACTION", Arrays.asList("movement_pattern_id"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesExerciseHasMovementPattern = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExerciseHasMovementPattern = new TableInfo("exercise_has_movement_pattern", _columnsExerciseHasMovementPattern, _foreignKeysExerciseHasMovementPattern, _indicesExerciseHasMovementPattern);
        final TableInfo _existingExerciseHasMovementPattern = TableInfo.read(connection, "exercise_has_movement_pattern");
        if (!_infoExerciseHasMovementPattern.equals(_existingExerciseHasMovementPattern)) {
          return new RoomOpenDelegate.ValidationResult(false, "exercise_has_movement_pattern(pl.pollub.android.powerstrongapp.model.ExerciseHasMovementPattern).\n"
                  + " Expected:\n" + _infoExerciseHasMovementPattern + "\n"
                  + " Found:\n" + _existingExerciseHasMovementPattern);
        }
        final Map<String, TableInfo.Column> _columnsMovementPattern = new HashMap<String, TableInfo.Column>(2);
        _columnsMovementPattern.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovementPattern.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysMovementPattern = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesMovementPattern = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovementPattern = new TableInfo("movement_pattern", _columnsMovementPattern, _foreignKeysMovementPattern, _indicesMovementPattern);
        final TableInfo _existingMovementPattern = TableInfo.read(connection, "movement_pattern");
        if (!_infoMovementPattern.equals(_existingMovementPattern)) {
          return new RoomOpenDelegate.ValidationResult(false, "movement_pattern(pl.pollub.android.powerstrongapp.model.MovementPattern).\n"
                  + " Expected:\n" + _infoMovementPattern + "\n"
                  + " Found:\n" + _existingMovementPattern);
        }
        final Map<String, TableInfo.Column> _columnsExerciseTargetMuscleGroup = new HashMap<String, TableInfo.Column>(2);
        _columnsExerciseTargetMuscleGroup.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseTargetMuscleGroup.put("target_muscle_group_id", new TableInfo.Column("target_muscle_group_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExerciseTargetMuscleGroup = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysExerciseTargetMuscleGroup.add(new TableInfo.ForeignKey("exercise", "NO ACTION", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        _foreignKeysExerciseTargetMuscleGroup.add(new TableInfo.ForeignKey("target_muscle_group", "NO ACTION", "NO ACTION", Arrays.asList("target_muscle_group_id"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesExerciseTargetMuscleGroup = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExerciseTargetMuscleGroup = new TableInfo("exercise_target_muscle_group", _columnsExerciseTargetMuscleGroup, _foreignKeysExerciseTargetMuscleGroup, _indicesExerciseTargetMuscleGroup);
        final TableInfo _existingExerciseTargetMuscleGroup = TableInfo.read(connection, "exercise_target_muscle_group");
        if (!_infoExerciseTargetMuscleGroup.equals(_existingExerciseTargetMuscleGroup)) {
          return new RoomOpenDelegate.ValidationResult(false, "exercise_target_muscle_group(pl.pollub.android.powerstrongapp.model.ExerciseTargetMuscleGroup).\n"
                  + " Expected:\n" + _infoExerciseTargetMuscleGroup + "\n"
                  + " Found:\n" + _existingExerciseTargetMuscleGroup);
        }
        final Map<String, TableInfo.Column> _columnsTargetMuscleGroup = new HashMap<String, TableInfo.Column>(2);
        _columnsTargetMuscleGroup.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetMuscleGroup.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTargetMuscleGroup = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTargetMuscleGroup = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTargetMuscleGroup = new TableInfo("target_muscle_group", _columnsTargetMuscleGroup, _foreignKeysTargetMuscleGroup, _indicesTargetMuscleGroup);
        final TableInfo _existingTargetMuscleGroup = TableInfo.read(connection, "target_muscle_group");
        if (!_infoTargetMuscleGroup.equals(_existingTargetMuscleGroup)) {
          return new RoomOpenDelegate.ValidationResult(false, "target_muscle_group(pl.pollub.android.powerstrongapp.model.TargetMuscleGroup).\n"
                  + " Expected:\n" + _infoTargetMuscleGroup + "\n"
                  + " Found:\n" + _existingTargetMuscleGroup);
        }
        final Map<String, TableInfo.Column> _columnsEffortType = new HashMap<String, TableInfo.Column>(3);
        _columnsEffortType.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEffortType.put("effortTypeName", new TableInfo.Column("effortTypeName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEffortType.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysEffortType = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesEffortType = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEffortType = new TableInfo("effort_type", _columnsEffortType, _foreignKeysEffortType, _indicesEffortType);
        final TableInfo _existingEffortType = TableInfo.read(connection, "effort_type");
        if (!_infoEffortType.equals(_existingEffortType)) {
          return new RoomOpenDelegate.ValidationResult(false, "effort_type(pl.pollub.android.powerstrongapp.model.EffortType).\n"
                  + " Expected:\n" + _infoEffortType + "\n"
                  + " Found:\n" + _existingEffortType);
        }
        final Map<String, TableInfo.Column> _columnsPlannedExercise = new HashMap<String, TableInfo.Column>(8);
        _columnsPlannedExercise.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercise.put("exerciseOrder", new TableInfo.Column("exerciseOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercise.put("plannedReps", new TableInfo.Column("plannedReps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercise.put("plannedSets", new TableInfo.Column("plannedSets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercise.put("plannedWeight", new TableInfo.Column("plannedWeight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercise.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercise.put("training_day_id", new TableInfo.Column("training_day_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedExercise.put("effort_type_id", new TableInfo.Column("effort_type_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysPlannedExercise = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysPlannedExercise.add(new TableInfo.ForeignKey("training_day", "NO ACTION", "NO ACTION", Arrays.asList("training_day_id"), Arrays.asList("id")));
        _foreignKeysPlannedExercise.add(new TableInfo.ForeignKey("exercise", "NO ACTION", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        _foreignKeysPlannedExercise.add(new TableInfo.ForeignKey("effort_type", "NO ACTION", "NO ACTION", Arrays.asList("effort_type_id"), Arrays.asList("id")));
        final Set<TableInfo.Index> _indicesPlannedExercise = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlannedExercise = new TableInfo("planned_exercise", _columnsPlannedExercise, _foreignKeysPlannedExercise, _indicesPlannedExercise);
        final TableInfo _existingPlannedExercise = TableInfo.read(connection, "planned_exercise");
        if (!_infoPlannedExercise.equals(_existingPlannedExercise)) {
          return new RoomOpenDelegate.ValidationResult(false, "planned_exercise(pl.pollub.android.powerstrongapp.model.PlannedExercise).\n"
                  + " Expected:\n" + _infoPlannedExercise + "\n"
                  + " Found:\n" + _existingPlannedExercise);
        }
        final Map<String, TableInfo.Column> _columnsExecutedSet = new HashMap<String, TableInfo.Column>(8);
        _columnsExecutedSet.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSet.put("setNumber", new TableInfo.Column("setNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSet.put("executedReps", new TableInfo.Column("executedReps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSet.put("weightUsed", new TableInfo.Column("weightUsed", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSet.put("executionDate", new TableInfo.Column("executionDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSet.put("user_training_plan_user_id", new TableInfo.Column("user_training_plan_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSet.put("user_training_plan_plan_id", new TableInfo.Column("user_training_plan_plan_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExecutedSet.put("planned_exercise_id", new TableInfo.Column("planned_exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExecutedSet = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysExecutedSet.add(new TableInfo.ForeignKey("planned_exercise", "NO ACTION", "NO ACTION", Arrays.asList("planned_exercise_id"), Arrays.asList("id")));
        _foreignKeysExecutedSet.add(new TableInfo.ForeignKey("user_training_plan", "NO ACTION", "NO ACTION", Arrays.asList("user_training_plan_user_id", "user_training_plan_plan_id"), Arrays.asList("user_id", "training_plan_id")));
        final Set<TableInfo.Index> _indicesExecutedSet = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExecutedSet = new TableInfo("executed_set", _columnsExecutedSet, _foreignKeysExecutedSet, _indicesExecutedSet);
        final TableInfo _existingExecutedSet = TableInfo.read(connection, "executed_set");
        if (!_infoExecutedSet.equals(_existingExecutedSet)) {
          return new RoomOpenDelegate.ValidationResult(false, "executed_set(pl.pollub.android.powerstrongapp.model.ExecutedSet).\n"
                  + " Expected:\n" + _infoExecutedSet + "\n"
                  + " Found:\n" + _existingExecutedSet);
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "user", "user_training_plan", "training_plan", "training_method", "training_day", "exercise", "exercise_category", "exercise_has_movement_pattern", "movement_pattern", "exercise_target_muscle_group", "target_muscle_group", "effort_type", "planned_exercise", "executed_set");
  }

  @Override
  public void clearAllTables() {
    super.performClear(true, "user_training_plan", "training_plan", "user", "training_method", "training_day", "exercise_has_movement_pattern", "executed_set", "planned_exercise", "exercise", "exercise_category", "movement_pattern", "exercise_target_muscle_group", "target_muscle_group", "effort_type");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrainingPlanDao.class, TrainingPlanDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrainingDayDao.class, TrainingDayDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExerciseDao.class, ExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExecutedSetDao.class, ExecutedSetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EffortTypeDao.class, EffortTypeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExerciseCategoryDao.class, ExerciseCategoryDao_Impl.getRequiredConverters());
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
  public TrainingPlanDao trainingPlanDao() {
    if (_trainingPlanDao != null) {
      return _trainingPlanDao;
    } else {
      synchronized(this) {
        if(_trainingPlanDao == null) {
          _trainingPlanDao = new TrainingPlanDao_Impl(this);
        }
        return _trainingPlanDao;
      }
    }
  }

  @Override
  public TrainingDayDao trainingDayDao() {
    if (_trainingDayDao != null) {
      return _trainingDayDao;
    } else {
      synchronized(this) {
        if(_trainingDayDao == null) {
          _trainingDayDao = new TrainingDayDao_Impl(this);
        }
        return _trainingDayDao;
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
