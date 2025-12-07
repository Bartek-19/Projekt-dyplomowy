package pl.pollub.android.powerstrongapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations; // WAŻNE: To jest kluczowy import

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pollub.android.powerstrongapp.api.model.PlanCompletionRequestDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingDayDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.api.service.TrainingService;
import pl.pollub.android.powerstrongapp.data.local.AppDatabase;
import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao;
import pl.pollub.android.powerstrongapp.data.local.dao.PlannedExerciseDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.utils.AuthManager;
import pl.pollub.android.powerstrongapp.utils.DtoMapper;
import pl.pollub.android.powerstrongapp.utils.TrainingCycleCalculator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanRepository {

    private final TrainingPlanAndDayDao trainingPlanAndDayDao;
    private final PlannedExerciseDao plannedExerciseDao;
    private final ExecutedSetDao executedSetDao;
    private final TrainingService trainingService;
    private final AuthManager authManager;
    private final TrainingCycleCalculator calculator;
    private static final ExecutorService networkExecutor = Executors.newFixedThreadPool(4);
    private static final ExecutorService diskIO = Executors.newSingleThreadExecutor();

    public PlanRepository(Application application, TrainingService trainingService) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.trainingPlanAndDayDao = db.trainingPlanAndDayDao();
        this.plannedExerciseDao = db.plannedExerciseDao();
        this.executedSetDao = db.executedSetDao();
        this.trainingService = trainingService;
        this.authManager = AuthManager.getInstance(application);
        this.calculator = new TrainingCycleCalculator();
    }

    public LiveData<TrainingPlanEntity> getActiveTrainingPlan() {
        return trainingPlanAndDayDao.getActiveTrainingPlan();
    }

    public LiveData<List<TrainingDayEntity>> getDaysForPlan(int planId) {
        return trainingPlanAndDayDao.getDaysForPlan(planId);
    }

    public LiveData<List<PlannedExerciseEntity>> getPlannedExercisesForDay(int dayId) {
        return plannedExerciseDao.getPlannedExercisesForDay(dayId);
    }
    public LiveData<TrainingDayEntity> getNextTrainingDayInQueue() {
        return Transformations.switchMap(trainingPlanAndDayDao.getActiveTrainingPlan(), plan -> {
            if (plan == null) return new MutableLiveData<>(null);

            MediatorLiveData<TrainingDayEntity> result = new MediatorLiveData<>();

            LiveData<List<TrainingDayEntity>> daysLive = trainingPlanAndDayDao.getDaysForPlan(plan.getId());
            LiveData<Integer> historyCountLive = executedSetDao.getCompletedSessionsCount(plan.getId());

            result.addSource(daysLive, days ->
                    calculateNextDay(result, plan, days, historyCountLive.getValue())
            );
            result.addSource(historyCountLive, count ->
                    calculateNextDay(result, plan, daysLive.getValue(), count)
            );

            return result;
        });
    }

    private void calculateNextDay(MediatorLiveData<TrainingDayEntity> target,
                                  TrainingPlanEntity plan,
                                  List<TrainingDayEntity> days,
                                  Integer completedSessions) {
        if (days == null || days.isEmpty() || completedSessions == null) return;

        diskIO.execute(() -> {
            TrainingDayEntity nextDay = calculator.determineNextDayEntity(plan, days, completedSessions);
            target.postValue(nextDay);
        });
    }

    public LiveData<Long> getCalculatedNextTrainingDateTimestamp() {
        return Transformations.switchMap(trainingPlanAndDayDao.getActiveTrainingPlan(), plan -> {
            if (plan == null) return new MutableLiveData<>(null);

            MediatorLiveData<Long> result = new MediatorLiveData<>();
            LiveData<List<TrainingDayEntity>> daysLive = trainingPlanAndDayDao.getDaysForPlan(plan.getId());
            LiveData<Integer> historyCountLive = executedSetDao.getCompletedSessionsCount(plan.getId());

            result.addSource(daysLive, days ->
                    calculateNextDate(result, plan, days, historyCountLive.getValue())
            );
            result.addSource(historyCountLive, count ->
                    calculateNextDate(result, plan, daysLive.getValue(), count)
            );

            return result;
        });
    }

    private void calculateNextDate(MediatorLiveData<Long> target,
                                   TrainingPlanEntity plan,
                                   List<TrainingDayEntity> days,
                                   Integer completedSessions) {
        if (days == null || days.isEmpty() || completedSessions == null) return;

        diskIO.execute(() -> {
            Long nextDate = calculator.calculateNextTrainingDate(plan, days, completedSessions);
            target.postValue(nextDate);
        });
    }

    public LiveData<Integer> calculateCurrentStreak() {
        return Transformations.switchMap(trainingPlanAndDayDao.getActiveTrainingPlan(), plan -> {
            if (plan == null) return new MutableLiveData<>(0);

            LiveData<List<Long>> historyDatesLive = executedSetDao.getAllWorkoutDates();
            LiveData<List<TrainingDayEntity>> daysLive = trainingPlanAndDayDao.getDaysForPlan(plan.getId());

            MediatorLiveData<Integer> result = new MediatorLiveData<>();

            result.addSource(historyDatesLive, history ->
                    calculateStreak(result, plan, daysLive.getValue(), history)
            );
            result.addSource(daysLive, days ->
                    calculateStreak(result, plan, days, historyDatesLive.getValue())
            );

            return result;
        });
    }

    private void calculateStreak(MediatorLiveData<Integer> target,
                                 TrainingPlanEntity plan,
                                 List<TrainingDayEntity> days,
                                 List<Long> history) {
        if (days == null || history == null) return;

        diskIO.execute(() -> {
            if (days.isEmpty() || history.isEmpty()) {
                target.postValue(0);
                return;
            }
            long startMillis = LocalDate.parse(plan.getStartDate())
                    .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long nowMillis = System.currentTimeMillis();

            List<Long> expectedDates = calculator.getTrainingDatesInRange(plan, days, startMillis, nowMillis);

            int currentStreak = 0;
            for (int i = expectedDates.size() - 1; i >= 0; i--) {
                Long expectedDate = expectedDates.get(i);
                boolean executed = isDateExecuted(expectedDate, history);

                if (executed) {
                    currentStreak++;
                } else {
                    if (android.text.format.DateUtils.isToday(expectedDate)) {
                        continue;
                    }
                    break;
                }
            }
            target.postValue(currentStreak);
        });
    }
    public LiveData<Integer> getRemainingExercisesCount(int dayId) {
        return plannedExerciseDao.getExercisesCountForDay(dayId);
    }
    public LiveData<Integer> getTotalScheduledSessions(int planId) {
        MediatorLiveData<Integer> total = new MediatorLiveData<>();
        LiveData<List<TrainingDayEntity>> daysLive = trainingPlanAndDayDao.getDaysForPlan(planId);
        LiveData<TrainingPlanEntity> planLive = trainingPlanAndDayDao.getActiveTrainingPlan();

        MediatorLiveData<Object> combinedSource = new MediatorLiveData<>();
        combinedSource.addSource(planLive, plan -> combinedSource.setValue(new Object()));
        combinedSource.addSource(daysLive, days -> combinedSource.setValue(new Object()));

        total.addSource(combinedSource, ignored -> {
            TrainingPlanEntity plan = planLive.getValue();
            List<TrainingDayEntity> days = daysLive.getValue();

            if (plan == null || days == null || days.isEmpty()) {
                total.setValue(0);
                return;
            }
            int daysInTemplate = days.size();
            int durationWeeks = plan.getDurationOfCycle();
            if (durationWeeks <= 0) durationWeeks = 1;

            int maxTemplateWeek = 0;
            for (TrainingDayEntity d : days) {
                if (d.getWeekNumber() > maxTemplateWeek) maxTemplateWeek = d.getWeekNumber();
            }
            if (maxTemplateWeek == 0) maxTemplateWeek = 1;

            double loops = (double) durationWeeks / maxTemplateWeek;
            int totalSessions = (int) (daysInTemplate * loops);

            total.setValue(totalSessions);
        });

        return total;
    }
    public List<Long> getTrainingDatesInRange(TrainingPlanEntity plan, List<TrainingDayEntity> days, long startMillis, long endMillis) {
        return calculator.getTrainingDatesInRange(plan, days, startMillis, endMillis);
    }
    public LiveData<Integer> getCompletedPlansCount() {
        return trainingPlanAndDayDao.getCompletedPlansCount();
    }
    public TrainingDayEntity getDayForDate(TrainingPlanEntity plan, List<TrainingDayEntity> templateDays, long dateMillis) {
        return calculator.findDayForDate(plan, templateDays, dateMillis);
    }
    public void syncFullTrainingPlan() {
        networkExecutor.execute(() -> {
            trainingService.getActiveTrainingPlan().enqueue(new Callback<TrainingPlanFullDto>() {
                @Override
                public void onResponse(Call<TrainingPlanFullDto> call, Response<TrainingPlanFullDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            TrainingPlanEntity plan = DtoMapper.toTrainingPlanEntity(response.body());
                            List<TrainingDayEntity> days = DtoMapper.toTrainingDayEntityList(response.body().getTrainingDays());
                            List<PlannedExerciseEntity> exercises = new ArrayList<>();
                            if (response.body().getTrainingDays() != null) {
                                for (TrainingDayDto d : response.body().getTrainingDays()) {
                                    exercises.addAll(DtoMapper.toPlannedExerciseEntityList(d.getPlannedExercises(), d.getId()));
                                }
                            }
                            trainingPlanAndDayDao.updateFullTrainingPlan(plan, days, exercises);
                        });
                    } else if (response.code() == 404) {
                        AppDatabase.databaseWriteExecutor.execute(trainingPlanAndDayDao::clearTrainingPlan);
                    }
                }
                @Override
                public void onFailure(Call<TrainingPlanFullDto> call, Throwable t) {}
            });
        });
    }

    public void activatePlan(int planId, String startDate, Callback<Void> callback) {
        Integer userId = authManager.getUserId();
        if (userId == null) {
            callback.onFailure(null, new Throwable("User not logged in"));
            return;
        }

        trainingService.assignPlanToUser(planId, startDate).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        executedSetDao.deleteAllExecutedSets();
                        trainingPlanAndDayDao.clearTrainingPlan();
                    });
                    syncFullTrainingPlan();
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void cancelActivePlan(Callback<Void> callback) {
        trainingService.cancelActivePlan().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    AppDatabase.databaseWriteExecutor.execute(trainingPlanAndDayDao::clearTrainingPlan);
                }
                callback.onResponse(call, response);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void completePlan(PlanCompletionRequestDto dto, Callback<Void> callback) {
        trainingService.completeActivePlan(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    diskIO.execute(trainingPlanAndDayDao::markActivePlanAsCompleted);
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    private boolean isDateExecuted(Long expectedMillis, List<Long> historyMillis) {
        LocalDate expected = Instant.ofEpochMilli(expectedMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        for (Long h : historyMillis) {
            LocalDate actual = Instant.ofEpochMilli(h).atZone(ZoneId.systemDefault()).toLocalDate();
            if (expected.isEqual(actual)) return true;
        }
        return false;
    }

    public void createCustomPlan(TrainingPlanFullDto planDto, Callback<Void> callback) {
        trainingService.createCustomPlan(planDto).enqueue(callback);
    }
    public void getAvailablePlans(Callback<List<TrainingPlanFullDto>> callback) {
        trainingService.getPlanTemplates().enqueue(callback);
    }
}