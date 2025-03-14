package com.example.festadbolso.UI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.festadbolso.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JoinActivity extends Activity {

    private static final String TAG = "JoinActivity";
    private static final int PORT = 12345;

    private EditText ipAddressEditText;
    private Button joinButton;
    private TextView statusTextView;
    private TextView roleDisplayTextView;

    private ExecutorService executor;
    private Socket socket;
    private ObjectInputStream inputStream;

    private Player clientPlayer;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // Initialize UI components
        ipAddressEditText = findViewById(R.id.ipAddressEditText); // Add this to your layout
        joinButton = findViewById(R.id.joinButton);
        statusTextView = findViewById(R.id.statusTextView);
        roleDisplayTextView = findViewById(R.id.roleDisplayTextView);

        // Set default IP (common hotspot IP)
        ipAddressEditText.setText("192.168.43.1");

        // Initialize executor
        executor = Executors.newSingleThreadExecutor();

        // Set button click listener
        joinButton.setOnClickListener(v -> {
            String ipAddress = ipAddressEditText.getText().toString().trim();
            if (ipAddress.isEmpty()) {
                Toast.makeText(this, "Please enter host IP address", Toast.LENGTH_SHORT).show();
                return;
            }

            joinButton.setEnabled(false);
            statusTextView.setText("Connecting to host...");

            connectToHost(ipAddress);
        });
    }

    private void connectToHost(String hostIp) {
        executor.execute(() -> {
            try {
                // Connect to the host
                socket = new Socket(hostIp, PORT);
                inputStream = new ObjectInputStream(socket.getInputStream());
                isConnected = true;

                runOnUiThread(() -> {
                    statusTextView.setText("Connected to host. Waiting for game to start...");
                    joinButton.setVisibility(View.GONE);
                });

                // Start listening for messages from the host
                listenForMessages();

            } catch (IOException e) {
                Log.e(TAG, "Failed to connect to host", e);
                runOnUiThread(() -> {
                    statusTextView.setText("Connection failed: " + e.getMessage());
                    joinButton.setEnabled(true);
                });
            }
        });
    }

    private void listenForMessages() {
        while (isConnected) {
            try {
                Object receivedObject = inputStream.readObject();

                if (receivedObject instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) receivedObject;
                    String type = (String) data.get("type");

                    if (type.equals("message")) {
                        String content = (String) data.get("content");
                        runOnUiThread(() -> {
                            statusTextView.setText(content);
                        });
                    } else if (type.equals("gameData")) {
                        processGameData(data);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                Log.e(TAG, "Error receiving message", e);
                break;
            }
        }

        // Clean up if connection is lost
        disconnect();
    }

    private void processGameData(Map<String, Object> data) {
        String playerId = (String) data.get("playerId");
        List<Player> players = (List<Player>) data.get("players");

        // Find our player in the list
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                clientPlayer = player;
                break;
            }
        }

        // Update UI with player role information
        runOnUiThread(() -> {
            if (clientPlayer != null) {
                String roleInfo = "You are a " + clientPlayer.getRole();

                if (clientPlayer.getRole().equals("Agent")) {
                    roleInfo += " at " + clientPlayer.getLocation();
                }

                roleDisplayTextView.setText(roleInfo);
                statusTextView.setText("Game started!");
            }
        });
    }

    private void disconnect() {
        isConnected = false;

        // Close socket
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing socket", e);
        }

        // Update UI
        runOnUiThread(() -> {
            statusTextView.setText("Disconnected from host");
            joinButton.setVisibility(View.VISIBLE);
            joinButton.setEnabled(true);
            roleDisplayTextView.setText("");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
        executor.shutdown();
    }
}