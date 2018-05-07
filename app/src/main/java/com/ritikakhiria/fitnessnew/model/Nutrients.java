package com.ritikakhiria.fitnessnew.model;

public class Nutrients
{
    private String gm;

    private String unit;

    private String nutrient;

    private String nutrient_id;

    private String value;

    public String getGm ()
    {
        return gm;
    }

    public void setGm (String gm)
    {
        this.gm = gm;
    }

    public String getUnit ()
    {
        return unit;
    }

    public void setUnit (String unit)
    {
        this.unit = unit;
    }

    public String getNutrient ()
    {
        return nutrient;
    }

    public void setNutrient (String nutrient)
    {
        this.nutrient = nutrient;
    }

    public String getNutrient_id ()
    {
        return nutrient_id;
    }

    public void setNutrient_id (String nutrient_id)
    {
        this.nutrient_id = nutrient_id;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gm = "+gm+", unit = "+unit+", nutrient = "+nutrient+", nutrient_id = "+nutrient_id+", value = "+value+"]";
    }
}
