package com.ritikakhiria.fitnessnew.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ritikakhiria.fitnessnew.Interfaces.Communicator;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.fragment.AddFoodsFragment;
import com.ritikakhiria.fitnessnew.fragment.BarcodeFragment;
import com.ritikakhiria.fitnessnew.fragment.MydietFragment;

import static com.ritikakhiria.fitnessnew.Utils.Utils.foodTypeId;

public class DietType extends AppCompatActivity implements Communicator {

    String TAG = DietType.class.getSimpleName();

    Button break_fast, lunch, dinner;
    public CustomViewPager viewPager;
    public TabLayout tabLayout;
    public String dataforADDFood = "0";
    public boolean isRemoved = false;
    public int AfterRemoved = 0;
    private int type;
    private String yesOrNO;
    private String date;
    private String tag = DietType.class.getSimpleName();

    @Override
    public void respond(int data) {
        dataforADDFood = String.valueOf(data);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_type_scheduler);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        if(intent!=null){
            type = intent.getIntExtra("type",0);
            yesOrNO = intent.getStringExtra("yesOrNo");
            date = intent.getStringExtra("date");
            Log.d(TAG,"type"+type);
            Log.d(TAG,"yesOrNO"+yesOrNO);
        }

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutMealPlanner);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.disableScroll(true);//Disable swipe view pager

    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[] { "My Meal", "Add Food", "Barcode" };

        public SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MydietFragment().newInstance(null, type,yesOrNO,date);
                //return new AddFoodsFragment();
            } else if (position == 1){
                return new AddFoodsFragment();
            } else {
                return new BarcodeFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"BarCode 0    : ");
        super.onActivityResult(requestCode, resultCode, data);
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"BarCode 1: "+result);
        if (result != null) {
            // String Barcode
            if(result.getContents()!=null){
                Intent intent = new Intent(this,BarcodeAndSearchRecipeActivity.class);
                intent.putExtra("fromFragment","barcode");
                intent.putExtra("typeOfFoodID",  foodTypeId);
                intent.putExtra("barcode",result.getContents());
                Log.d(tag,"Food Type Id : "+foodTypeId+"\n From Fragment : barcode"+"\n Barcode : "+result.getContents());
                //0030000060834
                startActivity(intent);
            }

            Log.d(TAG,"BarCode : "+result.getContents());
        }

    }

}
