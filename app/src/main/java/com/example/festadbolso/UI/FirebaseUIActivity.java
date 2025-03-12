package com.example.festadbolso.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.festadbolso.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUIActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button authButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui);

        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        authButton = findViewById(R.id.authButton);
        progressBar = findViewById(R.id.progressBar);

        // Get intent extras to check if it's a registration or login
        if (getIntent().hasExtra("is_register")) {
            isRegister = getIntent().getBooleanExtra("is_register", false);
        }

        // Check if user is already signed in (if not registering)
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null && !isRegister) {
            goToMainActivity();
        }

        // Set button text based on mode
        authButton.setText(isRegister ? "Register" : "Login");

        // Set click listener for authentication button
        authButton.setOnClickListener(v -> authenticateUser());
    }

    private void authenticateUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar and disable button
        progressBar.setVisibility(View.VISIBLE);
        authButton.setEnabled(false);

        if (isRegister) {
            // Register new user
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        authButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            goToMainActivity();
                        } else {
                            Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Login existing user
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        authButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                            goToMainActivity();
                        } else {
                            Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
