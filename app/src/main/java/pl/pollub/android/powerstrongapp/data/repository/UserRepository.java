package pl.pollub.android.powerstrongapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.api.model.UserExerciseMaxDto;
import pl.pollub.android.powerstrongapp.data.local.AppDatabase;
import pl.pollub.android.powerstrongapp.data.local.dao.ExerciseDao; // DODANE
import pl.pollub.android.powerstrongapp.data.local.dao.TrainingPlanAndDayDao;
import pl.pollub.android.powerstrongapp.data.local.dao.UserDao;
import pl.pollub.android.powerstrongapp.data.local.dao.UserRecordDao;
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.UserEntity;
import pl.pollub.android.powerstrongapp.api.service.UserService;
import pl.pollub.android.powerstrongapp.api.model.UserDto;
import pl.pollub.android.powerstrongapp.data.local.entity.UserRecordEntity;
import pl.pollub.android.powerstrongapp.utils.AuthManager;
import pl.pollub.android.powerstrongapp.utils.DtoMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRepository {
    private final UserDao userDao;
    private final ExerciseDao exerciseDao;
    private final UserRecordDao userRecordDao;
    private final TrainingPlanAndDayDao planDao;
    private final UserService userService;
    private final AuthManager authManager;

    public UserRepository(Application application, UserService userService) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.userDao = db.userDao();
        this.exerciseDao = db.exerciseDao();
        this.userRecordDao = db.userRecordDao();
        this.planDao = db.trainingPlanAndDayDao();
        this.userService = userService;
        this.authManager = AuthManager.getInstance(application);
    }

    public LiveData<UserEntity> getLiveUserDetails() {
        syncUserData();
        return userDao.getUser();
    }
    public void syncUserData() {
        userService.getUserDetails().enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserEntity entity = DtoMapper.toUserEntity(response.body());
                    AppDatabase.databaseWriteExecutor.execute(() -> userDao.insertUser(entity));
                }
            }
            @Override
            public void onFailure(Call<UserDto> call, Throwable t) { }
        });
    }
    public LiveData<List<UserExerciseMaxDto>> getCombinedUserRecords() {
        refreshRecordsFromApi();

        return Transformations.map(userRecordDao.getAllRecords(), entities -> {
            List<UserExerciseMaxDto> dtos = new ArrayList<>();
            if (entities == null) return dtos;

            for (UserRecordEntity entity : entities) {
                UserExerciseMaxDto dto = new UserExerciseMaxDto();
                dto.setExerciseName(entity.getExerciseName());
                dto.setCurrentOneRepMax(entity.getCurrentOneRepMax());
                dto.setBodyweight(entity.isBodyweight());
                dto.setLastUpdatedDate(entity.getLastUpdatedDate());
                dtos.add(dto);
            }
            return dtos;
        });
    }
    private void refreshRecordsFromApi() {
        userService.getUserRecords().enqueue(new Callback<List<UserExerciseMaxDto>>() {
            @Override
            public void onResponse(Call<List<UserExerciseMaxDto>> call, Response<List<UserExerciseMaxDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        List<UserRecordEntity> entities = response.body().stream().map(dto -> {
                            return new UserRecordEntity(
                                    dto.getExerciseId(),
                                    dto.getExerciseName(),
                                    dto.getCurrentOneRepMax(),
                                    dto.isBodyweight(),
                                    dto.getLastUpdatedDate()
                            );
                        }).collect(Collectors.toList());

                        userRecordDao.insertAll(entities);
                    });
                }
            }
            @Override public void onFailure(Call<List<UserExerciseMaxDto>> call, Throwable t) { }
        });
    }
    public LiveData<UserRecordEntity> getLatestRecord() {
        return userRecordDao.getLatestRecord();
    }
    public void syncPlanHistory() {
        userService.getPlanHistory().enqueue(new Callback<List<TrainingPlanFullDto>>() {
            @Override
            public void onResponse(Call<List<TrainingPlanFullDto>> call, Response<List<TrainingPlanFullDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        // Mapujemy DTO na Encje
                        List<TrainingPlanFullDto> dtos = response.body();

                        for (TrainingPlanFullDto dto : dtos) {
                            TrainingPlanEntity entity = DtoMapper.toTrainingPlanEntity(dto);
                            planDao.insertPlan(entity);
                        }
                    });
                }
            }
            @Override public void onFailure(Call<List<TrainingPlanFullDto>> call, Throwable t) { }
        });
    }
    public LiveData<List<TrainingPlanFullDto>> getPlanHistory() {
        syncPlanHistory();
        return Transformations.map(planDao.getAllTrainingPlans(), plans -> {
            List<TrainingPlanFullDto> history = new ArrayList<>();
            for (TrainingPlanEntity p : plans) {
                if ("COMPLETED".equals(p.getStatus()) || "ARCHIVED".equals(p.getStatus())) {
                    TrainingPlanFullDto dto = new TrainingPlanFullDto();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setStatus(p.getStatus());
                    dto.setStartDate(p.getStartDate());
                    history.add(dto);
                }
            }
            return history;
        });
    }

    public void deleteAccount(Callback<Void> callback) {
        userService.deleteAccount().enqueue(callback);
    }
    public void logoutUser() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.deleteAllUsers();
            userRecordDao.clearAll();
        });
        authManager.logout();
    }
}