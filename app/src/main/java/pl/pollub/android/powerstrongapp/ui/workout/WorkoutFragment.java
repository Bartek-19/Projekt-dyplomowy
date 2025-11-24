package pl.pollub.android.powerstrongapp.ui.workout;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.PlanCompletionRequestDto; // Import DTO ankiety
import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.databinding.FragmentWorkoutBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;
    private WorkoutViewModel viewModel;
    private WorkoutPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App app = App.getInstance();
        viewModel = new ViewModelProvider(requireActivity(), new WorkoutViewModel.Factory(
                app.getPlanRepository(),
                app.getWorkoutRepository()
        )).get(WorkoutViewModel.class);

        // Pobieranie argumentów (ID dnia treningowego)
        if (getArguments() != null) {
            int dayId = getArguments().getInt("TRAINING_DAY_ID", -1);
            if (dayId != -1) {
                viewModel.loadExercisesForDay(dayId);
            } else {
                Toast.makeText(requireContext(), "Błąd: Brak ID dnia", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigateUp();
            }
        }

        setupViewPager();
        observeData();
        setupButtons();
    }

    private void setupViewPager() {
        pagerAdapter = new WorkoutPagerAdapter(this, new ArrayList<>());
        binding.viewPagerExercises.setAdapter(pagerAdapter);

        // --- EFEKT KARUZELI ---
        binding.viewPagerExercises.setClipToPadding(false);
        binding.viewPagerExercises.setClipChildren(false);
        binding.viewPagerExercises.setOffscreenPageLimit(3);
        binding.viewPagerExercises.setPadding(40, 0, 40, 0); // Paddingi boczne (muszą pasować do XML)

        // Wyłączenie efektu 'overscroll' (niebieskiej poświaty na końcu)
        View child = binding.viewPagerExercises.getChildAt(0);
        if (child instanceof RecyclerView) {
            child.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        // Transformacje stron (skalowanie i marginesy)
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(16)); // Odstęp między kartami
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f); // Skalowanie bocznych kart do 85%
        });

        binding.viewPagerExercises.setPageTransformer(transformer);

        // Callback zmiany strony - aktualizacja UI (przycisków)
        binding.viewPagerExercises.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateUI(position);
            }
        });
    }

    private void observeData() {
        // 1. Obserwacja ćwiczeń do wykonania
        viewModel.plannedExercises.observe(getViewLifecycleOwner(), entities -> {
            if (entities != null && !entities.isEmpty()) {
                List<PlannedExerciseDto> dtos = mapEntitiesToDtos(entities);
                pagerAdapter.updateData(dtos);
                updateUI(binding.viewPagerExercises.getCurrentItem());
            }
        });

        // 2. NOWOŚĆ: Obserwacja czy pokazać ankietę zakończenia planu
        viewModel.showPlanCompletionDialog.observe(getViewLifecycleOwner(), show -> {
            if (Boolean.TRUE.equals(show)) {
                // Ukrywamy loader na przycisku jeśli był włączony
                binding.btnFinishWorkout.setText("Zakończ");
                binding.btnFinishWorkout.setEnabled(true);

                showCompletionDialog();
            }
        });

        // 3. NOWOŚĆ: Obserwacja czy wrócić do menu (po zakończeniu planu)
        viewModel.navigateBack.observe(getViewLifecycleOwner(), navigate -> {
            if (Boolean.TRUE.equals(navigate)) {
                Toast.makeText(requireContext(), "Plan zakończony! Gratulacje.", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).navigateUp();
            }
        });
    }

    private void setupButtons() {
        binding.btnFinishWorkout.setOnClickListener(v -> finishWorkout());

        binding.btnCancelWorkout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Przerwać trening?")
                    .setMessage("Postępy z tej sesji nie zostaną zapisane. Czy na pewno chcesz wyjść?")
                    .setPositiveButton("Tak, wyjdź", (dialog, which) -> {
                        NavHostFragment.findNavController(this).navigateUp();
                    })
                    .setNegativeButton("Wróć do ćwiczeń", null)
                    .show();
        });
    }

    private void finishWorkout() {
        binding.btnFinishWorkout.setEnabled(false);
        binding.btnFinishWorkout.setText("Zapisywanie...");

        viewModel.finishWorkoutAndSave(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (getContext() == null) return;

                // SCENARIUSZ A: To nie był ostatni trening planu
                // ViewModel wywołał ten callback -> Wychodzimy normalnie

                // Jeśli ViewModel wykryje koniec planu, to NIE wywoła tego callbacka,
                // tylko odpali LiveData 'showPlanCompletionDialog'.

                Toast.makeText(requireContext(), "Trening zapisany!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(WorkoutFragment.this).navigateUp();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (getContext() == null) return;

                // Błąd sieci lub inny problem -> Zapisz lokalnie i wyjdź
                binding.btnFinishWorkout.setEnabled(true);
                binding.btnFinishWorkout.setText("Zakończ");
                Toast.makeText(requireContext(), "Zapisano lokalnie (Offline)", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(WorkoutFragment.this).navigateUp();
            }
        });
    }

    // --- NOWA METODA: Wyświetlanie ankiety ---
    private void showCompletionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Inflate layoutu dialogu (upewnij się, że masz plik res/layout/dialog_plan_completion.xml)
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_plan_completion, null);

        CheckBox cbSleep = view.findViewById(R.id.cbSleep);
        CheckBox cbNutrition = view.findViewById(R.id.cbNutrition);
        View sleepHoursContainer = view.findViewById(R.id.sleepHoursContainer);
        EditText etSleepHours = view.findViewById(R.id.etSleepHours);
        SeekBar sbRating = view.findViewById(R.id.sbRating);
        TextView tvRating = view.findViewById(R.id.tvRatingValue);
        Button btnSubmit = view.findViewById(R.id.btnSubmitCompletion);

        // Logika pokazywania pola godzin snu
        cbSleep.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sleepHoursContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Logika paska oceny (SeekBar 0-9 -> Ocena 1-10)
        sbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRating.setText((progress + 1) + "/10");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        AlertDialog dialog = builder.setView(view).setCancelable(false).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btnSubmit.setOnClickListener(v -> {
            boolean nutrition = cbNutrition.isChecked();
            boolean sleep = cbSleep.isChecked();
            Double avgSleep = null;

            if (sleep) {
                String sleepText = etSleepHours.getText().toString();
                if (sleepText.isEmpty()) {
                    etSleepHours.setError("Podaj liczbę");
                    return;
                }
                try {
                    avgSleep = Double.parseDouble(sleepText);
                } catch (NumberFormatException e) {
                    etSleepHours.setError("Błędny format");
                    return;
                }
            }

            int rating = sbRating.getProgress() + 1;

            // Tworzymy DTO z odpowiedziami
            PlanCompletionRequestDto dto = new PlanCompletionRequestDto(nutrition, sleep, avgSleep, rating);

            // Wysyłamy do ViewModelu
            btnSubmit.setEnabled(false);
            btnSubmit.setText("Wysyłanie...");
            viewModel.submitPlanCompletion(dto);

            dialog.dismiss();
        });

        dialog.show();
    }

    private List<PlannedExerciseDto> mapEntitiesToDtos(List<PlannedExerciseEntity> entities) {
        return entities.stream().map(entity -> {
            PlannedExerciseDto dto = new PlannedExerciseDto();
            dto.setId(entity.getId());
            dto.setExerciseName(entity.getExerciseName());
            dto.setExerciseDescription(entity.getExerciseDescription());
            dto.setPlannedSets(entity.getPlannedSets());
            dto.setPlannedReps(entity.getPlannedReps());
            dto.setTargetWeight(entity.getTargetWeight());
            dto.setSuggestionType(entity.getSuggestionType());
            dto.setSuggestionValue(entity.getSuggestionValue());
            dto.setEffortType(entity.getEffortType());
            return dto;
        }).collect(Collectors.toList());
    }

    private void updateUI(int position) {
        int total = pagerAdapter.getItemCount();
        if (total == 0) return;

        // Przycisk Zakończ widoczny tylko na ostatnim slajdzie
        if (position == total - 1) {
            binding.btnFinishWorkout.setVisibility(View.VISIBLE);
        } else {
            binding.btnFinishWorkout.setVisibility(View.INVISIBLE);
        }
    }
}