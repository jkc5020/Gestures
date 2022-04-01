package com.example.gestures.Services;

import android.annotation.SuppressLint;
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
import android.os.Bundle;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.gestures.MainActivity;
import com.example.gestures.R;

/**
 * The flashlight service(needs to be changed for a name)
 * is in charge of handling all the gestures by monitoring the sensors
 */

public class Flashlight_service extends Service implements SensorEventListener {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private SensorManager sensorManager; //Manages listening to all device sensors

    // coordinates for sensors
    private float lastX;
    private float lastY;
    private float lastZ;

    private boolean isFirstTime; // checks if flashlight action has only done once to help with
                                // motion accuracy, that way a "chop" will have to be done twice to
                                // execute action

    boolean isFlashOn;
    CameraManager cameraManager; // accesses camera to gain Flashlight capability
    private Vibrator vibrator;
    private int count;
    private AudioManager audioManager;
    private boolean isFaceDown = false;
    NotificationManager manager;
    int current_mode; // stores the current ringer mode for the ringer

    // flash, dnd, silence are true if user wants these actions to be used, false if not

    private boolean flash;
    private boolean dnd;
    private boolean silence;
    private int sliderValue; // for sensitivity of flashlight

    public Flashlight_service() {
        sensorManager = null;
    }

    /**
     * Registers all the required services
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, 10 * 1000);
        Sensor gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Creates a new notification channel
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            NotificationChannel dndChannel = new NotificationChannel(
                    "dnd", "dnd", NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(dndChannel);
        }
    }

    /**
     * Creates a notification for the foreground service,
     * and start the foreground service
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String input = extras.getString("service");
        flash = extras.getBoolean("Flashlight");
        dnd = extras.getBoolean("dnd");
        silence = extras.getBoolean("silence");
        sliderValue = extras.getInt("Sensitivity");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                //.setChannelId("Foreground Service Channel")
                .build();
        startForeground(1, notification);

        return START_STICKY;

    }

    /**
     * Turns on/off dnd by monitoring the gravity sensor
     * Several conditionals ensure the feature only turns on based on the right scenario
     * @param sensorEvent - the sensorEvent from original method
     */
    @SuppressLint("NotificationTrampoline")
    private void turnDND(SensorEvent sensorEvent) {
        // turns on dnd
        if (sensorEvent.values[2] < -9.7 && dnd) {
            if (current_mode != 0) {
                current_mode = audioManager.getRingerMode();
            }
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // turns on dnd
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            }

            System.out.println(sensorEvent.values[2]);

            isFaceDown = true;

        }
        // turns off DND, and sends notification asking if user would like to keep it on
        if (sensorEvent.values[2] > 5 && isFaceDown && dnd) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }

            Intent broadcastIntent = new Intent(this, Receiver.class);
            broadcastIntent.setAction("user_yes");
            PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder;
            builder = new NotificationCompat.Builder(this, "dnd")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Phone flipped")
                    .setContentText("Your phone has been flipped and DND was deactivated would you like to turn it on again?")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(R.mipmap.ic_launcher, "Yes", actionIntent);

            System.out.println(sensorEvent.values[2]);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


            int id1 = 2;
            notificationManager.notify(id1, builder.build());
            isFaceDown = false;
        }
        System.out.println(sensorEvent.values[2]);

    }


    /**
     * Handles what event to execute upon change in sensor data and uses several helper methods
     *
     * @param sensorEvent - any change in the device sensor monitoring
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            turnFlashlight(sensorEvent);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            turnDND(sensorEvent);

        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //turnSilence(sensorEvent);
        }

    }

    /**
     * turns on silent mode, not currently used
     *
     * @param sensorEvent - sensorEvent from original method
     */
    private void turnSilence(SensorEvent sensorEvent) {
        float currentX = sensorEvent.values[0];
        float currentY = sensorEvent.values[1];
        float currentZ = sensorEvent.values[2];
        int count = 0;
        if (silence) if (Math.abs(currentY) > 5) {
            count += 1;
        }

        if (count > 2) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

        }
    }

    /**
     * Turns on the flashlight by monitoring the accelerometer
     *
     * @param sensorEvent -  sensorEvent from original method
     */
    private void turnFlashlight(SensorEvent sensorEvent) {
        float currentX = sensorEvent.values[0];
        float currentY = sensorEvent.values[1];
        float currentZ = sensorEvent.values[2];

        // turns on/off flashlight if user selected this feature
        if (isFirstTime && flash) {

            //calculations
            float xDifference = Math.abs(lastX - currentX);
            float yDifference = Math.abs(lastY - currentY);
            float zDifference = Math.abs(lastZ - currentZ);

            // threshold from slider on UI
            float shakeThreshold;

            // default value
            if(sliderValue == 0) {
                shakeThreshold = (float) 5.0;
            }
            else{
                shakeThreshold = (float) (11.0 - sliderValue);
            }
            if (xDifference > shakeThreshold) {
                count++;
                //Log.d(TAG, "Count " + count);
            }

            // will only turn on count if enough motion change of the right magnitude is done
            if (count == 20) {
                {
                    // vibrates the phone

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot
                                (500, VibrationEffect.DEFAULT_AMPLITUDE));

                    } else {
                        vibrator.vibrate(500);
                    }

                    // turn on flashlight if it is off
                    if (!isFlashOn) {
                        try {
                            cameraManager.setTorchMode("0", true);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                        isFlashOn = true;
                    }
                    // turn off flashlight if it is on
                    else {
                        try {
                            cameraManager.setTorchMode("0", false);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                        isFlashOn = false;
                    }
                }

                // reset count
                count = 0;
            }


        }

        // set values
        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        isFirstTime = true;
    }

    /**
     * Calculates sensitivity of flashlight based on seekBar
     * @return - the value
     */
    private float calcSensitivity() {
        if(sliderValue > 0 && sliderValue <=25){
            return 2;
        }
        else if(sliderValue > 25 && sliderValue <=50){
            return 3;
        }

        else if(sliderValue > 50 && sliderValue <=75){
            return 4;
        }
        else {
            return 5;
        }
    }

    /**
     * Function required for interface implementation
     *
     * @param sensor
     * @param i
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}






