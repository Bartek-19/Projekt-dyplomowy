package pl.pollub.android.powerstrongapp.ui.plan_list;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingDayDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.databinding.DialogPlanDetailsBinding;
import pl.pollub.android.powerstrongapp.utils.SuggestionUtils;

public class PlanDetailsDialogFragment extends DialogFragment {

    private static final String ARG_PLAN = "plan_dto";
    private TrainingPlanFullDto plan;
    private DialogPlanDetailsBinding binding;

    private int colorOnSurface;
    private int colorVariant;
    private int colorSurface;

    public static PlanDetailsDialogFragment newInstance(TrainingPlanFullDto plan) {
        PlanDetailsDialogFragment fragment = new PlanDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLAN, plan);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogPlanDetailsBinding.inflate(inflater, container, false);
        if (binding.btnCloseDialog != null) {
            binding.btnCloseDialog.setOnClickListener(v -> dismiss());
        }
        colorOnSurface = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurface, Color.BLACK);
        colorVariant = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant, Color.GRAY);
        colorSurface = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorSurface, Color.WHITE);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.85);
                window.setLayout(width, height);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            plan = (TrainingPlanFullDto) getArguments().getSerializable(ARG_PLAN);
        }

        if (plan != null) {
            binding.tvSheetTitle.setText(plan.getName());
            generateExpandedSchedule(); // Nowa metoda generująca
        }
    }

    private void generateExpandedSchedule() {
        binding.daysContainer.removeAllViews();

        List<TrainingDayDto> templateDays = plan.getTrainingDays();
        if (templateDays == null || templateDays.isEmpty()) return;

        // 1. Ustal długość szablonu (ile tygodni jest zdefiniowanych w bazie?)
        int maxTemplateWeek = 0;
        for (TrainingDayDto day : templateDays) {
            if (day.getWeekNumber() > maxTemplateWeek) {
                maxTemplateWeek = day.getWeekNumber();
            }
        }
        if (maxTemplateWeek == 0) maxTemplateWeek = 1; // Zabezpieczenie

        // 2. Ustal całkowitą długość planu (np. 8 tygodni)
        int totalWeeks = plan.getDurationOfCycle();
        if (totalWeeks == 0) totalWeeks = maxTemplateWeek; // Jeśli duration nie ustawione, pokaż tylko szablon

        // 3. Pętla główna: Generujemy widok Tydzień po Tygodniu
        for (int currentWeek = 1; currentWeek <= totalWeeks; currentWeek++) {

            // --- NAGŁÓWEK TYGODNIA ---
            addWeekHeader(currentWeek);

            // --- LOGIKA ZAPĘTLANIA ---
            // Jeśli jesteśmy w 3. tygodniu, a szablon ma 1 tydzień: ((3-1) % 1) + 1 = 1.
            // Jeśli jesteśmy w 3. tygodniu, a szablon ma 2 tygodnie: ((3-1) % 2) + 1 = 1.
            // Jeśli jesteśmy w 2. tygodniu, a szablon ma 2 tygodnie: ((2-1) % 2) + 1 = 2.
            int templateWeekToUse = ((currentWeek - 1) % maxTemplateWeek) + 1;

            // Pobierz dni pasujące do tego tygodnia szablonu
            List<TrainingDayDto> daysForThisWeek = getDaysForWeek(templateDays, templateWeekToUse);

            // Posortuj je po dayOrder, żeby poniedziałek był przed środą
            Collections.sort(daysForThisWeek, Comparator.comparingInt(TrainingDayDto::getDayOrder));

            // Wygeneruj widoki dla dni
            for (TrainingDayDto day : daysForThisWeek) {
                addDayView(day);
            }
        }
    }

    private List<TrainingDayDto> getDaysForWeek(List<TrainingDayDto> allDays, int weekNumber) {
        List<TrainingDayDto> result = new ArrayList<>();
        for (TrainingDayDto day : allDays) {
            if (day.getWeekNumber() == weekNumber) {
                result.add(day);
            }
        }
        return result;
    }

    private void addWeekHeader(int weekNumber) {
        TextView tvWeekHeader = new TextView(requireContext());
        tvWeekHeader.setText("TYDZIEŃ " + weekNumber);
        tvWeekHeader.setTextSize(14);
        tvWeekHeader.setTextColor(colorVariant);
        tvWeekHeader.setTypeface(null, Typeface.BOLD);
        tvWeekHeader.setPadding(16, 32, 16, 8); // Większy padding z góry dla odstępu
        binding.daysContainer.addView(tvWeekHeader);
    }

    private void addDayView(TrainingDayDto day) {
        // Kontener Dnia (Karta)
        LinearLayout dayLayout = new LinearLayout(requireContext());
        dayLayout.setOrientation(LinearLayout.VERTICAL);
        dayLayout.setPadding(24, 24, 24, 24);

        // Tło: Białe z szarą ramką lub systemowe
        dayLayout.setBackgroundResource(android.R.drawable.editbox_dropdown_light_frame);

        // Marginesy między dniami
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        dayLayout.setLayoutParams(params);

        // 1. Nagłówek Dnia (np. "Poniedziałek (Rozwiń)")
        TextView tvDayName = new TextView(requireContext());
        tvDayName.setText(day.getDayName());
        tvDayName.setTextSize(18);
        tvDayName.setTextColor(colorOnSurface);
        tvDayName.setTypeface(null, Typeface.BOLD);

        // Dodajemy strzałkę/ikonkę tekstową
        tvDayName.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
        tvDayName.setCompoundDrawablePadding(16);

        dayLayout.addView(tvDayName);

        // 2. Kontener Ćwiczeń (Domyślnie UKRYTY)
        LinearLayout exercisesLayout = new LinearLayout(requireContext());
        exercisesLayout.setOrientation(LinearLayout.VERTICAL);
        exercisesLayout.setVisibility(View.GONE);
        exercisesLayout.setPadding(0, 16, 0, 0); // Odstęp od tytułu

        if (day.getPlannedExercises() != null) {
            // Sortowanie ćwiczeń po exerciseOrder
            Collections.sort(day.getPlannedExercises(), Comparator.comparingInt(PlannedExerciseDto::getExerciseOrder));

            for (PlannedExerciseDto ex : day.getPlannedExercises()) {
                addExerciseView(exercisesLayout, ex);
            }
        }
        dayLayout.addView(exercisesLayout);

        // 3. Logika Kliknięcia (Rozwijanie)
        View.OnClickListener toggleListener = v -> {
            boolean isVisible = exercisesLayout.getVisibility() == View.VISIBLE;
            exercisesLayout.setVisibility(isVisible ? View.GONE : View.VISIBLE);

            // Zmiana ikony strzałki (góra/dół)
            int arrowIcon = isVisible ? android.R.drawable.arrow_down_float : android.R.drawable.arrow_up_float;
            tvDayName.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowIcon, 0);
        };

        dayLayout.setOnClickListener(toggleListener);

        binding.daysContainer.addView(dayLayout);
    }

    private void addExerciseView(LinearLayout container, PlannedExerciseDto ex) {
        LinearLayout exRow = new LinearLayout(requireContext());
        exRow.setOrientation(LinearLayout.VERTICAL);
        exRow.setPadding(8, 8, 8, 16);

        // Nazwa ćwiczenia
        TextView tvName = new TextView(requireContext());
        tvName.setText("• " + ex.getExerciseName());
        tvName.setTypeface(null, Typeface.BOLD);
        tvName.setTextSize(16);
        tvName.setTextColor(colorOnSurface);
        exRow.addView(tvName);

        // Szczegóły (Serie x Powtórzenia @ Ciężar)
        TextView tvDetails = new TextView(requireContext());
        String targetInfo = SuggestionUtils.getFormattedTarget(requireContext(), ex);

        String detailsText = String.format(Locale.getDefault(),
                "%d serii x %d powt. (%s)",
                ex.getPlannedSets(), ex.getPlannedReps(), targetInfo);

        tvDetails.setText(detailsText);
        tvDetails.setTextColor(colorVariant);
        tvDetails.setPadding(32, 0, 0, 0); // Wcięcie
        exRow.addView(tvDetails);

        container.addView(exRow);
    }
}