package com.ritikakhiria.fitnessnew.Utils;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.model.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ActivityRecognizedService extends IntentService {

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    private int wakingSteps = 0;
    private int runningSteps = 0;
    private int stairsSteps = 0;
    private SharedPreferences pref;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("ActivityRecogition", "onHandleIntent ");
        if (ActivityRecognitionResult.hasResult(intent)) {
            Log.e("ActivityRecogition", "hasResult");
            pref = getSharedPreferences(Common.STEP_OF_DAY, MODE_PRIVATE);
            pref.edit().remove("stair_steps").remove("running_steps").remove("walking_steps").apply();
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        Log.e("ActivityRecogition", "handleDetectedActivities");
        SharedPreferences.Editor editor = pref.edit();
        for (DetectedActivity activity : probableActivities) {
            switch (activity.getType()) {
                case DetectedActivity.TILTING: {
                    stairsSteps++;
                    Log.e("ActivityRecogition", "In Stairs: " + activity.getConfidence());
                    editor.putInt("activity_status", DetectedActivity.TILTING);
                    editor.putInt(Common.STAIR_STEPS, stairsSteps);
                    editor.apply();
                    notify(DetectedActivity.TILTING);
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e("ActivityRecogition", "Walking: " + activity.getConfidence());
                    wakingSteps++;
                    editor.putInt("activity_status", DetectedActivity.WALKING);
                    editor.putInt(Common.WALKING_STEPS, wakingSteps);
                    editor.commit();
                    notify(DetectedActivity.WALKING);
                    break;
                }
                case DetectedActivity.RUNNING: {
                    runningSteps++;
                    Log.e("ActivityRecogition", "Running: " + activity.getConfidence());
                    editor.putInt("activity_status", DetectedActivity.RUNNING);
                    editor.putInt(Common.RUNNING_STEPS, runningSteps);
                    editor.commit();
                    notify(DetectedActivity.RUNNING);
                    break;
                }
            }
        }
    }

    private void notify(int activity) {
        EventBus.getDefault().post(new ActivityEvent(activity));
    }
}
