package com.ritikakhiria.fitnessnew.model;

import com.google.gson.Gson;

import com.google.gson.annotations.SerializedName;

public class NutritionModel {
    @SerializedName("id")
    private Integer id;
    @SerializedName("typeoffood")
    private Integer typeOfFood;
    @SerializedName("save_date")
    private String date;
    @SerializedName("calory")
    private Integer calories;
    @SerializedName("fat")
    private Float fat;
    @SerializedName("suger")
    private Float sugar;
    @SerializedName("carbs")
    private Float carb;
    @SerializedName("fiber")
    private Float fiber;
    @SerializedName("protein")
    private Float protein;
    @SerializedName("timestemp")
    private String timestamp;
    private int status;
    private int prevCal;

    public int getPrevCal() {
        return prevCal;
    }

    public void setPrevCal(int prevCal) {
        this.prevCal = prevCal;
    }

    public Float getSugar() {
        return sugar;
    }

    public void setSugar(Float sugar) {
        this.sugar = sugar;
    }

    public Float getCarb() {
        return carb;
    }

    public void setCarb(Float carb) {
        this.carb = carb;
    }

    public Float getFiber() {
        return fiber;
    }

    public void setFiber(Float fiber) {
        this.fiber = fiber;
    }

    public Float getProtein() {
        return protein;
    }

    public void setProtein(Float protein) {
        this.protein = protein;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Float getFat() {
        return fat;
    }

    public void setFat(Float fat) {
        this.fat = fat;
    }

    public Integer getTypeOfFood() {
        return typeOfFood;
    }

    public void setTypeOfFood(Integer typeOfFood) {
        this.typeOfFood = typeOfFood;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

