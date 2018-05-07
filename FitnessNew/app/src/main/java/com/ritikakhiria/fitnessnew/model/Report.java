package com.ritikakhiria.fitnessnew.model;

public class Report
{
    private String total;

    private String subset;

    private Foods[] foods;

    private String start;

    private String sr;

    private String end;

    private String groups;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public String getSubset ()
    {
        return subset;
    }

    public void setSubset (String subset)
    {
        this.subset = subset;
    }

    public Foods[] getFoods ()
    {
        return foods;
    }

    public void setFoods (Foods[] foods)
    {
        this.foods = foods;
    }

    public String getStart ()
    {
        return start;
    }

    public void setStart (String start)
    {
        this.start = start;
    }

    public String getSr ()
    {
        return sr;
    }

    public void setSr (String sr)
    {
        this.sr = sr;
    }

    public String getEnd ()
    {
        return end;
    }

    public void setEnd (String end)
    {
        this.end = end;
    }

    public String getGroups ()
    {
        return groups;
    }

    public void setGroups (String groups)
    {
        this.groups = groups;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total = "+total+", subset = "+subset+", foods = "+foods+", start = "+start+", sr = "+sr+", end = "+end+", groups = "+groups+"]";
    }
}