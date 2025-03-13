package com.example.festadbolso.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.festadbolso.Firestore.FirestoreHelper;
import com.example.festadbolso.R;
import com.example.festadbolso.UI.FirebaseUIActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// For the image
import com.bumptech.glide.Glide;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

//For firestore interaction
//Just for reset password
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private Button loginButton;
    private Button registerButton;
    private Button randomGameButton;
    private Button logoutButton;
    private ImageView appLogoImageView; // Add this line if you're using the ImageView

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        welcomeTextView = findViewById(R.id.welcomeTextView);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        randomGameButton = findViewById(R.id.randomGameButton);
        logoutButton = findViewById(R.id.logoutButton);
        // Inside onCreate() after setContentView()
        ImageView appLogoImageView = findViewById(R.id.appLogoImageView);
        Glide.with(this)
                .load(R.drawable.app_logo) // or a URL
                .circleCrop()
                .into(appLogoImageView);

        // Set button click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFirebaseAuth(false);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFirebaseAuth(true);
            }
        });

        randomGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRandomGame();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in
        updateUI(mAuth.getCurrentUser());
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //User is signed in
            // Display username instead of email if available
            String displayName = user.getDisplayName();
            welcomeTextView.setText("Welcome, " + (displayName != null && !displayName.isEmpty() ? displayName : user.getEmail()));

            loginButton.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            //User is not signed in
            welcomeTextView.setText("Welcome to Festa d'Bolso");
            loginButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        }
    }


    private void goToFirebaseAuth(boolean isRegister) {
        Intent intent = new Intent(this, FirebaseUIActivity.class);
        // You could pass a flag to indicate register vs login if needed
        intent.putExtra("is_register", isRegister);
        startActivity(intent);
    }

    private void startRandomGame() {
        // For now, just show a toast message
        Toast.makeText(this, "Random game will be implemented soon!", Toast.LENGTH_SHORT).show();

        // In the future, you would start a new activity or fragment for the game
        // Intent intent = new Intent(this, RandomGameActivity.class);
        // startActivity(intent);
        FirestoreHelper.getHighestGameId(new FirestoreHelper.OnHighestIdRetrievedListener() {
            @Override
            public void onHighestIdRetrieved(long highestId) {
                // This callback will be triggered with the highest id from the Firestore collection
                // The highestId is of type 'long'
                Log.d("Firestore", "The highest ID is: " + highestId);
            }

            @Override
            public void onError(Exception e) {
                // Handle error if fetching the highest ID fails
                Log.e("Firestore", "Error retrieving highest ID", e);
            }
        });


        //Add Games
        FirestoreHelper.addGames(this);


        // Get games
        FirestoreHelper.getGames(this);

    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
        updateUI(null);
    }
}