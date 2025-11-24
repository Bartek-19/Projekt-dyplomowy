package pl.pollub.android.powerstrongapp.ui.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;

import pl.pollub.android.powerstrongapp.api.model.UserExerciseMaxDto;

public class RecordsFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter<UserExerciseMaxDto> adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = new ListView(requireContext());
        return listView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProfileViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(ProfileViewModel.class);

        int colorOnSurface = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurface, Color.BLACK);
        int colorVariant = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant, Color.GRAY);

        adapter = new ArrayAdapter<UserExerciseMaxDto>(requireContext(), android.R.layout.simple_list_item_2, android.R.id.text1, new ArrayList<>()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView text1 = v.findViewById(android.R.id.text1);
                TextView text2 = v.findViewById(android.R.id.text2);

                text1.setTextColor(colorOnSurface);

                UserExerciseMaxDto item = getItem(position);
                if (item != null) {
                    text1.setText(item.getExerciseName());

                    Double max = item.getCurrentOneRepMax();
                    boolean hasRecord = max != null && max > 0;

                    if (hasRecord) {
                        // Teraz pobieramy flagę bezpośrednio z DTO
                        if (item.isBodyweight()) {
                            text2.setText("Max: " + max.intValue() + " powt.");
                        } else {
                            text2.setText("Max: " + max + " kg");
                        }
                        text2.setTextColor(colorOnSurface);
                    } else {
                        text2.setText("Brak rekordu");
                        text2.setTextColor(colorVariant);
                    }
                }
                return v;
            }
        };

        listView.setAdapter(adapter);

        viewModel.getRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null) {
                adapter.clear();
                adapter.addAll(records);
                adapter.notifyDataSetChanged();
            }
        });
    }
}