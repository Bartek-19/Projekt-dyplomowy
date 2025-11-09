package pl.pollub.android.powerstrongapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import pl.pollub.android.powerstrongapp.data.local.AppDatabase;
import pl.pollub.android.powerstrongapp.data.local.dao.UserDao;
import pl.pollub.android.powerstrongapp.data.local.entity.UserEntity;
import pl.pollub.android.powerstrongapp.api.UserService;
import pl.pollub.android.powerstrongapp.api.model.UserDto;
import pl.pollub.android.powerstrongapp.utils.DtoMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserDao userDao;
    private final UserService userService;

    public UserRepository(Application application, UserService userService) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.userDao = db.userDao();
        this.userService = userService;
    }
    public LiveData<UserEntity> getLiveUserDetails(int userId) {
        refreshUser(userId);
        return userDao.getUser();
    }
    private void refreshUser(int userId) {
        userService.getUserDetails(userId).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDto dto = response.body();
                    // Mapowanie DTO na ENCJĘ
                    UserEntity entity = DtoMapper.toUserEntity(dto); // Zakładam, że masz klasę DtoMapper

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        // Zapis do lokalnej bazy (usunięcie starych i wstawienie nowych)
                        userDao.insertUser(entity);
                    });
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                // Obsługa błędu - UI nadal używa LiveData z Room
            }
        });
    }

    /**
     * Usuwa dane użytkownika z bazy lokalnej (np. po wylogowaniu).
     */
    public void logoutUser() {
        AppDatabase.databaseWriteExecutor.execute(userDao::deleteAllUsers);
    }
}