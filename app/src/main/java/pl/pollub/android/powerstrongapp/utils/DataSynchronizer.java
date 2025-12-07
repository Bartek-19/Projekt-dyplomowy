package pl.pollub.android.powerstrongapp.utils;

import android.content.Context;
import android.util.Log;
import pl.pollub.android.powerstrongapp.App;

public class DataSynchronizer {
    public static void syncAllData(Context context) {
        App app = (App) context.getApplicationContext();
        AuthManager auth = app.getAuthManager();

        app.getReferenceRepository().syncDictionaries();

        if (auth.isUserLoggedIn()) {
            app.getUserRepository().syncUserData();
            app.getUserRepository().syncPlanHistory();
            app.getPlanRepository().syncFullTrainingPlan();
            app.getWorkoutRepository().synchronizeExecutedSets();
        }
    }
}