package pl.pollub.android.powerstrongapp.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import pl.pollub.android.powerstrongapp.R;
import pl.pollub.android.powerstrongapp.api.RetrofitClient;
import pl.pollub.android.powerstrongapp.api.model.auth.RegisterRequest;
import pl.pollub.android.powerstrongapp.api.model.UserDto;
import pl.pollub.android.powerstrongapp.api.service.UserService;
import pl.pollub.android.powerstrongapp.databinding.FragmentRegisterBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCreateAccount.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String username = binding.etRegUsername.getText().toString().trim();
        String email = binding.etRegEmail.getText().toString().trim();
        String password = binding.etRegPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        // Tworzymy obiekt żądania rejestracji
        RegisterRequest request = new RegisterRequest(username, email, password);
        UserService service = RetrofitClient.getUserService(requireContext());

        // Wysyłamy do API
        service.register(request).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (!isAdded()) return;
                setLoading(false);

                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), getString(R.string.register_success), Toast.LENGTH_LONG).show();
                    // Wracamy do ekranu logowania
                    Navigation.findNavController(requireView()).navigateUp();
                } else {
                    Toast.makeText(requireContext(), getString(R.string.register_failed, response.code()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                if (!isAdded()) return;
                setLoading(false);
                Toast.makeText(requireContext(), getString(R.string.error_network, t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.progressBarReg.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnCreateAccount.setEnabled(!isLoading);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}