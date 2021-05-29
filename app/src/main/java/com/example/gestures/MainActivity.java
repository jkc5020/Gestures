package com.example.gestures;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gestures.Services.Flashlight_service;


/// Creates the app mainActivity including toggle switches and text views
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final int MICROSECOND = 1000000;
    private Switch flashLightSwitch;
    private static final String SWITCH2 = "switch2";
    private static final String SWITCH3 = "switch3";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    private Switch dndSwitch;
    private Switch serviceSwitch;
    private boolean flashOn;
    private NotificationManager notificationManager;
    private boolean flashLightOn;
    private boolean dndOn;
    private boolean dndChecked;
    private boolean serviceOn;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
        }
        flashLightSwitch = (Switch) findViewById(R.id.switch1);
        dndSwitch = (Switch) findViewById(R.id.switch2);
        serviceSwitch = (Switch) findViewById(R.id.switch3);
        button = (Button) findViewById(R.id.button_update);

        loadData();
        updateViews();

    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    /// saves user prefs for later so when the app is reopened it isn't
    /// reset to its default values
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH1, flashLightSwitch.isChecked());
        editor.putBoolean(SWITCH2, dndSwitch.isChecked());
        editor.putBoolean((SWITCH3), serviceSwitch.isChecked());

        editor.apply();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        flashOn = sharedPreferences.getBoolean(SWITCH1, false);
        dndChecked = sharedPreferences.getBoolean(SWITCH2, false);
        serviceOn = sharedPreferences.getBoolean(SWITCH3, false);
    }

    /// starts the service if flashlight is checked, contains nested function
    public void updateViews() {
        flashLightSwitch.setChecked(flashOn);
        serviceSwitch.setChecked(serviceOn);
        dndSwitch.setChecked(dndChecked);
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, Flashlight_service.class);
        Bundle extras = new Bundle();
        extras.putBoolean("Service", true);
        extras.putBoolean("Flashlight", flashLightSwitch.isChecked());
        extras.putBoolean("dnd", dndSwitch.isChecked());
        extras.putString("service", "foreground service on");
        serviceIntent.putExtras(extras);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    /// stops the service
    private void stopService() {
        Intent serviceIntent = new Intent(this, Flashlight_service.class);
        stopService(serviceIntent);
    }


    public void updateSettings(View view) {
        if (serviceSwitch.isChecked()) {
            startService();

        } else {
            stopService();
        }
    }
}