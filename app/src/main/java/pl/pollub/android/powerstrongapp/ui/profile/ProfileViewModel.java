package pl.pollub.android.powerstrongapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.api.model.UserDto;
import pl.pollub.android.powerstrongapp.api.model.UserExerciseMaxDto;
import pl.pollub.android.powerstrongapp.data.local.entity.UserEntity;
import pl.pollub.android.powerstrongapp.data.repository.UserRepository;
import retrofit2.Callback;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository;

    public ProfileViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<UserEntity> getUserDetails() {
        return userRepository.getLiveUserDetails();
    }

    public LiveData<List<UserExerciseMaxDto>> getRecords() {
        return userRepository.getCombinedUserRecords();
    }

    public LiveData<List<TrainingPlanFullDto>> getHistory() {
        return userRepository.getPlanHistory();
    }

    public void logout() {
        userRepository.logoutUser();
    }

    public void deleteAccount(Callback<Void> callback) {
        userRepository.deleteAccount(callback);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final UserRepository userRepository;
        public Factory(UserRepository userRepository) { this.userRepository = userRepository; }
        @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ProfileViewModel(userRepository);
        }
    }
}