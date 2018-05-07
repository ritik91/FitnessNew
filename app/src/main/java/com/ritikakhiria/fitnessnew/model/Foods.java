package com.ritikakhiria.fitnessnew.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Foods
{
//    private ArrayList<Nutrients> nutrients;

    private Nutrients[] nutrients;

    private String weight;

    private String measure;

    private String name;

    private String ndbno;

    private String image;

//    public ArrayList<Nutrients> getNutrients() {
//        return nutrients;
//    }
//
//    public void setNutrients(ArrayList<Nutrients> nutrients) {
//        this.nutrients = nutrients;
//    }

    public Nutrients[] getNutrients ()
    {
        return nutrients;
    }

    public void setNutrients (Nutrients[] nutrients)
    {
        this.nutrients = nutrients;
    }

    public String getWeight ()
    {
        return weight;
    }

    public void setWeight (String weight)
    {
        this.weight = weight;
    }

    public String getMeasure ()
    {
        return measure;
    }

    public void setMeasure (String measure)
    {
        this.measure = measure;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getNdbno ()
    {
        return ndbno;
    }

    public void setNdbno (String ndbno)
    {
        this.ndbno = ndbno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [nutrients = "+nutrients+", weight = "+weight+", measure = "+measure+", name = "+name+", ndbno = "+ndbno+", image = "+image+"]";
    }
}


