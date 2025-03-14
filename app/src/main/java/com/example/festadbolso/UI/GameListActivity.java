package com.example.festadbolso.UI;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.festadbolso.R;
import com.example.festadbolso.adapter.GameAdapter;
import com.example.festadbolso.model.Game;
import com.example.festadbolso.Firestore.FirestoreHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class GameListActivity extends AppCompatActivity {

    private RecyclerView gamesRecyclerView;
    private GameAdapter gameAdapter;
    private List<Game> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        // Initialize RecyclerView
        gamesRecyclerView = findViewById(R.id.gamesRecyclerView);
        gamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        gameAdapter = new GameAdapter(games);
        gamesRecyclerView.setAdapter(gameAdapter);

        // Fetch games from Firestore
        fetchAllGames();
    }

    private void fetchAllGames() {
        // Show loading indicator
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading games...");
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

                    // Extract game details
                    for (int i = 0; i < documents.length(); i++) {
                        JSONObject document = documents.getJSONObject(i);
                        JSONObject fields = document.getJSONObject("fields");

                        String id = fields.getJSONObject("id").getString("integerValue");
                        String name = fields.getJSONObject("name").getString("stringValue");
                        String description = fields.getJSONObject("description").getString("stringValue");
                        String imageUrl = fields.getJSONObject("imageUrl").getString("stringValue");

                        // Add game to the list
                        games.add(new Game(id, name, description, imageUrl));
                    }

                    // Notify adapter of data changes
                    gameAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                // Handle error
            }
        });
    }
}