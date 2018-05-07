package com.ritikakhiria.fitnessnew.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.ritikakhiria.fitnessnew.Interfaces.Communicator;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.model.Foods;
import com.ritikakhiria.fitnessnew.model.MealSchedulerModel;
import com.ritikakhiria.fitnessnew.model.Nutrients;

public class DietChart extends AppCompatActivity {

    private String tag = DietChart.class.getSimpleName();

    Session session;
    ArrayList<Foods> myFoods;
    String foodData = "";
    String from = "";
    private boolean isAddedProduct = false;
    private Bundle mbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_chart);
//        init();
        Utils.ClearCalorieShared(getApplicationContext(), "Calorie", "0");

        int getValueFrom = 1;//Set here a value from last activity OR Fragment for breakfast, launch and dinner
        mbundle = new Bundle();
        mbundle = getIntent().getExtras();

        if (mbundle != null) {
            getValueFrom = mbundle.getInt("typeOfFoodID");

            if (getValueFrom == 1) {
                from = Utils.breakfast;
                synchronized (this) {
                    init();
                }

            } else if (getValueFrom == 2) {
                from = Utils.lunch;
                synchronized (this) {
                    init();
                }

            } else if (getValueFrom == 3) {
                from = Utils.dinner;
                synchronized (this) {
                    init();
                }
            }
        }
    }

    private void init() {
        session = Session.getSession(DietChart.this);
        myFoods = new ArrayList<>();

//        String from = getIntent().getExtras().getString("from");
        if (from.equalsIgnoreCase(Utils.breakfast)) {
            foodData = session.getBreakfastDiet();
        } else if (from.equalsIgnoreCase(Utils.lunch)) {
            foodData = session.getLunchDiet();
        } else {
            foodData = session.getDinnerDiet();
        }

        try {
            JSONArray jarr = new JSONArray(foodData);
            for (int i = 0; i < jarr.length(); i++) {
                Log.d(tag, "value of i : " + i);
                JSONObject jobj = jarr.getJSONObject(i);
                Foods foods = new Foods();

                String measure = jobj.getString("measure");
                foods.setMeasure(measure);

                String name = jobj.getString("name");
                foods.setName(name);

                String ndbno = jobj.getString("ndbno");
                foods.setNdbno(ndbno);

                String weight = jobj.getString("weight");
                foods.setWeight(weight);

                foods.setImage(jobj.getString("image"));

                JSONArray innerJar = jobj.getJSONArray("nutrients");
                Nutrients[] nutrients = new Nutrients[innerJar.length()];
                for (int j = 0; j < innerJar.length(); j++) {
                    JSONObject innerJob = innerJar.getJSONObject(j);
                    Nutrients nutrient = new Nutrients();

                    String gm = innerJob.getString("gm");
                    nutrient.setGm(gm);

                    String _nutrient = innerJob.getString("nutrient");
                    nutrient.setNutrient(_nutrient);

                    String nutrient_id = innerJob.getString("nutrient_id");
                    nutrient.setNutrient_id(nutrient_id);

                    String unit = innerJob.getString("unit");
                    nutrient.setUnit(unit);

                    String value = innerJob.getString("value");
                    nutrient.setValue(value);

                    nutrients[j] = nutrient;
                }
                foods.setNutrients(nutrients);

                myFoods.add(foods);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    private String getString(ArrayList<Foods> obj) {
//        Gson gson = new Gson();
//        return gson.toJson(obj);
//    }

//    private void populateViews(){
//        if(myFoods.size()>0){
//            LinearLayout item = (LinearLayout)findViewById(R.id.main_time_table);
//            for (int i=0;i<myFoods.size();i++){
//
//                Foods model = myFoods.get(i);
//
//                View child = getLayoutInflater().inflate(R.layout.day_layout, null);
//                TextView day_name = (TextView)child.findViewById(R.id.day_name);
//                day_name.setText("Name "+model.getName());
//
//                TextView weight = (TextView)child.findViewById(R.id.weight);
//                weight.setText("Weight "+model.getWeight());
//
//                TextView measure = (TextView)child.findViewById(R.id.measure);
//                measure.setText("Measure "+model.getMeasure());
//
//                CheckBox check_food = (CheckBox)child.findViewById(R.id.check_food);
//                check_food.setVisibility(View.GONE);
//
//                LinearLayout main_day_table = (LinearLayout)child.findViewById(R.id.main_day_table);
//
//                Nutrients[] innerModels = model.getNutrients();
//                for (int j=0;j<innerModels.length;j++){
//                    Nutrients innerModel = innerModels[j];
//                    View inner_child = getLayoutInflater().inflate(R.layout.time_row, null);
//
//                    TextView nutrient_id = (TextView)inner_child.findViewById(R.id.nutrient_id);
//                    nutrient_id.setText(""+innerModel.getNutrient_id());
//
//                    TextView nutrient = (TextView)inner_child.findViewById(R.id.nutrient);
//                    nutrient.setText(""+innerModel.getNutrient());
//
//                    TextView unit = (TextView)inner_child.findViewById(R.id.unit);
//                    unit.setText(""+innerModel.getUnit());
//
//                    TextView value = (TextView)inner_child.findViewById(R.id.value);
//                    value.setText(""+innerModel.getValue());
//
//                    TextView gm = (TextView)inner_child.findViewById(R.id.gm);
//                    gm.setText(""+innerModel.getGm());
//
//                    main_day_table.addView(inner_child);
//                }
//
//                item.addView(child);
//            }
//        }
//
//    }
}
