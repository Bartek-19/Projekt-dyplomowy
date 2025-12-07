package pl.pollub.android.powerstrongapp.utils;

import android.content.Context;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.PlannedExerciseDto;

public class SuggestionUtils {
    public static String getFormattedTarget(Context context, PlannedExerciseDto dto) {
        if (dto.getTargetWeight() != null && dto.getTargetWeight() > 0) {
            return context.getString(R.string.weight_kg, String.valueOf(dto.getTargetWeight()));
        }

        String type = dto.getSuggestionType();
        Double value = dto.getSuggestionValue();

        if (type == null || value == null) {
            return context.getString(R.string.sugg_unknown);
        }

        switch (type) {
            case "RPE":
                int rpe = value.intValue();
                int reserve = 10 - rpe;
                return context.getString(R.string.sugg_rpe, String.valueOf(value), reserve);

            case "BODYWEIGHT":
                int bwRpe = value.intValue();
                return context.getString(R.string.sugg_bodyweight, String.valueOf(bwRpe));

            case "PERCENT":
                int percent = (int) (value * 100);
                return context.getString(R.string.sugg_percent, percent);

            case "FIND_MAX":
                return context.getString(R.string.sugg_find_max, value.intValue());

            default:
                return context.getString(R.string.sugg_unknown);
        }
    }
}