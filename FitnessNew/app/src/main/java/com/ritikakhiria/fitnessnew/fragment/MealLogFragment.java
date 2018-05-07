package com.ritikakhiria.fitnessnew.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.HistoryModel;
import com.ritikakhiria.fitnessnew.model.NutritionModel;
import com.ritikakhiria.fitnessnew.widget.StatefulRecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class MealLogFragment extends Fragment {

    public StatefulRecyclerView recyclerView;
    public TextView txerror;
    public List<NutritionModel> nutritionModels;
    public NutritionAdapter nutritionAdapter = null;

    private String tag = MealLogFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_log_history, container, false);

        recyclerView = (StatefulRecyclerView) view.findViewById(R.id.recycle_list);
        txerror = (TextView) view.findViewById(R.id.txerror);

        nutritionModels = DBHelper.getInstance(this.getActivity()).getAllNutrition();
        if (nutritionModels == null) {
            nutritionModels = new ArrayList<>();
        } else {
            txerror.setVisibility(View.GONE);
        }

        nutritionAdapter = new NutritionAdapter(this.getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(nutritionAdapter);

        return view;
    }

    public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.MyViewHolder> {

        private Context context;

        public NutritionAdapter(Context context) {
            super();
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTimestamp, tvFoodType, tvCalories, tvProtein, tvCarbs, tvFat, tvSugar;

            public MyViewHolder(View view) {
                super(view);
                tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);
                tvFoodType = (TextView) view.findViewById(R.id.tvFoodType);
                tvCalories = (TextView) view.findViewById(R.id.tvCalories);
                tvProtein = (TextView) view.findViewById(R.id.tv_protein_value);
                tvFat = (TextView) view.findViewById(R.id.tv_fat_value);
                tvCarbs = (TextView) view.findViewById(R.id.tv_carbs);
//                tvSugar = (TextView) view.findViewById(R.id.tv_sugar_value);
            }
        }

        /* Inflating the view to show the Activity Tracker history data. */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row_meal_log, parent, false);

            return new MyViewHolder(itemView);
        }

        /* Populating the Activity Tracker data on the View of RecycleView */
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            NutritionModel nutritionModel = nutritionModels.get(position);
            Log.d(tag, "Calories have burned : " + nutritionModel.getCalories());
            holder.tvTimestamp.setText(nutritionModel.getDate());
            switch (nutritionModel.getTypeOfFood()) {
                case 1:
                    holder.tvFoodType.setText("Breakfast");
                    break;
                case 2:
                    holder.tvFoodType.setText("Lunch");
                    break;
                case 3:
                    holder.tvFoodType.setText("Dinner");
                    break;
            }

            holder.tvCalories.setText(Utils.round(nutritionModel.getCalories(), 2) + " Kcal");
            holder.tvProtein.setText(Utils.round(nutritionModel.getProtein(), 2) + " g");
            holder.tvFat.setText(Utils.round(nutritionModel.getFat(), 2) + " g");
            holder.tvCarbs.setText(Utils.round(nutritionModel.getCarb(), 2) + " g");
//            holder.tvSugar.setText(Utils.round(nutritionModel.getSugar(), 2) + " g");
        }

        @Override
        public int getItemCount() {
            return nutritionModels.size();
        }
    }

}
