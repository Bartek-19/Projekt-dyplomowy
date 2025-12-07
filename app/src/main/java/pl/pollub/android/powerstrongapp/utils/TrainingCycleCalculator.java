package pl.pollub.android.powerstrongapp.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import pl.pollub.android.powerstrongapp.data.local.entity.TrainingDayEntity;
import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;

public class TrainingCycleCalculator {

    public TrainingCycleCalculator() {
    }

    public Long calculateNextTrainingDate(TrainingPlanEntity plan, List<TrainingDayEntity> days, int completedSessions) {
        if (plan == null || plan.getStartDate() == null || days == null || days.isEmpty()) {
            return null;
        }

        try {
            List<TrainingDayEntity> sortedDays = new ArrayList<>(days);
            sortedDays.sort(Comparator.comparingInt(TrainingDayEntity::getDayOrder));

            LocalDate planStart = LocalDate.parse(plan.getStartDate());

            int occupiedDaysInCycle = 0;
            for (TrainingDayEntity d : sortedDays) {
                occupiedDaysInCycle += d.getDaysGap() + 1;
            }
            int fullCycleLength = (int) (Math.ceil(occupiedDaysInCycle / 7.0) * 7);
            if (fullCycleLength == 0) fullCycleLength = 7;

            int numberOfDaysInTemplate = sortedDays.size();
            int fullCyclesCompleted = completedSessions / numberOfDaysInTemplate;
            int remainderSessions = completedSessions % numberOfDaysInTemplate;

            LocalDate cursor = planStart.plusDays((long) fullCyclesCompleted * fullCycleLength);

            for (int i = 0; i < numberOfDaysInTemplate; i++) {
                TrainingDayEntity day = sortedDays.get(i);
                cursor = cursor.plusDays(day.getDaysGap());

                if (i == remainderSessions) {
                    return cursor.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }
                cursor = cursor.plusDays(1);
            }
            return cursor.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }
    public TrainingDayEntity determineNextDayEntity(TrainingPlanEntity plan, List<TrainingDayEntity> days, int completedSessions) {
        if (plan == null || days == null || days.isEmpty()) return null;

        List<TrainingDayEntity> sortedDays = new ArrayList<>(days);
        sortedDays.sort(Comparator.comparingInt(TrainingDayEntity::getWeekNumber).thenComparingInt(TrainingDayEntity::getDayOrder));

        int totalTemplateDays = sortedDays.size();
        int lastWeekInTemplate = sortedDays.get(sortedDays.size() - 1).getWeekNumber();
        if (lastWeekInTemplate == 0) lastWeekInTemplate = 1;

        int duration = plan.getDurationOfCycle();
        int loopMultiplier = 1;

        if (duration > lastWeekInTemplate) {
            loopMultiplier = duration / lastWeekInTemplate;
        }

        int maxTotalSessions = totalTemplateDays * loopMultiplier;

        if (duration > 0 && completedSessions >= maxTotalSessions) {
            return null;
        }

        int nextDayIndex = completedSessions % totalTemplateDays;
        return sortedDays.get(nextDayIndex);
    }
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

            int occupiedDaysInCycle = 0;
            for (TrainingDayEntity d : sortedDays) occupiedDaysInCycle += d.getDaysGap() + 1;

            int fullCycleLength = (int) (Math.ceil(occupiedDaysInCycle / 7.0) * 7);
            if (fullCycleLength == 0) fullCycleLength = 7;
            int loopGap = fullCycleLength - occupiedDaysInCycle;

            LocalDate cursorDate = planStart;
            int maxCycles = (plan.getDurationOfCycle() > 0) ? plan.getDurationOfCycle() + 2 : 52;

            for (int cycle = 0; cycle < maxCycles; cycle++) {
                for (TrainingDayEntity day : sortedDays) {
                    cursorDate = cursorDate.plusDays(day.getDaysGap());

                    if (planEndDate != null && !cursorDate.isBefore(planEndDate)) return dates;
                    if (cursorDate.isAfter(rangeEnd)) return dates;

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