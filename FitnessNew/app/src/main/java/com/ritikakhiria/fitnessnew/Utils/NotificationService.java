package com.ritikakhiria.fitnessnew.Utils;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ritikakhiria.fitnessnew.activity.DietType;


public class NotificationService extends IntentService {

    String TAG = NotificationService.class.getSimpleName();
    /**
     * Creates an IntentService.  Invoked by subclass's constructor.
     */
    public NotificationService() {
        super("voiceIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Log.d(TAG,"Hello :"+intent.getAction());
        final int type = intent.getIntExtra("type",0);
        final String date = intent.getStringExtra("date");
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(type);
        Log.d(TAG,"Hello fd :"+type);
        switch (intent.getAction()) {
            case "yes":
                Handler leftHandler = new Handler(Looper.getMainLooper());
                leftHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getBaseContext(), "You clicked the left button", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(getBaseContext(), DietType.class);
                        intent1.putExtra("type",type);
                        intent1.putExtra("date",date);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("yesOrNo","yes");
                        startActivity(intent1);
                    }
                });
                break;
            case "no":
                Handler rightHandler = new Handler(Looper.getMainLooper());
                rightHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getBaseContext(), "You clicked the right button", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(getBaseContext(), DietType.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("type",type);
                        intent1.putExtra("date",date);
                        intent1.putExtra("yesOrNo","no");
                        startActivity(intent1);
                    }
                });
                break;
        }
    }
}

