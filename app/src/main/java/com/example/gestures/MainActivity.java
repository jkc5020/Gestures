package com.example.gestures;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
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
    Switch aSwitch;
    private Vibrator vibrator;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isFlashOn = false;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        isFirstTime = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        }
        aSwitch = (Switch) findViewById(R.id.switch1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Registered accelerometer listener");
        // gives permission to use sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, MICROSECOND * 10);
        Log.d(TAG,"onCreate: Registered sensor");
//        aSwitch.setOnClickListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//            }
//        });



    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Log.d(TAG, "onSensorChanged: X: " + sensorEvent.values[0] + "Y: " + sensorEvent.values[1] + "Z:" + sensorEvent.values[2]);
        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(isFirstTime){
            xDifference = Math.abs(lastX- currentX);
            yDifference = Math.abs(lastY-currentY);
            zDifference = Math.abs(lastZ - currentZ);
            if(xDifference > shakeThreshold){
                count ++;
                Log.d(TAG, "Count " + count);
            }

            if(count == 4){
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        if (!isFlashOn) {
                            try {
                                cameraManager.setTorchMode("0", true);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                            isFlashOn = true;
                        } else {
                            try {
                                cameraManager.setTorchMode("0", false);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                            isFlashOn = false;
                        }
                    } else {
                        vibrator.vibrate(500);
                    }
                }

            count = 0;
            }


        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        isFirstTime = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }


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