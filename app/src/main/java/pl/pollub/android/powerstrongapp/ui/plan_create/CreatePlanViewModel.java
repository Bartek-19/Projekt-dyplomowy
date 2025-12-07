package pl.pollub.android.powerstrongapp.ui.plan_create;

import android.app.Application; // Import Application
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel; // Zmiana z ViewModel na AndroidViewModel
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingDayDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingMethodEntity;
import pl.pollub.android.powerstrongapp.data.repository.PlanRepository;
import pl.pollub.android.powerstrongapp.data.repository.ReferenceRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePlanViewModel extends AndroidViewModel {

    private final PlanRepository planRepository;
    private final ReferenceRepository referenceRepository;
    private final TrainingPlanFullDto tempPlan = new TrainingPlanFullDto();

    private final MutableLiveData<Integer> _weeksCount = new MutableLiveData<>(1);
    public LiveData<Integer> weeksCount = _weeksCount;

    private final MutableLiveData<List<TrainingDayDto>> _days = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<TrainingDayDto>> days = _days;

    private final MutableLiveData<Boolean> _saveSuccess = new MutableLiveData<>();
    public LiveData<Boolean> saveSuccess = _saveSuccess;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> getError() { return _error; }

    public CreatePlanViewModel(@NonNull Application application, PlanRepository pRepo, ReferenceRepository rRepo) {
        super(application);
        this.planRepository = pRepo;
        this.referenceRepository = rRepo;
        tempPlan.setTrainingDays(new ArrayList<>());
    }

    public LiveData<List<ExerciseEntity>> getAvailableExercises() {
        return referenceRepository.getAllExercises();
    }
    public LiveData<List<TrainingMethodEntity>> getAvailableTrainingMethods() {
        return referenceRepository.getAllTrainingMethods();
    }

    public int getPlanDuration() {
        return tempPlan.getDurationOfCycle();
    }

    public void setBasicInfo(String name, int durationWeeks, int methodId) {
        tempPlan.setName(name);
        tempPlan.setDurationOfCycle(durationWeeks);
        tempPlan.setTrainingMethodId(methodId);

        if (_weeksCount.getValue() != null && _weeksCount.getValue() > durationWeeks) {
            _weeksCount.setValue(durationWeeks);
        }
    }

    public void addNewWeek() {
        int current = _weeksCount.getValue() != null ? _weeksCount.getValue() : 1;
        if (current < tempPlan.getDurationOfCycle()) {
            _weeksCount.setValue(current + 1);
        } else {
            _error.setValue(getApplication().getString(R.string.error_limit_reached, tempPlan.getDurationOfCycle()));
        }
    }

    public void removeWeek(int weekNumberToRemove) {
        List<TrainingDayDto> currentDays = tempPlan.getTrainingDays();
        currentDays.removeIf(d -> d.getWeekNumber() == weekNumberToRemove);

        for (TrainingDayDto d : currentDays) {
            if (d.getWeekNumber() > weekNumberToRemove) {
                d.setWeekNumber(d.getWeekNumber() - 1);
                d.setDayOrder(d.getDayOrder() - 7);
            }
        }

        int currentWeeks = _weeksCount.getValue() != null ? _weeksCount.getValue() : 1;
        if (currentWeeks > 1) {
            _weeksCount.setValue(currentWeeks - 1);
        }

        recalculateDaysStructure();
        _days.setValue(tempPlan.getTrainingDays());
    }

    public boolean isWeekEmpty(int weekNumber) {
        for (TrainingDayDto d : tempPlan.getTrainingDays()) {
            if (d.getWeekNumber() == weekNumber) return false;
        }
        return true;
    }

    public boolean hasDay(int weekNumber, int dayCycleIndex) {
        int absoluteOrder = ((weekNumber - 1) * 7) + dayCycleIndex;
        for (TrainingDayDto d : tempPlan.getTrainingDays()) {
            if (d.getDayOrder() == absoluteOrder) return true;
        }
        return false;
    }

    public TrainingDayDto getDay(int weekNumber, int dayCycleIndex) {
        int absoluteOrder = ((weekNumber - 1) * 7) + dayCycleIndex;
        for (TrainingDayDto d : tempPlan.getTrainingDays()) {
            if (d.getDayOrder() == absoluteOrder) return d;
        }
        return null;
    }

    public void addDayToWeek(int weekNumber, int dayCycleIndex) {
        TrainingDayDto day = new TrainingDayDto();
        day.setWeekNumber(weekNumber);
        day.setDayName(getApplication().getString(R.string.day_x_label, dayCycleIndex));
        day.setPlannedExercises(new ArrayList<>());
        int absoluteOrder = ((weekNumber - 1) * 7) + dayCycleIndex;
        day.setDayOrder(absoluteOrder);
        tempPlan.getTrainingDays().add(day);
        recalculateDaysStructure();
        _days.setValue(tempPlan.getTrainingDays());
    }

    public void removeDay(int weekNumber, int dayCycleIndex) {
        int absoluteOrder = ((weekNumber - 1) * 7) + dayCycleIndex;
        tempPlan.getTrainingDays().removeIf(d -> d.getDayOrder() == absoluteOrder);
        recalculateDaysStructure();
        _days.setValue(tempPlan.getTrainingDays());
    }

    private void recalculateDaysStructure() {
        List<TrainingDayDto> daysList = tempPlan.getTrainingDays();
        if (daysList.isEmpty()) return;
        Collections.sort(daysList, Comparator.comparingInt(TrainingDayDto::getDayOrder));
        for (int i = 0; i < daysList.size(); i++) {
            TrainingDayDto current = daysList.get(i);
            if (i == 0) {
                current.setDaysGap(0);
            } else {
                TrainingDayDto prev = daysList.get(i - 1);
                int diff = current.getDayOrder() - prev.getDayOrder();
                current.setDaysGap(Math.max(0, diff - 1));
            }
        }
    }

    public void addExerciseToDay(TrainingDayDto dayDto, PlannedExerciseDto exerciseDto) {
        if (dayDto.getPlannedExercises() == null) dayDto.setPlannedExercises(new ArrayList<>());
        exerciseDto.setExerciseOrder(dayDto.getPlannedExercises().size() + 1);
        dayDto.getPlannedExercises().add(exerciseDto);
        _days.setValue(tempPlan.getTrainingDays());
    }

    public List<TrainingDayDto> getDaysList() { return tempPlan.getTrainingDays(); }
    public TrainingPlanFullDto getPlanSummary() { return tempPlan; }

    public void savePlan(String startDate) {
        if (tempPlan.getName() == null || tempPlan.getName().isEmpty()) {
            _error.setValue(getApplication().getString(R.string.error_no_name));
            return;
        }
        List<TrainingDayDto> cleanedDays = new ArrayList<>();
        List<TrainingDayDto> originalDays = tempPlan.getTrainingDays();
        if (originalDays != null) {
            for (TrainingDayDto day : originalDays) {
                if (day.getPlannedExercises() != null && !day.getPlannedExercises().isEmpty()) {
                    cleanedDays.add(day);
                }
            }
        }
        if (cleanedDays.isEmpty()) {
            _error.setValue(getApplication().getString(R.string.error_no_exercises_in_plan));
            return;
        }
        tempPlan.setTrainingDays(cleanedDays);
        recalculateDaysStructure();
        tempPlan.setStartDate(startDate);

        planRepository.createCustomPlan(tempPlan, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    _saveSuccess.setValue(true);
                } else {
                    _error.setValue(getApplication().getString(R.string.error_server_code, String.valueOf(response.code())));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _error.setValue(getApplication().getString(R.string.error_network_detailed, t.getMessage()));
            }
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Application application;
        private final PlanRepository pRepo;
        private final ReferenceRepository rRepo;
        public Factory(Application app, PlanRepository p, ReferenceRepository r) {
            this.application = app; this.pRepo = p; this.rRepo = r;
        }
        @Override public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CreatePlanViewModel(application, pRepo, rRepo);
        }
    }
}