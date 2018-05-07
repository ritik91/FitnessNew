package com.ritikakhiria.fitnessnew.retrofit;

import com.ritikakhiria.fitnessnew.response.MyResponse;
import com.ritikakhiria.fitnessnew.response.MyResponseNutrition;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("traking.php")
    Call<MyResponse> getTracking(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("traking.php")
    Call<MyResponse> saveTracking(@FieldMap Map<String, String> params);

    @GET("nutrition.php")
    Call<MyResponseNutrition> getNutrition(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("nutrition.php")
    Call<MyResponseNutrition> saveNutrition(@FieldMap Map<String, String> params);
}
