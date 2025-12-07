package pl.pollub.android.powerstrongapp.ui.plan_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.databinding.ItemPlanBinding;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<TrainingPlanFullDto> plans = new ArrayList<>();
    private final OnPlanInteractionListener listener;

    public interface OnPlanInteractionListener {
        void onSelectPlan(TrainingPlanFullDto plan);
        void onShowDetails(TrainingPlanFullDto plan);
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
            // Użycie String resource
            binding.tvWeeksCount.setText(plan.getDurationOfCycle() + " " + itemView.getContext().getString(R.string.weeks_short));

            binding.btnSelectPlan.setOnClickListener(v -> listener.onSelectPlan(plan));
            binding.getRoot().setOnClickListener(v -> listener.onSelectPlan(plan));
            binding.getRoot().setOnLongClickListener(v -> {
                listener.onShowDetails(plan);
                return true;
            });
        }
    }
}