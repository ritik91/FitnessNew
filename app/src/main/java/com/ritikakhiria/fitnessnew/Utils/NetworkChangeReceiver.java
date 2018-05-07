package com.ritikakhiria.fitnessnew.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.service.AppJobManager;
import com.ritikakhiria.fitnessnew.service.SendNutritionsDataToServerJob;
import com.ritikakhiria.fitnessnew.service.SendTrackingDataToServerJob;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (checkInternet(context)) {
            String s = Session.getSession(context).getUserId();
            if (!TextUtils.isEmpty(s) && DBHelper.getInstance(context).getUnSyncedTracking().size() > 0) {
                AppJobManager.getJobManager().addJobInBackground(new SendTrackingDataToServerJob(s));
            }
            if (!TextUtils.isEmpty(s) && DBHelper.getInstance(context).getUnsyncNutrition().size() > 0) {
                AppJobManager.getJobManager().addJobInBackground(new SendNutritionsDataToServerJob(s));
            }
        }

    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    public class ServiceManager {

        Context context;

        public ServiceManager(Context base) {
            context = base;
        }

        public boolean isNetworkAvailable() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
}