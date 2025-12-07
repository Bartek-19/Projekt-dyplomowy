package pl.pollub.android.powerstrongapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;

public class PlanHistoryFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = new ListView(requireContext());
        return listView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProfileViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(ProfileViewModel.class);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        viewModel.getHistory().observe(getViewLifecycleOwner(), history -> {
            if (history != null) {
                adapter.clear();
                for (TrainingPlanFullDto plan : history) {
                    String status = plan.getStatus() != null ? plan.getStatus() : getString(R.string.status_finished);
                    adapter.add(plan.getName() + " (" + status + ")\n" + plan.getStartDate());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}