package com.example.gestures;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Switch;

import androidx.annotation.Nullable;

public class services extends Service implements SensorEventListener {

    public final int MIN_Time = 1000;
    SensorManager sensorManager = null;
    Vibrator vibrator = null;
    private long lastShakeTime = 0;
    private boolean isFlashLightOn = false;
    private Float shakeThreshold = 10.0f;
    Switch flashControl;

    public services(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager)getSystemService((SENSOR_SERVICE));
        if(sensorManager!= null){
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            long curTime = System.currentTimeMillis();
            if((curTime = lastShakeTime) > MIN_Time){
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double acceleration = Math.sqrt(Math.pow(x, 2)) + Math.pow(y,2) + Math.pow(z,2) - SensorManager.GRAVITY_EARTH;
                if(acceleration > shakeThreshold){
                    lastShakeTime = curTime;
                    if(!isFlashLightOn){
                        //isFlashLightOn = torchToggle("on");

                    }
                    else{
                        //isFlashLightOn = torchToggle("off");

                    }
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
