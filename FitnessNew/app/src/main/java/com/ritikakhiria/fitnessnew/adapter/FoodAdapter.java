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
import com.ritikakhiria.fitnessnew.model.GenericFood;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.CustomViewHolder> {

    String TAG = RestaurantAdapter.class.getSimpleName();

    private List<GenericFood> genericFoodList;
    private Context mContext;
    private final View.OnClickListener mClick;
    GenericFood genericFood;

    public FoodAdapter(Context context, List<GenericFood> genericFoodsList, View.OnClickListener mClick){
        this.genericFoodList = genericFoodsList;
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

        genericFood = genericFoodList.get(position);

        Glide.with(mContext).load(genericFood.getGenericFoodImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
//                .placeholder(R.drawable.image_not_available)
                .skipMemoryCache(true)
                .into(viewHolder.genericImageView);

        viewHolder.genericFoodName.setText(genericFood.getGenericFoodName());
        viewHolder.genericFoodBrand.setText(genericFood.getGenericFoodSubTitle());
        viewHolder.genericServingQty.setText(genericFood.getGenericFoodCal());
        viewHolder.restaurantFoodCalories.setText(genericFood.getGenericFoodCal());
        viewHolder.particularProductRow.setOnClickListener(mClick);
        viewHolder.particularProductRow.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != genericFoodList ? genericFoodList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView genericImageView;
        private TextView genericFoodName, genericFoodBrand, restaurantFoodCalories;
        protected TextView genericServingQty;
        private ImageButton restaurantFoodAdd;
        private LinearLayout particularProductRow;
//        private Context mContext;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.genericImageView = (ImageView) itemView.findViewById(R.id.restaurantFoodImage);
            this.genericFoodName = (TextView) itemView.findViewById(R.id.restaurantFoodName);
            this.genericFoodBrand = (TextView) itemView.findViewById(R.id.restaurantBrandName);
            this.genericServingQty = (TextView) itemView.findViewById(R.id.restaurantFoodServingQty);
            this.restaurantFoodCalories = (TextView) itemView.findViewById(R.id.restaurantFoodCalorie);
//            this.restaurantFoodAdd = (ImageButton) itemView.findViewById(R.id.restaurantAddButton);
            this.particularProductRow = (LinearLayout) itemView.findViewById(R.id.ll_particular_product_row);
        }
    }


}

