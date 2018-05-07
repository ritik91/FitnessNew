package com.ritikakhiria.fitnessnew.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.adapter.NutritionReportTabsPagerAdapter;

public class NutritionChartActivity extends AppCompatActivity {

    TabLayout topTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_nutrition);

        topTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        topTabLayout.addTab(topTabLayout.newTab().setText("WEEKLY"));
        topTabLayout.addTab(topTabLayout.newTab().setText("MONTHLY"));
        topTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final NutritionReportTabsPagerAdapter adapter = new NutritionReportTabsPagerAdapter(getSupportFragmentManager(), topTabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(topTabLayout));
        topTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //onTabSelected
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //onTabUnselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //onTabReselected
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivityForResult(new Intent(NutritionChartActivity.this, AddStaticFoodData.class), 1000);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}