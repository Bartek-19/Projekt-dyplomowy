package pl.pollub.android.powerstrongapp.ui.workout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;

public class WorkoutPagerAdapter extends FragmentStateAdapter {

    private final List<PlannedExerciseDto> exercises;

    public WorkoutPagerAdapter(@NonNull Fragment fragment, List<PlannedExerciseDto> exercises) {
        super(fragment);
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Przekazujemy teraz ćwiczenie, pozycję i całkowitą liczbę ćwiczeń
        return WorkoutExecutionFragment.newInstance(
                exercises.get(position),
                position,
                exercises.size()
        );
    }

    public void updateData(List<PlannedExerciseDto> newExercises) {
        this.exercises.clear();
        this.exercises.addAll(newExercises);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }
}