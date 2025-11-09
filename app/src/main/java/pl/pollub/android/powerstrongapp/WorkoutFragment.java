package pl.pollub.android.powerstrongapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.pollub.android.powerstrongapp.databinding.FragmentWorkoutBinding;

public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tutaj będzie logika:
        // 1. Pobierz dane treningu na dziś (z ViewModelu/Repozytorium)
        // 2. Użyj ViewPager2 + RecyclerView do wyświetlania kolejnych ćwiczeń
        // 3. Po wciśnięciu 'Zapisz' zaktualizuj bazę i przejdź do następnego ćwiczenia
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}