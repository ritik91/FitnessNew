package com.ritikakhiria.fitnessnew.Utils;

import android.content.Context;

public class Session {
    private static Session instance = null;
    Context context;

    public static Session getSession(Context context) {
        if (instance == null) {
            instance = new Session(context);
        }
        return instance;
    }

    public static void ClearSession(Context context) {
        Utils.ClearShared(context);
    }

    protected Session(Context context) {
        this.context = context;
    }

    public String getUserId() {
        return Utils.getShared(context, Utils.userId, "");
    }

    public void setUserId(String userId) {
        Utils.setShared(context, Utils.userId, userId);
    }

    public String getUsername() {
        return Utils.getShared(context, Utils.username, "");
    }

    public void setUsername(String username) {
        Utils.setShared(context, Utils.username, username);
    }

    public String getContact() {
        return Utils.getShared(context, Utils.contact, "");
    }

    public void setContact(String contact) {
        Utils.setShared(context, Utils.contact, contact);
    }


    public String getAge() {
        return Utils.getShared(context, Utils.age, "");
    }

    public void setAge(String age) {
        Utils.setShared(context, Utils.age, age);
    }


    public String getHeight() {
        return Utils.getShared(context, Utils.height, "");
    }

    public void setHeight(String height) {
        Utils.setShared(context, Utils.height, height);
    }

    public String getWeight() {
        return Utils.getShared(context, Utils.weight, "");
    }

    public void setWeight(String weight) {
        Utils.setShared(context, Utils.weight, weight);
    }


    public String getGender() {
        return Utils.getShared(context, Utils.gender, "");
    }

    public void setGender(String gender) {
        Utils.setShared(context, Utils.gender, gender);
    }


    public String getAllergy() {
        return Utils.getShared(context, Utils.allergy, "");
    }

    public void setAllergy(String allergy) {
        Utils.setShared(context, Utils.allergy, allergy);
    }

    public String getActivityLevel() {
        return Utils.getShared(context, Utils.activityLevel, "");
    }

    public void setActivityLevel(String activityLevel) {
        Utils.setShared(context, Utils.activityLevel, activityLevel);
    }

    public String getFoodHabit() {
        return Utils.getShared(context, Utils.foodHabit, "");
    }

    public void setFoodHabit(String foodHabit) {
        Utils.setShared(context, Utils.foodHabit, foodHabit);
    }

    public String getUserEmail() {
        return Utils.getShared(context, Utils.userEmail, "");
    }

    public void setUserEmail(String userEmail) {
        Utils.setShared(context, Utils.userEmail, userEmail);
    }

    public String getUserProfileUrl() {
        return Utils.getShared(context, Utils.userProfileUrl, "");
    }

    public void setUserProfileUrl(String userProfileUrl) {
        Utils.setShared(context, Utils.userProfileUrl, userProfileUrl);
    }
    public String getUserProfile() {
        return Utils.getShared(context, Utils.userProfile, "");
    }

    public void setUserProfile(String userProfile) {
        Utils.setShared(context, Utils.userProfile, userProfile);
    }

//    public String getDiet() {
//        return Utils.getShared(context,Utils.diet,"");
//    }
//
//    public void setDiet(String diet) {
//        Utils.setShared(context,Utils.diet,diet);
//    }

//    private String breakfastDiet = "";
//    private String lunchDiet = "";
//    private String dinnerDiet = "";

    public String getBreakfastDiet() {
        return Utils.getShared(context, Utils.breakfast, "");
    }

    public void setBreakfastDiet(String breakfastDiet) {
        Utils.setShared(context, Utils.breakfast, breakfastDiet);
    }

    public String getLunchDiet() {
        return Utils.getShared(context, Utils.lunch, "");
    }

    public void setLunchDiet(String lunchDiet) {
        Utils.setShared(context, Utils.lunch, lunchDiet);
    }

    public String getDinnerDiet() {
        return Utils.getShared(context, Utils.dinner, "");
    }

    public void setDinnerDiet(String dinnerDiet) {
        Utils.setShared(context, Utils.dinner, dinnerDiet);
    }
}
