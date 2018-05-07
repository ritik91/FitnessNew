package com.ritikakhiria.fitnessnew.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Common;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.activity.MySteps;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.HistoryModel;
import com.ritikakhiria.fitnessnew.model.MyStepModel;
import com.ritikakhiria.fitnessnew.stesp.StepDetector;
import com.ritikakhiria.fitnessnew.stesp.StepListener;

import java.util.ArrayList;
import java.util.List;

public class StepCountingService extends Service implements SensorEventListener, StepListener {

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private Sensor stepDetectorSensor;
    public static int numSteps = 0;
    private boolean serviceStopped; // Boolean variable to control the repeating timer.
    private NotificationManager notificationManager;
    private int counterSteps;
    private int runningCount, stairCount;
    private boolean isRunning, isStair;
    private int walkingSteps;
    private int walkTime, runTime, stairsTime;
    private int i = 0;
    private Intent intent;
    private static final String TAG = "StepService";
    public static final String BROADCAST_ACTION = "com.websmithing.yusuf.mybroadcast";
    private final Handler handler = new Handler();
    private int counter = 0;
    private String steps = "0", distance = "0", calories = "0.0";
    private Session session;
    private StepDetector simpleStepDetector;
    private SharedPreferences pref;
    int preSteps = 0;

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        pref = getSharedPreferences(Common.STEP_OF_DAY, MODE_PRIVATE);
        session = Session.getSession(this);
        synchronized (this) {
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            simpleStepDetector = new StepDetector();
            simpleStepDetector.registerListener(this);
        }
        List<HistoryModel> d = DBHelper.getInstance(this).getTodayTrackingData(Utils.todayDate());
        if (d != null && d.size() > 0) {
            steps = d.get(0).getSteps() + "";
            distance = d.get(0).getDistance() + "";
            calories = d.get(0).getCaloriesBurned() + "";
            runningCount = d.get(0).getRunning();
            stairCount = d.get(0).getStairs();
            walkingSteps = d.get(0).getWalking();
            preSteps = runningCount + stairCount + walkingSteps;
            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis();
            MyStepModel stepModel = new MyStepModel();
            stepModel.setDistance(Float.parseFloat(distance));
            stepModel.setSteps(Long.parseLong(steps));
            stepModel.setCaloriesBurned(Double.parseDouble(calories));
            int totalWalkTime = walkingSteps + (int)(endTime - startTime);
            walkTime = totalWalkTime / 133;
            stepModel.setWalking(walkTime);
            int totalRunTime = runningCount + (int)(endTime - startTime);
            runTime = totalRunTime / 178;
            stepModel.setRunning(runTime);
            int totalStairsTime = stairCount + (int)(endTime - startTime);
            stairsTime = totalStairsTime / 90;
            stepModel.setStairs(stairsTime);
            stepModel.setStatus(d.get(0).getStatus());
            intent.putExtra("step", stepModel.getSteps() + "");
            intent.putExtra("distance", Utils.round(stepModel.getDistance(), 2) + "");
            intent.putExtra("calories", Utils.roundDouble(stepModel.getCaloriesBurned(), 2) + "");
            intent.putExtra("walk", String.valueOf(stepModel.getWalking()));
            intent.putExtra("running", String.valueOf(stepModel.getRunning()));
            intent.putExtra("stair", String.valueOf(stepModel.getStairs()));
            intent.putExtra("status", String.valueOf(stepModel.getStatus()));
            sendBroadcast(intent);
        } else {
            numSteps = 0;
        }
    }

    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Service", "Start");
        showNotification();
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepCounterSensor, 0);
        sensorManager.registerListener(this, stepDetectorSensor, 0);
        serviceStopped = false;
        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData); // 0 seconds
        return START_STICKY;
    }

    /**
     * A client is binding to the service with bindService()
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called when The service is no longer used and is being destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Service", "Stop");
        serviceStopped = true;
        dismissNotification();
    }

    /**
     * Called when the overall system is running low on memory, and actively running processes should trim their memory usage.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void startCount(Sensor countSensor) {
        numSteps = 0;
        Log.d(TAG, "Step counter is :" + countSensor);
        sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /////////////////__________________ Sensor Event. __________________//////////////////
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor2 = event.sensor;
        i++;
        if (sensor2.getType() == Sensor.TYPE_STEP_COUNTER) {
            /*
            A step counter event contains the total number of steps since the listener
            was first registered. We need to keep track of this initial value to calculate the
            number of steps taken, as the first value a listener receives is undefined.
            */
            startCount(sensor2);
            if (counterSteps < 1) {
                // initial value
                counterSteps = (int) event.values[0];
            }

            // Calculate steps taken based on first counter value received.
            numSteps = preSteps + (int) event.values[0] - counterSteps;
            switch (pref.getInt("activity_status", 0)) {
                case 5:
                    isRunning = false;
                    isStair = true;
                    break;
                case 8:
                    isRunning = true;
                    isStair = false;
                    break;
                case 7:
                    isRunning = false;
                    isStair = false;
                    break;
            }
            if (isRunning) {
                runningCount++;
            } else if (isStair) {
                stairCount++;
            }

            steps = numSteps + "";
            // steps_count.setText(steps);
            // lblDistance.setText(getDistanceRun(numSteps) + "");
            distance = getDistanceRun(numSteps) + "";
            if (!TextUtils.isEmpty(session.getWeight()))
                calories = (Utils.caloriesBurnedForWalking(Float.parseFloat(session.getWeight()), numSteps) + "");

            int walk = numSteps - (runningCount + stairCount);
            walkingSteps = walk;


        }
    }

    public float getDistanceRun(long steps) {
        float distance = (float) (steps * 78) / (float) 100000;
        return distance;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // _ Manage notification. _
    private void showNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Activity Tracker");
        notificationBuilder.setContentText("Tracker running in the background");
        notificationBuilder.setSmallIcon(R.drawable.fitness);
        notificationBuilder.setColor(Color.parseColor("#FFFF8800"));
        int colorLED = Color.argb(255, 0, 255, 0);
        notificationBuilder.setLights(colorLED, 500, 500);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setOngoing(true);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

    private void dismissNotification() {
        notificationManager.cancel(0);
    }


    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!serviceStopped) { // Only allow the repeating timer while service is running
                // (once service is stopped the flag state will change and the code inside the conditional statement here will not execute).
                // Call the method that broadcasts the data to the Activity..
                broadcastSensorValue();
                // Call "handler.postDelayed" again, after a specified delay.
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void broadcastSensorValue() {
        saveTracking();
    }

    private void saveTracking() {
        MyStepModel stepModel = new MyStepModel();
        if (!DBHelper.getInstance(this).getTodayTracking()) {
            distance = "0";
            steps = "0";
            calories = "0.0";
            walkingSteps = 0;
            runningCount = 0;
            stairCount = 0;
            numSteps = 0;
            stepModel.setDistance(Float.parseFloat(distance));
            stepModel.setSteps(Long.parseLong(steps));
            stepModel.setCaloriesBurned(Double.parseDouble(calories));
            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis();
            int totalWalkTime = walkingSteps + (int)(endTime - startTime);
            walkTime = totalWalkTime / 133;
            stepModel.setWalking(walkTime);
            int totalRunTime = runningCount + (int)(endTime - startTime);
            runTime = totalRunTime / 178;
            stepModel.setRunning(runTime);
            int totalStairsTime = stairCount + (int)(endTime - startTime);
            stairsTime = totalStairsTime / 90;
            stepModel.setStairs(stairsTime);
            stepModel.setStatus(0);
            DBHelper.getInstance(this).insertTrackingNew(stepModel);
        } else {
            stepModel.setDistance(Float.parseFloat(distance));
            stepModel.setSteps(Long.parseLong(steps));
            stepModel.setCaloriesBurned(Double.parseDouble(calories));
            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis();
            int totalWalkTime = walkingSteps + (int)(endTime - startTime);
            walkTime = totalWalkTime / 133;
            stepModel.setWalking(walkTime);
            int totalRunTime = runningCount + (int)(endTime - startTime);
            runTime = totalRunTime / 178;
            stepModel.setRunning(runTime);
            int totalStairsTime = stairCount + (int)(endTime - startTime);
            stairsTime = totalStairsTime / 90;
            stepModel.setStairs(stairsTime);
            stepModel.setStatus(0);
            DBHelper.getInstance(this).updateTrackingNew(stepModel);
        }
        intent.putExtra("step", stepModel.getSteps() + "");
        intent.putExtra("distance", Utils.round(stepModel.getDistance(), 2) + "");
        intent.putExtra("calories", Utils.roundDouble(stepModel.getCaloriesBurned(), 2) + "");
        intent.putExtra("walk", String.valueOf(stepModel.getWalking()));
        intent.putExtra("running", String.valueOf(stepModel.getRunning()));
        intent.putExtra("stair", String.valueOf(stepModel.getStairs()));
        intent.putExtra("status", String.valueOf(stepModel.getStatus()));
        sendBroadcast(intent);
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        String steps = numSteps + "";
        intent.putExtra("step", steps);
        intent.putExtra("distance", getDistanceRun(numSteps) + "");
        intent.putExtra("calories", Utils.caloriesBurnedForWalking(Float.parseFloat(session.getWeight()), numSteps) + "");
        sendBroadcast(intent);

    }

}