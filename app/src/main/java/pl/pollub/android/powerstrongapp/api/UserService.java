package pl.pollub.android.powerstrongapp.api;

import pl.pollub.android.powerstrongapp.api.model.UserDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    // Przykład pobierania danych użytkownika po ID
    @GET("/api/users/{id}")
    Call<UserDto> getUserDetails(@Path("id") int userId);
}
