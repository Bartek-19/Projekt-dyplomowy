package pl.pollub.android.powerstrongapp.utils;

import android.content.Context;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;

public class SuggestionUtils {

    /**
     * Metoda zwraca sformatowany tekst ciężaru lub sugestii.
     */
    public static String getFormattedTarget(Context context, PlannedExerciseDto dto) {
        // 1. Jeśli serwer przysłał konkretny ciężar -> Wyświetlamy go
        if (dto.getTargetWeight() != null && dto.getTargetWeight() > 0) {
            return context.getString(R.string.weight_kg, String.valueOf(dto.getTargetWeight()));
        }

        // 2. Jeśli targetWeight jest null/0, sprawdzamy typ sugestii
        String type = dto.getSuggestionType();
        Double value = dto.getSuggestionValue();

        if (type == null || value == null) {
            return context.getString(R.string.sugg_unknown);
        }

        switch (type) {
            case "RPE":
                // value = 8.0 -> Zapas = 2
                int rpe = value.intValue();
                int reserve = 10 - rpe;
                return context.getString(R.string.sugg_rpe, String.valueOf(value), reserve);

            case "BODYWEIGHT":
                // To samo co RPE, ale inny tekst
                int bwRpe = value.intValue();
                return context.getString(R.string.sugg_bodyweight, String.valueOf(bwRpe));

            case "PERCENT":
                // 0.75 -> 75%
                int percent = (int) (value * 100);
                return context.getString(R.string.sugg_percent, percent);

            case "FIND_MAX":
                // value = liczba powtórzeń
                return context.getString(R.string.sugg_find_max, value.intValue());

            default:
                return context.getString(R.string.sugg_unknown);
        }
    }
}