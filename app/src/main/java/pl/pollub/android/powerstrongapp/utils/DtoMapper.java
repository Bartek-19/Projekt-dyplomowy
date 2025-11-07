package pl.pollub.android.powerstrongapp.utils;

import java.util.List;
import java.util.stream.Collectors;

// Importy dla DTO (zakładamy, że masz je w api.model)
import pl.pollub.android.powerstrongapp.api.model.*;
// Importy dla Encji (zakładamy, że masz je w data.local.entity)
import pl.pollub.android.powerstrongapp.data.local.entity.*;

/**
 * Klasa statyczna do mapowania obiektów DTO (Serwer) na Encje (Room) i na odwrót.
 * Utrzymuje separację między warstwą sieciową a bazodanową (Single Source of Truth).
 */
public class DtoMapper {

    // =========================================================================
    // I. MAPOWANIE DTO -> ENTITY (Pobieranie danych z serwera do Room)
    // =========================================================================

    /** 1. Mapuje UserDto na UserEntity. */
    public static UserEntity toUserEntity(UserDto dto) {
        if (dto == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setCreateDate(dto.getCreateDate());
        return entity;
    }

    /** 2. Mapuje TrainingPlanFullDto na TrainingPlanEntity. */
    public static TrainingPlanEntity toTrainingPlanEntity(TrainingPlanFullDto dto) {
        if (dto == null) return null;
        TrainingPlanEntity entity = new TrainingPlanEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDurationOfCycle(dto.getDurationOfCycle());
        entity.setStartDate(dto.getStartDate());
        entity.setActive(true); // Aktywny plan z serwera to zawsze true

        // Finalna korekta: EffortType jest do całego planu.
        entity.setEffortType(dto.getEffortType());

        return entity;
    }

    /** 3. Mapuje listę TrainingDayDto na listę TrainingDayEntity. */
    public static List<TrainingDayEntity> toTrainingDayEntityList(List<TrainingDayDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> {
                    TrainingDayEntity entity = new TrainingDayEntity();
                    entity.setId(dto.getId());
                    entity.setTrainingPlanId(dto.getTrainingPlanId());
                    entity.setDayName(dto.getDayName());
                    entity.setDayOrder(dto.getDayOrder());
                    entity.setWeekNumber(dto.getWeekNumber());
                    // UWAGA: Pole effortTypeName zostało usunięte z dnia
                    return entity;
                })
                .collect(Collectors.toList());
    }

    /** 4. Mapuje listę PlannedExerciseDto na PlannedExerciseEntity.
     * Wymaga przekazania klucza obcego trainingDayId. */
    public static List<PlannedExerciseEntity> toPlannedExerciseEntityList(List<PlannedExerciseDto> dtos, int trainingDayId) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> {
                    PlannedExerciseEntity entity = new PlannedExerciseEntity();
                    entity.setId(dto.getId());
                    entity.setTrainingDayId(trainingDayId);
                    entity.setExerciseName(dto.getExerciseName());
                    entity.setExerciseDescription(dto.getExerciseDescription());
                    entity.setExerciseOrder(dto.getExerciseOrder());
                    entity.setPlannedSets(dto.getPlannedSets());
                    entity.setPlannedReps(dto.getPlannedReps());
                    entity.setPlannedWeight(dto.getPlannedWeight());
                    return entity;
                })
                .collect(Collectors.toList());
    }

    /** 5. Mapuje ExecutedSetDto na ExecutedSetEntity (tworzenie rekordu Outbox).
     * Wymaga timestampu zapisu lokalnego. */
    public static ExecutedSetEntity toExecutedSetEntity(ExecutedSetDto dto, long timestamp) {
        if (dto == null) return null;
        ExecutedSetEntity entity = new ExecutedSetEntity();
        // localId jest generowane przez Room (autoGenerate = true)
        entity.setPlannedExerciseId(dto.getPlannedExerciseId());
        entity.setSetNumber(dto.getSetNumber());
        entity.setExecutedReps(dto.getExecutedReps());
        entity.setWeightUsed(dto.getWeightUsed());
        entity.setExecutionTimestamp(timestamp); // Lokalny timestamp zapisu
        entity.setSyncStatus(0); // Oznaczenie jako NIESYNCHRONIZOWANY
        return entity;
    }

    /** 6. Mapuje listę ExerciseDto na ExerciseEntity (dane referencyjne). */
    public static List<ExerciseEntity> toExerciseEntityList(List<ExerciseDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> {
                    ExerciseEntity entity = new ExerciseEntity();
                    entity.setId(dto.getId());
                    entity.setName(dto.getName());
                    entity.setDescription(dto.getDescription());
                    // Spłaszczona kategoria (zgodnie z decyzją)
                    entity.setExerciseCategory(dto.getCategoryName());
                    return entity;
                })
                .collect(Collectors.toList());
    }

    /** 7. Mapuje listę MovementPatternDto na MovementPatternEntity (dane referencyjne). */
    public static List<MovementPatternEntity> toMovementPatternEntityList(List<MovementPatternDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> {
                    MovementPatternEntity entity = new MovementPatternEntity();
                    entity.setId(dto.getId());
                    entity.setName(dto.getName());
                    return entity;
                })
                .collect(Collectors.toList());
    }

    /** 8. Mapuje listę TargetMuscleGroupDto na TargetMuscleGroupEntity (dane referencyjne). */
    public static List<TargetMuscleGroupEntity> toTargetMuscleGroupEntityList(List<TargetMuscleGroupDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> {
                    TargetMuscleGroupEntity entity = new TargetMuscleGroupEntity();
                    entity.setId(dto.getId());
                    entity.setName(dto.getName());
                    return entity;
                })
                .collect(Collectors.toList());
    }

    // =========================================================================
    // II. MAPOWANIE ENTITY -> DTO (Wysyłanie danych z Room na serwer - Outbox)
    // =========================================================================

    /** 9. Mapuje ExecutedSetEntity z Outbox na ExecutedSetDto. */
    public static ExecutedSetDto toExecutedSetDto(ExecutedSetEntity entity) {
        if (entity == null) return null;
        ExecutedSetDto dto = new ExecutedSetDto();

        // Używamy plannedExerciseId. localId (klucz lokalny) nie jest potrzebny na serwerze.
        dto.setPlannedExerciseId(entity.getPlannedExerciseId());
        dto.setSetNumber(entity.getSetNumber());
        dto.setExecutedReps(entity.getExecutedReps());
        dto.setWeightUsed(entity.getWeightUsed());

        // UWAGA: Serwer może oczekiwać formatu daty w postaci Stringa (np. ISO 8601),
        // a nie long. Jeśli Twój ExecutedSetDto ma pole date, musisz użyć
        // pomocniczej klasy formatującej datę z long na String.
        // Zakładamy na potrzeby DTO, że serwer użyje czasu przyjęcia.

        return dto;
    }

    /** 10. Mapuje listę ExecutedSetEntity na listę ExecutedSetDto. */
    public static List<ExecutedSetDto> toExecutedSetDtoList(List<ExecutedSetEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(DtoMapper::toExecutedSetDto)
                .collect(Collectors.toList());
    }
}