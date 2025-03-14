package com.example.festadbolso.UI;

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

import androidx.appcompat.app.AppCompatActivity;

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

public class MrWhiteActivity extends AppCompatActivity {

    private static final String TAG = "MrWhiteActivity";
    private static final int PORT = 12346; // Different port from Spy game

    private Button hostButton;
    private Button joinButton;
    private TextView statusTextView;

    private WifiManager wifiManager;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_white);

        // Initialize UI components
        statusTextView = findViewById(R.id.statusTextView);
        hostButton = findViewById(R.id.hostButton);
        joinButton = findViewById(R.id.joinButton);

        // Initialize executor for thread management
        executor = Executors.newCachedThreadPool();

        // Get WiFi service
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Set button listeners
        hostButton.setOnClickListener(v -> {
            // Start the HostMrWhiteActivity
            Intent hostIntent = new Intent(MrWhiteActivity.this, HostMrWhiteActivity.class);
            startActivity(hostIntent);
        });

        joinButton.setOnClickListener(v -> {
            // Start the JoinMrWhiteActivity
            Intent joinIntent = new Intent(MrWhiteActivity.this, JoinMrWhiteActivity.class);
            startActivity(joinIntent);
        });
    }
}