package pl.pollub.android.powerstrongapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;
import androidx.annotation.NonNull;

public class NetworkMonitor {

    private static final String TAG = "NetworkMonitor";
    private static final long MIN_SYNC_INTERVAL_MS = 10_000;
    private static long lastSyncTime = 0;

    public static void startMonitoring(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) return;

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                Log.d(TAG, "Wykryto połączenie z internetem!");

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSyncTime > MIN_SYNC_INTERVAL_MS) {
                    lastSyncTime = currentTime;
                    Log.i(TAG, "Automatyczne wywołanie DataSynchronizer...");
                    DataSynchronizer.syncAllData(context);
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                Log.d(TAG, "Utracono połączenie z internetem.");
            }
        });
    }
}