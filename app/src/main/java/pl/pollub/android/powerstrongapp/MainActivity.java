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

        DataSynchronizer.syncAllData(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
            observeActivePlan();

            binding.bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_plans) {
                    if (hasActivePlan) {
                        navController.navigate(R.id.nav_active_plan, null, getNavOptions());
                    } else {
                        navController.navigate(R.id.nav_plans, null, getNavOptions());
                    }
                    return true;
                }
                return NavigationUI.onNavDestinationSelected(item, navController);
            });

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
                item.setTitle(getString(R.string.menu_my_plan));
                item.setIcon(android.R.drawable.ic_menu_my_calendar);
            } else {
                item.setTitle(getString(R.string.menu_start));
                item.setIcon(android.R.drawable.ic_input_add);
            }
        }
    }

    private NavOptions getNavOptions() {
        return new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(R.id.nav_home, false)
                .build();
    }
}