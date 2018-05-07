package com.ritikakhiria.fitnessnew.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.ritikakhiria.fitnessnew.Interfaces.Communicator;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.activity.DietType;
import com.ritikakhiria.fitnessnew.activity.MySteps;
import com.ritikakhiria.fitnessnew.activity.Profile;
import com.ritikakhiria.fitnessnew.adapter.dietAdapter;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.EditModel;
import com.ritikakhiria.fitnessnew.model.Foods;
import com.ritikakhiria.fitnessnew.model.Nutrients;
import com.ritikakhiria.fitnessnew.model.NutritionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ritikakhiria.fitnessnew.Utils.Utils.foodTypeId;
import static com.ritikakhiria.fitnessnew.Utils.Utils.getCalorieShared;
import static com.ritikakhiria.fitnessnew.Utils.Utils.getLeftCalorie;
import static com.ritikakhiria.fitnessnew.Utils.Utils.isFloat;
//import static com.ritikakhiria.fitnessnew.activity.MySteps.numSteps;
import static com.ritikakhiria.fitnessnew.service.StepCountingService.numSteps;
import static java.lang.Boolean.getBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class MydietFragment extends Fragment {

    private String tag = MydietFragment.class.getSimpleName();

    private Session session;
    ArrayList<Foods> myFoods_B, myFoods_D, myFoods_L;
    String foodData = "";
    dietAdapter dietAdapterBreakfast, dietAdapterLunch, dietAdapterDinner;
    RecyclerView brekfastRV, lunchRV, dinnerRV;
    private Communicator comm;
    int TotalCalories = 0, goalCalorie, totalLeftCal = 0;
    int exerciseCalorie, leftCaloriesValue;
    TextView Calories, goal, totalCalorie, protein, fat, carbs, exercise, leftCalories;
    private int particularFoodCalories = 0;
    private int totalCaloriesOfLunch = 0;
    private int totalCaloriesOfDinner = 0;
    static int typeOfFood;
    static String takeFoodYesORNo = null;
    static String takeFoodDate = null;
    NutritionModel nutritionModel;
    private int foodCalorie;

    private ArrayList<HashMap<String, String>> mlist;

    public MydietFragment() {
    }

    private Context context;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final MydietFragment newInstance(String message,int type, String yesOrNo, String date) {
        typeOfFood = type;
        takeFoodYesORNo = yesOrNo;
        takeFoodDate = date;
        Log.d("MydietFragment","Notification date : "+date);

        MydietFragment f = new MydietFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        comm = (Communicator) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v3 = inflater.inflate(R.layout.fragment_my_diet, container, false);

        Calories = (TextView) v3.findViewById(R.id.calorieView);
        goal = (TextView) v3.findViewById(R.id.goalText);
        exercise = (TextView) v3.findViewById(R.id.exerciseText);
        leftCalories = (TextView) v3.findViewById(R.id.leftText);
        totalCalorie = (TextView) v3.findViewById(R.id.totalCalorieText);
        protein = (TextView) v3.findViewById(R.id.proteinText);
        fat = (TextView) v3.findViewById(R.id.fatText);
        carbs = (TextView) v3.findViewById(R.id.carbsText);

        session = Session.getSession(getActivity());
        mlist = new ArrayList<>(DBHelper.getInstance(getActivity()).getTodayBurnedCalories());

        ImageView break_fast = (ImageView) v3.findViewById(R.id.breakfast_Add);
        break_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startDiet(Utils.breakfast);
                foodTypeId = 1;
                comm.respond(1);
                ((DietType) getActivity()).viewPager.setCurrentItem(1, true);
            }

        });

        ImageView lunch = (ImageView) v3.findViewById(R.id.lunchADD);
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startDiet(Utils.lunch);
                foodTypeId = 2;
                comm.respond(2);
                ((DietType) getActivity()).viewPager.setCurrentItem(1, true);
            }

        });

        ImageView dinner = (ImageView) v3.findViewById(R.id.dinner_Add);
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startDiet(Utils.dinner);
                foodTypeId = 3;
                comm.respond(3);
                ((DietType) getActivity()).viewPager.setCurrentItem(1, true);
            }
        });

        brekfastRV = (RecyclerView) v3.findViewById(R.id.breakfastRV);
        lunchRV = (RecyclerView) v3.findViewById(R.id.lunchRV);
        dinnerRV = (RecyclerView) v3.findViewById(R.id.dinnerRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        brekfastRV.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        lunchRV.setLayoutManager(linearLayoutManager2);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(context);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        dinnerRV.setLayoutManager(linearLayoutManager3);

        Utils.ClearCalorieShared(context, "Calorie", "0");
        nutritionModel = new NutritionModel();
        nutritionModel.setTypeOfFood(typeOfFood);
        init();
        return v3;
    }

    private void init() {
        session = Session.getSession(context);

        myFoods_B = new ArrayList<>();
        myFoods_D = new ArrayList<>();
        myFoods_L = new ArrayList<>();

        setBreakfastView();
        setLunchView();
        setDinnerView();

        String prev = Utils.getCalorieShared(context, "Calorie", "0");
        Log.d(tag,"nutritionModel :"+nutritionModel.getId());
        Calories.setText(prev);

        foodCalorie = Integer.parseInt(Utils.getCalorieShared(context, "Calorie", "0"));
//        Log.d(tag, "food calories: "+foodCalorie);
//        int left = goalCalorie - foodCalorie + exerciseCalorie;
//        leftCalories.setText(String.valueOf(left));
//        Log.d(tag,"left calories :"+leftCalories.getText().toString());

        int goalOfCal = (int) Utils.calculateCaloriesRequired(String.valueOf(session.getGender()), String.valueOf(session.getActivityLevel()),
                Float.parseFloat(session.getHeight()), Float.parseFloat(session.getWeight()), Integer.parseInt(session.getAge()));
        goal.setText(String.valueOf(goalOfCal));
        totalCalorie.setText(String.valueOf( (int) Utils.calculateCaloriesRequired(String.valueOf(session.getGender()), String.valueOf(session.getActivityLevel()),
                Float.parseFloat(session.getHeight()), Float.parseFloat(session.getWeight()), Integer.parseInt(session.getAge()))) + " Kcal");
        protein.setText(String.valueOf((float) Math.round(Utils.getProtein(Float.parseFloat(session.getWeight())))) + " g");
        fat.setText(String.valueOf((float) Math.round(Utils.getFat())) + " g");
        carbs.setText(String.valueOf((float) Math.round(Utils.getCarbs())) + " g");

        exerciseCalorie = (int) Utils.caloriesBurnedForWalking(Float.parseFloat(session.getWeight()), numSteps);
        exercise.setText(String.valueOf(exerciseCalorie));

        goalCalorie = (int) Utils.calculateCaloriesRequired(String.valueOf(session.getGender()), String.valueOf(session.getActivityLevel()),
                Float.parseFloat(session.getHeight()), Float.parseFloat(session.getWeight()), Integer.parseInt(session.getAge()));


        leftCaloriesValue = Utils.getLeftCalorie(context, "Calorie", "0", Float.parseFloat(session.getWeight()), numSteps);
        leftCalories.setText(String.valueOf(leftCaloriesValue));


        if("yes".equals(takeFoodYesORNo)){
            DBHelper.getInstance(getActivity()).saveNutrition(nutritionModel);
        }


    }

//    public int getLeftCalorie() {
//        int left;
//        left = goalCalorie - foodCalorie + exerciseCalorie;
//        Log.d(tag, "left calories, goal, food, exercise: "+left + goalCalorie + foodCalorie + exerciseCalorie);
//        return left;
//    }

    public void setBreakfastView() {
        foodData = session.getBreakfastDiet();
        Log.d(tag, "Food Data : " + foodData);
        myFoods_B.clear();
        try {
            if (!foodData.trim().equals("")) {

                JSONArray jarr = new JSONArray(foodData);
                for (int i = 0; i < jarr.length(); i++) {
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

                        if (typeOfFood == 1) {
                            saveNutrition(Integer.valueOf(nutrient_id), isFloat(gm));
                        }
                       /* Log.d(tag,"is float  :"+isFloat(value)+"\n Real value : "+value);
                            saveNutrition(Integer.valueOf(nutrient_id),isFloat(value));*/
                        if (_nutrient.equals("Energy")) {

                            String prev = Utils.getCalorieShared(context, "Calorie", "0");
                            TotalCalories = parseInt(prev) + parseInt(value);
                            if (typeOfFood == 1) {
                                particularFoodCalories += parseInt(value);
                                nutritionModel.setCalories(particularFoodCalories);
                                nutritionModel.setDate(takeFoodDate);
                                Log.d(tag, "Total calories Break fast : " + particularFoodCalories);
                            }
                            Utils.setCalorieShared(context, "Calorie", String.valueOf(TotalCalories));

                        }

                        nutrients[j] = nutrient;
                    }
                    foods.setNutrients(nutrients);

                    myFoods_B.add(foods);
                }

                if (myFoods_B != null && myFoods_B.size() > 0) {
                    dietAdapterBreakfast = new dietAdapter(context, myFoods_B, Utils.breakfast, Calories, Utils.image);
                    brekfastRV.setAdapter(dietAdapterBreakfast);


                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setLunchView() {
        foodData = session.getLunchDiet();
        myFoods_L.clear();
        try {
            if (!foodData.trim().equals("")) {

                JSONArray jarr = new JSONArray(foodData);
                for (int i = 0; i < jarr.length(); i++) {
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

//                    JSONObject jObjImg = jobj.getJSONObject("photo");
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
                        if (typeOfFood == 2) {
                            saveNutrition(Integer.valueOf(nutrient_id), isFloat(gm));
                        }
                        if (_nutrient.equals("Energy")) {

                            String prev = Utils.getCalorieShared(context, "Calorie", "0");
                            TotalCalories = parseInt(prev) + parseInt(value);
                            if (typeOfFood == 2) {
                                particularFoodCalories += parseInt(value);
                                nutritionModel.setDate(takeFoodDate);
                                Log.d(tag, "Total calories Break fast : " + particularFoodCalories);

                            }
                            Utils.setCalorieShared(context, "Calorie", String.valueOf(TotalCalories));
                        }

                        nutrients[j] = nutrient;
                    }
                    foods.setNutrients(nutrients);

                    myFoods_L.add(foods);
                }

                if (myFoods_L != null && myFoods_L.size() > 0) {
                    dietAdapterLunch = new dietAdapter(context, myFoods_L, Utils.lunch, Calories, Utils.image);
                    lunchRV.setAdapter(dietAdapterLunch);

                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setDinnerView() {
        foodData = session.getDinnerDiet();
        myFoods_D.clear();
        try {
            if (!foodData.trim().equals("")) {

                JSONArray jarr = new JSONArray(foodData);
                for (int i = 0; i < jarr.length(); i++) {
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

//                    JSONObject jObjImg = jobj.getJSONObject("photo");
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
                        if (typeOfFood == 3) {
                            saveNutrition(Integer.valueOf(nutrient_id), isFloat(gm));
                        }
                        if (_nutrient.equals("Energy")) {

                            String prev = Utils.getCalorieShared(context, "Calorie", "0");
                            TotalCalories = parseInt(prev) + parseInt(value);
                            if (typeOfFood == 3) {
                                particularFoodCalories += parseInt(value);
                                nutritionModel.setDate(takeFoodDate);
                                Log.d(tag, "Total calories Break fast : " + particularFoodCalories);
                            }
                            Utils.setCalorieShared(context, "Calorie", String.valueOf(TotalCalories));
                        }

                        nutrients[j] = nutrient;
                    }
                    foods.setNutrients(nutrients);

                    myFoods_D.add(foods);
                }

                if (myFoods_D != null && myFoods_D.size() > 0) {
                    dietAdapterDinner = new dietAdapter(context, myFoods_D, Utils.dinner, Calories, Utils.image);
                    dinnerRV.setAdapter(dietAdapterDinner);

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void saveNutrition(int nutritionId, float value) {
        float lastValue = 0;
        switch (nutritionId) {
            case 203: // 203 means Protein
                if (nutritionModel.getProtein() != null) {
                    lastValue = nutritionModel.getProtein();
                }
                nutritionModel.setProtein(lastValue + value);
                break;
            case 204: //204 means Fat
                if (nutritionModel.getFat() != null) {
                    lastValue = nutritionModel.getFat();
                }
                nutritionModel.setFat(lastValue + value);
                break;
            case 205: //205 means Carbohydrate
                if (nutritionModel.getCarb() != null) {
                    lastValue = nutritionModel.getCarb();
                }
                nutritionModel.setCarb(lastValue + value);
                break;
            case 208: //208 means Calories
                if (nutritionModel.getCalories() != null) {
                    lastValue = nutritionModel.getCalories();
                }
                nutritionModel.setCalories((int) (lastValue + value));
                break;
            case 269: //269 means Sugar
                if (nutritionModel.getSugar() != null) {
                    lastValue = nutritionModel.getSugar();
                }
                nutritionModel.setSugar(lastValue + value);
                break;
            case 291: // 291 means fiber
                if (nutritionModel.getFiber() != null) {
                    lastValue = nutritionModel.getFiber();
                }
                nutritionModel.setFiber(lastValue + value);
                break;
        }
    }


}

