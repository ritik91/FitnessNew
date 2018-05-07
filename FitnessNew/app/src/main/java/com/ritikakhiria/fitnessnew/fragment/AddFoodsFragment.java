package com.ritikakhiria.fitnessnew.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ritikakhiria.fitnessnew.R;

public class AddFoodsFragment extends Fragment{

    public static final String TAG = AddFoodsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_food_fragment, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_addFood);
        viewPager.setOffscreenPageLimit(4);

        // Create an adapter that knows which fragment should be shown on each page
        AddFoodFragmentPagerAdapter adapter = new AddFoodFragmentPagerAdapter(getChildFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_add_food);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public class AddFoodFragmentPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[] { "Food", "Recipe", "Restaurant", "Barcode" };

        public AddFoodFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FragmentTabFood();
            } else if (position == 1){
                return new FragmentNewTabRecipe();
//                return new SearchRecipeFragment();
            } else if(position == 2){
                return new FragmentTabRestaurant();
            }else {
                return new BarcodeFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }
}
