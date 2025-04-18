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

public class JoinMrWhiteActivity extends Activity {

    private static final String TAG = "JoinMrWhiteActivity";
    private static final int PORT = 12346;

    private EditText ipAddressEditText;
    private Button joinButton;
    private TextView statusTextView;
    private TextView roleDisplayTextView;

    private ExecutorService executor;
    private Socket socket;
    private ObjectInputStream inputStream;

    private MrWhitePlayer clientPlayer;
    private boolean isConnected = false;

    private Button toggleRoleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_white_join);

        // Initialize UI components
        ipAddressEditText = findViewById(R.id.ipAddressEditText);
        joinButton = findViewById(R.id.joinButton);
        statusTextView = findViewById(R.id.statusTextView);
        roleDisplayTextView = findViewById(R.id.roleDisplayTextView);
        toggleRoleButton = findViewById(R.id.toggleRoleButton);

        // Set default IP (common hotspot IP)
        ipAddressEditText.setText("192.168.43.1");

        // Initialize executor
        executor = Executors.newSingleThreadExecutor();

        // Set button click listener
        joinButton.setOnClickListener(v -> {
            String ipAddress = ipAddressEditText.getText().toString().trim();
            if (ipAddress.isEmpty()) {
                Toast.makeText(this, "Por favor insira o IP do anfitrião", Toast.LENGTH_SHORT).show();
                return;
            }

            joinButton.setEnabled(false);
            statusTextView.setText("A ligar ao anfitrião...");

            connectToHost(ipAddress);
        });

        toggleRoleButton.setOnClickListener(v -> {
            if (roleDisplayTextView.getVisibility() == View.GONE) {
                roleDisplayTextView.setVisibility(View.VISIBLE);
                toggleRoleButton.setText("Esconder papel");
            } else {
                roleDisplayTextView.setVisibility(View.GONE);
                toggleRoleButton.setText("Mostrar papel");
            }
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
                    statusTextView.setText("Ligado ao anfitrião. À espera que o jogo comece...");
                    joinButton.setVisibility(View.GONE);
                });

                // Start listening for messages from the host
                listenForMessages();

            } catch (IOException e) {
                Log.e(TAG, "Erro ao ligar ao anfitrião", e);
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
        List<MrWhitePlayer> players = (List<MrWhitePlayer>) data.get("players");

        // Find our player in the list
        for (MrWhitePlayer player : players) {
            if (player.getId().equals(playerId)) {
                clientPlayer = player;
                break;
            }
        }

        // Update UI with player role information
        runOnUiThread(() -> {
            if (clientPlayer != null) {
                String roleInfo;
                if (clientPlayer.getRole().equals("MrWhite")) {
                    roleInfo = "Tu és Mr White e a tua palavra é: " + clientPlayer.getWord();
                } else {
                    roleInfo = "Tu és um jogador regular e a tua palavra é: " + clientPlayer.getWord();
                }

                roleDisplayTextView.setText(roleInfo);
                statusTextView.setText("Jogo começado! Usem palavras individuais para descrever a vossa palavra à vez.");
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