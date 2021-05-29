package com.example.gestures;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gestures.Services.Flashlight_service;


/// Creates the app mainActivity including toggle switches and text views
public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    private final int MICROSECOND = 1000000;
    private Switch flashLightSwitch;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    private boolean switchOnOff;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()){
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
        }
        flashLightSwitch = (Switch) findViewById(R.id.switch1);
        flashLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    startService();
                }
                else {
                    stopService();
                }
            }
        });

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
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH1, flashLightSwitch.isChecked());
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);
    }

    /// starts the service if flashlight is checked, contains nested function
    public void updateViews(){
        flashLightSwitch.setChecked(switchOnOff);
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, Flashlight_service.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    /// stops the service
    private void stopService(){
        Intent serviceIntent = new Intent(this, Flashlight_service.class);
        stopService(serviceIntent);
    }


}