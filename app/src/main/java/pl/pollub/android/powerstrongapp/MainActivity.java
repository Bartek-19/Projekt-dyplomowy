package pl.pollub.android.powerstrongapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import pl.pollub.android.powerstrongapp.data.local.entity.TrainingPlanEntity;
import pl.pollub.android.powerstrongapp.databinding.ActivityMainBinding;
import pl.pollub.android.powerstrongapp.utils.DataSynchronizer;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean hasActivePlan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Synchronizacja na start
        DataSynchronizer.syncAllData(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // 2. Setup podstawowy (dla Home i Profile)
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

            // 3. Nasłuchuj zmian w bazie (Czy jest plan?)
            observeActivePlan();

            // 4. Własna obsługa kliknięcia w menu
            binding.bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                // ŚRODKOWY GUZIK (ID w menu musi być nav_plans)
                if (itemId == R.id.nav_plans) {
                    if (hasActivePlan) {
                        // SCENARIUSZ A: JEST PLAN -> IDŹ DO PODGLĄDU
                        navController.navigate(R.id.nav_active_plan, null, getNavOptions());
                    } else {
                        // SCENARIUSZ B: BRAK PLANU -> IDŹ DO LISTY WYBORU
                        navController.navigate(R.id.nav_plans, null, getNavOptions());
                    }
                    return true;
                }

                // Reszta guzików (Home, Profile) - standardowo
                return NavigationUI.onNavDestinationSelected(item, navController);
            });

            // 5. Ukrywanie paska na ekranach logowania/treningu
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.loginFragment ||
                        destination.getId() == R.id.registerFragment ||
                        destination.getId() == R.id.workoutFragment) {
                    binding.bottomNavigation.setVisibility(View.GONE);
                } else {
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void observeActivePlan() {
        App app = (App) getApplication();
        app.getPlanRepository().getActiveTrainingPlan().observe(this, plan -> {
            updateBottomMenuState(plan);
        });
    }

    private void updateBottomMenuState(TrainingPlanEntity plan) {
        hasActivePlan = (plan != null);
        Menu menu = binding.bottomNavigation.getMenu();
        MenuItem item = menu.findItem(R.id.nav_plans);

        if (item != null) {
            if (hasActivePlan) {
                // Zmieniamy wygląd na "Mój Plan"
                item.setTitle("Mój Plan");
                item.setIcon(android.R.drawable.ic_menu_my_calendar); // Lub Twoja ikona R.drawable.ic_calendar
            } else {
                // Zmieniamy wygląd na "Rozpocznij"
                item.setTitle("Rozpocznij");
                item.setIcon(android.R.drawable.ic_input_add);
            }
        }
    }

    // Opcje nawigacji (żeby nie tworzyć stosu miliona fragmentów)
    private NavOptions getNavOptions() {
        return new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(R.id.nav_home, false)
                .build();
    }
}