package com.ritikakhiria.fitnessnew.model;

public class HistoryModel {
    private int _id;
    private int steps;
    private int month;
    private Long date;
    private Float distance;
    private Float caloriesBurned;
    private int walking, running, stairs;
    private String saveDate;
    private int status;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(Float caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getWalking() {
        return walking;
    }

    public void setWalking(int walking) {
        this.walking = walking;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }

    public int getStairs() {
        return stairs;
    }

    public void setStairs(int stairs) {
        this.stairs = stairs;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }
}
