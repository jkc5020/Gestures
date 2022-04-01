package com.example.gestures.Services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.gestures.MainActivity;

/**
 * Broadcast receiver for notification button action handling
 */
public class Receiver extends android.content.BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        } else if (intent.getAction().equals("user_yes")) {
            NotificationManager manager;
            manager = context.getSystemService(NotificationManager.class);
            manager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);

        }
    }
}
