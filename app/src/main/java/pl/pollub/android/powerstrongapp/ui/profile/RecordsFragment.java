package pl.pollub.android.powerstrongapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.UserExerciseMaxDto;
import pl.pollub.android.powerstrongapp.databinding.FragmentRecordsBinding; // Binding Fragmentu
import pl.pollub.android.powerstrongapp.databinding.ItemRecordBinding;    // Binding Wiersza

public class RecordsFragment extends Fragment {

    private FragmentRecordsBinding binding;
    private ArrayAdapter<UserExerciseMaxDto> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecordsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProfileViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(ProfileViewModel.class);

        adapter = new ArrayAdapter<UserExerciseMaxDto>(requireContext(), 0, new ArrayList<>()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ItemRecordBinding itemBinding;

                if (convertView == null) {
                    itemBinding = ItemRecordBinding.inflate(LayoutInflater.from(getContext()), parent, false);
                    convertView = itemBinding.getRoot();
                    convertView.setTag(itemBinding);
                } else {
                    itemBinding = (ItemRecordBinding) convertView.getTag();
                }

                UserExerciseMaxDto item = getItem(position);
                if (item != null) {
                    itemBinding.tvExerciseName.setText(item.getExerciseName());

                    Double max = item.getCurrentOneRepMax();
                    boolean hasRecord = max != null && max > 0;

                    if (hasRecord) {
                        if (item.isBodyweight()) {
                            itemBinding.tvRecordValue.setText(getString(R.string.max_reps, max.intValue()));
                        } else {
                            itemBinding.tvRecordValue.setText(getString(R.string.max_weight, String.valueOf(max)));
                        }
                    } else {
                        itemBinding.tvRecordValue.setText(getString(R.string.no_record));
                    }
                }
                return convertView;
            }
        };

        binding.listView.setAdapter(adapter);

        viewModel.getRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null) {
                adapter.clear();
                adapter.addAll(records);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}