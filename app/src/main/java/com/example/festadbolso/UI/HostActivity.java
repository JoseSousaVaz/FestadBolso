package com.example.festadbolso.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.festadbolso.R;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HostActivity extends Activity {

    private static final String TAG = "HostActivity";
    private static final int PORT = 12345;

    private Button hotspotButton;
    private Button startGameButton;
    private TextView statusTextView;
    private TextView ipAddressTextView;
    private TextView playersConnectedTextView;
    private TextView roleDisplayTextView;

    private WifiManager wifiManager;
    private ServerSocket serverSocket;
    private ExecutorService executor;

    private List<Player> players = new ArrayList<>();
    private Map<String, ClientHandler> clientHandlers = new HashMap<>();
    private Player hostPlayer;
    private boolean isServerRunning = false;

    private Button toggleRoleButton;

    // Game settings
    private String[] locations = {
            "Praia", // Beach
            "Banco", // Bank
            "Escola", // School
            "Hospital", // Hospital
            "Restaurante", // Restaurant
            "Casino", // Casino
            "Polícia", // Police station
            "Biblioteca", // Library
            "Teatro", // Theater
            "Centro comercial", // Shopping mall
            "Aeroporto", // Airport
            "Estação de comboios", // Train station
            "Parque", // Park
            "Supermercado", // Supermarket
            "Farmácia", // Pharmacy
            "Cinema", // Cinema
            "Museu", // Museum
            "Estádio", // Stadium
            "Café", // Café
            "Discoteca", // Nightclub
            "Zoo", // Zoo
            "Jardim botânico", // Botanical garden
            "Piscina", // Swimming pool
            "Ginásio", // Gym
            "Praça", // Square
            "Mercado", // Market
            "Castelo", // Castle
            "Igreja", // Church
            "Câmara municipal", // City hall
            "Universidade", // University
            "Estação de autocarros", // Bus station
            "Porto", // Harbor
            "Montanha", // Mountain
            "Floresta", // Forest
            "Cemitério", // Cemetery
            "Feira", // Fair
            "Livraria", // Bookstore
            "Sala de concertos", // Concert hall
            "Galeria de arte", // Art gallery
            "Estúdio de cinema", // Film studio
            "Estação de metro", // Subway station
            "Parque de campismo", // Camping site
            "Praça de touros", // Bullring
            "Aquário", // Aquarium
            "Planetário", // Planetarium
            "Observatório", // Observatory
            "Estádio"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        // Initialize UI components
        hotspotButton = findViewById(R.id.hostButton);
        startGameButton = findViewById(R.id.startGameButton);
        statusTextView = findViewById(R.id.statusTextView);
        ipAddressTextView = findViewById(R.id.ipAddressTextView);
        playersConnectedTextView = findViewById(R.id.playersConnectedTextView);
        roleDisplayTextView = findViewById(R.id.roleDisplayTextView);
        toggleRoleButton = findViewById(R.id.toggleRoleButton);


        // Initialize host player and add to players list
        hostPlayer = new Player("Host");
        players.add(hostPlayer);

        // Initialize executor for thread management
        executor = Executors.newCachedThreadPool();

        // Get WiFi service
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Set initial UI state
        updateUI();

        // Set button click listeners
        hotspotButton.setOnClickListener(v -> setupHotspot());

        startGameButton.setOnClickListener(v -> {
            if (players.size() >= 3) {
                // Important: Run this on a background thread
                executor.execute(this::distributeRoles);
                startGameButton.setEnabled(false);
            } else {
                Toast.makeText(this, "É preciso pelo menos 2 jogadores para começar", Toast.LENGTH_SHORT).show();
            }
        });

        // Start server in background
        startServer();

        toggleRoleButton.setOnClickListener(v -> {
            if (roleDisplayTextView.getVisibility() == View.GONE) {
                roleDisplayTextView.setVisibility(View.VISIBLE);
                toggleRoleButton.setText("Hide Role");
            } else {
                roleDisplayTextView.setVisibility(View.GONE);
                toggleRoleButton.setText("Show Role");
            }
        });
    }

    private void setupHotspot() {
        // We can only guide the user to enable hotspot manually
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);

        Toast.makeText(this, "Please enable WiFi Hotspot manually", Toast.LENGTH_LONG).show();
        statusTextView.setText("Enabling hotspot...");

        // Update the IP address after a delay to allow hotspot to initialize
        executor.execute(() -> {
            try {
                Thread.sleep(5000);
                runOnUiThread(this::updateIpAddress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateIpAddress() {
        String ipAddress = getLocalIpAddress();
        if (ipAddress != null) {
            ipAddressTextView.setText("O teu IP: " + ipAddress);
            Log.d(TAG, "IP Address: " + ipAddress);
        } else {
            ipAddressTextView.setText("IP Address not available");
            Log.e(TAG, "Failed to get IP address");
        }
    }

    private String getLocalIpAddress() {
        try {
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            if (ipAddress == 0) {
                // Try to get the IP address for the hotspot interface
                // This is a simplified approach and might need further adjustments
                return "192.168.43.1"; // Common default hotspot IP
            }
            return Formatter.formatIpAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startServer() {
        if (isServerRunning) return;

        executor.execute(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                isServerRunning = true;
                Log.d(TAG, "Server started on port " + PORT);

                runOnUiThread(() -> {
                    statusTextView.setText("Servidor ligado. À espera de jogadores...");
                    updateIpAddress();
                });

                while (isServerRunning) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        Log.d(TAG, "New client connected: " + clientSocket.getInetAddress());

                        // Create and start a new client handler
                        ClientHandler handler = new ClientHandler(clientSocket);
                        executor.execute(handler);
                    } catch (IOException e) {
                        if (isServerRunning) {
                            Log.e(TAG, "Error accepting client connection", e);
                        }
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to start server", e);
                runOnUiThread(() -> statusTextView.setText("Failed to start server: " + e.getMessage()));
            }
        });
    }

    private void stopServer() {
        isServerRunning = false;

        // Close all client connections
        for (ClientHandler handler : clientHandlers.values()) {
            handler.close();
        }
        clientHandlers.clear();

        // Close server socket
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing server socket", e);
            }
        }
    }

    private void distributeRoles() {
        // This method now runs on a background thread
        String chosenLocation = locations[new Random().nextInt(locations.length)];
        Log.d(TAG, "Chosen location: " + chosenLocation);

        // Determine number of spies based on player count
        int numSpies = players.size() > 5 ? 2 : 1;
        Log.d(TAG, "Number of spies: " + numSpies);

        // Create a list to track spy indices
        List<Integer> spyIndices = new ArrayList<>();
        Random random = new Random();

        // Select spies
        while (spyIndices.size() < numSpies) {
            int spyIndex = random.nextInt(players.size());
            // Make sure we don't select the same player twice
            if (!spyIndices.contains(spyIndex)) {
                spyIndices.add(spyIndex);
            }
        }

        // Assign roles and location
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (spyIndices.contains(i)) {
                player.setRole("Espião");
                player.setLocation("Unknown");
            } else {
                player.setRole("Agente");
                player.setLocation(chosenLocation);
            }
        }

        // Show host's role on UI thread
        runOnUiThread(() -> {
            roleDisplayTextView.setText("Tu és um " + hostPlayer.getRole() +
                    (hostPlayer.getRole().equals("Agente") ?
                            " na/no " + hostPlayer.getLocation() : ""));
            statusTextView.setText("O jogo já começou! Papéis distribuidos.");
        });

        // Make a local final copy of the client handlers to avoid concurrency issues
        final Map<String, ClientHandler> handlersSnapshot = new HashMap<>(clientHandlers);

        // Send roles to all players (still in background thread)
        for (ClientHandler handler : handlersSnapshot.values()) {
            try {
                handler.sendGameData();
            } catch (Exception e) {
                Log.e(TAG, "Error sending game data to client", e);
            }
        }
    }

    private void updateUI() {
        runOnUiThread(() -> {
            playersConnectedTextView.setText("Jogadores ligados: " + players.size());
            startGameButton.setEnabled(players.size() >= 2);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServer();
        executor.shutdown();
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream outputStream;
        private Player clientPlayer;
        private boolean isRunning = true;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                Log.e(TAG, "Error creating streams", e);
                isRunning = false;
            }
        }

        @Override
        public void run() {
            try {
                // Create a new player for this client
                clientPlayer = new Player("Player " + (players.size()));

                // Add player to the game
                synchronized (players) {
                    players.add(clientPlayer);
                    clientHandlers.put(clientPlayer.getId(), this);
                }

                // Update UI
                updateUI();

                // Send initial confirmation to client
                sendMessage("Connected to host successfully!");

                // Keep connection open until game ends or connection lost
                while (isRunning) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } finally {
                close();
            }
        }

        public void sendMessage(String message) {
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("type", "message");
                data.put("content", message);
                outputStream.writeObject(data);
                outputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Error sending message", e);
                isRunning = false;
            }
        }

        public void sendGameData() {
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("type", "gameData");
                data.put("playerId", clientPlayer.getId());
                data.put("players", players);
                outputStream.writeObject(data);
                outputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Error sending game data", e);
                isRunning = false;
            }
        }

        public void close() {
            isRunning = false;

            // Remove player from the game
            synchronized (players) {
                players.remove(clientPlayer);
                clientHandlers.remove(clientPlayer.getId());
            }

            // Update UI
            updateUI();

            // Close socket
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing socket", e);
            }
        }
    }
}