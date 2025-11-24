package pl.pollub.android.powerstrongapp.ui.plan_create;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingDayDto;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.data.local.entity.ExerciseEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingMethodEntity;
import pl.pollub.android.powerstrongapp.databinding.DialogAddExerciseBinding;
import pl.pollub.android.powerstrongapp.databinding.DialogCreatePlanWizardBinding;

public class CreatePlanWizardDialogFragment extends DialogFragment {

    private DialogCreatePlanWizardBinding binding;
    private CreatePlanViewModel viewModel;
    private int currentStep = 0;

    private int colorOnSurface;
    private int colorVariant;
    private int colorSurface;
    private int colorOutline;
    private int colorPrimary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCreatePlanWizardBinding.inflate(inflater, container, false);
        colorOnSurface = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurface, Color.BLACK);
        colorVariant = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant, Color.GRAY);
        colorSurface = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorSurface, Color.WHITE);
        colorOutline = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOutline, Color.LTGRAY);
        colorPrimary = MaterialColors.getColor(requireContext(), androidx.appcompat.R.attr.colorPrimary, Color.BLUE);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App app = App.getInstance();
        viewModel = new ViewModelProvider(this, new CreatePlanViewModel.Factory(
                app.getPlanRepository(),
                app.getReferenceRepository()
        )).get(CreatePlanViewModel.class);

        setupStep1();
        setupNavigation();
        setupObservers();

        // Inicjalizacja stanu UI (ustawi przycisk na "Anuluj" i włączy go)
        goToStep(0);
    }

    private void setupStep1() {
        binding.sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.tvDurationLabel.setText(progress + " tygodni");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ArrayAdapter<TrainingMethodEntity> adapter = new ArrayAdapter<TrainingMethodEntity>(requireContext(), android.R.layout.simple_spinner_item) {};
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spTrainingMethod.setAdapter(adapter);

        binding.spTrainingMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TrainingMethodEntity method = (TrainingMethodEntity) parent.getItemAtPosition(position);
                if (method != null) {
                    if (method.getDurationOfCycle() > 0) {
                        binding.sbDuration.setVisibility(View.GONE);
                        binding.tvDurationLabel.setText(method.getDurationOfCycle() + " tygodni (cykl stały)");
                    } else {
                        binding.sbDuration.setVisibility(View.VISIBLE);
                        binding.tvDurationLabel.setText(binding.sbDuration.getProgress() + " tygodni");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        viewModel.getAvailableTrainingMethods().observe(getViewLifecycleOwner(), methods -> {
            if (methods != null) {
                adapter.clear();

                // ZMIANA: Filtrowanie metody testowej (teraz ignorujemy ID = 1)
                List<TrainingMethodEntity> filteredMethods = methods.stream()
                        .filter(m -> m.getId() != 1)
                        .collect(Collectors.toList());

                adapter.addAll(filteredMethods);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupNavigation() {
        binding.btnNext.setOnClickListener(v -> {
            if (currentStep == 0) {
                if (binding.etPlanName.getText().toString().isEmpty()) {
                    binding.etPlanName.setError("Wymagane");
                    return;
                }
                TrainingMethodEntity selectedMethod = (TrainingMethodEntity) binding.spTrainingMethod.getSelectedItem();
                if (selectedMethod == null) {
                    Toast.makeText(requireContext(), "Wybierz metodę treningową", Toast.LENGTH_SHORT).show();
                    return;
                }

                int finalDuration;
                if (selectedMethod.getDurationOfCycle() > 0) {
                    finalDuration = selectedMethod.getDurationOfCycle();
                } else {
                    finalDuration = binding.sbDuration.getProgress();
                }

                viewModel.setBasicInfo(
                        binding.etPlanName.getText().toString(),
                        finalDuration,
                        selectedMethod.getId());

                goToStep(1);

            } else if (currentStep == 1) {
                if (viewModel.getDaysList().isEmpty()) {
                    Toast.makeText(requireContext(), "Plan jest pusty! Dodaj dni.", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean anyValidDay = false;
                for(TrainingDayDto d : viewModel.getDaysList()) {
                    if(d.getPlannedExercises() != null && !d.getPlannedExercises().isEmpty()) {
                        anyValidDay = true; break;
                    }
                }
                if (!anyValidDay) {
                    Toast.makeText(requireContext(), "Przynajmniej jeden dzień musi mieć dodane ćwiczenia!", Toast.LENGTH_SHORT).show();
                    return;
                }

                generateSummary();
                goToStep(2);

            } else if (currentStep == 2) {
                showStartDatePicker();
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            if (currentStep == 0) {
                // To wywołuje się, gdy przycisk ma napis "Anuluj"
                dismiss();
            } else {
                goToStep(currentStep - 1);
            }
        });

        binding.btnAddCycleWeek.setOnClickListener(v -> viewModel.addNewWeek());
    }

    private void showStartDatePicker() {
        final java.util.Calendar c = java.util.Calendar.getInstance();
        int year = c.get(java.util.Calendar.YEAR);
        int month = c.get(java.util.Calendar.MONTH);
        int day = c.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String dateToSend = String.format(java.util.Locale.US, "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    viewModel.savePlan(dateToSend);
                },
                year, month, day);

        datePickerDialog.setMessage("Kiedy chcesz rozpocząć ten plan?");
        datePickerDialog.show();
    }

    private void goToStep(int step) {
        currentStep = step;
        binding.viewFlipper.setDisplayedChild(step);

        binding.btnBack.setEnabled(true);
        if (step == 0) {
            // Tutaj ustawiamy napis na Anuluj dla pierwszego kroku
            binding.btnBack.setText("Anuluj");
        } else {
            binding.btnBack.setText("Wstecz");
        }

        // ZARZĄDZANIE WIDOCZNOŚCIĄ PRZYCISKU DODAWANIA TYGODNIA
        if (step == 1) {
            int currentWeeks = viewModel.weeksCount.getValue() != null ? viewModel.weeksCount.getValue() : 1;
            int maxWeeks = viewModel.getPlanDuration();

            if (currentWeeks < maxWeeks) {
                binding.btnAddCycleWeek.setVisibility(View.VISIBLE);
            } else {
                binding.btnAddCycleWeek.setVisibility(View.GONE);
            }
        } else {
            binding.btnAddCycleWeek.setVisibility(View.GONE);
        }

        switch (step) {
            case 0: binding.tvStepTitle.setText("Krok 1: Podstawy"); binding.btnNext.setText("Dalej"); break;
            case 1: binding.tvStepTitle.setText("Krok 2: Struktura Planu"); binding.btnNext.setText("Dalej"); break;
            case 2: binding.tvStepTitle.setText("Krok 3: Podsumowanie"); binding.btnNext.setText("Zapisz i Rozpocznij"); break;
        }
    }

    private void setupObservers() {
        viewModel.days.observe(getViewLifecycleOwner(), d -> refreshStructureView());
        viewModel.weeksCount.observe(getViewLifecycleOwner(), w -> {
            refreshStructureView();
            if (currentStep == 1) goToStep(1);
        });
        viewModel.saveSuccess.observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Plan utworzony!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show();
        });
    }

    private void refreshStructureView() {
        binding.structureContainer.removeAllViews();
        int totalWeeks = viewModel.weeksCount.getValue() != null ? viewModel.weeksCount.getValue() : 1;
        List<TrainingDayDto> allDays = viewModel.getDaysList();

        for (int w = 1; w <= totalWeeks; w++) {
            LinearLayout headerContainer = new LinearLayout(requireContext());
            headerContainer.setOrientation(LinearLayout.HORIZONTAL);
            headerContainer.setGravity(Gravity.CENTER_VERTICAL);
            headerContainer.setPadding(0, 24, 0, 16);

            TextView weekHeader = new TextView(requireContext());
            weekHeader.setText("TYDZIEŃ " + w);
            weekHeader.setTextSize(16);
            weekHeader.setTextColor(colorOnSurface);
            weekHeader.setTypeface(null, Typeface.BOLD);
            headerContainer.addView(weekHeader, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

            if (totalWeeks > 1) {
                TextView btnDeleteWeek = new TextView(requireContext());
                btnDeleteWeek.setText("Usuń");
                btnDeleteWeek.setTextColor(Color.RED);
                btnDeleteWeek.setTextSize(12);
                btnDeleteWeek.setPadding(16, 8, 16, 8);

                final int weekToRemove = w;
                btnDeleteWeek.setOnClickListener(v -> confirmRemoveWeek(weekToRemove));
                headerContainer.addView(btnDeleteWeek);
            }

            binding.structureContainer.addView(headerContainer);

            View visualizer = createWeekVisualizer(w);
            binding.structureContainer.addView(visualizer);

            List<TrainingDayDto> daysInWeek = new ArrayList<>();
            for(TrainingDayDto d : allDays) if(d.getWeekNumber() == w) daysInWeek.add(d);
            Collections.sort(daysInWeek, Comparator.comparingInt(TrainingDayDto::getDayOrder));

            for (TrainingDayDto day : daysInWeek) {
                binding.structureContainer.addView(createDayItemView(day));
            }
        }
    }

    private void confirmRemoveWeek(int weekNumber) {
        if (!viewModel.isWeekEmpty(weekNumber)) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Usunąć Tydzień " + weekNumber + "?")
                    .setMessage("Ten tydzień zawiera dni treningowe. Czy na pewno chcesz go usunąć?")
                    .setPositiveButton("Usuń", (d, w) -> viewModel.removeWeek(weekNumber))
                    .setNegativeButton("Anuluj", null)
                    .show();
        } else {
            viewModel.removeWeek(weekNumber);
        }
    }

    private View createWeekVisualizer(int weekNumber) {
        LinearLayout visualizer = new LinearLayout(requireContext());
        visualizer.setOrientation(LinearLayout.HORIZONTAL);
        visualizer.setGravity(Gravity.CENTER_HORIZONTAL);
        visualizer.setPadding(0, 0, 0, 24);

        int size = dpToPx(48);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(dpToPx(6), 0, dpToPx(6), 0);

        for (int i = 1; i <= 7; i++) {
            final int dayIndex = i;
            TextView dayBtn = new TextView(requireContext());
            dayBtn.setLayoutParams(params);
            dayBtn.setGravity(Gravity.CENTER);
            dayBtn.setTextSize(16);
            dayBtn.setTypeface(null, Typeface.BOLD);
            dayBtn.setText(String.valueOf(i));

            boolean isActive = viewModel.hasDay(weekNumber, dayIndex);

            if (isActive) {
                dayBtn.setBackgroundResource(R.drawable.bg_day_active);
                dayBtn.setTextColor(Color.WHITE);
                dayBtn.setElevation(dpToPx(4));
            } else {
                dayBtn.setBackgroundResource(R.drawable.bg_day_inactive);
                dayBtn.setTextColor(colorVariant);
                dayBtn.setElevation(0);
            }

            dayBtn.setOnClickListener(v -> handleDayToggle(weekNumber, dayIndex, isActive));
            visualizer.addView(dayBtn);
        }
        return visualizer;
    }

    private void handleDayToggle(int weekNumber, int dayIndex, boolean currentlyActive) {
        if (!currentlyActive) {
            viewModel.addDayToWeek(weekNumber, dayIndex);
        } else {
            TrainingDayDto day = viewModel.getDay(weekNumber, dayIndex);
            if (day != null && day.getPlannedExercises() != null && !day.getPlannedExercises().isEmpty()) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Usunąć dzień " + dayIndex + "?")
                        .setMessage("Zawiera ćwiczenia. Czy na pewno?")
                        .setPositiveButton("Usuń", (d, w) -> viewModel.removeDay(weekNumber, dayIndex))
                        .setNegativeButton("Anuluj", null)
                        .show();
            } else {
                viewModel.removeDay(weekNumber, dayIndex);
            }
        }
    }

    private View createDayItemView(TrainingDayDto day) {
        MaterialCardView card = new MaterialCardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setRadius(dpToPx(12));
        card.setCardElevation(dpToPx(4));
        card.setCardBackgroundColor(colorSurface);

        LinearLayout cardContent = new LinearLayout(requireContext());
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(32, 32, 32, 32);
        card.addView(cardContent);

        LinearLayout header = new LinearLayout(requireContext());
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView badge = new TextView(requireContext());
        int dayCycleIndex = ((day.getDayOrder() - 1) % 7) + 1;
        badge.setText(String.valueOf(dayCycleIndex));
        badge.setTextColor(Color.WHITE);
        badge.setTextSize(14);
        badge.setTypeface(null, Typeface.BOLD);
        badge.setGravity(Gravity.CENTER);
        badge.setBackgroundResource(R.drawable.bg_day_active);

        LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(dpToPx(32), dpToPx(32));
        badgeParams.setMargins(0, 0, 24, 0);
        badge.setLayoutParams(badgeParams);
        header.addView(badge);

        TextView tvName = new TextView(requireContext());
        tvName.setText(day.getDayName());
        tvName.setTextSize(18);
        tvName.setTextColor(colorOnSurface);
        tvName.setTypeface(null, Typeface.BOLD);
        header.addView(tvName);
        cardContent.addView(header);

        View line = new View(requireContext());
        line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1)));
        line.setBackgroundColor(colorOutline);
        ((LinearLayout.LayoutParams) line.getLayoutParams()).setMargins(0, 24, 0, 24);
        cardContent.addView(line);

        if (day.getPlannedExercises() != null && !day.getPlannedExercises().isEmpty()) {
            for (PlannedExerciseDto ex : day.getPlannedExercises()) {
                TextView exTxt = new TextView(requireContext());
                exTxt.setText("•  " + ex.getExerciseName() + "  (" + ex.getPlannedSets() + " × " + ex.getPlannedReps() + ")");
                exTxt.setTextColor(colorVariant);
                exTxt.setTextSize(14);
                exTxt.setPadding(0, 0, 0, 12);
                cardContent.addView(exTxt);
            }
        } else {
            TextView empty = new TextView(requireContext());
            empty.setText("Brak ćwiczeń");
            empty.setTextSize(12);
            empty.setTextColor(colorVariant);
            empty.setGravity(Gravity.CENTER_HORIZONTAL);
            cardContent.addView(empty);
        }

        Button btnAdd = new Button(requireContext());
        btnAdd.setText("+ Dodaj Ćwiczenie");
        btnAdd.setTextSize(12);
        btnAdd.setBackgroundColor(Color.TRANSPARENT);
        btnAdd.setTextColor(colorPrimary);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(40));
        btnParams.gravity = Gravity.END;
        btnAdd.setLayoutParams(btnParams);

        btnAdd.setOnClickListener(v -> showAddExerciseDialog(day));
        cardContent.addView(btnAdd);

        return card;
    }

    private void showAddExerciseDialog(TrainingDayDto day) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogAddExerciseBinding exBinding = DialogAddExerciseBinding.inflate(getLayoutInflater());
        builder.setView(exBinding.getRoot());

        String[] effortTypes = {"Max", "Volume", "Dynamic", "Speed"};
        ArrayAdapter<String> effortAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, effortTypes);
        effortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exBinding.spEffortType.setAdapter(effortAdapter);

        ArrayAdapter<ExerciseEntity> exerciseAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exBinding.spExercise.setAdapter(exerciseAdapter);

        // Ładowanie ćwiczeń
        viewModel.getAvailableExercises().observe(getViewLifecycleOwner(), exercises -> {
            if (exercises != null) {
                exerciseAdapter.clear();
                exerciseAdapter.addAll(exercises);
                exerciseAdapter.notifyDataSetChanged();
            }
        });

        // NOWOŚĆ: Reakcja na zmianę ćwiczenia
        exBinding.spExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ExerciseEntity selected = (ExerciseEntity) parent.getItemAtPosition(position);

                if (selected != null && selected.isBodyweight()) {
                    exBinding.spEffortType.setEnabled(false);
                } else {
                    exBinding.spEffortType.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        builder.setPositiveButton("Dodaj", (d, w) -> {
            ExerciseEntity selectedExercise = (ExerciseEntity) exBinding.spExercise.getSelectedItem();
            if (selectedExercise == null) return;

            try {
                String setsStr = exBinding.etSets.getText().toString();
                String repsStr = exBinding.etReps.getText().toString();

                if (setsStr.isEmpty() || repsStr.isEmpty()) {
                    Toast.makeText(requireContext(), "Wypełnij serie i powtórzenia", Toast.LENGTH_SHORT).show();
                    return;
                }

                int sets = Integer.parseInt(setsStr);
                int reps = Integer.parseInt(repsStr);

                PlannedExerciseDto dto = new PlannedExerciseDto();
                dto.setId(selectedExercise.getId());
                dto.setExerciseName(selectedExercise.getName());
                dto.setPlannedSets(sets);
                dto.setPlannedReps(reps);

                // LOGIKA BODYWEIGHT PRZY TWORZENIU
                if (selectedExercise.isBodyweight()) {
                    // Kluczowe: ustawiamy typ sugestii na BODYWEIGHT
                    dto.setSuggestionType("BODYWEIGHT");
                    dto.setSuggestionValue(0.0);
                    dto.setEffortType("Bodyweight"); // Opcjonalnie nadpisujemy typ wysiłku
                    dto.setTargetWeight(null);
                } else {
                    // Standardowa logika dla ciężarów
                    dto.setSuggestionType("RPE");
                    dto.setSuggestionValue(8.0);
                    dto.setEffortType((String) exBinding.spEffortType.getSelectedItem());
                    dto.setTargetWeight(null); // Ciężar i tak wyliczy serwer/algorytm
                }

                viewModel.addExerciseToDay(day, dto);

            } catch (Exception e) {
                Toast.makeText(requireContext(), "Błąd danych: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Anuluj", null);
        builder.show();
    }

    private void generateSummary() {
        TrainingPlanFullDto p = viewModel.getPlanSummary();
        String summary = "Nazwa: " + p.getName() + "\n" +
                "Długość: " + p.getDurationOfCycle() + " tyg.\n" +
                "Liczba dni: " + p.getTrainingDays().size();
        binding.tvSummary.setText(summary);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDestroyView() { super.onDestroyView(); binding = null; }
}