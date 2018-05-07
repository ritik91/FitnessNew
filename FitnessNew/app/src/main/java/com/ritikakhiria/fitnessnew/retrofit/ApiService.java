package com.ritikakhiria.fitnessnew.retrofit;

import com.ritikakhiria.fitnessnew.response.ApiResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * List of web services are being used
 */
public interface ApiService {
    @Multipart
    @POST("upload.php")
    Call<ApiResponse> updateProfile(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part photo);

    @POST("traking.php")
    Call<ApiResponse> sendTracking(@FieldMap Map<String, String> params);

    @GET("traking.php")
    Call<ApiResponse> getTracking(@FieldMap Map<String, String> params);


    @POST("nutrition.php")
    Call<ApiResponse> sendNutrition(@FieldMap Map<String, String> params);

    @GET("nutrition.php")
    Call<ApiResponse> getNutrition(@FieldMap Map<String, String> params);
}
