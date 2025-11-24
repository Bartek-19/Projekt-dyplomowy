package pl.pollub.android.powerstrongapp.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.data.repository.ReferenceRepository;
import pl.pollub.android.powerstrongapp.data.repository.UserRepository;
import pl.pollub.android.powerstrongapp.databinding.FragmentProfileBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        App app = (App) requireActivity().getApplication();

        // TWORZENIE REPOZYTORIÓW
        UserRepository userRepo = app.getUserRepository();
        // INICJALIZACJA VIEWMODELU Z DWOMA REPOZYTORIAMI
        viewModel = new ViewModelProvider(this, new ProfileViewModel.Factory(userRepo))
                .get(ProfileViewModel.class);

        setupUI();
        observeData();
    }

    private void setupUI() {
        binding.viewPagerProfile.setAdapter(new FragmentStateAdapter(this) {
            @NonNull @Override public Fragment createFragment(int position) {
                return position == 0 ? new RecordsFragment() : new PlanHistoryFragment();
            }
            @Override public int getItemCount() { return 2; }
        });

        new TabLayoutMediator(binding.tabLayoutProfile, binding.viewPagerProfile, (tab, position) -> {
            tab.setText(position == 0 ? "Rekordy" : "Historia");
        }).attach();

        binding.btnSettings.setOnClickListener(v -> showSettingsDialog());

        binding.btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Wylogować?")
                    .setPositiveButton("Tak", (d, w) -> {
                        viewModel.logout();
                        navigateToLogin();
                    })
                    .setNegativeButton("Nie", null)
                    .show();
        });

        binding.btnDeleteAccount.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Usunąć konto?")
                    .setMessage("Tej operacji nie można cofnąć.")
                    .setPositiveButton("Usuń na zawsze", (d, w) -> performDeleteAccount())
                    .setNegativeButton("Anuluj", null)
                    .show();
        });
    }

    private void observeData() {
        viewModel.getUserDetails().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvUsername.setText(user.getUsername());
            }
        });
    }

    private void showSettingsDialog() {
        // Opcje: 0=Jasny, 1=Ciemny, 2=Systemowy
        String[] themes = {"Jasny", "Ciemny", "Systemowy"};

        // Sprawdzamy obecny motyw, żeby zaznaczyć dobrą opcję
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        int checkedItem = -1;

        if (currentMode == AppCompatDelegate.MODE_NIGHT_NO) checkedItem = 0;
        else if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) checkedItem = 1;
        else checkedItem = 2;

        new AlertDialog.Builder(requireContext())
                .setTitle("Motyw Aplikacji")
                .setSingleChoiceItems(themes, checkedItem, (dialog, which) -> {
                    switch (which) {
                        case 0: // Jasny
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case 1: // Ciemny
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case 2: // Systemowy (zgodny z ustawieniami Androida)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                    }
                    // Zapisz wybór w SharedPreferences, jeśli chcesz pamiętać po restarcie!
                    // Na razie działa w obrębie sesji.
                    dialog.dismiss();
                })
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void performDeleteAccount() {
        viewModel.deleteAccount(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Konto usunięte.", Toast.LENGTH_SHORT).show();
                    viewModel.logout();
                    navigateToLogin();
                } else {
                    Toast.makeText(requireContext(), "Błąd: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Błąd sieci.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        try {
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_profile_to_loginFragment);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Wylogowano. Zrestartuj aplikację.", Toast.LENGTH_LONG).show();
        }
    }
}