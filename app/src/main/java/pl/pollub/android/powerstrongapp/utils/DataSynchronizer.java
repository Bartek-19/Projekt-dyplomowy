package pl.pollub.android.powerstrongapp.utils;

import android.content.Context;
import android.util.Log;
import pl.pollub.android.powerstrongapp.App;

public class DataSynchronizer {

    private static final String TAG = "DataSync";

    public static void syncAllData(Context context) {
        App app = (App) context.getApplicationContext();
        AuthManager auth = app.getAuthManager();

        Log.d(TAG, ">>> START SYNCHRONIZACJI DANYCH <<<");

        app.getReferenceRepository().syncDictionaries();

        if (auth.isUserLoggedIn()) {
            Log.d(TAG, "Użytkownik zalogowany - pobieram dane prywatne...");
            app.getUserRepository().syncUserData();
            app.getUserRepository().syncPlanHistory();
            app.getPlanRepository().syncFullTrainingPlan();
            app.getWorkoutRepository().synchronizeExecutedSets();

        } else {
            Log.d(TAG, "Użytkownik niezalogowany - pomijam dane prywatne.");
        }
    }
}