package pl.pollub.android.powerstrongapp.ui.plan_active;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.List;
import java.util.Locale;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.ExecutedHistoryDto; // ZMIANA: Import DTO
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.databinding.FragmentActivePlanBinding;
import pl.pollub.android.powerstrongapp.databinding.ItemExerciseRowBinding;
import pl.pollub.android.powerstrongapp.utils.TrainingCycleCalculator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivePlanFragment extends Fragment {

    private FragmentActivePlanBinding binding;
    private ActivePlanViewModel viewModel;

    // ZMIANA: Typ listy to teraz ExecutedHistoryDto
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

        setupObservers();
        setupButtons();
    }

    private void setupObservers() {
        viewModel.getActivePlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan != null) {
                binding.tvPlanName.setText(plan.getName());
                binding.tvPlanDetails.setText("Cykl: " + plan.getDurationOfCycle() + " tygodni\nStart: " + plan.getStartDate());

                // ZMIANA: Pobieramy historię jako DTO
                viewModel.getExecutedSetsHistoryForPlan(plan.getId()).observe(getViewLifecycleOwner(), historyList -> {
                    cachedHistory = historyList;
                    loadCalendarData(plan, historyList);
                });

            } else {
                NavHostFragment.findNavController(this).navigateUp();
            }
        });
    }

    // ZMIANA: Parametr przyjmuje List<ExecutedHistoryDto>
    private void loadCalendarData(TrainingPlanEntity plan, List<ExecutedHistoryDto> historyList) {
        viewModel.getPlanDays(plan.getId()).observe(getViewLifecycleOwner(), days -> {
            if (days != null && !days.isEmpty()) {
                setupCalendar(plan, days, historyList);
            }
        });
    }

    // ZMIANA: Parametr przyjmuje List<ExecutedHistoryDto>
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

            // 1. Daty zaplanowane (niebieskie)
            List<Long> trainingDates =  viewModel.getPlannedTrainingDates(
                            plan,
                            days,
                            viewStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                            viewEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

            // 2. Daty ukończone (zielone) - ZMIANA: Używamy nowej metody ViewModelu dla DTO
            List<Long> completedDates = viewModel.getCompletedDatesFromHistory(historyList);

            CalendarAdapter adapter = new CalendarAdapter(calendarDays, trainingDates, completedDates, dateMillis -> {
                showDayDetails(dateMillis, plan, days);
            });
            binding.rvCalendar.setLayoutManager(new GridLayoutManager(requireContext(), 7));
            binding.rvCalendar.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(requireContext(), "Błąd kalendarza: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDayDetails(long dateMillis, TrainingPlanEntity plan, List<TrainingDayEntity> daysTemplate) {
        TrainingDayEntity dayEntity = viewModel.getDayEntityForDate(dateMillis, plan, daysTemplate);
        if (dayEntity == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_day_details, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvTitle = dialogView.findViewById(R.id.tvDialogDayTitle);
        TextView tvDate = dialogView.findViewById(R.id.tvDialogDate);
        LinearLayout container = dialogView.findViewById(R.id.dialogExercisesContainer);
        ProgressBar progressBar = dialogView.findViewById(R.id.dialogLoading);
        TextView tvEmpty = dialogView.findViewById(R.id.tvDialogEmpty);
        View btnClose = dialogView.findViewById(R.id.btnCloseDialog);

        tvTitle.setText(dayEntity.getDayName());
        LocalDate date = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        tvDate.setText(date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())));

        btnClose.setOnClickListener(v -> dialog.dismiss());

        viewModel.getExercisesForClickedDate(dateMillis, plan, daysTemplate).observe(getViewLifecycleOwner(), exercises -> {
            progressBar.setVisibility(View.GONE);
            container.removeAllViews();

            if (exercises == null || exercises.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                container.addView(tvEmpty);
            } else {
                tvEmpty.setVisibility(View.GONE);

                exercises.sort((e1, e2) -> Integer.compare(
                        e1.getExerciseOrder() != null ? e1.getExerciseOrder() : 0,
                        e2.getExerciseOrder() != null ? e2.getExerciseOrder() : 0
                ));

                for (PlannedExerciseEntity ex : exercises) {
                    ItemExerciseRowBinding rowBinding = ItemExerciseRowBinding.inflate(
                            getLayoutInflater(), container, true
                    );

                    String weightText = (ex.getTargetWeight() != null && ex.getTargetWeight() > 0)
                            ? " @ " + ex.getTargetWeight() + "kg" : "";

                    String text = "• " + ex.getExerciseName() + " (" +
                            ex.getPlannedSets() + "x" + ex.getPlannedReps() + weightText + ")";

                    rowBinding.tvExerciseDetail.setText(text);
                }
            }
        });
        dialog.show();
    }


    private void setupButtons() {
        binding.btnCancelPlan.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Anulować plan?")
                    .setMessage("Czy na pewno chcesz przerwać obecny cykl treningowy? Tej operacji nie można cofnąć.")
                    .setPositiveButton("Tak, anuluj", (dialog, which) -> performCancelPlan())
                    .setNegativeButton("Nie", null)
                    .show();
        });

        binding.btnHistory.setOnClickListener(v -> {
            if (cachedHistory == null || cachedHistory.isEmpty()) {
                Toast.makeText(requireContext(), "Brak wykonanych treningów w tym planie.", Toast.LENGTH_SHORT).show();
            } else {
                // TERAZ TYPY SIĘ ZGADZAJĄ: cachedHistory to List<ExecutedHistoryDto>
                HistoryDialogFragment historyDialog = new HistoryDialogFragment(cachedHistory);
                historyDialog.show(getChildFragmentManager(), "history");
            }
        });
    }

    private void performCancelPlan() {
        binding.btnCancelPlan.setEnabled(false);
        binding.btnCancelPlan.setText("Anulowanie...");

        viewModel.cancelCurrentPlan(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (getContext() == null) return;

                binding.btnCancelPlan.setEnabled(true);
                binding.btnCancelPlan.setText("Anuluj Plan");

                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Plan został anulowany.", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(ActivePlanFragment.this).navigateUp();
                } else {
                    Toast.makeText(requireContext(), "Błąd serwera: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (getContext() == null) return;
                binding.btnCancelPlan.setEnabled(true);
                binding.btnCancelPlan.setText("Anuluj Plan");
                Toast.makeText(requireContext(), "Błąd sieci: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}