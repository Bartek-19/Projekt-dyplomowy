package pl.pollub.android.powerstrongapp.ui.workout;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.List;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.ExecutedSetDto;
import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;
import pl.pollub.android.powerstrongapp.databinding.ItemExerciseBinding;

public class WorkoutExecutionFragment extends Fragment {

    private ItemExerciseBinding binding;
    private WorkoutViewModel workoutViewModel;
    private PlannedExerciseDto plannedExercise;
    private int currentPosition;
    private int totalExercises;

    private static final String ARG_EXERCISE_DATA = "exercise_data";
    private static final String ARG_POSITION = "arg_position";
    private static final String ARG_TOTAL = "arg_total";

    private final List<EditText> repInputFields = new ArrayList<>();
    private final List<EditText> weightInputFields = new ArrayList<>();

    public static WorkoutExecutionFragment newInstance(PlannedExerciseDto plannedExercise, int position, int total) {
        WorkoutExecutionFragment fragment = new WorkoutExecutionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXERCISE_DATA, plannedExercise);
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_TOTAL, total);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ItemExerciseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App app = App.getInstance();
        workoutViewModel = new ViewModelProvider(requireActivity(), new WorkoutViewModel.Factory(
                app.getPlanRepository(),
                app.getWorkoutRepository()
        )).get(WorkoutViewModel.class);

        if (getArguments() != null) {
            plannedExercise = (PlannedExerciseDto) getArguments().getSerializable(ARG_EXERCISE_DATA);
            currentPosition = getArguments().getInt(ARG_POSITION, 0);
            totalExercises = getArguments().getInt(ARG_TOTAL, 0);

            if (plannedExercise != null) {
                setupUI();
                restorePreviousResults();
            }
        }
    }

    private void setupUI() {
        if (totalExercises > 0) {
            binding.tvExerciseCounter.setText(getString(R.string.exercise_counter_format, (currentPosition + 1), totalExercises));
            binding.tvExerciseCounter.setVisibility(View.VISIBLE);
        } else {
            binding.tvExerciseCounter.setVisibility(View.GONE);
        }

        binding.tvExerciseName.setText(plannedExercise.getExerciseName());

        String desc = plannedExercise.getExerciseDescription();
        if (desc == null || desc.isEmpty()) {
            binding.tvExerciseDescription.setVisibility(View.GONE);
        } else {
            binding.tvExerciseDescription.setText(desc);
            binding.tvExerciseDescription.setVisibility(View.VISIBLE);
        }
        boolean isBodyweight = "BODYWEIGHT".equalsIgnoreCase(plannedExercise.getSuggestionType());
        binding.tvHeaderWeight.setVisibility(isBodyweight ? View.GONE : View.VISIBLE);
        if (binding.tvHeaderReps != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.tvHeaderReps.getLayoutParams();
            params.weight = isBodyweight ? 0.6f : 0.3f;
            params.setMarginEnd(isBodyweight ? 0 : 16);
            binding.tvHeaderReps.setLayoutParams(params);
        }
        createSetInputFields(plannedExercise);
    }

    private void createSetInputFields(PlannedExerciseDto exercise) {
        repInputFields.clear();
        weightInputFields.clear();
        binding.seriesContainer.removeAllViews();

        int setsCount = exercise.getPlannedSets() != null ? exercise.getPlannedSets() : 0;
        if (setsCount == 0) return;

        String targetText = getTargetDescription(exercise);
        boolean isBodyweight = "BODYWEIGHT".equalsIgnoreCase(exercise.getSuggestionType());

        int colorOnSurface = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurface, Color.BLACK);
        int colorOutline = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOutline, Color.LTGRAY);

        for (int i = 1; i <= setsCount; i++) {
            LinearLayout setRow = new LinearLayout(requireContext());
            setRow.setOrientation(LinearLayout.HORIZONTAL);
            setRow.setPadding(0, 12, 0, 12);
            setRow.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvTarget = new TextView(requireContext());
            tvTarget.setText(targetText);
            tvTarget.setTextSize(14);
            tvTarget.setTextColor(colorOnSurface);

            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f);
            tvTarget.setLayoutParams(labelParams);
            setRow.addView(tvTarget);

            EditText etReps = new EditText(requireContext());
            etReps.setHint(String.valueOf(exercise.getPlannedReps()));
            etReps.setInputType(InputType.TYPE_CLASS_NUMBER);
            etReps.setGravity(Gravity.CENTER);
            etReps.setTextColor(colorOnSurface);
            etReps.setBackgroundResource(android.R.drawable.edit_text);

            float repsWeight = isBodyweight ? 0.6f : 0.3f;
            LinearLayout.LayoutParams repsParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, repsWeight);

            if (!isBodyweight) {
                repsParams.setMarginEnd(16);
            }

            etReps.setLayoutParams(repsParams);
            setRow.addView(etReps);
            repInputFields.add(etReps);

            EditText etWeight = new EditText(requireContext());
            if (isBodyweight) {
                etWeight.setVisibility(View.GONE);
                etWeight.setText("0");
            } else {
                if (exercise.getTargetWeight() != null && exercise.getTargetWeight() > 0) {
                    etWeight.setHint(String.valueOf(exercise.getTargetWeight()));
                } else {
                    etWeight.setHint("-");
                }
                etWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                etWeight.setGravity(Gravity.CENTER);
                etWeight.setTextColor(colorOnSurface);

                LinearLayout.LayoutParams weightParams = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.3f);
                etWeight.setLayoutParams(weightParams);
                setRow.addView(etWeight);
            }

            weightInputFields.add(etWeight);

            setupTextWatcher(etReps);
            if (!isBodyweight) {
                setupTextWatcher(etWeight);
            }

            binding.seriesContainer.addView(setRow);

            if (i < setsCount) {
                View line = new View(requireContext());
                line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                line.setBackgroundColor(colorOutline);
                binding.seriesContainer.addView(line);
            }
        }
    }

    private String getTargetDescription(PlannedExerciseDto ex) {
        int reps = ex.getPlannedReps() != null ? ex.getPlannedReps() : 0;

        if (ex.getTargetWeight() != null && ex.getTargetWeight() > 0) {
            return getString(R.string.target_weight_format, reps, String.valueOf(ex.getTargetWeight()));
        }

        String type = ex.getSuggestionType();

        if ("BODYWEIGHT".equalsIgnoreCase(type)) {
            return getString(R.string.target_bodyweight, reps);
        }

        if ("RPE".equalsIgnoreCase(type)) {
            double rpeVal = ex.getSuggestionValue() != null ? ex.getSuggestionValue() : 8.0;
            return getString(R.string.target_rpe, reps, String.valueOf(rpeVal));
        }

        if ("FIND_MAX".equalsIgnoreCase(type)) {
            return getString(R.string.target_find_max, reps);
        }

        if (ex.getEffortType() != null && !ex.getEffortType().isEmpty()) {
            return getString(R.string.target_effort, reps, ex.getEffortType());
        }

        return getString(R.string.target_reps_only, reps);
    }

    private void setupTextWatcher(EditText editText) {
        editText.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                saveDataToViewModel();
            }
        });
    }

    private void restorePreviousResults() {
        if (plannedExercise == null || plannedExercise.getId() == null) return;
        List<ExecutedSetDto> savedResults = workoutViewModel.getResultsForExercise(plannedExercise.getId());

        if (savedResults != null && !savedResults.isEmpty()) {
            for (int i = 0; i < savedResults.size(); i++) {
                if (i >= repInputFields.size()) break;
                ExecutedSetDto set = savedResults.get(i);

                if (set.getExecutedReps() > 0) {
                    repInputFields.get(i).setText(String.valueOf(set.getExecutedReps()));
                }
                if (set.getWeightUsed() > 0) {
                    weightInputFields.get(i).setText(String.valueOf(set.getWeightUsed()));
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDataToViewModel();
    }

    private void saveDataToViewModel() {
        if (plannedExercise == null) return;
        List<ExecutedSetDto> results = getExecutedSetDtos();
        workoutViewModel.saveCurrentExerciseResults(plannedExercise.getId(), results);
    }

    public List<ExecutedSetDto> getExecutedSetDtos() {
        List<ExecutedSetDto> records = new ArrayList<>();
        if (plannedExercise == null || plannedExercise.getId() == null) return records;

        boolean isBodyweight = "BODYWEIGHT".equalsIgnoreCase(plannedExercise.getSuggestionType());

        for (int i = 0; i < repInputFields.size(); i++) {
            String repsText = repInputFields.get(i).getText().toString();
            String weightText = "";
            if (i < weightInputFields.size()) {
                weightText = weightInputFields.get(i).getText().toString();
            }

            if (repsText.isEmpty()) continue;

            try {
                int actualReps = Integer.parseInt(repsText);
                double actualWeight = 0.0;

                if (isBodyweight) {
                    actualWeight = 0.0;
                } else {
                    if (!weightText.isEmpty()) {
                        actualWeight = Double.parseDouble(weightText.replace(',', '.'));
                    }
                }

                ExecutedSetDto dto = new ExecutedSetDto(
                        plannedExercise.getId(),
                        i + 1,
                        actualReps,
                        actualWeight
                );
                records.add(dto);
            } catch (NumberFormatException e) {}
        }
        return records;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}