package com.example.festadbolso.UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import com.example.festadbolso.R;

import java.lang.reflect.Method;

public class SpyActivity extends AppCompatActivity {

    private TextView testResultsTextView;
    private Button testHotspotButton;
    private Button hostButton;
    private Button joinButton;

    private ImageView spy_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testResultsTextView = findViewById(R.id.testResults);
        testHotspotButton = findViewById(R.id.testHotspotButton);
        hostButton = findViewById(R.id.hostButton);
        joinButton = findViewById(R.id.joinButton);

        testResultsTextView.setVisibility(View.GONE); //don't show text
        testHotspotButton.setVisibility(View.GONE); //don't show text


        // Log when onCreate is called
        Log.d("SpyActivity", "onCreate called");

        // Set button listeners
        testHotspotButton.setOnClickListener(v -> performTests());

        hostButton.setOnClickListener(v -> {
            // Start the HostActivity
            Intent hostIntent = new Intent(SpyActivity.this, HostActivity.class);
            startActivity(hostIntent);
        });

        joinButton.setOnClickListener(v -> {
            // Start the JoinActivity
            Intent joinIntent = new Intent(SpyActivity.this, JoinActivity.class);
            startActivity(joinIntent);
        });
    }

    private void performTests() {
        StringBuilder results = new StringBuilder();

        // Check if device supports tethering (for Android versions before Q)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                try {
                    // Use reflection to access the getTetherableWifiRegexs() method
                    Method method = connectivityManager.getClass().getMethod("getTetherableWifiRegexs");
                    String[] tetherableWifiRegexs = (String[]) method.invoke(connectivityManager);
                    if (tetherableWifiRegexs.length > 0) {
                        results.append("Device supports Wi-Fi Hotspot (Older Android versions)\n");
                        Log.d("SpyActivity", "Device supports Wi-Fi Hotspot (Older Android versions)");
                    } else {
                        results.append("Device does NOT support Wi-Fi Hotspot (Older Android versions)\n");
                        Log.d("SpyActivity", "Device does NOT support Wi-Fi Hotspot (Older Android versions)");
                    }
                } catch (Exception e) {
                    results.append("Error accessing tethering method on older Android versions.\n");
                    e.printStackTrace();
                    Log.d("SpyActivity", "Error accessing tethering method on older Android versions.");
                }
            }
        } else {
            // For Android Q and above, check if hotspot is enabled using WifiManager
            if (isHotspotEnabled()) {
                results.append("Hotspot is enabled.\n");
                Log.d("SpyActivity", "Hotspot is enabled.");
            } else {
                results.append("Hotspot is disabled.\n");
                Log.d("SpyActivity", "Hotspot is disabled.");
            }
        }

        // Update the UI with results
        testResultsTextView.setText(results.toString());
    }

    private boolean isHotspotEnabled() {
        try {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            Boolean isHotspotEnabled = (Boolean) method.invoke(wifiManager);
            Log.d("SpyActivity", "isWifiApEnabled: " + isHotspotEnabled);
            return isHotspotEnabled != null && isHotspotEnabled;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SpyActivity", "Error accessing hotspot state.");
            return false;
        }
    }
}
