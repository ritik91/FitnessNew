//package com.ritikakhiria.fitnessnew.activity;
//
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.res.Resources;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.ActivityRecognition;
//import com.google.android.gms.location.DetectedActivity;
//import com.ritikakhiria.fitnessnew.R;
//import com.ritikakhiria.fitnessnew.Utils.ActivityRecognizedService;
//import com.ritikakhiria.fitnessnew.Utils.Common;
//import com.ritikakhiria.fitnessnew.Utils.Session;
//import com.ritikakhiria.fitnessnew.Utils.Utils;
//import com.ritikakhiria.fitnessnew.db.DBHelper;
//import com.ritikakhiria.fitnessnew.model.ActivityEvent;
//import com.ritikakhiria.fitnessnew.model.MyStepModel;
//import com.ritikakhiria.fitnessnew.stesp.StepDetector;
//import com.ritikakhiria.fitnessnew.stesp.StepListener;
//import com.txusballesteros.widgets.FitChart;
//import com.txusballesteros.widgets.FitChartValue;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class MyStepsPrev extends AppCompatActivity implements SensorEventListener,
//        StepListener, GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener {
//
//    private Button start_stop;
//    private TextView steps_count, lblDistance, lblActivity, lblCaloriesBurned;
//    private TextView lblWalkingCount, lblRunningCount, lblStairCount;
//    public FitChart mfitChart;
//    private StepDetector simpleStepDetector;
//    private SensorManager sensorManager;
//    private Sensor accel;
//    private static final String TEXT_NUM_STEPS = " STEPS";//:
//    private static final String TEXT_TOTAL_STEPS = " TOTAL STEPS : ";
//    public static  int numSteps, totalsteps = 0;
//    private Session session;
//    private ListView listSteps;
//    private MyStepModel stepModel;
//    public GoogleApiClient mApiClient;
//    private int i = 0;
//    private String tag = MyStepsPrev.class.getSimpleName();
//    private int counterSteps;
//    private int st;
//    private float runningCount = 0, stairCount = 0;
//    int totalStairsTime, totalRunningTime, totalWalkingTime;
//    private boolean isRunning, isStair;
//    private String TAG = MyStepsPrev.class.getSimpleName();
//    private int walkingSteps;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mystep2);
//        mApiClient = new GoogleApiClient.Builder(this)
//                .addApi(ActivityRecognition.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        mApiClient.connect();
//        mfitChart = (FitChart)findViewById(R.id.fitChart);
//        mfitChart.setMinValue(0f);
//        mfitChart.setMaxValue(100f);
//        init();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }
//
//    private void init() {
//        session = Session.getSession(MyStepsPrev.this);
//        synchronized (this) {
//            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//            simpleStepDetector = new StepDetector();
//            simpleStepDetector.registerListener(this);
//        }
//
//        lblDistance = (TextView) findViewById(R.id.lblDistance);
//        lblActivity = (TextView) findViewById(R.id.lblActivity);
//        lblCaloriesBurned = (TextView) findViewById(R.id.lblCaloriesBurned);
//
//        lblWalkingCount = (TextView) findViewById(R.id.textWalking);
//        lblRunningCount = (TextView) findViewById(R.id.textRunning);
//        lblStairCount = (TextView) findViewById(R.id.textStairs);
//
//        steps_count = (TextView) findViewById(R.id.steps_count);
//        start_stop = (Button) findViewById(R.id.start_stop);
//        start_stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Sensor countSensor =
//                        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//                if(countSensor!=null) {
//                    if (start_stop.getText().toString().equalsIgnoreCase("START")) {
//                        start_stop.setText("STOP");
//                        startCount(countSensor);
//                        startActivity();
//                    } else {
//                        start_stop.setText("START");
//                        stopActivity();
//                        saveTracking();
//                        showActivityData();
//                        reset();
//                    }
//                    Resources resources = getResources();
//                    Collection<FitChartValue> values = new ArrayList<>();
//                    values.add(new FitChartValue(30f, resources.getColor(R.color.chart_value_1)));
//                    values.add(new FitChartValue(25f, resources.getColor(R.color.chart_value_2)));
//                    values.add(new FitChartValue(20f, resources.getColor(R.color.chart_value_3)));
//                    values.add(new FitChartValue(15f, resources.getColor(R.color.chart_value_4)));
//                    values.add(new FitChartValue(10f, resources.getColor(R.color.chart_value_5)));
//                    mfitChart.setValues(values);
//                }else{
//                    Toast.makeText(MyStepsPrev.this,"Sensor not found",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    private void startCount(Sensor countSensor) {
//        numSteps = 0;
//
//        Log.d(TAG,"Step counter is :"+countSensor);
//        sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        Sensor sensor2 = event.sensor;
//        i++;
//        if (sensor2.getType() == Sensor.TYPE_STEP_COUNTER) {
//            /*
//            A step counter event contains the total number of steps since the listener
//            was first registered. We need to keep track of this initial value to calculate the
//            number of steps taken, as the first value a listener receives is undefined.
//            */
//            if (counterSteps < 1) {
//                // initial value
//                counterSteps = (int) event.values[0];
//            }
//
//            // Calculate steps taken based on first counter value received.
//            numSteps = (int) event.values[0] - counterSteps;
//
//            if (isStair) {
//                stairCount = (float) (stairCount + 0.5);
//                long startTime = System.currentTimeMillis();
//                long endTime = System.currentTimeMillis();
//                stairCount = stairCount + (endTime - startTime);
//                totalStairsTime = (int) (stairCount/360);
////                runningCount++;
//            } else if (isRunning) {
//                runningCount = (float) (runningCount + 0.3);
//                long startTime = System.currentTimeMillis();
//                long endTime = System.currentTimeMillis();
//                runningCount = runningCount + (endTime - startTime);
//                totalRunningTime = (int) (runningCount/360);
////                stairCount++;
//            }
//
//            // time for running, walking and stairs dec 2
////            if (sensor2.getType() == Sensor.TYPE_ACCELEROMETER) {
////                float x = event.values[0];
////                float y = event.values[1];
////                float z = event.values[2];
////                time_breakdown(x, y, z);
////            }
//
//            Log.d(tag, "steps : " + numSteps);
//            String steps = "" + numSteps + TEXT_NUM_STEPS;
//            steps_count.setText(steps);
//            lblDistance.setText(getDistanceRun(numSteps) + "");
//            if (!TextUtils.isEmpty(session.getWeight()))
//                lblCaloriesBurned.setText(Utils.caloriesBurnedForWalking(Float.parseFloat(session.getWeight()), numSteps) + "");
//
//            int walk = numSteps - ((int)runningCount) - ((int)stairCount);
//            totalWalkingTime = walk/360;
//            walkingSteps = walk;
//            lblWalkingCount.setText(String.valueOf(totalWalkingTime));
//            lblRunningCount.setText(String.valueOf(totalRunningTime));
//            lblStairCount.setText(String.valueOf(totalStairsTime));
//
//        }
//
//    }
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
//
//    @Override
//    public void step(long timeNs) {
//        numSteps++;
//        String steps = "" + numSteps + TEXT_NUM_STEPS;
//        steps_count.setText(steps);
//        lblDistance.setText(getDistanceRun(numSteps) + "");
//        lblCaloriesBurned.setText(Utils.caloriesBurnedForWalking(Float.parseFloat(session.getWeight()), numSteps) + "");
//    }
//
//    public float getDistanceRun(long steps) {
//        float distance = (float) (steps * 78) / (float) 100000;
//        return distance;
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        start_stop.setEnabled(true);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    private void startActivity() {
//        Intent intent = new Intent(this, ActivityRecognizedService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 1000, pendingIntent);
//    }
//
//    private void stopActivity() {
//        Intent intent = new Intent(this, ActivityRecognizedService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mApiClient, pendingIntent);
//    }
//
//    private void showActivityData() {
//        SharedPreferences pref = getSharedPreferences(Common.STEP_OF_DAY, MODE_PRIVATE);
//        int stairs = pref.getInt(Common.STAIR_STEPS, 0);
//        int running = pref.getInt(Common.RUNNING_STEPS, 0);
//        int walking = pref.getInt(Common.WALKING_STEPS, 0);
//
//        Log.d("STEPS -- ", "STAIR_STEPS : " + stairs + "\nRUNNING_STEPS : " + running + "\nWALKING_STEPS : " + walking);
//    }
//
//    private void saveTracking() {
//        unregisterListeners();
//        stepModel = new MyStepModel();
//        stepModel.setDate(System.currentTimeMillis());
//        if (!TextUtils.isEmpty(lblDistance.getText().toString()))
//            stepModel.setDistance(Float.parseFloat(lblDistance.getText().toString()));
//        stepModel.setSteps(numSteps);
//        if (!TextUtils.isEmpty(lblCaloriesBurned.getText().toString()))
//            stepModel.setCaloriesBurned(Double.parseDouble(lblCaloriesBurned.getText().toString()));
//        Log.d(tag, "Walking final steps : " + walkingSteps);
////        stepModel.setWalking(walkingSteps);
////        stepModel.setRunning(runningCount);
////        stepModel.setStairs(stairCount);
//
//        stepModel.setWalking(totalWalkingTime);
//        stepModel.setRunning(totalRunningTime);
//        stepModel.setStairs(totalStairsTime);
//
//
//        DBHelper.getInstance(this).saveTracking(stepModel);
//    }
//
//    private void reset() {
//        finish();
//        startActivity(getIntent());
//        Intent intent = new Intent(MyStepsPrev.this, UserLogActivity.class);
//        startActivity(intent);
//    }
//
//    /**
//     * Unregisters the sensor listener if it is registered.
//     */
//    private void unregisterListeners() {
//        sensorManager.unregisterListener(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ActivityEvent activityEvent) {
//        switch (activityEvent.getActivity()) {
//            case DetectedActivity.WALKING:
//                isRunning = false;
//                isStair = false;
//                lblActivity.setText("WALKING");
//                break;
//            case DetectedActivity.TILTING:
//                isRunning = false;
//                isStair = true;
//                lblActivity.setText("TILTING");
//                break;
//            case DetectedActivity.RUNNING:
//                isRunning = true;
//                isStair = false;
//                lblActivity.setText("RUNNING");
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        stopActivity();
//    }
//}