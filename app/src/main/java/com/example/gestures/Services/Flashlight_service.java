package com.example.gestures.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.gestures.MainActivity;
import com.example.gestures.R;

public class Flashlight_service extends Service implements SensorEventListener {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private SensorManager sensorManager = null;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float xDifference, yDifference, zDifference;
    private boolean isFirstTime;
    private final float shakeThreshold = (float) 5.0;
    boolean isFlashOn;
    CameraManager cameraManager;
    private Vibrator vibrator;
    private int count;
    private Sensor accelerometer = null;
    private Sensor gyroscope = null;
    private Sensor gravity = null;
    private AudioManager audioManager;
    private boolean isFaceDown = false;
    private final int id1 = 1;
    NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, 10* 1000);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        return START_STICKY;

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            currentX = sensorEvent.values[0];
            currentY = sensorEvent.values[1];
            currentZ = sensorEvent.values[2];

            if (isFirstTime) {
                xDifference = Math.abs(lastX - currentX);
                yDifference = Math.abs(lastY - currentY);
                zDifference = Math.abs(lastZ - currentZ);
                if (xDifference > shakeThreshold) {
                    count++;
                    //Log.d(TAG, "Count " + count);
                }

                if (count == 20) {
                    {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot
                                    (500, VibrationEffect.DEFAULT_AMPLITUDE));
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
            if(currentZ < -9.7){
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT); //ringer mode
            }

            lastX = currentX;
            lastY = currentY;
            lastZ = currentZ;
            isFirstTime = true;
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY){
            if(sensorEvent.values[2]<-9.7){
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                System.out.println(sensorEvent.values[2]);

                isFaceDown = true;

            }
            if(sensorEvent.values[2]>5 && isFaceDown){
                Intent dndIntent = new Intent(this, AudioManager.class);
                dndIntent.setAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Phone flipped")
                        .setContentText("Your phone has been flipped would you like to deactivate dnd?")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                System.out.println(sensorEvent.values[2]);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                notificationManager.notify(id1, builder.build());
                isFaceDown = false;
            }
            System.out.println(sensorEvent.values[2]);

        }
    }
    private void turnDND(){

    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
