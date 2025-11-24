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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import pl.pollub.android.powerstrongapp.api.model.ExecutedHistoryDto; // Używamy Twojego DTO

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

        // Główny kontener z przewijaniem
        ScrollView scrollView = new ScrollView(requireContext());
        LinearLayout container = new LinearLayout(requireContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(32, 32, 32, 32);
        scrollView.addView(container);

        // Tytuł okna
        TextView title = new TextView(requireContext());
        title.setText("Historia Treningów");
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 32);
        title.setTextColor(colorOnSurface);
        container.addView(title);

        if (historyData == null || historyData.isEmpty()) {
            // Stan pusty
            TextView empty = new TextView(requireContext());
            empty.setText("Brak historii dla tego planu.");
            empty.setGravity(Gravity.CENTER);
            empty.setTextColor(colorVariant);
            container.addView(empty);
        } else {
            // --- LOGIKA GRUPOWANIA ---

            // 1. Grupujemy całą listę po dacie (Jako String ISO: YYYY-MM-DD dla łatwego sortowania)
            Map<String, List<ExecutedHistoryDto>> groupedByDate = historyData.stream()
                    .collect(Collectors.groupingBy(item -> {
                        if (item.getExecutionTimestamp() == null) return "Nieznana data";
                        LocalDate date = Instant.ofEpochMilli(item.getExecutionTimestamp())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return date.toString();
                    }));

            // 2. Iterujemy po datach (posortowanych malejąco - najnowsze na górze)
            groupedByDate.entrySet().stream()
                    .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                    .forEach(entry -> {
                        String dateIsoStr = entry.getKey();
                        List<ExecutedHistoryDto> itemsInDay = entry.getValue();

                        // Formatowanie daty dla użytkownika (np. "poniedziałek, 24 listopada")
                        String displayDate = dateIsoStr;
                        try {
                            if (!dateIsoStr.equals("Nieznana data")) {
                                LocalDate ld = LocalDate.parse(dateIsoStr);
                                displayDate = ld.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault()));
                                // Pierwsza litera wielka
                                displayDate = displayDate.substring(0, 1).toUpperCase() + displayDate.substring(1);
                            }
                        } catch (Exception e) {}

                        // --- SEKCJA DATY ---

                        // Nagłówek Daty
                        TextView dateHeader = new TextView(requireContext());
                        dateHeader.setText(displayDate);
                        dateHeader.setTextSize(18);
                        dateHeader.setTextColor(colorPrimary);
                        dateHeader.setTypeface(null, Typeface.BOLD);
                        dateHeader.setPadding(0, 24, 0, 8);
                        container.addView(dateHeader);

                        // Linia oddzielająca
                        android.view.View line = new android.view.View(requireContext());
                        line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                        line.setBackgroundColor(colorOutline);
                        container.addView(line);

                        // 3. Wewnątrz daty grupujemy po nazwie ćwiczenia
                        Map<String, List<ExecutedHistoryDto>> groupedByExercise = itemsInDay.stream()
                                .collect(Collectors.groupingBy(item ->
                                        item.getExerciseName() != null ? item.getExerciseName() : "Nieznane ćwiczenie"));

                        groupedByExercise.forEach((exName, exItems) -> {
                            // Kontener dla pojedynczego ćwiczenia
                            LinearLayout exRow = new LinearLayout(requireContext());
                            exRow.setOrientation(LinearLayout.VERTICAL);
                            exRow.setPadding(16, 16, 0, 16);

                            // Nazwa ćwiczenia
                            TextView exTitle = new TextView(requireContext());
                            exTitle.setText("• " + exName);
                            exTitle.setTypeface(null, Typeface.BOLD);
                            exTitle.setTextSize(16);
                            exTitle.setTextColor(colorOnSurface);
                            exRow.addView(exTitle);

                            // Sortujemy serie po numerze (1, 2, 3...)
                            exItems.sort((s1, s2) -> Integer.compare(s1.getSetNumber(), s2.getSetNumber()));

                            // Budujemy tekst serii (np. "Seria 1: 5x100.0kg")
                            StringBuilder setsBuilder = new StringBuilder();
                            for (ExecutedHistoryDto s : exItems) {
                                setsBuilder.append("Seria ").append(s.getSetNumber())
                                        .append(":  ")
                                        .append(s.getExecutedReps()).append(" powt.  ×  ")
                                        .append(s.getWeightUsed()).append(" kg\n");
                            }

                            // Wyświetlamy serie
                            TextView setsText = new TextView(requireContext());
                            setsText.setText(setsBuilder.toString().trim());
                            setsText.setTextSize(14);
                            setsText.setTextColor(colorOnSurface);
                            setsText.setPadding(32, 4, 0, 0); // Wcięcie dla serii
                            exRow.addView(setsText);

                            container.addView(exRow);
                        });
                    });
        }

        builder.setView(scrollView);
        builder.setPositiveButton("Zamknij", null);

        Dialog d = builder.create();
        if(d.getWindow() != null) {
            d.getWindow().setBackgroundDrawable(new ColorDrawable(colorSurface));
        }
        return d;
    }
}