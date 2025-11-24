package pl.pollub.android.powerstrongapp.api.service;

import java.util.List;
import pl.pollub.android.powerstrongapp.api.model.ExecutedSetDto;
import pl.pollub.android.powerstrongapp.api.model.PlanCompletionRequestDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrainingService {
    @GET("/api/plans/active")
    Call<TrainingPlanFullDto> getActiveTrainingPlan();
    @POST("/api/executed-sets")
    Call<Void> sendExecutedSets(
            @Body List<ExecutedSetDto> executedSets
    );
    @GET("/api/plans/templates")
    Call<List<TrainingPlanFullDto>> getPlanTemplates();
    @POST("/api/plans/{templateId}/assign")
    Call<Void> assignPlanToUser(
            @Path("templateId") int templateId,
            @Query("startDate") String startDate
    );
    @POST("/api/plans/custom")
    Call<Void> createCustomPlan(@Body TrainingPlanFullDto planDto);
    @POST("/api/plans/active/cancel")
    Call<Void> cancelActivePlan();
    @POST("/api/plans/active/complete")
    Call<Void> completeActivePlan(@Body PlanCompletionRequestDto completionData);
}