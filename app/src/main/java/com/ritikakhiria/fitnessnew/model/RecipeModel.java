package com.ritikakhiria.fitnessnew.model;

import java.io.Serializable;


public class RecipeModel implements Serializable {
    private String recipeTagID;
    private String recipeFoodName;
    private String servingQuanity;
    private String servingUnits;
//    private String calories;
    private String recipeFoodImage;
//    private String source;
//    private String fat;
//    private String Carbs;
//    private String Protein;


    public String getRecipeTagID() {
        return recipeTagID;
    }

    public void setRecipeTagID(String recipeTagID) {
        this.recipeTagID = recipeTagID;
    }

    public String getRecipeFoodName() {
        return recipeFoodName;
    }

    public void setRecipeFoodName(String recipeFoodName) {
        this.recipeFoodName = recipeFoodName;
    }

    public String getServingQuanity() {
        return servingQuanity;
    }

    public void setServingQuanity(String servingQuanity) {
        this.servingQuanity = servingQuanity;
    }

    public String getServingUnits() {
        return servingUnits;
    }

    public void setServingUnits(String servingUnits) {
        this.servingUnits = servingUnits;
    }

    //    public String getCalories() {
//        return calories;
//    }
//
//    public void setCalories(String calories) {
//        this.calories = calories;
//    }

    public String getRecipeFoodImage() {
        return recipeFoodImage;
    }

    public void setRecipeFoodImage(String recipeFoodImage) {
        this.recipeFoodImage = recipeFoodImage;
    }

//    public String getSource() {
//        return source;
//    }
//
//    public void setSource(String source) {
//        this.source = source;
//    }
//
//    public String getFat() {
//        return fat;
//    }
//
//    public void setFat(String fat) {
//        this.fat = fat;
//    }
//
//    public String getCarbs() {
//        return Carbs;
//    }
//
//    public void setCarbs(String carbs) {
//        Carbs = carbs;
//    }
//
//    public String getProtein() {
//        return Protein;
//    }
//
//    public void setProtein(String protein) {
//        Protein = protein;
//    }
}
