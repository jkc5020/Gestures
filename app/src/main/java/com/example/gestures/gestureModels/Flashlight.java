package com.example.gestures.gestureModels;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.gestures.MainActivity;
import com.example.gestures.R;

public class Flashlight extends Service implements SensorEventListener {
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, "FlashLight")
                .setContentTitle("Flashlight service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        return START_NOT_STICKY;

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(isFirstTime){
            xDifference = Math.abs(lastX- currentX);
            yDifference = Math.abs(lastY-currentY);
            zDifference = Math.abs(lastZ - currentZ);
            if(xDifference > shakeThreshold){
                count ++;
                //Log.d(TAG, "Count " + count);
            }

            if(count == 2){
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
}
