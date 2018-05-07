package com.ritikakhiria.fitnessnew.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.service.AppJobManager;
import com.ritikakhiria.fitnessnew.service.GetTrackingDataFromServer;
import com.ritikakhiria.fitnessnew.service.SendNutritionsDataToServerJob;
import com.ritikakhiria.fitnessnew.service.SendTrackingDataToServerJob;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotificationPublisher extends BroadcastReceiver {

    private String tag = NotificationPublisher.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra("type", 0);
        String date = intent.getStringExtra("date");
        String useID = intent.getStringExtra("userID");
        if (type == 4) {
            AppJobManager.getJobManager().addJobInBackground(new SendTrackingDataToServerJob(useID));
            AppJobManager.getJobManager().addJobInBackground(new SendNutritionsDataToServerJob(useID));
        } else {
            createNotification(context, type, date);
        }
    }

    private void createNotification(Context context, int type, String date) {
        // BEGIN_INCLUDE(notificationCompat)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the notification is clicked.
/*        Intent i = new Intent(context, HistoryChartActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, type, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);*/
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker(foodTitle(type));

        // Sets the small icon for the ticker
        builder.setSmallIcon(R.drawable.fitness);
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        Bundle bundle = new Bundle();
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        Intent yesIntent = new Intent(context, NotificationService.class);
        yesIntent.setAction("yes");
        bundle.putInt("type", type);
        bundle.putString("date", date);
        yesIntent.putExtras(bundle);
        contentView.setOnClickPendingIntent(R.id.btn_yes, PendingIntent.getService(context, type, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        Intent noIntent = new Intent(context, NotificationService.class);
        noIntent.setAction("no");
        noIntent.putExtras(bundle);
        contentView.setOnClickPendingIntent(R.id.btn_no, PendingIntent.getService(context, type, noIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        // Set text on a TextView in the RemoteViews programmatically.
        contentView.setTextViewText(R.id.tv_question, foodTitle(type));

        /* Workaround: Need to set the content view here directly on the notification.
         * NotificationCompatBuilder contains a bug that prevents this from working on platform
         * versions HoneyComb.
         * See https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed, when expanded the
        // big content view set here is displayed.)
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflate and set the layout for the expanded notification view
            RemoteViews expandedView =
                    new RemoteViews(context.getPackageName(), R.layout.notification_expanded);

        /*    Intent yesIntentExpanded = new Intent(context, NotificationService.class);
            yesIntentExpanded.setAction("yes");
            bundle.putInt("type",type);
            bundle.putString("date",date);
            yesIntentExpanded.putExtras(bundle);*/
            expandedView.setOnClickPendingIntent(R.id.btn_yes, PendingIntent.getService(context, type, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT));

           /* Intent noIntentExpanded = new Intent(context, NotificationService.class);
            noIntentExpanded.setAction("no");
            noIntentExpanded.putExtras(bundle);*/
            expandedView.setOnClickPendingIntent(R.id.btn_no, PendingIntent.getService(context, type, noIntent, PendingIntent.FLAG_UPDATE_CURRENT));
            // Set text on a TextView in the RemoteViews programmatically.
            expandedView.setTextViewText(R.id.tv_question, foodTitle(type));

           /* Intent leftIntent = new Intent(context, NotificationService.class);
            leftIntent.setAction("voice");
            expandedView.setOnClickPendingIntent(R.id.btn_voice, PendingIntent.getService(context, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT));*/
            notification.bigContentView = expandedView;
        }
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(type, notification);
        // END_INCLUDE(notify)
    }

    private String foodTitle(int type) {
        String food = null;
        switch (type) {
            case 1:
                food = "Have you had breakfast?";
                break;
            case 2:
                food = "Have you had lunch?";
                break;
            case 3:
                food = "Have you had dinner?";
                break;
        }
        return food;
    }


}