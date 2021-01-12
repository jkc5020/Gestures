package com.example.gestures;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gestures.Services.Flashlight_service;
import com.example.gestures.gestureModels.Flashlight;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    private final int MICROSECOND = 1000000;
    private SensorManager sensorManager;
    Sensor accelerometer;
    boolean isSwitch;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float xDifference, yDifference, zDifference;
    private boolean isFirstTime;
    private final float shakeThreshold = (float) 5.0;
    boolean isFlashOn;
    CameraManager cameraManager;
    private Switch aSwitch;
    private Vibrator vibrator;
    private int count;
    Flashlight flashlight;
    public TextView textView;
    public EditText editText;
    public Button applyTextButton;
    public Button saveButton;
    private Switch flashLightSwitch;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SWITCH1 = "switch1";

    private boolean switchOnOff;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void updateViews(){
        flashLightSwitch.setChecked(switchOnOff);
    }
    private void startService() {
        Intent serviceIntent = new Intent(this, Flashlight_service.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void stopService(){
        Intent serviceIntent = new Intent(this, Flashlight_service.class);
        stopService(serviceIntent);
    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        Log.d(TAG, "onSensorChanged: X: " + sensorEvent.values[0] + "Y: " + sensorEvent.values[1] + "Z:" + sensorEvent.values[2]);
//        currentX = sensorEvent.values[0];
//        currentY = sensorEvent.values[1];
//        currentZ = sensorEvent.values[2];
//
//        if(isFirstTime){
//            xDifference = Math.abs(lastX- currentX);
//            yDifference = Math.abs(lastY-currentY);
//            zDifference = Math.abs(lastZ - currentZ);
//            if(xDifference > shakeThreshold){
//                count ++;
//                Log.d(TAG, "Count " + count);
//            }
//
//            if(count == 2){
//                {
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
//                        if (!isFlashOn) {
//                            try {
//                                cameraManager.setTorchMode("0", true);
//                            } catch (CameraAccessException e) {
//                                e.printStackTrace();
//                            }
//                            isFlashOn = true;
//                        } else {
//                            try {
//                                cameraManager.setTorchMode("0", false);
//                            } catch (CameraAccessException e) {
//                                e.printStackTrace();
//                            }
//                            isFlashOn = false;
//                        }
//                    } else {
//                        vibrator.vibrate(500);
//                    }
//                }
//
//            count = 0;
//            }
//
//
//        }
//
//        lastX = currentX;
//        lastY = currentY;
//        lastZ = currentZ;
//        isFirstTime = true;
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//
//    }

//    /**
//     * Dispatch onResume() to fragments.  Note that for better inter-operation
//     * with older versions of the platform, at the point of this call the
//     * fragments attached to the activity are <em>not</em> resumed.
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
//            sensorManager.registerListener(this, accelerometer, 5* MICROSECOND);
//        }
//
//    }
//
//    /**
//     * Dispatch onPause() to fragments.
//     */
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG, "onPause");
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
//            sensorManager.registerListener(this, accelerometer, 5* MICROSECOND);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "onDestroy");
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
//            sensorManager.registerListener(this, accelerometer, 5* MICROSECOND);
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop");
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
//            sensorManager.registerListener(this, accelerometer, 5* MICROSECOND);
//        }
//
//    }
    //    public void toggle_LED(View v) throws CameraAccessException {
//        Button button = (Button) v;
//        if(button.getText().toString().equals("Switch On")){
//            button.setText("switch off");
//            toggle("on");
//
//        }
//
//    }
//    private void toggle (String cmd) throws CameraAccessException {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
//            String cameraId = null;
//            if(cameraManager!=null){
//                cameraId = cameraManager.getCameraIdList()[0];
//                if(cmd.equals("on")){
//                    cameraManager.setTorchMode(cameraId, true);
//                    isSwitch = true;
//                }
//                else {
//                    cameraManager.setTorchMode(cameraId, false);
//                    isSwitch = false;
//                }
//            }
//        }
//    }
}