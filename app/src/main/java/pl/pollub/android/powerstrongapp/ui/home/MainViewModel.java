package pl.pollub.android.powerstrongapp.ui.home;

import android.text.format.DateUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import lombok.Getter;
import pl.pollub.android.powerstrongapp.data.local.entity.UserEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.UserRecordEntity;
import pl.pollub.android.powerstrongapp.data.repository.PlanRepository;
import pl.pollub.android.powerstrongapp.data.repository.UserRepository;
import pl.pollub.android.powerstrongapp.data.repository.WorkoutRepository;
import pl.pollub.android.powerstrongapp.utils.AuthManager;

public class MainViewModel extends ViewModel {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    public final AuthManager authManager;

    // --- GŁÓWNE DANE ---
    @Getter
    private final LiveData<TrainingPlanEntity> activeTrainingPlan;
    private final LiveData<UserEntity> currentUser;

    // --- KALENDARZ / TRENING ---
    private final LiveData<TrainingDayEntity> _nextTrainingDay;
    private final LiveData<Long> _allowedDateTimestamp;
    private final LiveData<Integer> _exercisesRemainingCount;

    // --- STATYSTYKI (DLA HOME) ---
    private final LiveData<Integer> completedPlansCount;
    private final LiveData<UserRecordEntity> lastRecord;
    private final LiveData<Integer> currentStreak;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    // Gettery dla UI
    public LiveData<UserEntity> getCurrentUser() { return currentUser; }
    public LiveData<TrainingDayEntity> getNextTrainingDay() { return _nextTrainingDay; }
    public LiveData<Long> getAllowedDateTimestamp() { return _allowedDateTimestamp; }
    public LiveData<Integer> getExercisesRemainingCount() { return _exercisesRemainingCount; }

    public LiveData<Integer> getCompletedPlansCount() { return completedPlansCount; }
    public LiveData<UserRecordEntity> getLastRecord() { return lastRecord; }
    public LiveData<Integer> getCurrentStreak() { return currentStreak; }
    public LiveData<Boolean> isLoading() { return _isLoading; }
    private final LiveData<Integer> progressCompleted;
    private final LiveData<Integer> progressTotal;

    public MainViewModel(PlanRepository planRepo, UserRepository userRepo, WorkoutRepository workoutRepo, AuthManager authManager) {
        this.planRepository = planRepo;
        this.userRepository = userRepo;
        this.workoutRepository = workoutRepo;
        this.authManager = authManager;

        // 1. Dane podstawowe
        this.activeTrainingPlan = planRepo.getActiveTrainingPlan();
        Integer userId = authManager.getUserId();
        this.currentUser = (userId != null) ? userRepo.getLiveUserDetails() : new MutableLiveData<>(null);

        // 2. Logika "Co ćwiczyć dzisiaj?"
        this._nextTrainingDay = planRepo.getNextTrainingDayInQueue();
        this._allowedDateTimestamp = planRepo.getCalculatedNextTrainingDateTimestamp();

        this._exercisesRemainingCount = Transformations.switchMap(_nextTrainingDay, day -> {
            if (day == null) return new MutableLiveData<>(0);
            return planRepo.getRemainingExercisesCount(day.getId());
        });

        // 3. Statystyki
        this.completedPlansCount = planRepo.getCompletedPlansCount();
        this.lastRecord = userRepo.getLatestRecord();

        // UWAGA: Upewnij się, że dodałeś metodę calculateCurrentStreak() do PlanRepository w poprzednim kroku!
        this.currentStreak = planRepo.calculateCurrentStreak();

        // 4. Synchronizacja przy starcie
        syncFullTrainingPlan();
        userRepo.syncPlanHistory(); // Ważne dla licznika ukończonych planów
        this.progressTotal = Transformations.switchMap(activeTrainingPlan, plan -> {
            if (plan == null) return new MutableLiveData<>(0);
            return planRepo.getTotalScheduledSessions(plan.getId()); // Metoda z PlanRepository
        });

        // Liczba wykonanych sesji (zależy od ActivePlan, pobieramy z WorkoutRepository)
        this.progressCompleted = Transformations.switchMap(activeTrainingPlan, plan -> {
            if (plan == null) return new MutableLiveData<>(0);
            return workoutRepo.getCompletedSessionsCount(plan.getId()); // Metoda z WorkoutRepository
        });
    }

    public LiveData<Integer> getProgressCompleted() { return progressCompleted; }
    public LiveData<Integer> getProgressTotal() { return progressTotal; }

    public void syncFullTrainingPlan() {
        if (!authManager.isUserLoggedIn()) return;
        _isLoading.setValue(true);
        planRepository.syncFullTrainingPlan();
        _isLoading.postValue(false);
    }

    // Helper: Czy dzisiaj można ćwiczyć? (Data minęła lub jest dzisiaj)
    public boolean isTrainingReady() {
        Long timestamp = _allowedDateTimestamp.getValue();
        if (timestamp == null) return true; // Fallback
        return timestamp <= System.currentTimeMillis() || DateUtils.isToday(timestamp);
    }

    // Fabryka ViewModelu
    public static class Factory implements ViewModelProvider.Factory {
        private final PlanRepository planRepo;
        private final UserRepository userRepo;
        private final WorkoutRepository workoutRepo;
        private final AuthManager authManager;

        public Factory(PlanRepository planRepo, UserRepository userRepo, AuthManager authManager, WorkoutRepository workoutRepo) {
            this.planRepo = planRepo;
            this.userRepo = userRepo;
            this.authManager = authManager;
            this.workoutRepo = workoutRepo;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MainViewModel(planRepo, userRepo, workoutRepo, authManager);
        }
    }
}