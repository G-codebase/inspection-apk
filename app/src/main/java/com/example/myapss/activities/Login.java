package com.example.myapss.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapss.models.LoginRequest;
import com.example.myapss.models.LoginResponse;
import com.example.myapss.network.LoginApi;
import com.example.myapss.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapss.R;

public class Login extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private ProgressBar passwordStrengthBar;
    private TextView passwordHintText;
    private Button loginButton;
    private TextView errorMessage;
    private ImageView logoImage;
    private LinearLayout loginCard;

    // Password validation requirements
    private boolean hasUpperCase = false;
    private boolean hasLowerCase = false;
    private boolean hasNumber = false;
    private boolean hasSpecialChar = false;
    private boolean hasMinLength = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initializeViews();
        setupPasswordValidation();
    }

    private void initializeViews() {
        logoImage = findViewById(R.id.logo_image);
        loginCard = findViewById(R.id.login_card);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        passwordStrengthBar = findViewById(R.id.password_strength_bar);
        passwordHintText = findViewById(R.id.password_hint_text);
        loginButton = findViewById(R.id.login_button);
        errorMessage = findViewById(R.id.error_message);

        loginButton.setOnClickListener(v -> handleLogin());
    }

    private void setupPasswordValidation() {
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                updatePasswordStrength(password);
                updatePasswordHints(password);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updatePasswordStrength(String password) {
        hasUpperCase = password.matches(".*[A-Z].*");
        hasLowerCase = password.matches(".*[a-z].*");
        hasNumber = password.matches(".*\\d.*");
        hasSpecialChar = password.matches(".*[@$!%*?&].*");
        hasMinLength = password.length() >= 8;

        int strength = 0;
        if (hasUpperCase) strength += 20;
        if (hasLowerCase) strength += 20;
        if (hasNumber) strength += 20;
        if (hasSpecialChar) strength += 20;
        if (hasMinLength) strength += 20;

        passwordStrengthBar.setProgress(strength);

        // Change color based on strength
        if (strength < 40) {
            passwordStrengthBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (strength < 80) {
            passwordStrengthBar.getProgressDrawable().setColorFilter(
                    Color.parseColor("#FFA500"), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            passwordStrengthBar.getProgressDrawable().setColorFilter(
                    Color.parseColor("#22C55E"), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private void updatePasswordHints(String password) {
        StringBuilder hints = new StringBuilder();
        if (!hasMinLength) hints.append("• 8+ characters\n");
        if (!hasUpperCase) hints.append("• Uppercase letter (A-Z)\n");
        if (!hasLowerCase) hints.append("• Lowercase letter (a-z)\n");
        if (!hasNumber) hints.append("• Number (0-9)\n");
        if (!hasSpecialChar) hints.append("• Special character (@$!%*?&)");

        if (hints.length() > 0) {
            passwordHintText.setText(hints.toString().trim());
            passwordHintText.setVisibility(View.VISIBLE);
        } else {
            passwordHintText.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        // Clear previous error
        errorMessage.setVisibility(View.GONE);

        // Validate email
        if (email.isEmpty()) {
            showError("Email is required");
            return;
        }

        if (!email.endsWith("@sjvgmail.com")) {
            showError("Access denied: Use your SJV Marine email (@sjvgmail.com)");
            return;
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@sjvgmail\\.com$")) {
            showError("Invalid email format");
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            showError("Password is required");
            return;
        }

        if (!hasUpperCase || !hasLowerCase || !hasNumber || !hasSpecialChar || !hasMinLength) {
            showError("Password does not meet requirements");
            return;
        }

        // Login successful
        loginButton.setText("Logging in...");
        loginButton.setEnabled(false);
//
//        // Simulate login delay
//        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
//            // Save login state to SharedPreferences
//            android.content.SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
//            prefs.edit().putString("user_email", email).putBoolean("is_logged_in", true).apply();
//
//            Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
//
//            // Navigate to Home
//            Intent intent = new Intent(Login.this, Main.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }, 1000);
//    }

        // ✅ CALL API
        LoginRequest request = new LoginRequest(email, password);

        LoginApi api = ApiClient.getInstance().create(LoginApi.class);

        Call<LoginResponse> call = api.login(request);

        call.enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   retrofit2.Response<LoginResponse> response) {

                loginButton.setEnabled(true);
                loginButton.setText("Login");

                if (response.isSuccessful() && response.body() != null &&
                        response.body().success) {

                    // ✅ Save login state
                    android.content.SharedPreferences prefs =
                            getSharedPreferences("user_prefs", MODE_PRIVATE);

                    prefs.edit()
                            .putBoolean("is_logged_in", true)
                            .putString("user_email", email)
                            .putLong("user_id", response.body().userId)
                            .apply();

                    Toast.makeText(Login.this,
                            "Login successful!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Login.this, Main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    showError("Invalid email or password");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginButton.setEnabled(true);
                loginButton.setText("Login");
                showError("Server error: " + t.getMessage());
            }
        });
    }
}