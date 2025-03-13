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
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseUIActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button authButton;

    private Button homeButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private boolean isRegister = false;

    // Add this new field
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui);

        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText); // Add this
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        authButton = findViewById(R.id.authButton);
        homeButton = findViewById(R.id.homeButton);
        progressBar = findViewById(R.id.progressBar);

        // Get intent extras to check if it's a registration or login
        if (getIntent().hasExtra("is_register")) {
            isRegister = getIntent().getBooleanExtra("is_register", false);
        }

        // Hide username field if it's a login
        usernameEditText.setVisibility(isRegister ? View.VISIBLE : View.GONE);

        authButton.setText(isRegister ? "Register" : "Login");

        authButton.setOnClickListener(v -> authenticateUser());

        homeButton.setOnClickListener(v -> goToMainActivity());
    }

    private void authenticateUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String username = isRegister ? usernameEditText.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty() || (isRegister && username.isEmpty())) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        authButton.setEnabled(false);

        if (isRegister) {
            // Register new user
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                // Set username in FirebaseUser profile
                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username)
                                                .build())
                                        .addOnCompleteListener(profileUpdateTask -> {
                                            progressBar.setVisibility(View.GONE);
                                            authButton.setEnabled(true);
                                            if (profileUpdateTask.isSuccessful()) {
                                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                                goToMainActivity();
                                            }
                                        });
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            authButton.setEnabled(true);
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
