package com.ritikakhiria.fitnessnew.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.Foods;
import com.ritikakhiria.fitnessnew.model.NutritionModel;
import com.ritikakhiria.fitnessnew.widget.StatefulRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class LunchBarGraph extends AppCompatActivity implements OnChartValueSelectedListener {

    private String tag = LunchBarGraph.class.getSimpleName();

    private BarChart mChart;
    private boolean grapAdded = false;
    private YAxis leftAxis;
    private int totalLeftXis;

    private Bundle mbundle;
    public int getValueFrom;

    private StatefulRecyclerView recyclerView;
    private TextView txerror;
    private List<NutritionModel> nutritionModels;
    private List<Foods> mFoods;
    private LunchHistoryAdapter mLunchHistoryAdapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_barchart);

        recyclerView = (StatefulRecyclerView) findViewById(R.id.stepsRCView);
        txerror = (TextView) findViewById(R.id.txerror);

        mChart = (BarChart) findViewById(R.id.chart1);

        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

//        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
//        mChart.setDragEnabled(true);
//        mChart.setScaleEnabled(true);
//        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        leftAxis = mChart.getAxisLeft();

        // add data
        setData();

        mChart.animateXY(2000, 2000);

        XAxis xAxis = mChart.getXAxis();

        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        // Setting up the limit line
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

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

        nutritionModels = DBHelper.getInstance(this).getAllNutrition();
        if (nutritionModels == null) {
            nutritionModels = new ArrayList<NutritionModel>();
        } else {
            txerror.setVisibility(View.GONE);
        }

        mLunchHistoryAdapter = new LunchHistoryAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mLunchHistoryAdapter);

    }


    private void setData() {

        ArrayList<BarEntry> lunchArrayList = new ArrayList<BarEntry>();

        ArrayList<HashMap<String,String>> mlist = new ArrayList<>(DBHelper.getInstance(this).nutritionWeeklyDataLunch());
//        ArrayList<Foods> mlist1 = new ArrayList<Foods>();

        for (int i = 0; i < 7; i++) {
            if (i < mlist.size()) {
                getDay(mlist.get(i).get("date"));
                Log.d(tag, "Timestamp : " + mlist.get(i).get("timestamp"));
                Log.d(tag, "Day : " + mlist.get(i).get("date"));
            }
        }

        for (int j = 0; j < 7; j++) {
            Log.d(tag, "Value of J before : " + j);
            for (int k = 0; k < mlist.size(); k++) {
                if ((j == getDay(mlist.get(k).get("date")))) {

                    Log.d(tag, "Value of K : " + k);
                    Log.d(tag, "SIZE OF mlist(k) : " + mlist.size());
                    Log.d(tag, "DATE : " + mlist.get(k).get("date"));
                    Log.d(tag, "Calories of food : " + mlist.get(k).get("totalCalories"));
                    Log.d(tag, "Timestamp : " + mlist.get(k).get("timestamp"));
                    Log.d(tag, "Type of Food : " + mlist.get(k).get("typeOfFood"));

                    lunchArrayList.add(new BarEntry(j, Float.parseFloat(mlist.get(k).get("totalCalories"))));

                    totalLeftXis = (int) (totalLeftXis + (Float.parseFloat(mlist.get(k).get("totalCalories"))));
                    grapAdded = true;
                    break;
                } else {
                    grapAdded = false;
                }
            }

            if (!grapAdded) {
                // Log.d(tag, "Value of J After : " + j);
                lunchArrayList.add(new BarEntry(j, 0, getDayName(j)));
            }

            Log.d(tag, "left xis : " + totalLeftXis);
            //set Left Axis


        }

        leftAxis.setAxisMaximum(totalLeftXis + 50);

        BarDataSet setLunch;

        // create a dataset and give it a type

        setLunch = new BarDataSet(lunchArrayList, "");
//        setSteps.setAxisDependency(YAxis.AxisDependency.LEFT);
        setLunch.setColor(getResources().getColor(R.color.colorCalories));
//        setSteps.setHighLightColor(Color.rgb(244, 117, 117));

        // create a data object with the datasets
        BarData data = new BarData(setLunch);
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


    public class LunchHistoryAdapter extends RecyclerView.Adapter<LunchBarGraph.LunchHistoryAdapter.MyViewHolder> {

        private Context context;

        public LunchHistoryAdapter(Context context) {
            super();
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTimestamp, tvCalories;
            private LinearLayout mLinearLayout;

            public MyViewHolder(View view) {
                super(view);
                mLinearLayout = (LinearLayout) view.findViewById(R.id.stepsDate);
                tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);
                tvCalories = (TextView) view.findViewById(R.id.tvSteps);
            }
        }

        /* Inflating the view to show the Activity Tracker history data. */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_meal_bar_chart, parent, false);

            return new MyViewHolder(itemView);
        }

        /* Populating the Activity Tracker data on the View of RecycleView */
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            NutritionModel nutritionModel = nutritionModels.get(position);

            if (nutritionModel.getTypeOfFood().equals(2) && (nutritionModel.getTypeOfFood() != 1) && (nutritionModel.getTypeOfFood() != 3)) {
//                Log.d(tag, "Name of food : " + mFoods.getName());
                holder.mLinearLayout.setVisibility(View.VISIBLE);
                holder.tvTimestamp.setText(nutritionModel.getDate());
                holder.tvCalories.setText(nutritionModel.getCalories() + " kcal");
            } else {
                holder.mLinearLayout.setVisibility(View.GONE);
            }
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
