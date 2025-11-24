package pl.pollub.android.powerstrongapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
public class AuthManager {

    private static final String PREFS_NAME = "AuthPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_TOKEN = "authToken";
    private static final int DEFAULT_USER_ID = -1;

    private static AuthManager instance;
    private final SharedPreferences prefs;

    private AuthManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
        return instance;
    }

    public void saveAuthData(int userId, String token) {
        prefs.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_TOKEN, token)
                .apply();
    }
    @Nullable
    public Integer getUserId() {
        int id = prefs.getInt(KEY_USER_ID, DEFAULT_USER_ID);
        return id != DEFAULT_USER_ID ? id : null;
    }
    @Nullable
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }
    public boolean isUserLoggedIn() {
        return getUserId() != null;
    }

    public void logout() {
        prefs.edit()
                .remove(KEY_USER_ID)
                .remove(KEY_TOKEN)
                .apply();
    }
}