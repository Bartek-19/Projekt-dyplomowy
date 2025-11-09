package pl.pollub.android.powerstrongapp;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import pl.pollub.android.powerstrongapp.api.model.ExecutedSetDto;
import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;
import pl.pollub.android.powerstrongapp.databinding.ItemExerciseBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExerciseExecutionFragment extends Fragment {

    private ItemExerciseBinding binding;

    private static final String ARG_EXERCISE_DATA = "exercise_data";

    // Listy do przechowywania referencji do pól wprowadzania danych, abyśmy mogli je odczytać.
    private final List<EditText> repInputFields = new ArrayList<>();
    private final List<EditText> weightInputFields = new ArrayList<>();


    // 1. Metoda statyczna do bezpiecznego tworzenia instancji z DTO (używana przez WorkoutPagerAdapter)
    public static ExerciseExecutionFragment newInstance(PlannedExerciseDto plannedExercise) {
        ExerciseExecutionFragment fragment = new ExerciseExecutionFragment();
        Bundle args = new Bundle();
        // Przekazujemy cały obiekt (musi implementować Serializable/Parcelable)
        args.putSerializable(ARG_EXERCISE_DATA, plannedExercise);
        fragment.setArguments(args);
        return fragment;
    }

    // 2. Fragment LifeCycle: Tworzenie Widoku
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ItemExerciseBinding jest generowany z item_exercise.xml
        binding = ItemExerciseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // 3. Fragment LifeCycle: Logika Widoku
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            PlannedExerciseDto plannedExercise = (PlannedExerciseDto) getArguments().getSerializable(ARG_EXERCISE_DATA);

            if (plannedExercise != null) {
                // Ustawienie statycznych danych
                binding.tvExerciseName.setText(plannedExercise.getExerciseName());
                binding.tvExerciseDescription.setText(plannedExercise.getExerciseDescription());

                // Dynamiczne tworzenie pól do wprowadzania danych
                createSetInputFields(plannedExercise);
            }
        }
    }

    /**
     * Dynamicznie tworzy wiersz dla każdej serii, używając danych z PlannedExerciseDto.
     */
    private void createSetInputFields(PlannedExerciseDto plannedExercise) {

        repInputFields.clear();
        weightInputFields.clear();
        binding.seriesContainer.removeAllViews(); // Czyścimy kontener

        // Tworzymy listę opisów serii
        List<String> setsInfo = plannedExercise.getSetsInfo();

        int setNumber = 1;

        for (String info : setsInfo) {
            // Tworzenie głównego wiersza (LinearLayout)
            LinearLayout setRow = new LinearLayout(requireContext());
            setRow.setOrientation(LinearLayout.HORIZONTAL);
            setRow.setPadding(0, 8, 0, 8);

            // --- 1. Numer serii i info planu ---
            TextView tvSetLabel = new TextView(requireContext());
            tvSetLabel.setText(String.format(Locale.getDefault(), "Seria %d: %s", setNumber, info));
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.45f);
            tvSetLabel.setLayoutParams(labelParams);
            tvSetLabel.setTextSize(16f);
            tvSetLabel.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            setRow.addView(tvSetLabel);

            // --- 2. Wprowadzanie Powtórzeń ---
            EditText etReps = new EditText(requireContext());
            etReps.setHint("Powt.");
            etReps.setInputType(InputType.TYPE_CLASS_NUMBER);
            etReps.setTextSize(14f);
            etReps.setEms(3);
            LinearLayout.LayoutParams repsParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f);
            repsParams.setMarginEnd(16);
            etReps.setLayoutParams(repsParams);
            setRow.addView(etReps);
            repInputFields.add(etReps);

            // --- 3. Wprowadzanie Ciężaru (w kg) ---
            EditText etWeight = new EditText(requireContext());
            etWeight.setHint("Kg");
            // Umożliwia wprowadzanie liczb dziesiętnych
            etWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etWeight.setTextSize(14f);
            etWeight.setEms(4);
            LinearLayout.LayoutParams weightParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.3f);
            etWeight.setLayoutParams(weightParams);
            setRow.addView(etWeight);
            weightInputFields.add(etWeight);

            // Dodaj wiersz do głównego kontenera serii
            binding.seriesContainer.addView(setRow);
            setNumber++;
        }
    }

    /**
     * PUBLICZNA METODA: Pobiera dane wprowadzone przez użytkownika i mapuje je na DTO do wysyłki na serwer.
     * @return Lista rekordów wykonanych serii (ExecutedSetDto)
     */
    public List<ExecutedSetDto> getExecutedSetDtos() {
        List<ExecutedSetDto> records = new ArrayList<>();

        PlannedExerciseDto plannedExercise = (PlannedExerciseDto) getArguments().getSerializable(ARG_EXERCISE_DATA);

        // Zabezpieczenie przed brakiem danych
        if (plannedExercise == null || plannedExercise.getId() == null) {
            return records;
        }

        Integer plannedExerciseId = plannedExercise.getId();

        for (int i = 0; i < repInputFields.size(); i++) {

            EditText repEt = repInputFields.get(i);
            EditText weightEt = weightInputFields.get(i);

            String repsText = repEt.getText().toString();
            String weightText = weightEt.getText().toString();

            try {
                // Parsowanie danych z pól tekstowych
                int actualReps = repsText.isEmpty() ? 0 : Integer.parseInt(repsText);
                // Zamiana przecinka na kropkę (dla Double.parseDouble) i parsowanie
                double actualWeight = weightText.isEmpty() ? 0.0 : Double.parseDouble(weightText.replace(',', '.'));

                // Tworzenie obiektu ExecutedSetDto gotowego do wysłania
                ExecutedSetDto dto = new ExecutedSetDto(
                        plannedExerciseId,
                        i + 1, // setNumber zaczyna się od 1
                        actualReps,
                        actualWeight
                );

                records.add(dto);

            } catch (NumberFormatException e) {
                // W przypadku błędu formatu, traktujemy serię jako niewypełnioną (można zmienić logikę)
                // W tym przypadku po prostu kontynuujemy, pomijając wadliwy rekord.
            }
        }

        return records;
    }


    // 4. Czyszczenie Bindingu
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}