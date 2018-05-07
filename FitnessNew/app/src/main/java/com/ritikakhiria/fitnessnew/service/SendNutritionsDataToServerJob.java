package com.ritikakhiria.fitnessnew.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.NutritionModel;
import com.ritikakhiria.fitnessnew.response.MyResponseNutrition;
import com.ritikakhiria.fitnessnew.retrofit.ApiInterface;
import com.ritikakhiria.fitnessnew.retrofit.AppRetrofitManager;
import com.ritikakhiria.fitnessnew.retrofit.NetworkException;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class SendNutritionsDataToServerJob extends Job {
    public static final String TAG = SendNutritionsDataToServerJob.class.getCanonicalName();
    String muserId;

    public SendNutritionsDataToServerJob(String userId) {
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
        final List<NutritionModel> unSentMsg = DBHelper.getInstance(getApplicationContext()).getUnsyncNutrition();
        if (unSentMsg != null && unSentMsg.size() > 0) {
            for (int i = 0; i < unSentMsg.size(); i++) {
                final Map<String, String> params = new HashMap();
                params.put("id", muserId);
                params.put("save_date", unSentMsg.get(i).getDate() + "");
                params.put("fat", unSentMsg.get(i).getFat() + "");
                params.put("carbs", unSentMsg.get(i).getCarb() + "");
                params.put("protein", unSentMsg.get(i).getProtein() + "");
                params.put("fiber", unSentMsg.get(i).getFiber() + "");
                params.put("suger", unSentMsg.get(i).getSugar() + "");
                params.put("calory", unSentMsg.get(i).getCalories() + "");
                //params.put("timestemp", unSentMsg.get(i).getDate() + "");
                params.put("typeoffood", unSentMsg.get(i).getTypeOfFood() + "");
                // params.put("month", unSentMsg.get(i).getMonth() + "");
                Call<MyResponseNutrition> request = service.saveNutrition(params);
                MyResponseNutrition product = AppRetrofitManager.performRequest(request);
                if (product != null && product.getResponse() == 200) {
                    DBHelper.getInstance(getApplicationContext()).updateNutritionStatus(1, unSentMsg.get(i).getDate());

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