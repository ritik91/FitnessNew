package com.ritikakhiria.fitnessnew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.model.RecipeModel;

import java.util.List;

/* Adapter class for recipe */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    String TAG = RecipeAdapter.class.getSimpleName();

    private List<RecipeModel> recipeItemList;
    private Context mContext;
    private final View.OnClickListener mClick;

    RecipeModel recipeItem;

    public RecipeAdapter(Context context, List<RecipeModel> recipeItemList, View.OnClickListener mClick) {
        this.recipeItemList = recipeItemList;
        this.mContext = context;
        this.mClick = mClick;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_food_list, null, false);
        RecyclerView.LayoutParams Lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(Lp);
        return new RecipeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecipeViewHolder customViewHolder, final int position) {
        recipeItem = recipeItemList.get(position);

        Glide.with(mContext).load(recipeItem.getRecipeFoodImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                //.override(100, 100) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                .skipMemoryCache(true)
                .into(customViewHolder.recipeImage);
        customViewHolder.label.setText(recipeItem.getRecipeFoodName());
//        customViewHolder.source.setText(recipeItem.getSource());
        customViewHolder.servingQty.setText(recipeItem.getServingQuanity());
        customViewHolder.servingUnits.setText(recipeItem.getServingUnits());
//        customViewHolder.calories.setText(recipeItem.getCalories());
        customViewHolder.particularProductRow.setOnClickListener(mClick);
        customViewHolder.particularProductRow.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != recipeItemList ? recipeItemList.size() : 0);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImage;
        private TextView label;
        private TextView servingQty;
        private TextView servingUnits;
//        private TextView calories;
        private LinearLayout particularProductRow;

        public RecipeViewHolder(View view) {
            super(view);
            this.recipeImage = (ImageView) view.findViewById(R.id.recipeFoodImage);
            this.label = (TextView) view.findViewById(R.id.recipeFoodName);
//            this.source = (TextView) view.findViewById(R.id.recipeBrandName);
            this.servingQty= (TextView) view.findViewById(R.id.recipeFoodServingQty);
            this.servingUnits= (TextView) view.findViewById(R.id.recipeFoodServingUnit);
//            this.calories= (TextView) view.findViewById(R.id.recipeFoodCalorie);
            this.particularProductRow = (LinearLayout) view.findViewById(R.id.ll_particular_product_row_recipe);

        }
    }

}