package com.ritikakhiria.fitnessnew.activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.fragment.HistoryFragment;
import com.ritikakhiria.fitnessnew.fragment.MealLogFragment;

public class UserLogActivity extends AppCompatActivity {

    public static final String TAG = UserLogActivity.class.getSimpleName();

    public CustomViewPager viewPager;
    public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_history);
        init();
    }

    private void init() {
        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (CustomViewPager) findViewById(R.id.viewpager_history);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutLogHistory);
        tabLayout.setupWithViewPager(viewPager);

    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[] { "Activity Log", "Meal Log" };

        public SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HistoryFragment();
            }  else {
                return new MealLogFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

}
