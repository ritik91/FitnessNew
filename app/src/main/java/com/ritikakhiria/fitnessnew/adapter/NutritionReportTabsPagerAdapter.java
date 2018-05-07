package com.ritikakhiria.fitnessnew.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ritikakhiria.fitnessnew.fragment.NutritionMonthlyReportFragment;
import com.ritikakhiria.fitnessnew.fragment.NutritionWeeklyReportFragment;

public class NutritionReportTabsPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public NutritionReportTabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NutritionWeeklyReportFragment tab1 = new NutritionWeeklyReportFragment(); // for 1st tab
                return tab1;
            case 1:
                NutritionMonthlyReportFragment tab2 = new NutritionMonthlyReportFragment(); // for 2nd tab
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}