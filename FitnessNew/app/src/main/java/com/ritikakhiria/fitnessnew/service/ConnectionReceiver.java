package com.ritikakhiria.fitnessnew.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Objects;


public class ConnectionReceiver extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences sharedpreferences = context.getSharedPreferences("Mypref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            boolean isServiceStopped = sharedpreferences.getBoolean("isServiceStopped", true);
            if (isServiceStopped) {
                context.stopService(new Intent(context, StepCountingService.class));
                editor.putBoolean("isServiceStopped", true);
                editor.apply();
            }
            context.startService(new Intent(context, StepCountingService.class));
            editor.putBoolean("isServiceStopped", false);
            editor.apply();
        }
    }
}
