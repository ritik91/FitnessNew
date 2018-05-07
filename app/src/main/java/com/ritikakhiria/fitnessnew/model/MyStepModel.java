package com.ritikakhiria.fitnessnew.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class MyStepModel {
    @SerializedName("id")
    private long _id;
    @SerializedName("steps")
    private long steps;
    @SerializedName("date")
    private long date;
    @SerializedName("distance")
    private float distance;

    @SerializedName("walking")
    private float walking;
    @SerializedName("running")
    private float running;
    @SerializedName("stairs")
    private float stairs;

    @SerializedName("calories")
    private double caloriesBurned;
    @SerializedName("save_date")
    private String saveDate;
    @SerializedName("status")
    private int status;
    @SerializedName("month")
    private String month;

    @SerializedName("timestemp")
    private String timestamp;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public long getSteps() {
        return this.steps;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getWalking() {
        return walking;
    }

    public void setWalking(float walking) {
        this.walking = walking;
    }

    public float getRunning() {
        return running;
    }

    public void setRunning(float running) {
        this.running = running;
    }

    public float getStairs() {
        return stairs;
    }

    public void setStairs(float stairs) {
        this.stairs = stairs;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String toJson() {
        String json = null;
        Gson gson = new Gson();
        json = gson.toJson(this, MyStepModel.class);
        return json;
    }

    public static MyStepModel getObject(String json) {
        MyStepModel stepModel = null;
        Gson gson = new Gson();
        stepModel = gson.fromJson(json, MyStepModel.class);
        return stepModel;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }
}
