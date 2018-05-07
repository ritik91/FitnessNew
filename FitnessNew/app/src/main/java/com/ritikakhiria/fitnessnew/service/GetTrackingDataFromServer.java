package com.ritikakhiria.fitnessnew.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.response.MyResponse;
import com.ritikakhiria.fitnessnew.retrofit.ApiInterface;
import com.ritikakhiria.fitnessnew.retrofit.AppRetrofitManager;
import com.ritikakhiria.fitnessnew.retrofit.NetworkException;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class GetTrackingDataFromServer extends Job {
    String mid;
    public static final String TAG = GetTrackingDataFromServer.class.getCanonicalName();

    public GetTrackingDataFromServer(String id) {
        super(new Params(100)
                .requireNetwork()
                .groupBy(TAG)
                .addTags(TAG).persist()
        );

        mid = id;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        ApiInterface service = AppRetrofitManager.getApiInterface();

        Map<String, String> params = new HashMap();
        params.put("id", mid);
        Call<MyResponse> request = service.getTracking(params);
        MyResponse product = AppRetrofitManager.performRequest(request);
        if (product != null && product.getResponse() == 200) {
            DBHelper.getInstance(getApplicationContext()).insertTrackingNewFromServer(product.getResult());
        } else {
            Log.d("TAG GET", product.getResponse() + "");

        }


    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.d("TAG GET", "Canceled");
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
