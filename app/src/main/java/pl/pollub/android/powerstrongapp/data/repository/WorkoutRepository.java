package pl.pollub.android.powerstrongapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pollub.android.powerstrongapp.api.model.ExecutedHistoryDto;
import pl.pollub.android.powerstrongapp.api.model.ExecutedSetDto;
import pl.pollub.android.powerstrongapp.api.service.TrainingService;
import pl.pollub.android.powerstrongapp.data.local.dao.ExecutedSetDao;
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao;
import pl.pollub.android.powerstrongapp.data.local.entity.ExecutedSetEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.enums.SyncStatus;
import pl.pollub.android.powerstrongapp.utils.DtoMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutRepository {

    private final ExecutedSetDao executedSetDao;
    private final TrainingPlanAndDayDao planDao;
    private final TrainingService trainingService;

    private final ExecutorService diskIO = Executors.newSingleThreadExecutor();
    private final ExecutorService networkExecutor = Executors.newFixedThreadPool(4);

    public WorkoutRepository(ExecutedSetDao executedSetDao, TrainingPlanAndDayDao planDao, TrainingService trainingService) {
        this.executedSetDao = executedSetDao;
        this.planDao = planDao;
        this.trainingService = trainingService;
    }
    public LiveData<Integer> getCompletedSessionsCount(int planId) {
        return executedSetDao.getCompletedSessionsCount(planId);
    }

    public void completeWorkoutSession(int planId, List<ExecutedSetDto> executedSets, Callback<Boolean> uiCallback) {
        trainingService.sendExecutedSets(executedSets).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                boolean synced = response.isSuccessful();
                saveExecutedSetsLocally(executedSets, synced, () -> {
                    checkIfPlanIsFinished(planId, uiCallback);
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                saveExecutedSetsLocally(executedSets, false, () -> {
                    checkIfPlanIsFinished(planId, uiCallback);
                });
            }
        });
    }
    private void saveExecutedSetsLocally(List<ExecutedSetDto> dtos, boolean isSynced, Runnable onSaved) {
        if (dtos == null || dtos.isEmpty()) {
            if (onSaved != null) onSaved.run();
            return;
        }
        diskIO.execute(() -> {
            List<ExecutedSetEntity> entities = new ArrayList<>();
            long currentTimestamp = System.currentTimeMillis();

            for (ExecutedSetDto dto : dtos) {
                if (dto.getExecutedReps() != null && dto.getExecutedReps() > 0) {
                    ExecutedSetEntity entity = DtoMapper.toExecutedSetEntity(dto, currentTimestamp, isSynced);
                    entities.add(entity);
                }
            }

            if (!entities.isEmpty()) {
                executedSetDao.insertAll(entities);
            }
            if (onSaved != null) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(onSaved);
            }
        });
    }

    public void synchronizeExecutedSets() {
        networkExecutor.execute(() -> {
            List<ExecutedSetEntity> unsyncedSets = executedSetDao.getSetsBySyncStatus(SyncStatus.NOT_SYNCED);
            if (unsyncedSets.isEmpty()) return;

            List<ExecutedSetDto> dtos = DtoMapper.toExecutedSetDtoList(unsyncedSets);
            try {
                Response<Void> response = trainingService.sendExecutedSets(dtos).execute();
                if (response.isSuccessful()) {
                    for (ExecutedSetEntity entity : unsyncedSets) {
                        entity.setSyncStatus(SyncStatus.SYNCED);
                    }
                    executedSetDao.update(unsyncedSets);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<List<ExecutedHistoryDto>> getHistory(int planId) {
        return Transformations.map(
                executedSetDao.getExecutedSetsHistoryForPlan(planId),
                DtoMapper::toExecutedHistoryDtoList
        );
    }

    private void checkIfPlanIsFinished(int planId, Callback<Boolean> callback) {
        diskIO.execute(() -> {
            List<TrainingDayEntity> days = planDao.getDaysForPlanSync(planId);
            TrainingPlanEntity plan = planDao.getActiveTrainingPlanSync();

            if (days == null || days.isEmpty() || plan == null) {
                postCallback(callback, false);
                return;
            }
            int completedSessions = executedSetDao.getCompletedSessionsCountSync(planId);

            int totalTemplateDays = days.size();
            int lastWeekInTemplate = 0;
            for(TrainingDayEntity d : days) if(d.getWeekNumber() > lastWeekInTemplate) lastWeekInTemplate = d.getWeekNumber();
            if (lastWeekInTemplate == 0) lastWeekInTemplate = 1;

            int duration = plan.getDurationOfCycle();
            int loopMultiplier = 1;
            if (duration > lastWeekInTemplate) {
                loopMultiplier = duration / lastWeekInTemplate;
            }
            int maxTotalSessions = totalTemplateDays * loopMultiplier;

            boolean isFinished = completedSessions >= maxTotalSessions;
            postCallback(callback, isFinished);
        });
    }

    private void postCallback(Callback<Boolean> callback, boolean result) {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                callback.onResponse(null, Response.success(result))
        );
    }
}