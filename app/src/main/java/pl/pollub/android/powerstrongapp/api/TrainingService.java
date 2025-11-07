package pl.pollub.android.powerstrongapp.api;

import java.util.List;
import pl.pollub.android.powerstrongapp.api.model.ExecutedSetDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrainingService {

    /**
     * Pobiera pełny, aktywny plan treningowy użytkownika (Plan, Dni, Zaplanowane Ćwiczenia)
     * używane do inicjalnej synchronizacji i buforowania całego planu w Room.
     * @param userId ID zalogowanego użytkownika
     */
    @GET("/api/training-plans/active/{userId}")
    Call<TrainingPlanFullDto> getFullActiveTrainingPlan(@Path("userId") int userId);

    /**
     * Wysyła paczkę (bulk) wykonanych serii na serwer (mechanizm Outbox).
     * @param executedSets Lista ExecutedSetDto do zsynchronizowania
     */
    @POST("/api/executed-sets/bulk")
    Call<Void> sendExecutedSets(@Body List<ExecutedSetDto> executedSets);
}