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

    @Getter
    private final LiveData<TrainingPlanEntity> activeTrainingPlan;
    private final LiveData<UserEntity> currentUser;

    private final LiveData<TrainingDayEntity> _nextTrainingDay;
    private final LiveData<Long> _allowedDateTimestamp;
    private final LiveData<Integer> _exercisesRemainingCount;

    private final LiveData<Integer> completedPlansCount;
    private final LiveData<UserRecordEntity> lastRecord;
    private final LiveData<Integer> currentStreak;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    public LiveData<UserEntity> getCurrentUser() { return currentUser; }
    public LiveData<TrainingDayEntity> getNextTrainingDay() { return _nextTrainingDay; }
    public LiveData<Long> getAllowedDateTimestamp() { return _allowedDateTimestamp; }
    public LiveData<Integer> getExercisesRemainingCount() { return _exercisesRemainingCount; }
    public LiveData<Boolean> getIsLoading() { return _isLoading; }
    public LiveData<Integer> getCompletedPlansCount() { return completedPlansCount; }
    public LiveData<UserRecordEntity> getLastRecord() { return lastRecord; }
    public LiveData<Integer> getCurrentStreak() { return currentStreak; }
    private final LiveData<Integer> progressCompleted;
    private final LiveData<Integer> progressTotal;

    public MainViewModel(PlanRepository planRepo, UserRepository userRepo, WorkoutRepository workoutRepo, AuthManager authManager) {
        this.planRepository = planRepo;
        this.userRepository = userRepo;
        this.workoutRepository = workoutRepo;
        this.authManager = authManager;

        this.activeTrainingPlan = planRepo.getActiveTrainingPlan();
        Integer userId = authManager.getUserId();
        this.currentUser = (userId != null) ? userRepo.getLiveUserDetails() : new MutableLiveData<>(null);

        this._nextTrainingDay = planRepo.getNextTrainingDayInQueue();
        this._allowedDateTimestamp = planRepo.getCalculatedNextTrainingDateTimestamp();

        this._exercisesRemainingCount = Transformations.switchMap(_nextTrainingDay, day -> {
            if (day == null) return new MutableLiveData<>(0);
            return planRepo.getRemainingExercisesCount(day.getId());
        });

        this.completedPlansCount = planRepo.getCompletedPlansCount();
        this.lastRecord = userRepo.getLatestRecord();

        this.currentStreak = planRepo.calculateCurrentStreak();

        syncFullTrainingPlan();
        userRepo.syncPlanHistory();
        this.progressTotal = Transformations.switchMap(activeTrainingPlan, plan -> {
            if (plan == null) return new MutableLiveData<>(0);
            return planRepo.getTotalScheduledSessions(plan.getId());
        });

        this.progressCompleted = Transformations.switchMap(activeTrainingPlan, plan -> {
            if (plan == null) return new MutableLiveData<>(0);
            return workoutRepo.getCompletedSessionsCount(plan.getId());
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
    public boolean isTrainingReady() {
        Long timestamp = _allowedDateTimestamp.getValue();
        if (timestamp == null) return true;
        return timestamp <= System.currentTimeMillis() || DateUtils.isToday(timestamp);
    }

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