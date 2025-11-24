package pl.pollub.android.powerstrongapp.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.RetrofitClient;
import pl.pollub.android.powerstrongapp.api.model.auth.AuthResponse;
import pl.pollub.android.powerstrongapp.api.model.auth.LoginRequest;
import pl.pollub.android.powerstrongapp.api.service.UserService;
import pl.pollub.android.powerstrongapp.utils.DataSynchronizer;
import pl.pollub.android.powerstrongapp.databinding.FragmentLoginBinding;
import pl.pollub.android.powerstrongapp.utils.AuthManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    // Używamy Bindingu, aby nie robić findViewById
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inicjalizacja Bindingu
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sprawdzenie, czy użytkownik jest już zalogowany
        if (AuthManager.getInstance(requireContext()).isUserLoggedIn()) {
            navigateToHome();
            return;
        }

        // Obsługa kliknięć przez binding
        binding.btnLogin.setOnClickListener(v -> attemptLogin());

        binding.btnRegister.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            // Upewnij się, że masz akcję w nav_graph.xml (id: action_loginFragment_to_registerFragment)
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    private void attemptLogin() {
        // Pobieranie tekstu z pól edycyjnych przez binding
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            // Używamy tekstu z strings.xml
            showToast(getString(R.string.error_empty_fields));
            return;
        }

        setLoading(true);

        UserService service = RetrofitClient.getUserService(requireContext());
        LoginRequest request = new LoginRequest(username, password);

        service.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!isAdded()) return;
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse body = response.body();
                    AuthManager.getInstance(requireContext())
                            .saveAuthData(body.getUserId(), body.getToken());

                    DataSynchronizer.syncAllData(requireContext());

                    showToast(getString(R.string.greeting_hello, body.getUsername()));
                    navigateToHome();
                } else {
                    if (response.code() == 401 || response.code() == 403) {
                        showToast(getString(R.string.error_invalid_credentials));
                    } else {
                        showToast(getString(R.string.error_server, response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                if (!isAdded()) return;
                setLoading(false);
                showToast(getString(R.string.error_network, t.getMessage()));
            }
        });
    }

    private void navigateToHome() {
        NavController navController = Navigation.findNavController(requireView());
        // Dodaj flagi w nav_graph (popBehavior), aby cofnięcie nie wracało do logowania!
        navController.navigate(R.id.action_loginFragment_to_homeFragment);
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!isLoading);
        binding.btnRegister.setEnabled(!isLoading);
        binding.etUsername.setEnabled(!isLoading);
        binding.etPassword.setEnabled(!isLoading);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Czyszczenie referencji do bindingu (ważne w Fragmentach)
        binding = null;
    }
}