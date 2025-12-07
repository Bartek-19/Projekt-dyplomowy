package pl.pollub.android.powerstrongapp.api.service;

import java.util.List;

import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.api.model.UserExerciseMaxDto;
import pl.pollub.android.powerstrongapp.api.model.auth.LoginRequest;
import pl.pollub.android.powerstrongapp.api.model.auth.AuthResponse;
import pl.pollub.android.powerstrongapp.api.model.UserDto;
import pl.pollub.android.powerstrongapp.api.model.auth.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @GET("/api/user/me")
    Call<UserDto> getUserDetails();
    @GET("/api/user/records")
    Call<List<UserExerciseMaxDto>> getUserRecords();
    @GET("/api/user/plans/history")
    Call<List<TrainingPlanFullDto>> getPlanHistory();
    @DELETE("/api/user/me")
    Call<Void> deleteAccount();
    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);
    @POST("/api/auth/register")
    Call<UserDto> register(@Body RegisterRequest registerRequest);
}
