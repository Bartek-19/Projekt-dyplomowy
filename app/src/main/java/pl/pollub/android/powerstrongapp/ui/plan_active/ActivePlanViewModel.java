package pl.pollub.android.powerstrongapp.ui.plan_active;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.pollub.android.powerstrongapp.api.model.ExecutedHistoryDto;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.data.repository.PlanRepository;
import pl.pollub.android.powerstrongapp.data.repository.WorkoutRepository;
import retrofit2.Callback;

public class ActivePlanViewModel extends ViewModel {

    private final PlanRepository planRepository;
    private final WorkoutRepository workoutRepository;

    public ActivePlanViewModel(PlanRepository planRepository, WorkoutRepository workoutRepository) {
        this.planRepository = planRepository;
        this.workoutRepository = workoutRepository;
    }

    public LiveData<TrainingPlanEntity> getActivePlan() {
        return planRepository.getActiveTrainingPlan();
    }

    public LiveData<List<TrainingDayEntity>> getPlanDays(int planId) {
        return planRepository.getDaysForPlan(planId);
    }

    public LiveData<List<PlannedExerciseEntity>> getExercisesForClickedDate(
            long dateMillis,
            TrainingPlanEntity plan,
            List<TrainingDayEntity> daysTemplate
    ) {
        // Logika szukania dnia jest teraz w PlanRepo (delegowana do kalkulatora)
        TrainingDayEntity day = planRepository.getDayForDate(plan, daysTemplate, dateMillis);
        if (day != null) {
            return planRepository.getPlannedExercisesForDay(day.getId());
        } else {
            return new MutableLiveData<>(null);
        }
    }

    public List<Long> getPlannedTrainingDates(TrainingPlanEntity plan, List<TrainingDayEntity> days, long startMillis, long endMillis) {
        return planRepository.getTrainingDatesInRange(plan, days, startMillis, endMillis);
    }

    public TrainingDayEntity getDayEntityForDate(long dateMillis, TrainingPlanEntity plan, List<TrainingDayEntity> daysTemplate) {
        return planRepository.getDayForDate(plan, daysTemplate, dateMillis);
    }

    // --- HISTORIA TRENINGÓW (WorkoutRepository) ---

    public LiveData<List<ExecutedHistoryDto>> getExecutedSetsHistoryForPlan(int planId) {
        return workoutRepository.getHistory(planId);
    }

    /**
     * Pomocnicza metoda do wyciągnięcia unikalnych dat z historii (dla zielonych kropek w kalendarzu).
     */
    public List<Long> getCompletedDatesFromHistory(List<ExecutedHistoryDto> historyList) {
        if (historyList == null || historyList.isEmpty()) return new ArrayList<>();

        return historyList.stream()
                .map(dto -> {
                    if (dto.getExecutionTimestamp() == null) return null;
                    LocalDate localDate = Instant.ofEpochMilli(dto.getExecutionTimestamp())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                })
                .filter(date -> date != null)
                .distinct()
                .collect(Collectors.toList());
    }

    public void cancelCurrentPlan(Callback<Void> callback) {
        planRepository.cancelActivePlan(callback);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final PlanRepository planRepo;
        private final WorkoutRepository workoutRepo;

        public Factory(PlanRepository planRepo, WorkoutRepository workoutRepo) {
            this.planRepo = planRepo;
            this.workoutRepo = workoutRepo;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ActivePlanViewModel.class)) {
                return (T) new ActivePlanViewModel(planRepo, workoutRepo);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}