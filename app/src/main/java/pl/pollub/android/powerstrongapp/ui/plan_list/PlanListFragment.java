package pl.pollub.android.powerstrongapp.ui.plan_list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Calendar;
import java.util.Locale;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.model.TrainingPlanFullDto;
import pl.pollub.android.powerstrongapp.databinding.FragmentPlanListBinding;
import pl.pollub.android.powerstrongapp.ui.plan_create.CreatePlanWizardDialogFragment;

public class PlanListFragment extends Fragment implements PlanAdapter.OnPlanInteractionListener {

    private FragmentPlanListBinding binding;
    private PlanListViewModel viewModel;
    private PlanAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlanListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App app = App.getInstance();
        PlanListViewModel.Factory factory = new PlanListViewModel.Factory(
                app.getPlanRepository());
        viewModel = new ViewModelProvider(this, factory).get(PlanListViewModel.class);

        setupRecyclerView();
        setupObservers();

        // Nawigacja do kreatora
        binding.fabCreatePlan.setOnClickListener(v -> {
            // Tworzymy instancję naszego nowego DialogFragmentu
            CreatePlanWizardDialogFragment wizard = new CreatePlanWizardDialogFragment();

            // Wyświetlamy go na pełnym ekranie (jako nakładkę)
            // "CreatePlanWizard" to tag, po którym system identyfikuje to okno
            wizard.show(getParentFragmentManager(), "CreatePlanWizard");
        });
    }

    private void setupRecyclerView() {
        adapter = new PlanAdapter(this);
        binding.rvPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPlans.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getPlans().observe(getViewLifecycleOwner(), plans -> {
            if (plans != null) adapter.setPlans(plans);
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading ->
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );

        // Sukces aktywacji -> Wróć do Home
        viewModel.isPlanActivated().observe(getViewLifecycleOwner(), activated -> {
            if (activated != null && activated) {
                Toast.makeText(requireContext(), "Plan przypisany pomyślnie!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigate(R.id.action_nav_plans_to_nav_home);
            }
        });

        // Błędy
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
        });
    }

    // --- INTERAKCJE Z ADAPTERA ---

    @Override
    public void onSelectPlan(TrainingPlanFullDto plan) {
        // 1. Użytkownik klika "Wybierz" -> Pokaż DatePicker
        showStartDatePicker(plan);
    }

    @Override
    public void onShowDetails(TrainingPlanFullDto plan) {
        // 2. Użytkownik przytrzymuje -> Pokaż BottomSheet
        PlanDetailsDialogFragment dialog = PlanDetailsDialogFragment.newInstance(plan);
        dialog.show(getParentFragmentManager(), "PlanDetails");
    }

    private void showStartDatePicker(TrainingPlanFullDto plan) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Format daty YYYY-MM-DD (wymagany przez backend)
                    String selectedDate = String.format(Locale.US, "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);

                    // Wywołanie ViewModelu
                    viewModel.activatePlan(plan.getId(), selectedDate);
                },
                year, month, day);

        datePickerDialog.setMessage("Kiedy chcesz rozpocząć treningi?");
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}