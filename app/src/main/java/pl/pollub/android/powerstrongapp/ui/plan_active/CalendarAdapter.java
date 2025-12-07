package pl.pollub.android.powerstrongapp.ui.plan_active;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;
import pl.pollub.android.powerstrongapp.R;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    private final List<Long> daysInMonth;
    private final List<Long> trainingDates;
    private final List<Long> completedDates;
    private final OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(long dateMillis);
    }
    public CalendarAdapter(List<Long> daysInMonth,
                           List<Long> trainingDates,
                           List<Long> completedDates,
                           OnDateClickListener listener) {
        this.daysInMonth = daysInMonth;
        this.trainingDates = trainingDates;
        this.completedDates = completedDates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Long date = daysInMonth.get(position);

        if (date == null) {
            holder.tvDay.setText("");
            holder.tvDay.setBackgroundColor(Color.TRANSPARENT);
            holder.itemView.setOnClickListener(null);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        holder.tvDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

        boolean isTraining = checkDateInList(cal, trainingDates);
        boolean isCompleted = checkDateInList(cal, completedDates);

        if (isCompleted) {
            holder.tvDay.setBackgroundResource(R.drawable.bg_day_active);
            holder.tvDay.getBackground().setTint(0xFF4CAF50);
            holder.tvDay.setTextColor(Color.WHITE);
            holder.itemView.setOnClickListener(v -> listener.onDateClick(date));
        }
        else if (isTraining) {
            holder.tvDay.setBackgroundResource(R.drawable.bg_day_active);
            holder.tvDay.getBackground().setTintList(null);
            holder.tvDay.setTextColor(Color.WHITE);
            holder.itemView.setOnClickListener(v -> listener.onDateClick(date));
        }
        else {
            boolean isToday = isSameDay(cal, System.currentTimeMillis());
            if (isToday) {
                holder.tvDay.setBackgroundResource(R.drawable.bg_day_inactive);
            } else {
                holder.tvDay.setBackgroundColor(Color.TRANSPARENT);
            }
            holder.tvDay.setTextColor(Color.BLACK);
            holder.itemView.setOnClickListener(null);
        }
    }

    private boolean checkDateInList(Calendar cal, List<Long> dates) {
        if (dates == null) return false;
        for (Long tDate : dates) {
            if (isSameDay(cal, tDate)) return true;
        }
        return false;
    }

    private boolean isSameDay(Calendar cal1, Long time2) {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(time2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    @Override public int getItemCount() { return daysInMonth.size(); }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
        }
    }
}