package pl.pollub.android.powerstrongapp.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.pollub.android.powerstrongapp.data.local.entity.UserEntity;
import pl.pollub.android.powerstrongapp.data.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final int currentUserId = 1; // Zastąp to ID zalogowanego użytkownika

    public UserViewModel(@NonNull Application application, UserRepository repository) {
        super(application);
        this.userRepository = repository;
    }

    // Zwraca dane użytkownika jako LiveData, które automatycznie się aktualizuje
    public LiveData<UserEntity> getUserDetails() {
        return userRepository.getLiveUserDetails(currentUserId);
    }

    // Metoda do logowania/rejestracji...
}