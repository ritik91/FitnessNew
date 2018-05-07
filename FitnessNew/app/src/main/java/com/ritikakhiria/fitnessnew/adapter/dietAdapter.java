package com.ritikakhiria.fitnessnew.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.activity.UserLogActivity;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.fragment.MealLogFragment;
import com.ritikakhiria.fitnessnew.model.Foods;
import com.ritikakhiria.fitnessnew.model.MealSchedulerModel;
import com.ritikakhiria.fitnessnew.model.Nutrients;
import com.ritikakhiria.fitnessnew.model.NutritionModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class dietAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Foods> myFoods;
    Session session;
    String name;
    MealSchedulerModel model;
    TextView Cal;
    String Img;
    private String TAG = dietAdapter.class.getSimpleName();
    private LayoutInflater inflater;


    public dietAdapter(Context context, ArrayList<Foods> myFoods, String name, TextView cal, String img) {
        super();
        this.context = context;
        model = new MealSchedulerModel();
        session = Session.getSession(context);
        this.myFoods = myFoods;
        this.name = name;
        this.Cal = cal;
        this.Img = img;
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < myFoods.size(); i++) {
            Log.d(TAG, "DATA OF FOODS " + myFoods.get(i).getName());
            Log.d(TAG, "DATA OF FOODS IMAGE " + myFoods.get(i).getImage());
        }
    }


    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_mydiet_chart, parent, false);
        MyHolder holder = new MyHolder(view, context);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        final Foods current = myFoods.get(position);

        myHolder.Fname.setText(current.getName());

        Glide.with(context)
                .load(current.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                //.override(100, 100) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                .skipMemoryCache(true)
//                .placeholder(R.drawable.image_not_available)
                .into(myHolder.Fimage);

        Nutrients[] innerModels = current.getNutrients();
//        ArrayList<Nutrients> innerModels = current.getNutrients();
        for (Nutrients innerModel : innerModels) {
            if (innerModel.getNutrient().equals("Energy")) {
                myHolder.Fcal.setText(innerModel.getValue());
            }
        }

        myHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myFoods != null) {
                    if (name.equalsIgnoreCase(Utils.breakfast)) {
                        Log.d(TAG, "adapter Position :" + position);
                        Log.d(TAG, "adapter food :" + current.getName());
                        Log.d(TAG, "adapter food image:" + current.getImage());

//                        for (int i = 0; i < current.getNutrients().length; i++) {
//                            Log.d(TAG, "Nutrition food :" + current.getNutrients().length);

                        long timestamp = System.currentTimeMillis();
                        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Timestamp(timestamp));
                        NutritionModel nm = new NutritionModel();
                        //nutritionModel.setId(Integer.parseInt((timestamp + "")));
                        nm.setDate(date);
                        if (!TextUtils.isEmpty(current.getNutrients()[0].getValue())) {
                            nm.setProtein(Float.parseFloat(current.getNutrients()[0].getValue()));
                        } else {
                            nm.setProtein(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[1].getValue())) {
                            nm.setFat(Float.parseFloat(current.getNutrients()[1].getValue()));
                        } else {
                            nm.setFat(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[2].getValue())) {
                            nm.setCarb(Float.parseFloat(current.getNutrients()[2].getValue()));
                        } else {
                            nm.setCarb(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[3].getValue())) {
                            nm.setCalories((int) (Double.parseDouble(current.getNutrients()[3].getValue())));
                        } else {
                            nm.setCalories(0);
                        }
//                        if (!TextUtils.isEmpty(current.getNutrients()[4].getValue())) {
//                            nm.setSugar(Float.parseFloat(current.getNutrients()[4].getValue()));
//                        } else {
//                            nm.setSugar(Float.parseFloat("0"));
//                        }
                        nm.setTypeOfFood(1);

                        DBHelper.getInstance(context).saveNutrition(nm);
                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
//                    }
                    else if (name.equalsIgnoreCase(Utils.lunch)) {
                        Log.d(TAG, "adapter Position :" + position);
                        Log.d(TAG, "adapter food :" + current.getName());

                        long timestamp = System.currentTimeMillis();
                        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Timestamp(timestamp));
                        NutritionModel nm = new NutritionModel();
                        // nutritionModel.setId(Integer.parseInt((timestamp + "")));
                        nm.setDate(date);
                        if (!TextUtils.isEmpty(current.getNutrients()[0].getValue())) {
                            nm.setProtein(Float.parseFloat(current.getNutrients()[0].getValue()));
                        } else {
                            nm.setProtein(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[1].getValue())) {
                            nm.setFat(Float.parseFloat(current.getNutrients()[1].getValue()));
                        } else {
                            nm.setFat(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[2].getValue())) {
                            nm.setCarb(Float.parseFloat(current.getNutrients()[2].getValue()));
                        } else {
                            nm.setCarb(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[3].getValue())) {
                            nm.setCalories((int) (Double.parseDouble(current.getNutrients()[3].getValue())));
                        } else {
                            nm.setCalories(0);
                        }
//                        if (!TextUtils.isEmpty(current.getNutrients()[4].getValue())) {
//                            nm.setSugar(Float.parseFloat(current.getNutrients()[4].getValue()));
//                        } else {
//                            nm.setSugar(Float.parseFloat("0"));
//                        }
                        nm.setTypeOfFood(2);

                        DBHelper.getInstance(context).saveNutrition(nm);
                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "adapter Position :" + position);
                        Log.d(TAG, "adapter food :" + current.getName());

                        long timestamp = System.currentTimeMillis();
                        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Timestamp(timestamp));
                        NutritionModel nm = new NutritionModel();
                        //  nutritionModel.setId(Integer.parseInt((timestamp + "")));
                        nm.setDate(date);
                        if (!TextUtils.isEmpty(current.getNutrients()[0].getValue())) {
                            nm.setProtein(Float.parseFloat(current.getNutrients()[0].getValue()));
                        } else {
                            nm.setProtein(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[1].getValue())) {
                            nm.setFat(Float.parseFloat(current.getNutrients()[1].getValue()));
                        } else {
                            nm.setFat(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[2].getValue())) {
                            nm.setCarb(Float.parseFloat(current.getNutrients()[2].getValue()));
                        } else {
                            nm.setCarb(Float.parseFloat("0"));
                        }
                        if (!TextUtils.isEmpty(current.getNutrients()[3].getValue())) {
                            nm.setCalories((int) (Double.parseDouble(current.getNutrients()[3].getValue())));
                        } else {
                            nm.setCalories(0);
                        }
//                        if (!TextUtils.isEmpty(current.getNutrients()[4].getValue())) {
//                            nm.setSugar(Float.parseFloat(current.getNutrients()[4].getValue()));
//                        } else {
//                            nm.setSugar(Float.parseFloat("0"));
//                        }
                        nm.setTypeOfFood(3);
                        DBHelper.getInstance(context).saveNutrition(nm);
                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return myFoods.size();
    }

    private String getString(ArrayList<Foods> obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView Fname, Fcal;
        ImageView removebtn, btnAdd, Fimage;

        // create constructor to get widget reference
        public MyHolder(View itemView, final Context context) {
            super(itemView);
            final Context context1 = context;
            Fname = (TextView) itemView.findViewById(R.id.foodname);
            Fcal = (TextView) itemView.findViewById(R.id.foodcalorie);
            Fimage = (ImageView) itemView.findViewById(R.id.foodImage);
            removebtn = (ImageView) itemView.findViewById(R.id.removebtn);
            btnAdd = (ImageView) itemView.findViewById(R.id.btn_add);
            /* Remove the saved food item from the RecycleView (Breakfast, Lunch and Dinner) */
            removebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removebtn.setEnabled(false);
                    myFoods.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), myFoods.size());
                    String prev = Utils.getCalorieShared(context, "Calorie", "0");
                    int value = Integer.parseInt(prev) - Integer.parseInt(Fcal.getText().toString());
                    Utils.setCalorieShared(context, "Calorie", String.valueOf(value));
                    String prev2 = Utils.getCalorieShared(context, "Calorie", "0");
                    Cal.setText(prev2);

                    if (myFoods != null) {
                        if (name.equalsIgnoreCase(Utils.breakfast)) {
                            session.setBreakfastDiet(getString(myFoods));
                        } else if (name.equalsIgnoreCase(Utils.lunch)) {
                            session.setLunchDiet(getString(myFoods));
                        } else {
                            session.setDinnerDiet(getString(myFoods));
                        }
                    }
                    removebtn.setEnabled(true);
                }
            });


        }
    }
}