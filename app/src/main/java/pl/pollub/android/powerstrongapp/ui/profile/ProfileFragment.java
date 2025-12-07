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
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;

import pl.pollub.android.powerstrongapp.App;
import pl.pollub.android.powerstrongapp.R;
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
        UserRepository userRepo = app.getUserRepository();
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
            tab.setText(position == 0 ? getString(R.string.tab_records) : getString(R.string.tab_history));
        }).attach();

        binding.btnSettings.setOnClickListener(v -> showSettingsDialog());

        binding.btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.logout_title)
                    .setPositiveButton(R.string.yes, (d, w) -> {
                        viewModel.logout();
                        navigateToLogin();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        binding.btnDeleteAccount.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.delete_account_title)
                    .setMessage(R.string.delete_account_message)
                    .setPositiveButton(R.string.delete_forever, (d, w) -> performDeleteAccount())
                    .setNegativeButton(R.string.cancel, null)
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
        String[] options = {
                getString(R.string.change_theme),
                getString(R.string.change_language)
        };

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.settings_dialog_title)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showThemeDialog();
                    } else {
                        showLanguageDialog();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showThemeDialog() {
        String[] themes = {
                getString(R.string.theme_light),
                getString(R.string.theme_dark),
                getString(R.string.theme_system)
        };

        int currentMode = AppCompatDelegate.getDefaultNightMode();
        int checkedItem;

        if (currentMode == AppCompatDelegate.MODE_NIGHT_NO) checkedItem = 0;
        else if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) checkedItem = 1;
        else checkedItem = 2;

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.app_theme_title)
                .setSingleChoiceItems(themes, checkedItem, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case 1:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case 2:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showLanguageDialog() {
        String[] languages = {
                getString(R.string.language_en),
                getString(R.string.language_pl)
        };

        String currentLang = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        int checkedItem = 0;

        if (currentLang.contains("pl")) {
            checkedItem = 1;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.change_language)
                .setSingleChoiceItems(languages, checkedItem, (dialog, which) -> {
                    if (which == 0) {
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"));
                    } else {
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("pl"));
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void performDeleteAccount() {
        viewModel.deleteAccount(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), R.string.account_deleted, Toast.LENGTH_SHORT).show();
                    viewModel.logout();
                    navigateToLogin();
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error_prefix) + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), R.string.error_network_short, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        try {
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_profile_to_loginFragment);
        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.logged_out_restart, Toast.LENGTH_LONG).show();
        }
    }
}