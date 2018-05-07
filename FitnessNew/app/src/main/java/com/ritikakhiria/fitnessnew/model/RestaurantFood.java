package com.ritikakhiria.fitnessnew.model;


import java.io.Serializable;

public class RestaurantFood implements Serializable{

    private Integer id;
    private String restaurantFoodName;
    private String restaurantBrandName;
    private String servingQuanity;
    private String restaurantFoodCalories;
    private String restaurantFoodNixItemId;
    private String restaurantFoodImage;

    public RestaurantFood() {
    }

    public RestaurantFood(Integer id, String restaurantFoodName, String restaurantBrandName,
                          String servingQuanity, String restaurantFoodCalories, String restaurantFoodNixItemId, String restaurantFoodImage) {
        this.id = id;
        this.restaurantFoodName = restaurantFoodName;
        this.restaurantBrandName = restaurantBrandName;
        this.servingQuanity = servingQuanity;
        this.restaurantFoodCalories = restaurantFoodCalories;
        this.restaurantFoodNixItemId = restaurantFoodNixItemId;
        this.restaurantFoodImage = restaurantFoodImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestaurantFoodName() {
        return restaurantFoodName;
    }

    public void setRestaurantFoodName(String restaurantFoodName) {
        this.restaurantFoodName = restaurantFoodName;
    }

    public String getRestaurantBrandName() {
        return restaurantBrandName;
    }

    public void setRestaurantBrandName(String restaurantBrandName) {
        this.restaurantBrandName = restaurantBrandName;
    }

    public String getServingQuanity() {
        return servingQuanity;
    }

    public void setServingQuanity(String servingQuanity) {
        this.servingQuanity = servingQuanity;
    }

    public String getRestaurantFoodCalories() {
        return restaurantFoodCalories;
    }

    public void setRestaurantFoodCalories(String restaurantFoodCalories) {
        this.restaurantFoodCalories = restaurantFoodCalories;
    }

    public String getRestaurantFoodNixItemId() {
        return restaurantFoodNixItemId;
    }

    public void setRestaurantFoodNixItemId(String restaurantFoodNixItemId) {
        this.restaurantFoodNixItemId = restaurantFoodNixItemId;
    }

    public String getRestaurantFoodImage() {
        return restaurantFoodImage;
    }

    public void setRestaurantFoodImage(String restaurantFoodImage) {
        this.restaurantFoodImage = restaurantFoodImage;
    }
}
