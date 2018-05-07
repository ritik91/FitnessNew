package com.ritikakhiria.fitnessnew.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.ActivityRecognizedService;
import com.ritikakhiria.fitnessnew.Utils.Common;
import com.ritikakhiria.fitnessnew.Utils.NotificationPublisher;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.ActivityEvent;
import com.ritikakhiria.fitnessnew.model.MyStepModel;
import com.ritikakhiria.fitnessnew.service.AppJobManager;
import com.ritikakhiria.fitnessnew.service.SendTrackingDataToServerJob;
import com.ritikakhiria.fitnessnew.service.StepCountingService;
import com.ritikakhiria.fitnessnew.stesp.StepDetector;
import com.ritikakhiria.fitnessnew.stesp.StepListener;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import okhttp3.internal.Util;

import static com.ritikakhiria.fitnessnew.Utils.Utils.getCurrentDate;
import static com.ritikakhiria.fitnessnew.Utils.Utils.setAlarm;

public class MySteps extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Button start_stop;
    private TextView steps_count, lblDistance, lblActivity, lblCaloriesBurned;
    private TextView lblWalkingCount, lblRunningCount, lblStairCount;
    public FitChart mfitChart;
    public GoogleApiClient mApiClient;
    public String step, distance, calories, walk, running, stair;
    public boolean isServiceStopped;
    private static final String TEXT_NUM_STEPS = " STEPS";//:
    public static SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystep2);
        sharedpreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
        mfitChart = (FitChart) findViewById(R.id.fitChart);
        mfitChart.setMinValue(0f);
        mfitChart.setMaxValue(100f);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        registerReceiver(broadcastReceiver, new IntentFilter(StepCountingService.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(broadcastReceiver);
    }

    private void init() {
        isServiceStopped = sharedpreferences.getBoolean("isServiceStopped", true);
        lblDistance = (TextView) findViewById(R.id.lblDistance);
        lblActivity = (TextView) findViewById(R.id.lblActivity);
        lblCaloriesBurned = (TextView) findViewById(R.id.lblCaloriesBurned);
        lblWalkingCount = (TextView) findViewById(R.id.textWalking);
        lblRunningCount = (TextView) findViewById(R.id.textRunning);
        lblStairCount = (TextView) findViewById(R.id.textStairs);
        steps_count = (TextView) findViewById(R.id.steps_count);
        start_stop = (Button) findViewById(R.id.start_stop);
        if (isServiceStopped) {
            start_stop.setText("START");
        } else {
            start_stop.setText("STOP");
        }
        start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SensorManager s = (SensorManager) getSystemService(SENSOR_SERVICE);
                Sensor countSensor = s.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if (countSensor != null) {
                    if (start_stop.getText().toString().equalsIgnoreCase("START")) {
                        start_stop.setText("STOP");
                        startService(new Intent(getBaseContext(), StepCountingService.class));
                        editor.putBoolean("isServiceStopped", false);
                        editor.apply();
                        startActivity();
                    } else {
                        start_stop.setText("START");
                        stopService(new Intent(getBaseContext(), StepCountingService.class));
                        editor.putBoolean("isServiceStopped", true);
                        editor.apply();
                        stopActivity();
                        AppJobManager.getJobManager().addJobInBackground(new SendTrackingDataToServerJob(Session.getSession(MySteps.this).getUserId()));
                        reset();
                    }
                    Resources resources = getResources();
                    Collection<FitChartValue> values = new ArrayList<>();
                    values.add(new FitChartValue(30f, resources.getColor(R.color.chart_value_1)));
                    values.add(new FitChartValue(25f, resources.getColor(R.color.chart_value_2)));
                    values.add(new FitChartValue(20f, resources.getColor(R.color.chart_value_3)));
                    values.add(new FitChartValue(15f, resources.getColor(R.color.chart_value_4)));
                    values.add(new FitChartValue(10f, resources.getColor(R.color.chart_value_5)));
                    mfitChart.setValues(values);
                } else {
                    Toast.makeText(MySteps.this, "Sensor not found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // --------------------------------------------------------------------------- \\
    // ___ create Broadcast Receiver ___ \\
    // create a BroadcastReceiver - to receive the message that is going to be broadcast from the Service
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // call updateUI passing in our intent which is holding the data to display.
            updateViews(intent);
        }
    };


    public void updateViews(Intent intent) {
        // retrieve data out of the intent.
        step = intent.getStringExtra("step");
        distance = intent.getStringExtra("distance");
        calories = intent.getStringExtra("calories");
        walk = intent.getStringExtra("walk");
        running = intent.getStringExtra("running");
        stair = intent.getStringExtra("stair");
        steps_count.setText(step);
        lblDistance.setText(distance);
        lblWalkingCount.setText(walk);
        lblCaloriesBurned.setText(calories);
        lblRunningCount.setText(running);
        lblStairCount.setText(stair);
        lblActivity.setText(TEXT_NUM_STEPS);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        start_stop.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void startActivity() {
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 1000, pendingIntent);
    }

    private void stopActivity() {
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mApiClient, pendingIntent);
    }


    private void reset() {
        finish();
        startActivity(getIntent());
        Intent intent = new Intent(MySteps.this, UserLogActivity.class);
        startActivity(intent);
    }

    /**
     * Unregisters the sensor listener if it is registered.
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityEvent activityEvent) {
        switch (activityEvent.getActivity()) {
            case DetectedActivity.WALKING:
                lblActivity.setText("WALKING");
                break;
            case DetectedActivity.TILTING:
                lblActivity.setText("STAIRS");
                break;
            case DetectedActivity.RUNNING:
                lblActivity.setText("RUNNING");
                break;
        }
    }


}

