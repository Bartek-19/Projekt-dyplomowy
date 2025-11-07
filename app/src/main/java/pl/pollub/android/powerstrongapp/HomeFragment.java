// HomeFragment.java
package pl.pollub.android.powerstrongapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment; // Za chwilę to wykorzystamy

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Zauważ zmianę importu!
import pl.pollub.android.powerstrongapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    // Krok 2.1: Zmiana sposobu obsługi View Binding
    private FragmentHomeBinding binding;

    // Przykładowe dane - w prawdziwej aplikacji będą pobierane z bazy danych
    private boolean hasActivePlan = true;
    private boolean isTrainingToday = true;
    private String userName = "Kamil";
    private int currentDay = 3;
    private int totalDays = 12;
    private int exercisesRemaining = 5;
    private String nextTrainingDate = "Sobota, 9 listopada";
    private int completedTrainings = 12;
    private int plannedTrainings = 16;
    private int streakDays = 5;
    private int lastPR = 140;


    // Krok 2.2: Użyj onCreateView do "nadmuchania" widoku
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Użyj FragmentHomeBinding (wygenerowanego z fragment_home.xml)
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // Krok 2.3: Użyj onViewCreated do uruchomienia logiki (zamiast onCreate z Aktywności)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cała Twoja logika z onCreate() idzie tutaj
        setupGreeting();
        setupTrainingStatus();
        setupStatistics();
    }

    // Krok 2.4: Wklejone wszystkie Twoje metody
    // (Poniżej jest DOKŁADNIE Twój kod, ale musimy poprawić parę rzeczy)

    private void setupGreeting() {
        // getString() wymaga teraz kontekstu
        binding.tvGreeting.setText(requireContext().getString(R.string.greeting_hello, userName));

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM", new Locale("pl", "PL"));
        String currentDate = dateFormat.format(new Date());
        binding.tvDate.setText(currentDate);
    }

    private void setupTrainingStatus() {
        hideAllTrainingLayouts();

        if (!hasActivePlan) {
            showLayout(binding.layoutNoPlan);
            binding.btnStartPlan.setOnClickListener(v -> {
                // Krok 2.5: ZMIANA NAWIGACJI
                // ŹLE (stary sposób): startActivity(new Intent(getActivity(), NewPlanActivity.class))

                // DOBRZE (nowy sposób z Jetpack Navigation):
                // NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_newPlanFragment);
                // Na razie zostawiamy to puste, skonfigurujemy to w kroku 4
            });
            return;
        }

        showLayout(binding.layoutPlanProgress);
        updateProgressBar(currentDay, totalDays);
        binding.tvPlanProgress.setText(
                // getString() wymaga teraz kontekstu
                requireContext().getString(R.string.plan_progress_days, currentDay, totalDays)
        );

        if (isTrainingToday) {
            showLayout(binding.layoutTrainingButton);
            binding.tvDayNumber.setText(requireContext().getString(R.string.day_number, currentDay));
            binding.tvExercisesRemaining.setText(getExercisesRemainingText(exercisesRemaining));

            binding.btnStartTraining.setOnClickListener(v -> {
                // Uruchamiaj nawigację tylko, jeśli masz ustawiony NavController
                NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_workoutFragment);
            });
        } else {
            showLayout(binding.layoutNoTraining);
            binding.tvNextTrainingDate.setText(
                    requireContext().getString(R.string.next_training_date, nextTrainingDate)
            );
        }
    }

    private void hideAllTrainingLayouts() {
        binding.layoutTrainingButton.setVisibility(View.GONE);
        binding.layoutNoTraining.setVisibility(View.GONE);
        binding.layoutNoPlan.setVisibility(View.GONE);
        binding.layoutPlanProgress.setVisibility(View.GONE);
    }

    private void showLayout(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void updateProgressBar(int current, int total) {
        int progress = total > 0 ? (int) ((float) current / total * 100) : 0;
        binding.progressBar.setProgress(progress);
    }

    private void setupStatistics() {
        binding.tvTrainingsCount.setText(requireContext().getString(R.string.trainings_count, completedTrainings, plannedTrainings));
        binding.tvStreakDays.setText(requireContext().getString(R.string.streak_days, streakDays));
        binding.tvLastPR.setText(requireContext().getString(R.string.last_pr_weight, lastPR));
    }

    @SuppressLint("StringFormatInvalid")
    private String getExercisesRemainingText(int count) {
        // getString() wymaga teraz kontekstu
        if (count == 1) {
            return requireContext().getString(R.string.exercises_remaining_single, count);
        } else if (count % 10 >= 2 && count % 10 <= 4 && (count % 100 < 10 || count % 100 >= 20)) {
            return requireContext().getString(R.string.exercises_remaining, count);
        } else {
            return requireContext().getString(R.string.exercises_remaining_many, count);
        }
    }

    // Metoda do odświeżenia widoku po zmianie danych
    public void refreshView() {
        setupGreeting();
        setupTrainingStatus();
        setupStatistics();
    }

    // Krok 2.6: Kluczowe dla fragmentów - czyszczenie bindingu
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Zapobiega wyciekom pamięci
    }
}