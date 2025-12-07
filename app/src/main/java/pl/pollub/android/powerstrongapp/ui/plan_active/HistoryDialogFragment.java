package pl.pollub.android.powerstrongapp.ui.plan_active;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.color.MaterialColors;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.ExecutedHistoryDto;

public class HistoryDialogFragment extends DialogFragment {

    private final List<ExecutedHistoryDto> historyData;

    public HistoryDialogFragment(List<ExecutedHistoryDto> historyData) {
        this.historyData = historyData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int colorPrimary = MaterialColors.getColor(requireContext(), androidx.appcompat.R.attr.colorPrimary, Color.BLUE);
        int colorOnSurface = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurface, Color.BLACK);
        int colorSurface = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorSurface, Color.WHITE);
        int colorOutline = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOutline, Color.LTGRAY);
        int colorVariant = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant, Color.GRAY);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        ScrollView scrollView = new ScrollView(requireContext());
        LinearLayout container = new LinearLayout(requireContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(32, 32, 32, 32);
        scrollView.addView(container);

        TextView title = new TextView(requireContext());
        title.setText(getString(R.string.history_title));
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 32);
        title.setTextColor(colorOnSurface);
        container.addView(title);

        if (historyData == null || historyData.isEmpty()) {
            TextView empty = new TextView(requireContext());
            empty.setText(getString(R.string.history_empty));
            empty.setGravity(Gravity.CENTER);
            empty.setTextColor(colorVariant);
            container.addView(empty);
        } else {
            Map<String, List<ExecutedHistoryDto>> groupedByDate = historyData.stream()
                    .collect(Collectors.groupingBy(item -> {
                        if (item.getExecutionTimestamp() == null) return getString(R.string.unknown_date);
                        LocalDate date = Instant.ofEpochMilli(item.getExecutionTimestamp())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return date.toString();
                    }));

            groupedByDate.entrySet().stream()
                    .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                    .forEach(entry -> {
                        String dateIsoStr = entry.getKey();
                        List<ExecutedHistoryDto> itemsInDay = entry.getValue();

                        String displayDate = dateIsoStr;
                        try {
                            if (!dateIsoStr.equals(getString(R.string.unknown_date))) {
                                LocalDate ld = LocalDate.parse(dateIsoStr);
                                displayDate = ld.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault()));
                                displayDate = displayDate.substring(0, 1).toUpperCase() + displayDate.substring(1);
                            }
                        } catch (Exception e) {}

                        TextView dateHeader = new TextView(requireContext());
                        dateHeader.setText(displayDate);
                        dateHeader.setTextSize(18);
                        dateHeader.setTextColor(colorPrimary);
                        dateHeader.setTypeface(null, Typeface.BOLD);
                        dateHeader.setPadding(0, 24, 0, 8);
                        container.addView(dateHeader);

                        android.view.View line = new android.view.View(requireContext());
                        line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                        line.setBackgroundColor(colorOutline);
                        container.addView(line);

                        Map<String, List<ExecutedHistoryDto>> groupedByExercise = itemsInDay.stream()
                                .collect(Collectors.groupingBy(item ->
                                        item.getExerciseName() != null ? item.getExerciseName() : getString(R.string.unknown_exercise)));

                        groupedByExercise.forEach((exName, exItems) -> {
                            LinearLayout exRow = new LinearLayout(requireContext());
                            exRow.setOrientation(LinearLayout.VERTICAL);
                            exRow.setPadding(16, 16, 0, 16);

                            TextView exTitle = new TextView(requireContext());
                            exTitle.setText("• " + exName);
                            exTitle.setTypeface(null, Typeface.BOLD);
                            exTitle.setTextSize(16);
                            exTitle.setTextColor(colorOnSurface);
                            exRow.addView(exTitle);

                            exItems.sort(Comparator.comparingInt(ExecutedHistoryDto::getSetNumber));

                            StringBuilder setsBuilder = new StringBuilder();
                            for (ExecutedHistoryDto s : exItems) {
                                setsBuilder.append(getString(R.string.set_format,
                                        s.getSetNumber(), s.getExecutedReps(), String.valueOf(s.getWeightUsed())));
                            }

                            TextView setsText = new TextView(requireContext());
                            setsText.setText(setsBuilder.toString().trim());
                            setsText.setTextSize(14);
                            setsText.setTextColor(colorOnSurface);
                            setsText.setPadding(32, 4, 0, 0);
                            exRow.addView(setsText);

                            container.addView(exRow);
                        });
                    });
        }

        builder.setView(scrollView);
        builder.setPositiveButton(R.string.close, null);

        Dialog d = builder.create();
        if(d.getWindow() != null) {
            d.getWindow().setBackgroundDrawable(new ColorDrawable(colorSurface));
        }
        return d;
    }
}