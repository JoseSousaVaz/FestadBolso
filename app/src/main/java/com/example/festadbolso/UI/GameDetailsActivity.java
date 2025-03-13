package com.example.festadbolso.UI;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.festadbolso.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class GameDetailsActivity extends AppCompatActivity {

    private TextView gameName, gameDescription, gameRules;
    private ImageView gameImage;
    private LinearLayout maxPlayersLayout, setupTimeLayout, playTimeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        // Initialize Views
        gameName = findViewById(R.id.game_name);
        gameImage = findViewById(R.id.game_image);
        gameDescription = findViewById(R.id.game_description);
        gameRules = findViewById(R.id.game_rules);
        maxPlayersLayout = findViewById(R.id.max_players_layout);
        setupTimeLayout = findViewById(R.id.setup_time_layout);
        playTimeLayout = findViewById(R.id.play_time_layout);

        // Get the passed random game ID
        long randomId = getIntent().getLongExtra("randomId", -1);
        if (randomId != -1) {
            fetchGameDetails(randomId); // Fetch the game details based on randomId
        } else {
            Log.e("GameDetails", "Random ID not passed correctly.");
        }
    }

    private void fetchGameDetails(long gameId) {
        // Assume you have a method to fetch game details from Firestore using the ID
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("games")
                .document(String.valueOf(gameId))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get game details from the document
                        String name = documentSnapshot.getString("name");
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        String description = documentSnapshot.getString("description");
                        String rules = documentSnapshot.getString("rules");
                        long maxPlayers = documentSnapshot.getLong("maxPlayers");
                        long setupTime = documentSnapshot.getLong("setupTime");
                        long playTime = documentSnapshot.getLong("playTime");

                        // Set data in the layout
                        gameName.setText(name);
                        // Use Glide or Picasso to load the image (for better handling of images)
                        Glide.with(this).load(imageUrl).into(gameImage);
                        gameDescription.setText(description);
                        gameRules.setText(rules);

                        // Set stats (max players, setup time, play time) - you can add dynamic values to the circular icons
                        setStats(maxPlayers, setupTime, playTime);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("GameDetails", "Failed to fetch game details", e);
                });
    }

    private void setStats(long maxPlayers, long setupTime, long playTime) {
        // Set values dynamically for stats (you can use TextViews or any other views for these)
        // Example: maxPlayersLayout.setText(String.valueOf(maxPlayers));
        // You'll have to dynamically change the content of your UI (text, images, etc.)
    }
}

