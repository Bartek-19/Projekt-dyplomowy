package pl.pollub.android.powerstrongapp.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;

/**
 * Klasa odpowiedzialna wyłącznie za logikę kalendarzową i cykliczną planu.
 * Nie pobiera danych z bazy - otrzymuje je jako argumenty.
 */
public class TrainingCycleCalculator {

    public TrainingCycleCalculator() {
        // Pusty konstruktor, klasa narzędziowa (ale nie statyczna, żeby można ją wstrzykiwać/mockować)
    }

    /**
     * Oblicza datę następnego treningu na podstawie liczby już wykonanych sesji.
     */
    public Long calculateNextTrainingDate(TrainingPlanEntity plan, List<TrainingDayEntity> days, int completedSessions) {
        if (plan == null || plan.getStartDate() == null || days == null || days.isEmpty()) {
            return null;
        }

        try {
            // 1. Sortowanie dni (bezpieczeństwo)
            List<TrainingDayEntity> sortedDays = new ArrayList<>(days);
            sortedDays.sort(Comparator.comparingInt(TrainingDayEntity::getDayOrder));

            LocalDate planStart = LocalDate.parse(plan.getStartDate());

            // 2. Obliczenie długości jednego pełnego cyklu (mikrocyklu) w dniach kalendarzowych
            int occupiedDaysInCycle = 0;
            for (TrainingDayEntity d : sortedDays) {
                occupiedDaysInCycle += d.getDaysGap() + 1; // +1 to dzień treningowy
            }
            // Zaokrąglamy do pełnych tygodni (opcjonalne, zależnie od logiki biznesowej, tu zachowuję Twoją logikę)
            int fullCycleLength = (int) (Math.ceil(occupiedDaysInCycle / 7.0) * 7);
            if (fullCycleLength == 0) fullCycleLength = 7;

            // 3. Przeskakujemy o liczbę pełnych cykli, które już wykonaliśmy
            int numberOfDaysInTemplate = sortedDays.size();
            int fullCyclesCompleted = completedSessions / numberOfDaysInTemplate;
            int remainderSessions = completedSessions % numberOfDaysInTemplate;

            LocalDate cursor = planStart.plusDays((long) fullCyclesCompleted * fullCycleLength);

            // 4. "Dohodzimy" do konkretnego dnia w bieżącym cyklu
            for (int i = 0; i < numberOfDaysInTemplate; i++) {
                TrainingDayEntity day = sortedDays.get(i);
                cursor = cursor.plusDays(day.getDaysGap());

                if (i == remainderSessions) {
                    return cursor.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }
                cursor = cursor.plusDays(1); // Przesuwamy kursor o sam dzień treningowy
            }

            // Fallback
            return cursor.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }

    /**
     * Zwraca encję dnia, który powinien być wykonany jako następny.
     */
    public TrainingDayEntity determineNextDayEntity(TrainingPlanEntity plan, List<TrainingDayEntity> days, int completedSessions) {
        if (plan == null || days == null || days.isEmpty()) return null;

        // Sortowanie
        List<TrainingDayEntity> sortedDays = new ArrayList<>(days);
        sortedDays.sort((d1, d2) -> {
            int w = Integer.compare(d1.getWeekNumber(), d2.getWeekNumber());
            if (w != 0) return w;
            return Integer.compare(d1.getDayOrder(), d2.getDayOrder());
        });

        // Sprawdzenie czy plan się nie skończył
        int totalTemplateDays = sortedDays.size();
        int lastWeekInTemplate = sortedDays.get(sortedDays.size() - 1).getWeekNumber();
        if (lastWeekInTemplate == 0) lastWeekInTemplate = 1;

        int duration = plan.getDurationOfCycle();
        int loopMultiplier = 1;

        // Logika powtarzania szablonu jeśli plan trwa dłużej niż szablon
        if (duration > lastWeekInTemplate) {
            loopMultiplier = duration / lastWeekInTemplate;
        }

        int maxTotalSessions = totalTemplateDays * loopMultiplier;

        // Jeśli wykonaliśmy już wszystkie zaplanowane sesje
        if (duration > 0 && completedSessions >= maxTotalSessions) {
            return null; // Koniec planu
        }

        // Wybieramy odpowiedni dzień z szablonu
        int nextDayIndex = completedSessions % totalTemplateDays;
        return sortedDays.get(nextDayIndex);
    }

    /**
     * Znajduje dni treningowe w zadanym przedziale czasu (dla kalendarza).
     */
    public List<Long> getTrainingDatesInRange(TrainingPlanEntity plan, List<TrainingDayEntity> days, long startMillis, long endMillis) {
        List<Long> dates = new ArrayList<>();
        if (plan == null || plan.getStartDate() == null || days == null || days.isEmpty()) return dates;

        try {
            LocalDate rangeStart = Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate rangeEnd = Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate planStart = LocalDate.parse(plan.getStartDate());

            LocalDate planEndDate = null;
            if (plan.getDurationOfCycle() > 0) {
                planEndDate = planStart.plusWeeks(plan.getDurationOfCycle());
            }

            List<TrainingDayEntity> sortedDays = new ArrayList<>(days);
            sortedDays.sort(Comparator.comparingInt(TrainingDayEntity::getDayOrder));

            // Logika cyklu (identyczna jak wyżej - DRY: Don't Repeat Yourself)
            int occupiedDaysInCycle = 0;
            for (TrainingDayEntity d : sortedDays) occupiedDaysInCycle += d.getDaysGap() + 1;

            int fullCycleLength = (int) (Math.ceil(occupiedDaysInCycle / 7.0) * 7);
            if (fullCycleLength == 0) fullCycleLength = 7;
            int loopGap = fullCycleLength - occupiedDaysInCycle;

            LocalDate cursorDate = planStart;
            int maxCycles = (plan.getDurationOfCycle() > 0) ? plan.getDurationOfCycle() + 2 : 52; // Zapas +2

            for (int cycle = 0; cycle < maxCycles; cycle++) {
                for (TrainingDayEntity day : sortedDays) {
                    cursorDate = cursorDate.plusDays(day.getDaysGap());

                    // Sprawdzamy warunki graniczne
                    if (planEndDate != null && !cursorDate.isBefore(planEndDate)) return dates; // Koniec planu
                    if (cursorDate.isAfter(rangeEnd)) return dates; // Wyszliśmy poza zakres kalendarza

                    // Jeśli data mieści się w zakresie (i nie jest przed startem zakresu)
                    if (!cursorDate.isBefore(rangeStart)) {
                        dates.add(cursorDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    }

                    cursorDate = cursorDate.plusDays(1);
                }
                cursorDate = cursorDate.plusDays(loopGap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }

    /**
     * Znajduje TrainingDayEntity dla konkretnej daty klikniętej w kalendarzu.
     */
    public TrainingDayEntity findDayForDate(TrainingPlanEntity plan, List<TrainingDayEntity> days, long dateMillis) {
        if (plan == null || plan.getStartDate() == null || days == null || days.isEmpty()) return null;
        try {
            LocalDate target = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate cursor = LocalDate.parse(plan.getStartDate());

            List<TrainingDayEntity> sortedDays = new ArrayList<>(days);
            sortedDays.sort(Comparator.comparingInt(TrainingDayEntity::getDayOrder));

            int occupied = 0;
            for(TrainingDayEntity d : sortedDays) occupied += d.getDaysGap() + 1;
            int fullCycle = (int) (Math.ceil(occupied/7.0)*7);
            if(fullCycle==0) fullCycle=7;
            int loopGap = fullCycle - occupied;

            int maxCycles = (plan.getDurationOfCycle() > 0) ? plan.getDurationOfCycle() + 1 : 52;

            for(int cycle=0; cycle < maxCycles; cycle++) {
                for(TrainingDayEntity day : sortedDays) {
                    cursor = cursor.plusDays(day.getDaysGap());
                    if(cursor.isEqual(target)) return day;
                    cursor = cursor.plusDays(1);
                }
                cursor = cursor.plusDays(loopGap);
                if(cursor.isAfter(target)) return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}