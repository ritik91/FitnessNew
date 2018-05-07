package com.ritikakhiria.fitnessnew.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.HistoryModel;
import com.ritikakhiria.fitnessnew.response.MyResponse;
import com.ritikakhiria.fitnessnew.retrofit.ApiInterface;
import com.ritikakhiria.fitnessnew.retrofit.AppRetrofitManager;
import com.ritikakhiria.fitnessnew.retrofit.NetworkException;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class SendTrackingDataToServerJob extends Job {
    public static final String TAG = SendTrackingDataToServerJob.class.getCanonicalName();
    String muserId;

    public SendTrackingDataToServerJob(String userId) {
        super(new Params(100)
                .requireNetwork()
                .groupBy(TAG)
                .addTags(TAG).persist()
        );
        muserId = userId;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        ApiInterface service = AppRetrofitManager.getApiInterface();
        final List<HistoryModel> unSentMsg = DBHelper.getInstance(getApplicationContext()).getUnSyncedTracking();
        if (unSentMsg != null && unSentMsg.size() > 0) {
            for (int i = 0; i < unSentMsg.size(); i++) {
                final Map<String, String> params = new HashMap();
                params.put("id", muserId);
                params.put("save_date", unSentMsg.get(i).getSaveDate() + "");
                params.put("distance", unSentMsg.get(i).getDistance() + "");
                params.put("calories", unSentMsg.get(i).getCaloriesBurned() + "");
                params.put("steps", unSentMsg.get(i).getSteps() + "");
                params.put("timestemp", unSentMsg.get(i).getDate() + "");
                params.put("data", "");
                params.put("walking", unSentMsg.get(i).getWalking() + "");
                params.put("running", unSentMsg.get(i).getRunning() + "");
                params.put("stairs", unSentMsg.get(i).getStairs() + "");
                params.put("month", unSentMsg.get(i).getMonth() + "");
                Call<MyResponse> request = service.saveTracking(params);
                MyResponse product = AppRetrofitManager.performRequest(request);
                if (product != null && product.getResponse() == 200) {
                    DBHelper.getInstance(getApplicationContext()).updateTrackingStatus(1, unSentMsg.get(i).getSaveDate());
                }
                Log.d("TAG SEND DATA", product.getResponse().toString());
            }
        }

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable instanceof NetworkException) {

            NetworkException error = (NetworkException) throwable;

            int statusCode = error.getResponse().raw().code();

            if (statusCode >= 400 && statusCode < 500) {
                return RetryConstraint.CANCEL;
            }
        } else if (throwable instanceof JSONException) {
            return RetryConstraint.CANCEL;
        }

        return RetryConstraint.RETRY;
    }
}
