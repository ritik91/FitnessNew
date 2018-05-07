package com.ritikakhiria.fitnessnew.model;

import com.google.gson.annotations.SerializedName;


public class MealModel{
    @SerializedName("id")
    Integer id;
    @SerializedName("email")
    String email;
    @SerializedName("measure")
    String measure;
    @SerializedName("name")
    String name;
    @SerializedName("ndbno")
    String ndbno;
    @SerializedName("weight")
    String weight;
    @SerializedName("nutrient")
    String nutrient;
    @SerializedName("nutrient_id")
    String nutrient_id;
    @SerializedName("unit")
    String unit;
    @SerializedName("value")
    String value;
    @SerializedName("Energy")
    String Energy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNdbno() {
        return ndbno;
    }

    public void setNdbno(String ndbno) {
        this.ndbno = ndbno;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNutrient() {
        return nutrient;
    }

    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
    }

    public String getNutrient_id() {
        return nutrient_id;
    }

    public void setNutrient_id(String nutrient_id) {
        this.nutrient_id = nutrient_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEnergy() {
        return Energy;
    }

    public void setEnergy(String energy) {
        Energy = energy;
    }
    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

}