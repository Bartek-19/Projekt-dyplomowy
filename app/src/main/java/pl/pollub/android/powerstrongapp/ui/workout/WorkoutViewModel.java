package pl.pollub.android.powerstrongapp.ui.workout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pollub.android.powerstrongapp.api.model.ExecutedSetDto;
import pl.pollub.android.powerstrongapp.api.model.PlanCompletionRequestDto;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.repository.PlanRepository;
import pl.pollub.android.powerstrongapp.data.repository.WorkoutRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutViewModel extends ViewModel {

    private final PlanRepository planRepository;
    private final WorkoutRepository workoutRepository;

    private final MutableLiveData<List<PlannedExerciseEntity>> _plannedExercises = new MutableLiveData<>();
    public LiveData<List<PlannedExerciseEntity>> plannedExercises = _plannedExercises;
    private final MutableLiveData<Boolean> _showPlanCompletionDialog = new MutableLiveData<>();
    public LiveData<Boolean> showPlanCompletionDialog = _showPlanCompletionDialog;

    private final MutableLiveData<Boolean> _navigateBack = new MutableLiveData<>();
    public LiveData<Boolean> navigateBack = _navigateBack;

    private final Map<Integer, List<ExecutedSetDto>> temporaryResults = new HashMap<>();
    private int currentPlanId = -1;

    public WorkoutViewModel(PlanRepository planRepository, WorkoutRepository workoutRepository) {
        this.planRepository = planRepository;
        this.workoutRepository = workoutRepository;
    }

    public void loadExercisesForDay(int dayId) {
        _showPlanCompletionDialog.setValue(false);
        _navigateBack.setValue(false);
        temporaryResults.clear();

        // Pobieramy ćwiczenia z PlanRepo
        planRepository.getPlannedExercisesForDay(dayId).observeForever(exercises -> {
            _plannedExercises.setValue(exercises);

            // Pobieramy ID planu (potrzebne do checkFinish)
            planRepository.getActiveTrainingPlan().observeForever(plan -> {
                if(plan != null) currentPlanId = plan.getId();
            });
        });
    }

    public void saveCurrentExerciseResults(int plannedExerciseId, List<ExecutedSetDto> results) {
        temporaryResults.put(plannedExerciseId, results);
    }

    public List<ExecutedSetDto> getResultsForExercise(int exerciseId) {
        return temporaryResults.get(exerciseId);
    }

    /**
     * Zapisuje trening i sprawdza czy to koniec planu.
     * Używa nowej, "czystej" metody w WorkoutRepository.
     */
    public void finishWorkoutAndSave(Callback<Void> uiCallback) {
        List<ExecutedSetDto> allExecutedSets = new ArrayList<>();
        for (List<ExecutedSetDto> sets : temporaryResults.values()) {
            allExecutedSets.addAll(sets);
        }

        if (currentPlanId == -1) {
            // Zabezpieczenie, gdyby plan nie zdążył się załadować (mało prawdopodobne)
            if(uiCallback != null) uiCallback.onResponse(null, Response.success(null));
            return;
        }

        // Delegujemy całą logikę (Send -> SaveLocal -> CheckFinish) do repozytorium
        workoutRepository.completeWorkoutSession(currentPlanId, allExecutedSets, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean isEndOfPlan = response.body() != null && response.body();

                if (isEndOfPlan) {
                    _showPlanCompletionDialog.setValue(true);
                } else {
                    // Normalny koniec treningu - wychodzimy
                    if(uiCallback != null) uiCallback.onResponse(null, Response.success(null));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Nawet w przypadku błędu (offline), repo zapisało dane lokalnie.
                // Więc uznajemy trening za zakończony i wychodzimy.
                if(uiCallback != null) uiCallback.onResponse(null, Response.success(null));
            }
        });
    }

    // Metoda wywoływana z Fragmentu po wypełnieniu ankiety
    public void submitPlanCompletion(PlanCompletionRequestDto dto) {
        // Ankieta to modyfikacja statusu planu, więc logicznie PlanRepository
        planRepository.completePlan(dto, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _navigateBack.setValue(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _navigateBack.setValue(true);
            }
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final PlanRepository planRepo;
        private final WorkoutRepository workoutRepo;

        public Factory(PlanRepository planRepo, WorkoutRepository workoutRepo) {
            this.planRepo = planRepo;
            this.workoutRepo = workoutRepo;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new WorkoutViewModel(planRepo, workoutRepo);
        }
    }
}