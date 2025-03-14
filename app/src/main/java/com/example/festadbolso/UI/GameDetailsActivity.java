package com.example.festadbolso.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.festadbolso.R;
import com.example.festadbolso.Firestore.FirestoreHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameDetailsActivity extends AppCompatActivity {
    private TextView gameName, gameDescription, gameRules;
    private ImageView gameImage;
    private TextView maxPlayersText, setupTimeText, playTimeText;

    private Button playButton;

    private long randomId= -1; // Declare outside the if block

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        // Initialize views
        // Initialize views
        gameName = findViewById(R.id.game_name);
        gameDescription = findViewById(R.id.game_description);
        gameRules = findViewById(R.id.game_rules);
        gameImage = findViewById(R.id.game_image);

        // Find the TextViews inside the layouts for stats
        maxPlayersText = findViewById(R.id.max_players_text); // Use the unique ID
        setupTimeText = findViewById(R.id.setup_time_text); // Use the unique ID
        playTimeText = findViewById(R.id.play_time_text); // Use the unique ID

        Button playButton = findViewById(R.id.playButton);


        // Get the random game ID from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("randomId")) {
            randomId = intent.getLongExtra("randomId", -1); // Retrieve as a long
            if (randomId != -1) {
                String randomIdString = String.valueOf(randomId); // Convert to String if needed
                Log.d("GameDetails", "Received randomId: " + randomIdString);
                fetchAllGames(randomIdString); // Pass the randomId as a String
                // Check if the ID matches any of the specified values
                if (randomId == 1 || randomId == 2) {
                    playButton.setVisibility(View.GONE); // Don't show the button
                }
            } else {
                Toast.makeText(this, "Error: Invalid game ID", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: No game ID provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SpyActivity when the play button is clicked
                Intent intent = null;
                if (randomId == 4){
                    // Start the CharadesActivity when the play button is clicked
                    intent = new Intent(GameDetailsActivity.this, CharadesActivity.class);
                } else if (randomId == 6) {
                    intent = new Intent(GameDetailsActivity.this, TruthOrDareActivity.class);
                } else if (randomId == 7){
                    intent = new Intent(GameDetailsActivity.this, SpyActivity.class);
                } else {
                    Toast.makeText(GameDetailsActivity.this, "Funcionalidade ainda não foi implementada", Toast.LENGTH_SHORT).show();
                }
                // Ensure intent is not null before starting the activity
                if (intent != null) {
                    startActivity(intent);
                }

            }
        });


        //                if (randomId == 5) {
        //                    Toast.makeText(GameDetailsActivity.this, "The game ID is 5", Toast.LENGTH_SHORT).show();
        //                }
    }

    private void fetchAllGames(String randomId) {
        // Show loading indicator
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading game details...");
        progressDialog.show();

        // Fetch all games
        FirestoreHelper.getGames(this, new FirestoreHelper.GamesCallback() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                try {
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray documents = jsonResponse.getJSONArray("documents");

                    // Find the game with the matching ID
                    for (int i = 0; i < documents.length(); i++) {
                        JSONObject document = documents.getJSONObject(i);
                        JSONObject fields = document.getJSONObject("fields");

                        // Extract the game ID
                        if (fields.has("id")) {
                            JSONObject idObject = fields.getJSONObject("id");
                            String gameId = idObject.getString("integerValue");

                            // Check if the game ID matches the randomId
                            if (gameId.equals(randomId)) {
                                // Populate the UI with the game details
                                populateGameDetails(fields);
                                return; // Exit the loop once the game is found
                            }
                        }
                    }

                    // If no matching game is found
                    Toast.makeText(GameDetailsActivity.this, "Game not found", Toast.LENGTH_SHORT).show();
                    Log.d("GameDetails", "No matching documents found");
                } catch (JSONException e) {
                    Log.e("GameDetails", "Error parsing JSON: " + e.toString());
                    //Toast.makeText(GameDetailsActivity.this, "Error loading game", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Log.e("GameDetails", "Error fetching games: " + error);
                //Toast.makeText(GameDetailsActivity.this, "Error loading game", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateGameDetails(JSONObject fields) throws JSONException {
        // Extract and display game name
        if (fields.has("name")) {
            String name = fields.getJSONObject("name").getString("stringValue");
            gameName.setText(name);
        }

        // Extract and display description
        if (fields.has("description")) {
            String description = fields.getJSONObject("description").getString("stringValue");
            gameDescription.setText(description);
        }

        // Extract and display rules
        if (fields.has("rules")) {
            String rules = fields.getJSONObject("rules").getString("stringValue");
            rules = rules.replace("\\n", "\n"); // Convert Firestore's stored "\n" into actual new lines
            gameRules.setText(rules);
        }

        // Extract and display max players
        if (fields.has("maxPlayers")) {
            int maxPlayers = fields.getJSONObject("maxPlayers").getInt("integerValue");
            maxPlayersText.setText("Máximo jogadores: " +String.valueOf(maxPlayers));
        }

        // Extract and display setup time
        if (fields.has("setupTime")) {
            int setupTime = fields.getJSONObject("setupTime").getInt("integerValue");
            setupTimeText.setText("Preparação (mins): " + setupTime);
        }

        // Extract and display play time
        if (fields.has("playTime")) {
            int playTime = fields.getJSONObject("playTime").getInt("integerValue");
            playTimeText.setText("Duração (mins): " + playTime);
        }

        // Load the image if URL exists
        if (fields.has("imageUrl")) {
            String imageUrl = fields.getJSONObject("imageUrl").getString("stringValue");
            // Use an image loading library like Glide or Picasso
            Glide.with(GameDetailsActivity.this)
                    .load(imageUrl)
                    .placeholder(R.drawable.sample_game_image)
                    .error(R.drawable.sample_game_image)
                    .into(gameImage);
        }

        // Extract and display categories (array)
        if (fields.has("categories")) {
            JSONArray categories = fields.getJSONObject("categories").getJSONArray("arrayValue");
            Log.d("GameDetails", "Categories: " + categories.toString());
        }
    }
}