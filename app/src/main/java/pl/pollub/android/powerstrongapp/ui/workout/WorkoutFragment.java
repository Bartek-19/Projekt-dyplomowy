package pl.pollub.android.powerstrongapp.ui.workout;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
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
import pl.pollub.android.powerstrongapp.api.model.PlanCompletionRequestDto;
import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;
import pl.pollub.android.powerstrongapp.data.local.entity.PlannedExerciseEntity;
import pl.pollub.android.powerstrongapp.databinding.DialogPlanCompletionBinding; // Import Bindingu Dialogu
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

        if (getArguments() != null) {
            int dayId = getArguments().getInt("TRAINING_DAY_ID", -1);
            if (dayId != -1) {
                viewModel.loadExercisesForDay(dayId);
            } else {
                Toast.makeText(requireContext(), R.string.error_no_day_id, Toast.LENGTH_SHORT).show();
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

        binding.viewPagerExercises.setClipToPadding(false);
        binding.viewPagerExercises.setClipChildren(false);
        binding.viewPagerExercises.setOffscreenPageLimit(3);
        binding.viewPagerExercises.setPadding(40, 0, 40, 0);

        View child = binding.viewPagerExercises.getChildAt(0);
        if (child instanceof RecyclerView) {
            child.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(16));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        binding.viewPagerExercises.setPageTransformer(transformer);

        binding.viewPagerExercises.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateUI(position);
            }
        });
    }

    private void observeData() {
        viewModel.plannedExercises.observe(getViewLifecycleOwner(), entities -> {
            if (entities != null && !entities.isEmpty()) {
                List<PlannedExerciseDto> dtos = mapEntitiesToDtos(entities);
                pagerAdapter.updateData(dtos);
                updateUI(binding.viewPagerExercises.getCurrentItem());
            }
        });

        viewModel.showPlanCompletionDialog.observe(getViewLifecycleOwner(), show -> {
            if (Boolean.TRUE.equals(show)) {
                binding.btnFinishWorkout.setText(R.string.finish);
                binding.btnFinishWorkout.setEnabled(true);
                showCompletionDialog();
            }
        });

        viewModel.navigateBack.observe(getViewLifecycleOwner(), navigate -> {
            if (Boolean.TRUE.equals(navigate)) {
                Toast.makeText(requireContext(), R.string.plan_finished_congrats, Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).navigateUp();
            }
        });
    }

    private void setupButtons() {
        binding.btnFinishWorkout.setOnClickListener(v -> finishWorkout());

        binding.btnCancelWorkout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.cancel_workout_title)
                    .setMessage(R.string.cancel_workout_message)
                    .setPositiveButton(R.string.yes_exit, (dialog, which) -> {
                        NavHostFragment.findNavController(this).navigateUp();
                    })
                    .setNegativeButton(R.string.return_to_workout, null)
                    .show();
        });
    }

    private void finishWorkout() {
        binding.btnFinishWorkout.setEnabled(false);
        binding.btnFinishWorkout.setText(R.string.saving);

        viewModel.finishWorkoutAndSave(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (getContext() == null) return;
                Toast.makeText(requireContext(), R.string.workout_saved, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(WorkoutFragment.this).navigateUp();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (getContext() == null) return;
                binding.btnFinishWorkout.setEnabled(true);
                binding.btnFinishWorkout.setText(R.string.finish);
                Toast.makeText(requireContext(), R.string.saved_offline, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(WorkoutFragment.this).navigateUp();
            }
        });
    }

    private void showCompletionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        DialogPlanCompletionBinding dialogBinding = DialogPlanCompletionBinding.inflate(LayoutInflater.from(requireContext()));
        builder.setView(dialogBinding.getRoot());

        dialogBinding.cbSleep.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dialogBinding.sleepHoursContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
        dialogBinding.sbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dialogBinding.tvRatingValue.setText((progress + 1) + "/10");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        AlertDialog dialog = builder.setCancelable(false).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialogBinding.btnSubmitCompletion.setOnClickListener(v -> {
            boolean nutrition = dialogBinding.cbNutrition.isChecked();
            boolean sleep = dialogBinding.cbSleep.isChecked();
            Double avgSleep = null;

            if (sleep) {
                String sleepText = dialogBinding.etSleepHours.getText().toString();
                if (sleepText.isEmpty()) {
                    dialogBinding.etSleepHours.setError(getString(R.string.error_enter_number));
                    return;
                }
                try {
                    avgSleep = Double.parseDouble(sleepText);
                } catch (NumberFormatException e) {
                    dialogBinding.etSleepHours.setError(getString(R.string.error_invalid_format));
                    return;
                }
            }

            int rating = dialogBinding.sbRating.getProgress() + 1;
            PlanCompletionRequestDto dto = new PlanCompletionRequestDto(nutrition, sleep, avgSleep, rating);

            dialogBinding.btnSubmitCompletion.setEnabled(false);
            dialogBinding.btnSubmitCompletion.setText(R.string.sending);
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
        if (position == total - 1) {
            binding.btnFinishWorkout.setVisibility(View.VISIBLE);
        } else {
            binding.btnFinishWorkout.setVisibility(View.INVISIBLE);
        }
    }
}