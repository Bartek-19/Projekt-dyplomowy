package pl.pollub.android.powerstrongapp.utils;

import java.util.List;
import java.util.stream.Collectors;

// Importy DTO i Encji
import pl.pollub.android.powerstrongapp.api.model.*;
import pl.pollub.android.powerstrongapp.data.local.entity.*;
import pl.pollub.android.powerstrongapp.data.local.entity.enums.SyncStatus;

/**
 * Klasa statyczna do mapowania obiektów DTO (Serwer) na Encje (Room) i na odwrót.
 * Używa wzorca Builder dla czytelności i bezpieczeństwa.
 */
public class DtoMapper {
    // MAPOWANIE DTO -> ENTITY
    public static UserEntity toUserEntity(UserDto userDto) {
        if (userDto == null) return null;

        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .createDate(userDto.getCreateDate())
                .build();
    }
    public static TrainingPlanEntity toTrainingPlanEntity(TrainingPlanFullDto trainingPlanFullDto) {
        if (trainingPlanFullDto == null) return null;
        return TrainingPlanEntity.builder()
                .id(trainingPlanFullDto.getId())
                .name(trainingPlanFullDto.getName())
                .durationOfCycle(trainingPlanFullDto.getDurationOfCycle())
                .startDate(trainingPlanFullDto.getStartDate())
                .status(trainingPlanFullDto.getStatus() != null ? trainingPlanFullDto.getStatus() : "ACTIVE")
                .build();
    }


    public static List<TrainingDayEntity> toTrainingDayEntityList(List<TrainingDayDto> trainingDayDtos) {
        if (trainingDayDtos == null) return List.of();
        return trainingDayDtos.stream()
                .map(trainingDayDto -> TrainingDayEntity.builder()
                        .id(trainingDayDto.getId())
                        .trainingPlanId(trainingDayDto.getTrainingPlanId())
                        .dayName(trainingDayDto.getDayName())
                        .dayOrder(trainingDayDto.getDayOrder())
                        .daysGap(trainingDayDto.getDaysGap())
                        .weekNumber(trainingDayDto.getWeekNumber())
                        .build())
                .collect(Collectors.toList());
    }
    public static List<PlannedExerciseEntity> toPlannedExerciseEntityList(List<PlannedExerciseDto> plannedExerciseDtos, int trainingDayId) {
        if (plannedExerciseDtos == null) return List.of();
        return plannedExerciseDtos.stream()
                .map(plannedExerciseDto -> PlannedExerciseEntity.builder()
                        .id(plannedExerciseDto.getId())
                        .trainingDayId(trainingDayId)
                        .exerciseName(plannedExerciseDto.getExerciseName())
                        .exerciseDescription(plannedExerciseDto.getExerciseDescription())
                        .exerciseOrder(plannedExerciseDto.getExerciseOrder())
                        .plannedSets(plannedExerciseDto.getPlannedSets())
                        .plannedReps(plannedExerciseDto.getPlannedReps())
                        .effortType(plannedExerciseDto.getEffortType())
                        .targetWeight(plannedExerciseDto.getTargetWeight())
                        .suggestionType(plannedExerciseDto.getSuggestionType())
                        .suggestionValue(plannedExerciseDto.getSuggestionValue())
                        .lastSyncTimestamp(System.currentTimeMillis())
                        .build())
                .collect(Collectors.toList());
    }
    public static ExecutedSetEntity toExecutedSetEntity(ExecutedSetDto executedSetDto, long timestamp, boolean isSynced) {
        if (executedSetDto == null) return null;

        return ExecutedSetEntity.builder()
                .plannedExerciseId(executedSetDto.getPlannedExerciseId())
                .setNumber(executedSetDto.getSetNumber())
                .executedReps(executedSetDto.getExecutedReps())
                .weightUsed(executedSetDto.getWeightUsed())
                .executionTimestamp(timestamp)
                .syncStatus(isSynced ? SyncStatus.SYNCED : SyncStatus.NOT_SYNCED)
                .build();
    }
    public static List<ExerciseEntity> toExerciseEntityList(List<ExerciseDto> exerciseDtos) {
        if (exerciseDtos == null) return List.of();
        return exerciseDtos.stream()
                .map(exerciseDto -> ExerciseEntity.builder()
                        .id(exerciseDto.getId())
                        .name(exerciseDto.getName())
                        .description(exerciseDto.getDescription())
                        .exerciseCategory(exerciseDto.getCategoryName())
                        .movementPatternIds(exerciseDto.getMovementPatternIds())
                        .targetMuscleGroupIds(exerciseDto.getTargetMuscleGroupIds())
                        .isBodyweight(exerciseDto.isBodyweight())
                        .build())
                .collect(Collectors.toList());
    }
    public static List<MovementPatternEntity> toMovementPatternEntityList(List<MovementPatternDto> movementPatternDtos) {
        if (movementPatternDtos == null) return List.of();
        return movementPatternDtos.stream()
                .map(movementPatternDto -> MovementPatternEntity.builder()
                        .id(movementPatternDto.getId())
                        .name(movementPatternDto.getName())
                        .build())
                .collect(Collectors.toList());
    }
    public static List<TargetMuscleGroupEntity> toTargetMuscleGroupEntityList(List<TargetMuscleGroupDto> targetMuscleGroupDtos) {
        if (targetMuscleGroupDtos == null) return List.of();
        return targetMuscleGroupDtos.stream()
                .map(targetMuscleGroupDto -> TargetMuscleGroupEntity.builder()
                        .id(targetMuscleGroupDto.getId())
                        .name(targetMuscleGroupDto.getName())
                        .build())
                .collect(Collectors.toList());
    }
    // MAPOWANIE ENTITY -> DTO
    public static ExecutedSetDto toExecutedSetDto(ExecutedSetEntity entity) {
        if (entity == null) return null;
        return ExecutedSetDto.builder()
                .plannedExerciseId(entity.getPlannedExerciseId())
                .setNumber(entity.getSetNumber())
                .executedReps(entity.getExecutedReps())
                .weightUsed(entity.getWeightUsed())
                .build();
    }

    public static PlannedExerciseDto toPlannedExerciseDto(PlannedExerciseEntity entity) {
        if (entity == null) return null;
        return PlannedExerciseDto.builder()
                .id(entity.getId())
                .exerciseName(entity.getExerciseName())
                .exerciseDescription(entity.getExerciseDescription())
                .exerciseOrder(entity.getExerciseOrder())
                .plannedSets(entity.getPlannedSets())
                .plannedReps(entity.getPlannedReps())
                .effortType(entity.getEffortType())
                .targetWeight(entity.getTargetWeight())
                .suggestionType(entity.getSuggestionType())
                .suggestionValue(entity.getSuggestionValue())
                .build();
    }

    public static TrainingDayDto toTrainingDayDto(TrainingDayEntity entity) {
        if (entity == null) return null;
        return TrainingDayDto.builder()
                .id(entity.getId())
                .trainingPlanId(entity.getTrainingPlanId())
                .dayName(entity.getDayName())
                .dayOrder(entity.getDayOrder())
                .daysGap(entity.getDaysGap())
                .weekNumber(entity.getWeekNumber())
                .build();
    }


    public static List<ExerciseCategoryEntity> toExerciseCategoryEntityList(List<ExerciseCategoryDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> ExerciseCategoryEntity.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .build())
                .collect(Collectors.toList());
    }
    public static List<EffortTypeEntity> toEffortTypeEntityList(List<EffortTypeDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(dto -> EffortTypeEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build()).collect(Collectors.toList());
    }



    public static List<TrainingMethodEntity> toTrainingMethodEntityList(List<TrainingMethodDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(dto -> TrainingMethodEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .durationOfCycle(dto.getDurationOfCycle() != null ? dto.getDurationOfCycle() : 0)
                .description(dto.getDescription())
                .build()).collect(Collectors.toList());
    }
    public static List<ExecutedSetDto> toExecutedSetDtoList(List<ExecutedSetEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(DtoMapper::toExecutedSetDto)
                .collect(Collectors.toList());
    }
    public static List<ExecutedHistoryDto> toExecutedHistoryDtoList(List<ExecutedSetWithExercise> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(DtoMapper::toExecutedHistoryDto)
                .collect(Collectors.toList());
    }

    private static ExecutedHistoryDto toExecutedHistoryDto(ExecutedSetWithExercise executedSetWithExercise) {
        if (executedSetWithExercise == null) return null;
        return ExecutedHistoryDto.builder()
                .setNumber(executedSetWithExercise.getSetNumber())
                .executedReps(executedSetWithExercise.getExecutedReps())
                .weightUsed(executedSetWithExercise.getWeightUsed())
                .executionTimestamp(executedSetWithExercise.getExecutionTimestamp())
                .exerciseName(executedSetWithExercise.getExerciseName())
                .build();
    }
}