package pl.pollub.android.powerstrongapp.ui.plan_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.databinding.ItemPlanBinding; // Upewnij się, że masz layout item_plan.xml

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<TrainingPlanFullDto> plans = new ArrayList<>();
    private final OnPlanInteractionListener listener;

    // Interfejs obsługujący oba zdarzenia
    public interface OnPlanInteractionListener {
        void onSelectPlan(TrainingPlanFullDto plan); // Kliknięcie
        void onShowDetails(TrainingPlanFullDto plan); // Przytrzymanie
    }

    public PlanAdapter(OnPlanInteractionListener listener) {
        this.listener = listener;
    }

    public void setPlans(List<TrainingPlanFullDto> plans) {
        this.plans = plans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlanBinding binding = ItemPlanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        TrainingPlanFullDto plan = plans.get(position);
        holder.bind(plan);
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    class PlanViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlanBinding binding;

        public PlanViewHolder(ItemPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TrainingPlanFullDto plan) {
            binding.tvPlanName.setText(plan.getName());
            binding.tvWeeksCount.setText(plan.getDurationOfCycle() + " tyg.");
            // binding.tvPlanDescription.setText(plan.getDescription()); // Jeśli masz opis w DTO

            // Kliknięcie przycisku "Wybierz"
            binding.btnSelectPlan.setOnClickListener(v -> listener.onSelectPlan(plan));

            // Kliknięcie w całą kartę -> Też wybór (lub szczegóły, zależy od preferencji)
            binding.getRoot().setOnClickListener(v -> listener.onSelectPlan(plan));

            // Dłuższe przytrzymanie -> Pokaż szczegóły
            binding.getRoot().setOnLongClickListener(v -> {
                listener.onShowDetails(plan);
                return true; // Zdarzenie skonsumowane
            });
        }
    }
}