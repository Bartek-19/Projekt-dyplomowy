package pl.pollub.android.powerstrongapp;

import android.app.Application;

import pl.pollub.android.powerstrongapp.api.RetrofitClient;
import pl.pollub.android.powerstrongapp.api.service.TrainingService;
import pl.pollub.android.powerstrongapp.data.local.AppDatabase;
import pl.pollub.android.powerstrongapp.data.repository.PlanRepository;
import pl.pollub.android.powerstrongapp.data.repository.ReferenceRepository;
import pl.pollub.android.powerstrongapp.data.repository.UserRepository;
import pl.pollub.android.powerstrongapp.data.repository.WorkoutRepository;
import pl.pollub.android.powerstrongapp.utils.AuthManager;
import pl.pollub.android.powerstrongapp.utils.NetworkMonitor;

public class App extends Application {
    private PlanRepository planRepository;
    private WorkoutRepository workoutRepository;
    private UserRepository userRepository;
    private ReferenceRepository referenceRepository;
    private AuthManager authManager;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.authManager = AuthManager.getInstance(this);

        AppDatabase db = AppDatabase.getDatabase(this);
        TrainingService trainingService = RetrofitClient.getTrainingService(this);

        this.planRepository = new PlanRepository(
                this,
                trainingService
        );
        this.workoutRepository = new WorkoutRepository(
                db.executedSetDao(),
                db.trainingPlanAndDayDao(),
                trainingService
        );
        this.userRepository = new UserRepository(
                this,
                RetrofitClient.getUserService(this)
        );
        this.referenceRepository = new ReferenceRepository(
                this,
                RetrofitClient.getReferenceService(this)
        );

        NetworkMonitor.startMonitoring(this);
    }

    public static App getInstance() {
        return instance;
    }
    public PlanRepository getPlanRepository() {
        return planRepository;
    }

    public WorkoutRepository getWorkoutRepository() {
        return workoutRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ReferenceRepository getReferenceRepository() {
        return referenceRepository;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}