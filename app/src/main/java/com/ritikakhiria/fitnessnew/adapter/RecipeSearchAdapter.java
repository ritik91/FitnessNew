//package com.ritikakhiria.fitnessnew.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.ritikakhiria.fitnessnew.R;
//import com.ritikakhiria.fitnessnew.model.RecipeModel;
//
//import java.util.List;
//
///**
// * Created by root on 10/10/17.
// */
//
//public class RecipeSearchAdapter extends RecyclerView.Adapter<RecipeSearchAdapter.RecipeViewHolder> {
//    private List<RecipeModel> recipeItemList;
//    private Context mContext;
//    private final View.OnClickListener mClick;
//    String TAG = RecipeSearchAdapter.class.getSimpleName();
//    RecipeModel recipeItem;
//
//    public RecipeSearchAdapter(Context context, List<RecipeModel> recipeItemList, View.OnClickListener mClick) {
//        this.recipeItemList = recipeItemList;
//        this.mContext = context;
//        this.mClick = mClick;
//    }
//
//    @Override
//    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.list_search_recipe, null, false);
//        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(lp);
//        return new RecipeViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(final RecipeViewHolder customViewHolder, final int position) {
//        recipeItem = recipeItemList.get(position);
//
//        Glide.with(mContext).load(recipeItem.getRecipeFoodImage())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()
//                //.override(100, 100) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
//                .skipMemoryCache(true)
//                .into(customViewHolder.recipeImage);
//        customViewHolder.label.setText(recipeItem.getLabel());
//        customViewHolder.source.setText(recipeItem.getSource());
//        customViewHolder.servingQty.setText(recipeItem.getServingQuanity());
//        customViewHolder.calories.setText(recipeItem.getCalories());
//        customViewHolder.particularProductRow.setOnClickListener(mClick);
//        customViewHolder.particularProductRow.setTag(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return (null != recipeItemList ? recipeItemList.size() : 0);
//    }
//
//    class RecipeViewHolder extends RecyclerView.ViewHolder {
//        protected ImageView recipeImage;
//        protected TextView label;
//        protected TextView source;
//        protected TextView servingQty;
//        protected TextView calories;
//        protected LinearLayout particularProductRow;
//
//        public RecipeViewHolder(View view) {
//            super(view);
//            this.recipeImage = (ImageView) view.findViewById(R.id.img_productImage);
//            this.label = (TextView) view.findViewById(R.id.tv_food_name);
//            this.source = (TextView) view.findViewById(R.id.tv_brand_name);
//            this.servingQty= (TextView) view.findViewById(R.id.tv_serving_quantity);
//            this.calories= (TextView) view.findViewById(R.id.tv_calories);
//            this.particularProductRow = (LinearLayout) view.findViewById(R.id.ll_particular_product_row);
//
//        }
//    }
//
//}
