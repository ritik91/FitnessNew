package com.ritikakhiria.fitnessnew.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.model.RestaurantFood;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.CustomViewHolder> {

    String TAG = RestaurantAdapter.class.getSimpleName();

    private List<RestaurantFood> mRestaurantFoodList;
    private Context mContext;
    private final View.OnClickListener mClick;
    RestaurantFood restaurantFood;

    public RestaurantAdapter(Context context, List<RestaurantFood> restaurantFoodsList, View.OnClickListener mClick){
        this.mRestaurantFoodList = restaurantFoodsList;
        this.mContext = context;
        this.mClick = mClick;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_food_list, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder viewHolder, final int position){

        restaurantFood = mRestaurantFoodList.get(position);

        Glide.with(mContext).load(restaurantFood.getRestaurantFoodImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
//                .placeholder(R.drawable.image_not_available)
                .skipMemoryCache(true)
                .into(viewHolder.restaurantImageView);

        viewHolder.restaurantFoodName.setText(restaurantFood.getRestaurantFoodName());
        viewHolder.restaurantFoodBrand.setText(restaurantFood.getRestaurantBrandName());
        viewHolder.restaurantServingQty.setText(restaurantFood.getServingQuanity());
        viewHolder.restaurantFoodCalories.setText(restaurantFood.getRestaurantFoodCalories());
        viewHolder.particularProductRow.setOnClickListener(mClick);
        viewHolder.particularProductRow.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != mRestaurantFoodList ? mRestaurantFoodList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView restaurantImageView;
        protected TextView restaurantFoodName, restaurantFoodBrand, restaurantFoodCalories;
        protected TextView restaurantServingQty;
//        protected ImageButton restaurantFoodAdd;
        protected LinearLayout particularProductRow;
//        private Context mContext;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.restaurantImageView = (ImageView) itemView.findViewById(R.id.restaurantFoodImage);
            this.restaurantFoodName = (TextView) itemView.findViewById(R.id.restaurantFoodName);
            this.restaurantFoodBrand = (TextView) itemView.findViewById(R.id.restaurantBrandName);
            this.restaurantServingQty = (TextView) itemView.findViewById(R.id.restaurantFoodServingQty);
            this.restaurantFoodCalories = (TextView) itemView.findViewById(R.id.restaurantFoodCalorie);
//            this.restaurantFoodAdd = (ImageButton) itemView.findViewById(R.id.restaurantAddButton);
            this.particularProductRow = (LinearLayout) itemView.findViewById(R.id.ll_particular_product_row);
        }
    }


}

