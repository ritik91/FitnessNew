package com.ritikakhiria.fitnessnew.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ritikakhiria.fitnessnew.fragment.LineChartMonthlyFragment;
import com.ritikakhiria.fitnessnew.fragment.LineChartWeeklyFragment;
//import com.ritikakhiria.fitnessnew.fragment.MonthlyChartFragment;
//import com.ritikakhiria.fitnessnew.fragment.WeeklyChartFragment;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public TabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LineChartWeeklyFragment tab1 = new LineChartWeeklyFragment(); // for 1st tab
                return tab1;
            case 1:
                LineChartMonthlyFragment tab2 = new LineChartMonthlyFragment(); // for 2nd tab
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