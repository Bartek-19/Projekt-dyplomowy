package pl.pollub.android.powerstrongapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ExerciseTargetMuscleGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExerciseTargetMuscleGroup relation);

    @Delete
    void delete(ExerciseTargetMuscleGroup relation);

    @Query("SELECT * FROM exercise_target_muscle_group WHERE exercise_id = :exerciseId")
    List<ExerciseTargetMuscleGroup> getTargetMuscleGroupIdsForExerciseId(int exerciseId);

    @Query("SELECT * FROM exercise_target_muscle_group WHERE target_muscle_group_id = :targetMuscleGroupId")
    List<ExerciseTargetMuscleGroup> getExerciseIdsForTargetMuscleGroupId(int targetMuscleGroupId);

    @Transaction
    @Query("SELECT tmg.* FROM target_muscle_group tmg INNER JOIN exercise_target_muscle_group etmg ON tmg.id = etmg.target_muscle_group_id WHERE etmg.exercise_id = :exerciseId")
    List<TargetMuscleGroup> getTargetMuscleGroupsForExerciseId(int exerciseId);
}
