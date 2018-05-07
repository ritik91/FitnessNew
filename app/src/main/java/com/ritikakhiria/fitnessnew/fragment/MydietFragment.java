package com.ritikakhiria.fitnessnew.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ritikakhiria.fitnessnew.Interfaces.Communicator;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.activity.BreakFastBarGarph;
import com.ritikakhiria.fitnessnew.activity.DietType;
import com.ritikakhiria.fitnessnew.activity.DinnerBarGraph;
import com.ritikakhiria.fitnessnew.activity.LunchBarGraph;
import com.ritikakhiria.fitnessnew.adapter.dietAdapter;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.Foods;
import com.ritikakhiria.fitnessnew.model.Nutrients;
import com.ritikakhiria.fitnessnew.model.NutritionModel;
import com.ritikakhiria.fitnessnew.widget.StatefulRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ritikakhiria.fitnessnew.Utils.Utils.foodTypeId;
import static com.ritikakhiria.fitnessnew.Utils.Utils.isFloat;
//import static com.ritikakhiria.fitnessnew.activity.MySteps.numSteps;
import static com.ritikakhiria.fitnessnew.service.StepCountingService.numSteps;
import static java.lang.Integer.parseInt;

public class MydietFragment extends Fragment implements OnChartValueSelectedListener {

    private String tag = MydietFragment.class.getSimpleName();

    private Session session;
    ArrayList<Foods> myFoods_B, myFoods_D, myFoods_L;
    String foodData = "";
    dietAdapter dietAdapterBreakfast, dietAdapterLunch, dietAdapterDinner;
    RecyclerView brekfastRV, lunchRV, dinnerRV;
    private Communicator comm;
    int TotalCalories = 0, goalCalorie, lunchRemCal = 0, dinnerRemCal = 0;
    int exerciseCalorie, leftCaloriesValue;
    TextView Calories, goal, totalCalorie, protein, fat, carbs, exercise, leftCalories;
    TextView graphBreakfast, graphLunch, graphDinner;
    TextView breakfastCal, lunchCal, dinnerCal, intakeCal, intakeCarbs, intakeFat, intakeProtein;
    private int particularFoodCalories = 0;
    private int totalCaloriesOfLunch = 0;
    private int totalCaloriesOfDinner = 0;
    static int typeOfFood;
    static String takeFoodYesORNo = null;
    static String takeFoodDate = null;
    NutritionModel nutritionModel;
    private int foodCalorie;
    static int totalCalForADay;

    private BarChart mChart;
    private boolean grapAdded = false;
    private YAxis leftAxis;
    private int totalLeftXis;

    private StatefulRecyclerView recyclerView;
    private TextView txerror;
    private ArrayList<HashMap<String, String>> nutritionModels;
    private TotalCalHistoryAdapter mTotalCalHistoryAdapter = null;


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
        totalCalorie = (TextView) v3.findViewById(R.id.intake_cal);
        protein = (TextView) v3.findViewById(R.id.intake_protein);
        fat = (TextView) v3.findViewById(R.id.intake_fat);
        carbs = (TextView) v3.findViewById(R.id.intake_carbs);

        breakfastCal = (TextView) v3.findViewById(R.id.breakfast_calorie);
        lunchCal = (TextView) v3.findViewById(R.id.lunch_calorie);
        dinnerCal = (TextView) v3.findViewById(R.id.dinner_calorie);

        intakeCal = (TextView) v3.findViewById(R.id.totalCalorieText);
        intakeCarbs = (TextView) v3.findViewById(R.id.carbsText);
        intakeFat = (TextView) v3.findViewById(R.id.fatText);
        intakeProtein = (TextView) v3.findViewById(R.id.proteinText);

        // Total calories barchart
        mChart = (BarChart) v3.findViewById(R.id.chart1);

        // Recycler view for total intake calories per day
        recyclerView = (StatefulRecyclerView) v3.findViewById(R.id.stepsRCView);
        txerror = (TextView) v3.findViewById(R.id.txerror);

        nutritionModels = DBHelper.getInstance(this.getActivity()).totalNutritionData();
        if (nutritionModels == null) {
            nutritionModels = new ArrayList<HashMap<String, String>>();
        } else {
            txerror.setVisibility(View.GONE);
        }

        mTotalCalHistoryAdapter = new TotalCalHistoryAdapter(this.getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mTotalCalHistoryAdapter);



        // intent to graph
        graphBreakfast = (TextView) v3.findViewById(R.id.break_fast);
        graphBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodTypeId = 1;
                comm.respond(1);
                Intent breakfastIntent = new Intent(getActivity(), BreakFastBarGarph.class);
                startActivity(breakfastIntent);
            }
        });

        graphLunch = (TextView) v3.findViewById(R.id.lunch);
        graphLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodTypeId = 2;
                comm.respond(2);
                Intent lunchIntent = new Intent(getActivity(), LunchBarGraph.class);
                startActivity(lunchIntent);
            }
        });

        graphDinner = (TextView) v3.findViewById(R.id.dinner);
        graphDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodTypeId = 3;
                comm.respond(3);
                Intent dinnerIntent = new Intent(getActivity(), DinnerBarGraph.class);
                startActivity(dinnerIntent);
            }
        });



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
//        Utils.ClearCalorieShared(context,"LunchCal", "0");

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

        ArrayList<HashMap<String, String>> mlist = new ArrayList<>(DBHelper.getInstance(getActivity()).weeklyNutritionDataInfo());

        String prev = Utils.getCalorieShared(context, "Calorie", "0");
        Log.d(tag,"nutritionModel :"+nutritionModel.getId());
//        Calories.setText(prev);

        // Showing the total calories intake by the user in day

        for (int j = 0; j < 7; j++) {
            Log.d(tag, "Value of J before : " + j);
            for (int k = 0; k < mlist.size(); k++) {
                if ((j == getDay(mlist.get(k).get("date")))) {
                    totalCalForADay = Integer.valueOf(mlist.get(k).get("totalCalories"));
                    Calories.setText(mlist.get(k).get("totalCalories"));
                }
            }
        }


        foodCalorie = Integer.parseInt(Utils.getCalorieShared(context, "Calorie", "0"));
//        Log.d(tag, "food calories: "+foodCalorie);
//        int left = goalCalorie - foodCalorie + exerciseCalorie;
//        leftCalories.setText(String.valueOf(left));
//        Log.d(tag,"left calories :"+leftCalories.getText().toString());

        // Showing the total carbs, protein and fat intake by the user in day
        for (int j = 0; j < 7; j++) {
            Log.d(tag, "Value of J before : " + j);
            for (int k = 0; k < mlist.size(); k++) {
                if ((j == getDay(mlist.get(k).get("date")))) {

                    intakeCal.setText(mlist.get(k).get("totalCalories"));
                    intakeCarbs.setText(mlist.get(k).get("totalCarbs"));
                    intakeProtein.setText((mlist.get(k).get("totalProtein")));
                    intakeFat.setText((mlist.get(k).get("totalFat")));
                }
            }
        }

        int goalOfCal = (int) Utils.calculateCaloriesRequired(String.valueOf(session.getGender()), String.valueOf(session.getActivityLevel()),
                Float.parseFloat(session.getHeight()), Float.parseFloat(session.getWeight()), Integer.parseInt(session.getAge()));

        goal.setText(String.valueOf(goalOfCal));

        totalCalorie.setText(String.valueOf( (int) Utils.calculateCaloriesRequired(String.valueOf(session.getGender()), String.valueOf(session.getActivityLevel()),
                Float.parseFloat(session.getHeight()), Float.parseFloat(session.getWeight()), Integer.parseInt(session.getAge()))) + " kcal");
//        intakeCal.setText(prev);

        protein.setText(String.valueOf(Math.round(Utils.getProtein(Float.parseFloat(session.getWeight())))) + " g");
//        int totalProteinVal = (int) ( (Float.parseFloat(mlist.get(0).get("protein"))) + (Float.parseFloat(mlist.get(1).get("protein"))));
//        intakeProtein.setText(String.valueOf(totalProteinVal));

        fat.setText(String.valueOf(Math.round(Utils.getFat())) + " g");


        carbs.setText(String.valueOf(Math.round(Utils.getCarbs())) + " g");


        // Showing the total calories intake for breakfast
        ArrayList<HashMap<String, String>> mlistB = new ArrayList<>(DBHelper.getInstance(getActivity()).nutritionWeeklyDataBreakfast1());

        for (int j = 0; j < 7; j++) {
            Log.d(tag, "Value of J before : " + j);
            for (int k = 0; k < mlistB.size(); k++) {
                if ((j == getDay(mlistB.get(k).get("date")))) {

                    breakfastCal.setText(mlistB.get(k).get("totalCalories"));
                    Log.e(tag, "Breakfast time calories: " + mlistB.get(k).get("totalCalories"));
                }
            }
        }

        // Showing the total calories intake for lunch
        ArrayList<HashMap<String, String>> mlistL = new ArrayList<>(DBHelper.getInstance(getActivity()).nutritionWeeklyDataLunch1());

        for (int j = 0; j < 7; j++) {
            Log.d(tag, "Value of J before : " + j);
            for (int k = 0; k < mlistL.size(); k++) {
                if ((j == getDay(mlistL.get(k).get("date")))) {

                    lunchCal.setText(mlistL.get(k).get("totalCalories"));
                    Log.e(tag, "Lunch time calories: " + mlistL.get(k).get("totalCalories"));
                }
            }
        }

        // Showing the total calories intake for dinner
        ArrayList<HashMap<String, String>> mlistD = new ArrayList<>(DBHelper.getInstance(getActivity()).nutritionWeeklyDataDinner1());

        for (int j = 0; j < 7; j++) {
            Log.d(tag, "Value of J before : " + j);
            for (int k = 0; k < mlistD.size(); k++) {
                if ((j == getDay(mlistD.get(k).get("date")))) {

                    dinnerCal.setText(mlistD.get(k).get("totalCalories"));
                    Log.e(tag, "Dinner time calories: " + mlistD.get(k).get("totalCalories"));
                }
            }
        }


        exerciseCalorie = (int) Utils.caloriesBurnedForWalking(Float.parseFloat(session.getWeight()), numSteps);
        exercise.setText(String.valueOf(exerciseCalorie));

        goalCalorie = (int) Utils.calculateCaloriesRequired(String.valueOf(session.getGender()), String.valueOf(session.getActivityLevel()),
                Float.parseFloat(session.getHeight()), Float.parseFloat(session.getWeight()), Integer.parseInt(session.getAge()));


//        leftCaloriesValue = Utils.getLeftCalorie(context, "Calories", "0", Float.parseFloat(session.getWeight()), numSteps);
        leftCaloriesValue = Utils.getLeftCalorieN(totalCalForADay, Float.parseFloat(session.getWeight()), numSteps);
        leftCalories.setText(String.valueOf(leftCaloriesValue));


        if("yes".equals(takeFoodYesORNo)){
            DBHelper.getInstance(getActivity()).saveNutrition(nutritionModel);
        }

        // BarChart
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        leftAxis = mChart.getAxisLeft();

        // add data
        setData();

        mChart.animateXY(2000, 2000);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = mChart.getXAxis();

        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        // Setting up the limit line
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);

            leftAxis.removeAllLimitLines();
            LimitLine ll = new LimitLine(goalOfCal, "Calorie Goal");
            ll.setLineColor(getResources().getColor(R.color.colorCalories));
            ll.setLineWidth(2f);
            ll.setTextColor(Color.BLACK);
            ll.setTextSize(9f);
            leftAxis.addLimitLine(ll);
//            leftAxis.enableGridDashedLine(10f,10f,0f);

        mChart.getAxisRight().setDrawLabels(false);

        final String[] ds = new String[7];
        ds[0] = "SUN";
        ds[1] = "MON";
        ds[2] = "TUE";
        ds[3] = "WED";
        ds[4] = "THUR";
        ds[5] = "FRI";
        ds[6] = "SAT";

        XAxis xval = mChart.getXAxis();
        xval.setDrawLabels(true);
        xval.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ds[Math.round(value)];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });



    }

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
//                            int cal = parseInt(value);
                            if (typeOfFood == 1) {
                                particularFoodCalories += parseInt(value);
                                nutritionModel.setCalories(particularFoodCalories);
                                nutritionModel.setDate(takeFoodDate);
                                Log.d(tag, "Total calories Break fast : " + particularFoodCalories);
                            }
                            Utils.setCalorieShared(context, "Calorie", String.valueOf(TotalCalories));
//                            Utils.setCalorieShared(context, "BreakfastCal", String.valueOf(TotalCalories));

//                            breakfastCal.setText(String.valueOf(TotalCalories) + " kcal");

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
//                            String prevLunch = Utils.getCalorieShared(context,"LunchCal","0");
                            TotalCalories = parseInt(prev) + parseInt(value);

                            lunchRemCal = lunchRemCal + parseInt(value);

                            if (typeOfFood == 2) {
                                particularFoodCalories += parseInt(value);
                                nutritionModel.setDate(takeFoodDate);
                                Log.d(tag, "Total calories Lunch : " + particularFoodCalories);
                            }
                            Utils.setCalorieShared(context, "Calorie", String.valueOf(TotalCalories));

//                            Utils.setCalorieShared(context,"LunchCal", String.valueOf(lunchRemCal));
//
//                            lunchCal.setText(String.valueOf(lunchRemCal) + " kcal");
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

                            dinnerRemCal = dinnerRemCal + parseInt(value);

                            if (typeOfFood == 3) {
                                particularFoodCalories += parseInt(value);
                                nutritionModel.setDate(takeFoodDate);
                                Log.d(tag, "Total calories Dinner : " + particularFoodCalories);
                            }
                            Utils.setCalorieShared(context, "Calorie", String.valueOf(TotalCalories));

//                            Utils.setCalorieShared(context,"DinnerCal", String.valueOf(dinnerRemCal));
//                            dinnerCal.setText(String.valueOf(dinnerRemCal) + " kcal");
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

    private void setData() {

        ArrayList<BarEntry> dinnerArrayList = new ArrayList<BarEntry>();

        ArrayList<HashMap<String, String>> mlist = new ArrayList<>(DBHelper.getInstance(getActivity()).weeklyNutritionData());

        for (int i = 0; i < 7; i++) {
            if (i < mlist.size()) {
                getDay(mlist.get(i).get("date"));
//                Log.d(tag, "name of food : " + mlist.get(i).getName());
                Log.d(tag, "Day : " + mlist.get(i).get("date"));
            }
        }

        for (int j = 0; j < 7; j++) {
            Log.d(tag, "Value of J before : " + j);
            for (int k = 0; k < mlist.size(); k++) {

                if ((j == getDay(mlist.get(k).get("date")))) {

                    Log.d(tag, "size of mlist: " + mlist.size());

                            Log.d(tag, "Value of K : " + k);
                            Log.d(tag, "SIZE OF mlist(k) : " + mlist.size());
                            Log.d(tag, "DATE : " + getDay(mlist.get(k).get("date")));
                            Log.d(tag, "Calories of food : " + mlist.get(k).get("totalCalories"));
//                    Log.d(tag, "Name of food : " + mlist1.get(k).getName());

                            dinnerArrayList.add(new BarEntry(j, Float.parseFloat(mlist.get(k).get("totalCalories"))));

                            totalLeftXis = (int) (totalLeftXis + Float.parseFloat(mlist.get(k).get("totalCalories")));
                            grapAdded = true;
                            break;
                } else {
                    grapAdded = false;
                }
            }

            if (!grapAdded) {
                // Log.d(tag, "Value of J After : " + j);
                dinnerArrayList.add(new BarEntry(j, 0, getDayName(j)));
            }

            Log.d(tag, "left xis : " + totalLeftXis);
            //set Left Axis


        }

        leftAxis.setAxisMaximum(totalLeftXis + 40);

        BarDataSet setDinner;

        // create a dataset and give it a type

        setDinner = new BarDataSet(dinnerArrayList, "");
//        setSteps.setAxisDependency(YAxis.AxisDependency.LEFT);
        setDinner.setColor(getResources().getColor(R.color.colorPrimaryDark));
//        setSteps.setHighLightColor(Color.rgb(244, 117, 117));

        // create a data object with the datasets
        BarData data = new BarData(setDinner);
        //LineData data = new LineData(setWalking);
        data.setBarWidth(0.9f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setData(data);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    public static int getDay(String date) {
        Calendar c = Calendar.getInstance();
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date1);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

        return dayOfWeek;
    }

    public String getDayName(Integer i) {
        String day = null;
        switch (i) {
            case 1:
                day = "SUN";
                break;
            case 2:
                day = "MON";
                break;
            case 3:
                day = "TUE";
                break;
            case 4:
                day = "WED";
                break;
            case 5:
                day = "THUR";
                break;
            case 6:
                day = "FRI";
                break;
            case 7:
                day = "SAT";
                break;
        }
        return day;
    }

    // Adapter to show the total intake calories per day
    public class TotalCalHistoryAdapter extends RecyclerView.Adapter<TotalCalHistoryAdapter.MyViewHolder> {

        private Context context;

        public TotalCalHistoryAdapter(Context context) {
            super();
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTimestamp, tvCalories;

            public MyViewHolder(View view) {
                super(view);
                tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);
                tvCalories = (TextView) view.findViewById(R.id.tvSteps);
            }
        }

        /* Inflating the view to show the Activity Tracker history data. */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_steps_bar_chart, parent, false);

            return new MyViewHolder(itemView);
        }

        /* Populating the Activity Tracker data on the View of RecycleView */
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            HashMap<String, String> nm = nutritionModels.get(position);

                holder.tvTimestamp.setText(nm.get("date"));
                holder.tvCalories.setText(nm.get("totalCalories") + " kcal");
        }

        @Override
        public int getItemCount() {
            return nutritionModels.size();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}

