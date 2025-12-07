package pl.pollub.android.powerstrongapp.api;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pl.pollub.android.powerstrongapp.api.service.ReferenceService;
import pl.pollub.android.powerstrongapp.api.service.TrainingService;
import pl.pollub.android.powerstrongapp.api.service.UserService;
import pl.pollub.android.powerstrongapp.utils.AuthManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  RetrofitClient {
    // Emulator: 10.0.2.2, Fizyczny telefon: 192.168.X.X
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();

                        String token = AuthManager.getInstance(context).getToken();

                        if (token != null && !token.isEmpty()) {
                            Request newRequest = originalRequest.newBuilder()
                                    .addHeader("Authorization", "Bearer " + token)
                                    .build();
                            return chain.proceed(newRequest);
                        }

                        return chain.proceed(originalRequest);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static ReferenceService getReferenceService(Context context) {
        return getClient(context).create(ReferenceService.class);
    }
    public static TrainingService getTrainingService(Context context) {
        return getClient(context).create(TrainingService.class);
    }
    public static UserService getUserService(Context context) {
        return getClient(context).create(UserService.class);
    }
}