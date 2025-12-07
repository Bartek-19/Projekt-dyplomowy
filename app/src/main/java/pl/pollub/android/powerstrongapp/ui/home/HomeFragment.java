package pl.pollub.android.powerstrongapp.ui.home;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.color.MaterialColors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App app = App.getInstance();

        viewModel = new ViewModelProvider(this, new MainViewModel.Factory(
                app.getPlanRepository(),
                app.getUserRepository(),
                app.getAuthManager(),
                app.getWorkoutRepository()
        )).get(MainViewModel.class);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                binding.homeLoading.setVisibility(View.VISIBLE);
                binding.homeContent.setVisibility(View.INVISIBLE);
            } else {
                binding.homeLoading.setVisibility(View.GONE);
                binding.homeContent.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getActiveTrainingPlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan == null) {
                showStateNoPlan();
            } else {
                updatePlanProgress(plan);
            }
        });

        viewModel.getAllowedDateTimestamp().observe(getViewLifecycleOwner(), timestamp -> {
            checkTrainingState();
        });

        viewModel.getNextTrainingDay().observe(getViewLifecycleOwner(), day -> {
            checkTrainingState();
        });

        viewModel.getCompletedPlansCount().observe(getViewLifecycleOwner(), count -> {
            binding.tvTrainingsCount.setText(String.valueOf(count != null ? count : 0));
            setStatLabel(binding.tvTrainingsCount, getString(R.string.completed_plans_label));
        });

        viewModel.getCurrentStreak().observe(getViewLifecycleOwner(), streak -> {
            binding.tvStreakDays.setText(String.valueOf(streak != null ? streak : 0));
            setStatLabel(binding.tvStreakDays, getString(R.string.streak_label));
        });

        viewModel.getLastRecord().observe(getViewLifecycleOwner(), record -> {
            if (record != null) {
                String val = record.isBodyweight()
                        ? getString(R.string.max_reps, record.getCurrentOneRepMax().intValue())
                        : getString(R.string.max_weight, String.valueOf(record.getCurrentOneRepMax().intValue()));
                binding.tvLastPR.setText(val);

                String name = record.getExerciseName();
                if (name.length() > 14) name = name.substring(0, 11) + "...";

                if (binding.tvPrExerciseName != null) {
                    binding.tvPrExerciseName.setText(name);
                }
            } else {
                binding.tvLastPR.setText("-");
                if (binding.tvPrExerciseName != null) {
                    binding.tvPrExerciseName.setText("");
                }
            }
        });

        viewModel.getExercisesRemainingCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) binding.tvExercisesRemaining.setText(getExercisesRemainingText(count));
        });

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), userEntity -> {
            setupGreeting(userEntity != null ? userEntity.getUsername() : getString(R.string.default_user));
        });

        viewModel.getProgressCompleted().observe(getViewLifecycleOwner(), completed -> {
            updateProgressBar(completed, viewModel.getProgressTotal().getValue());
        });

        viewModel.getProgressTotal().observe(getViewLifecycleOwner(), total -> {
            updateProgressBar(viewModel.getProgressCompleted().getValue(), total);
        });
    }

    private void updateProgressBar(Integer completed, Integer total) {
        int compVal = (completed != null) ? completed : 0;
        int totalVal = (total != null && total > 0) ? total : 1;

        binding.progressBar.setMax(totalVal);
        binding.progressBar.setProgress(compVal);

        int percent = (int) (((float) compVal / totalVal) * 100);
        String text = getString(R.string.plan_progress_format, compVal, totalVal, percent);
        binding.tvPlanProgress.setText(text);
    }

    private void checkTrainingState() {
        TrainingPlanEntity plan = viewModel.getActiveTrainingPlan().getValue();
        if (plan != null) {
            TrainingDayEntity day = viewModel.getNextTrainingDay().getValue();
            Long timestamp = viewModel.getAllowedDateTimestamp().getValue();
            updateTrainingButtonState(day, timestamp);
        }
    }

    private void showStateNoPlan() {
        binding.layoutTrainingButton.setVisibility(View.GONE);
        binding.layoutPlanProgress.setVisibility(View.GONE);
        binding.layoutNoTraining.setVisibility(View.GONE);
        binding.layoutNoPlan.setVisibility(View.VISIBLE);

        binding.btnStartPlan.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_nav_plans)
        );
    }

    private void updateTrainingButtonState(TrainingDayEntity day, Long timestamp) {
        if (day == null) {
            showPlanCompletedState();
            return;
        }

        binding.layoutNoPlan.setVisibility(View.GONE);
        binding.layoutNoTraining.setVisibility(View.GONE);
        binding.layoutPlanProgress.setVisibility(View.VISIBLE);
        binding.layoutTrainingButton.setVisibility(View.VISIBLE);

        binding.tvDayNumber.setText(day.getDayName());

        boolean isReady = viewModel.isTrainingReady();

        if (isReady) {
            binding.btnStartTraining.setText(R.string.start_training);
            binding.btnStartTraining.setEnabled(true);
            binding.btnStartTraining.setAlpha(1.0f);

            int colorPrimary = MaterialColors.getColor(binding.getRoot(), androidx.appcompat.R.attr.colorPrimary);
            binding.btnStartTraining.setBackgroundTintList(ColorStateList.valueOf(colorPrimary));

            binding.btnStartTraining.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putInt("TRAINING_DAY_ID", day.getId());
                NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_workoutFragment, args);
            });

        } else {
            String dateText = "???";
            if (timestamp != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM", Locale.getDefault());
                dateText = sdf.format(new Date(timestamp));
            }

            binding.btnStartTraining.setText(getString(R.string.next_label, dateText));
            binding.btnStartTraining.setEnabled(false);
            binding.btnStartTraining.setAlpha(0.7f);

            int colorDisabled = MaterialColors.getColor(binding.getRoot(), com.google.android.material.R.attr.colorOutline);
            binding.btnStartTraining.setBackgroundTintList(ColorStateList.valueOf(colorDisabled));

            binding.btnStartTraining.setOnClickListener(null);
        }
    }

    private void showPlanCompletedState() {
        binding.layoutTrainingButton.setVisibility(View.GONE);
        binding.layoutNoPlan.setVisibility(View.GONE);
        binding.layoutNoTraining.setVisibility(View.VISIBLE);
        binding.tvNextTrainingDate.setText(R.string.plan_finished_home_msg);
    }

    private void setStatLabel(TextView valueView, String labelText) {
        if (valueView.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) valueView.getParent();
            if (parent.getChildCount() > 1) {
                View child = parent.getChildAt(1);
                if (child instanceof TextView) {
                    ((TextView) child).setText(labelText);
                }
            }
        }
    }

    private void updatePlanProgress(TrainingPlanEntity plan) {
        if (plan == null) return;
        binding.tvPlanProgress.setText(getString(R.string.plan_progress_days, 1, plan.getDurationOfCycle()));
    }

    private void setupGreeting(String userName) {
        binding.tvGreeting.setText(getString(R.string.greeting_hello, userName));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());
        binding.tvDate.setText(dateFormat.format(new Date()));
    }

    @SuppressLint("StringFormatInvalid")
    private String getExercisesRemainingText(int count) {
        return getString(R.string.exercises_count_suffix, count);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.syncFullTrainingPlan();
    }

    @Override
    public void onDestroyView() { super.onDestroyView(); binding = null; }
}