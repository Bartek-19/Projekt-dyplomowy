package pl.pollub.android.powerstrongapp.ui.plan_list;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.data.repository.PlanRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class PlanListViewModel extends AndroidViewModel {

    private final PlanRepository repository;

    private final MutableLiveData<List<TrainingPlanFullDto>> _plans = new MutableLiveData<>();
    public LiveData<List<TrainingPlanFullDto>> getPlans() { return _plans; }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> getError() { return _error; }

    private final MutableLiveData<Boolean> _isPlanActivated = new MutableLiveData<>();
    public LiveData<Boolean> isPlanActivated() { return _isPlanActivated; }

    public PlanListViewModel(@NonNull Application application, PlanRepository repository) {
        super(application);
        this.repository = repository;
        loadPlans();
    }

    public void loadPlans() {
        _isLoading.setValue(true);
        repository.getAvailablePlans(new Callback<List<TrainingPlanFullDto>>() {

            @SuppressLint("StringFormatMatches")
            @Override
            public void onResponse(Call<List<TrainingPlanFullDto>> call, Response<List<TrainingPlanFullDto>> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    _plans.setValue(response.body());
                } else {
                    _error.setValue(getApplication().getString(R.string.error_fetching, response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<TrainingPlanFullDto>> call, Throwable t) {
                _isLoading.setValue(false);
                _error.setValue(getApplication().getString(R.string.error_network_detailed, t.getMessage()));
            }
        });
    }

    public void activatePlan(int planId, String startDate) {
        _isLoading.setValue(true);

        repository.activatePlan(planId, startDate, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    _isPlanActivated.setValue(true);
                } else {
                    _error.setValue(getApplication().getString(R.string.error_plan_activation));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _isLoading.setValue(false);
                _error.setValue(getApplication().getString(R.string.error_network_short));
            }
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Application application;
        private final PlanRepository repository;

        public Factory(Application application, PlanRepository repository) {
            this.application = application;
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PlanListViewModel(application, repository);
        }
    }
}