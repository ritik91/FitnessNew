package com.ritikakhiria.fitnessnew.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritikakhiria.fitnessnew.model.MyStepModel;

import java.util.ArrayList;
import java.util.List;

public class MyResponse {
    @SerializedName("response")
    @Expose
    private Integer response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private ArrayList<MyStepModel> result;


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

    public ArrayList<MyStepModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<MyStepModel> result) {
        this.result = result;
    }

}

