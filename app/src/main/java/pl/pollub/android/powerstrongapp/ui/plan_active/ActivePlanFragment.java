package pl.pollub.android.powerstrongapp.ui.plan_active;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.ExecutedHistoryDto;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.databinding.DialogDayDetailsBinding;
import pl.pollub.android.powerstrongapp.databinding.FragmentActivePlanBinding;
import pl.pollub.android.powerstrongapp.databinding.ItemExerciseRowBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivePlanFragment extends Fragment {

    private FragmentActivePlanBinding binding;
    private ActivePlanViewModel viewModel;
    private List<ExecutedHistoryDto> cachedHistory = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentActivePlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App app = App.getInstance();
        viewModel = new ViewModelProvider(this, new ActivePlanViewModel.Factory(
                app.getPlanRepository(),
                app.getWorkoutRepository()
        )).get(ActivePlanViewModel.class);

        setupButtons();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        viewModel.getActivePlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan != null) {
                binding.tvPlanName.setText(plan.getName());
                String simpleDetails = plan.getDurationOfCycle() + " " + getString(R.string.weeks_short) + "\n" + plan.getStartDate();
                binding.tvPlanDetails.setText(simpleDetails);

                viewModel.getExecutedSetsHistoryForPlan(plan.getId()).observe(getViewLifecycleOwner(), historyList -> {
                    cachedHistory = historyList;
                    loadCalendarData(plan, historyList);
                });

            } else {
                NavHostFragment.findNavController(this).navigateUp();
            }
        });
    }

    private void loadCalendarData(TrainingPlanEntity plan, List<ExecutedHistoryDto> historyList) {
        viewModel.getPlanDays(plan.getId()).observe(getViewLifecycleOwner(), days -> {
            if (days != null && !days.isEmpty()) {
                setupCalendar(plan, days, historyList);
            }
        });
    }

    private void setupCalendar(TrainingPlanEntity plan, List<TrainingDayEntity> days, List<ExecutedHistoryDto> historyList) {
        if (plan.getStartDate() == null) return;

        try {
            LocalDate planStart = LocalDate.parse(plan.getStartDate());
            int durationWeeks = plan.getDurationOfCycle();
            if (durationWeeks <= 0) durationWeeks = 4;

            LocalDate planEnd = planStart.plusWeeks(durationWeeks);
            LocalDate viewStart = planStart.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate viewEnd = planEnd.minusDays(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault());
            binding.tvCalendarRange.setText(planStart.format(fmt) + " - " + planEnd.minusDays(1).format(fmt));

            List<Long> calendarDays = new ArrayList<>();
            LocalDate curr = viewStart;
            while (!curr.isAfter(viewEnd)) {
                calendarDays.add(curr.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
                curr = curr.plusDays(1);
            }

            List<Long> trainingDates = viewModel.getPlannedTrainingDates(
                    plan,
                    days,
                    viewStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    viewEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

            List<Long> completedDates = viewModel.getCompletedDatesFromHistory(historyList);

            CalendarAdapter adapter = new CalendarAdapter(calendarDays, trainingDates, completedDates, dateMillis -> {
                showDayDetails(dateMillis, plan, days);
            });
            binding.rvCalendar.setLayoutManager(new GridLayoutManager(requireContext(), 7));
            binding.rvCalendar.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(requireContext(), getString(R.string.error_prefix) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDayDetails(long dateMillis, TrainingPlanEntity plan, List<TrainingDayEntity> daysTemplate) {
        TrainingDayEntity dayEntity = viewModel.getDayEntityForDate(dateMillis, plan, daysTemplate);
        if (dayEntity == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogDayDetailsBinding dialogBinding = DialogDayDetailsBinding.inflate(LayoutInflater.from(requireContext()));

        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialogBinding.tvDialogDayTitle.setText(dayEntity.getDayName());

        LocalDate date = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        dialogBinding.tvDialogDate.setText(date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())));

        dialogBinding.btnCloseDialog.setOnClickListener(v -> dialog.dismiss());

        viewModel.getExercisesForClickedDate(dateMillis, plan, daysTemplate).observe(getViewLifecycleOwner(), exercises -> {
            dialogBinding.dialogLoading.setVisibility(View.GONE);
            dialogBinding.dialogExercisesContainer.removeAllViews();

            if (exercises == null || exercises.isEmpty()) {
                dialogBinding.tvDialogEmpty.setVisibility(View.VISIBLE);
                dialogBinding.dialogExercisesContainer.addView(dialogBinding.tvDialogEmpty);
            } else {
                dialogBinding.tvDialogEmpty.setVisibility(View.GONE);

                exercises.sort(Comparator.comparingInt(e -> e.getExerciseOrder() != null ? e.getExerciseOrder() : 0));

                for (PlannedExerciseEntity ex : exercises) {
                    ItemExerciseRowBinding rowBinding = ItemExerciseRowBinding.inflate(
                            getLayoutInflater(), dialogBinding.dialogExercisesContainer, true
                    );

                    String weightText = (ex.getTargetWeight() != null && ex.getTargetWeight() > 0)
                            ? " @ " + ex.getTargetWeight() + "kg" : "";

                    String detailText = "• " + ex.getExerciseName() + " (" +
                            ex.getPlannedSets() + "x" + ex.getPlannedReps() + weightText + ")";

                    rowBinding.tvExerciseDetail.setText(detailText);
                }
            }
        });
        dialog.show();
    }

    private void setupButtons() {
        binding.btnCancelPlan.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.cancel_plan)
                    .setMessage(R.string.delete_account_message)
                    .setPositiveButton(R.string.yes, (dialog, which) -> performCancelPlan())
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        binding.btnHistory.setOnClickListener(v -> {
            if (cachedHistory == null || cachedHistory.isEmpty()) {
                Toast.makeText(requireContext(), R.string.history_empty, Toast.LENGTH_SHORT).show();
            } else {
                HistoryDialogFragment historyDialog = new HistoryDialogFragment(cachedHistory);
                historyDialog.show(getChildFragmentManager(), "history");
            }
        });
    }

    private void performCancelPlan() {
        binding.btnCancelPlan.setEnabled(false);
        binding.btnCancelPlan.setText(R.string.loading);

        viewModel.cancelCurrentPlan(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (getContext() == null) return;

                binding.btnCancelPlan.setEnabled(true);
                binding.btnCancelPlan.setText(R.string.cancel_plan);

                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), R.string.plan_finished_congrats, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(ActivePlanFragment.this).navigateUp();
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error_server_code, String.valueOf(response.code())), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (getContext() == null) return;
                binding.btnCancelPlan.setEnabled(true);
                binding.btnCancelPlan.setText(R.string.cancel_plan);
                Toast.makeText(requireContext(), getString(R.string.error_network_detailed, t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}