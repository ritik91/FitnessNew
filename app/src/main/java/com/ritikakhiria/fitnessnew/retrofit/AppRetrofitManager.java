package com.ritikakhiria.fitnessnew.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ritikakhiria.fitnessnew.Constant.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppRetrofitManager {

    private static Retrofit mRetrofit;

    private static ApiInterface apiInterface;

    public static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();

            apiInterface = mRetrofit.create(ApiInterface.class);
        }
        return mRetrofit;
    }

    public static <T> T performRequest(Call<T> request) throws IOException, NetworkException {
        retrofit2.Response<T> response = request.execute();

        if (response == null || !response.isSuccessful() || response.errorBody() != null) {
            throw new NetworkException(response);
        }

        return response.body();
    }

    public static ApiInterface getApiInterface() {
        if (apiInterface == null) {
            getRetrofit();
        }
        return apiInterface;
    }


}