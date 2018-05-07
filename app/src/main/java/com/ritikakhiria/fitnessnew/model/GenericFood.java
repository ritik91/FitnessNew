package com.ritikakhiria.fitnessnew.model;


public class GenericFood {

    private String genericFoodName;
    private String genericFoodSubTitle;
    private String servingQuanity;
    private String genericFoodNixItemId;
    private String genericFoodCal;
    private String genericFoodProtein;
    private String genericFoodFat;
    private String genericFoodCrabs;
    private String genericFoodImage;

    public GenericFood() {
    }

    public GenericFood(String genericFoodName, String genericFoodSubTitle, String servingQuanity, String genericFoodNixItemId,
                       String genericFoodCal, String genericFoodProtein, String genericFoodFat, String genericFoodCrabs, String genericFoodImage) {
        this.genericFoodName = genericFoodName;
        this.genericFoodSubTitle = genericFoodSubTitle;
        this.servingQuanity = servingQuanity;
        this.genericFoodNixItemId = genericFoodNixItemId;
        this.genericFoodCal = genericFoodCal;
        this.genericFoodProtein = genericFoodProtein;
        this.genericFoodFat = genericFoodFat;
        this.genericFoodCrabs = genericFoodCrabs;
        this.genericFoodImage = genericFoodImage;
    }

    public String getGenericFoodName() {
        return genericFoodName;
    }

    public void setGenericFoodName(String genericFoodName) {
        this.genericFoodName = genericFoodName;
    }

    public String getGenericFoodSubTitle() {
        return genericFoodSubTitle;
    }

    public void setGenericFoodSubTitle(String genericFoodSubTitle) {
        this.genericFoodSubTitle = genericFoodSubTitle;
    }

    public String getServingQuanity() {
        return servingQuanity;
    }

    public void setServingQuanity(String servingQuanity) {
        this.servingQuanity = servingQuanity;
    }

    public String getGenericFoodNixItemId() {
        return genericFoodNixItemId;
    }

    public void setGenericFoodNixItemId(String genericFoodNixItemId) {
        this.genericFoodNixItemId = genericFoodNixItemId;
    }

    public String getGenericFoodCal() {
        return genericFoodCal;
    }

    public void setGenericFoodCal(String genericFoodCal) {
        this.genericFoodCal = genericFoodCal;
    }

    public String getGenericFoodProtein() {
        return genericFoodProtein;
    }

    public void setGenericFoodProtein(String genericFoodProtein) {
        this.genericFoodProtein = genericFoodProtein;
    }

    public String getGenericFoodFat() {
        return genericFoodFat;
    }

    public void setGenericFoodFat(String genericFoodFat) {
        this.genericFoodFat = genericFoodFat;
    }

    public String getGenericFoodCrabs() {
        return genericFoodCrabs;
    }

    public void setGenericFoodCrabs(String genericFoodCrabs) {
        this.genericFoodCrabs = genericFoodCrabs;
    }

    public String getGenericFoodImage() {
        return genericFoodImage;
    }

    public void setGenericFoodImage(String genericFoodImage) {
        this.genericFoodImage = genericFoodImage;
    }
}
