package com.ritikakhiria.fitnessnew.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritikakhiria.fitnessnew.model.NutritionModel;

import java.util.ArrayList;
import java.util.List;

public class MyResponseNutrition {
    @SerializedName("response")
    @Expose
    private Integer response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private ArrayList<NutritionModel> result1;

    public ArrayList<NutritionModel> getResult1() {
        return result1;
    }

    public void setResult1(ArrayList<NutritionModel> result1) {
        this.result1 = result1;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

