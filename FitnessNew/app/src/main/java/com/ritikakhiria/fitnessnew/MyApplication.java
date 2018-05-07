package com.ritikakhiria.fitnessnew;


import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import com.ritikakhiria.fitnessnew.retrofit.AppRetrofitManager;
import com.ritikakhiria.fitnessnew.service.AppJobManager;

import io.fabric.sdk.android.Fabric;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        MultiDex.install(this);
        AppJobManager.getJobManager(this);
        AppRetrofitManager.getRetrofit();
    }
}

