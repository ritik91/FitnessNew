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
import com.ritikakhiria.fitnessnew.model.HistoryModel;
import com.ritikakhiria.fitnessnew.widget.StatefulRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DistanceBarGraph extends AppCompatActivity implements OnChartValueSelectedListener {

    private String tag = DistanceBarGraph.class.getSimpleName();

    private BarChart mChart;
    private boolean grapAdded = false;
    private YAxis leftAxis;
    private int totalLeftXis;

    private StatefulRecyclerView recyclerView;
    private TextView txerror;
    private List<HistoryModel> historyModels;
    private DistanceHistoryAdapter mDistanceHistoryAdapter = null;


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
        ds[0] = "MON";
        ds[1] = "TUE";
        ds[2] = "WED";
        ds[3] = "THUR";
        ds[4] = "FRI";
        ds[5] = "SAT";
        ds[6] = "SUN";

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

        historyModels = DBHelper.getInstance(this).getAllTracking();
        if (historyModels == null) {
            historyModels = new ArrayList<HistoryModel>();
        } else {
            txerror.setVisibility(View.GONE);
        }

        mDistanceHistoryAdapter = new DistanceHistoryAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mDistanceHistoryAdapter);

    }


    private void setData() {

        ArrayList<BarEntry> distanceArrayList = new ArrayList<BarEntry>();

        ArrayList<HistoryModel> mlist = new ArrayList<>(DBHelper.getInstance(this).weeklyData());

        for (int i = 0; i < 7; i++) {
            if (i < mlist.size()) {
                // Log.d(tag, "save Date : " + mlist.get(i).get("saveDate") + "Value of day : " + getDay(mlist.get(i).get("saveDate")));
                getDay(mlist.get(i).getSaveDate());
//                Log.d(tag, "totalSteps : " + mlist.get(i).getSteps());
                Log.d(tag, "Distance : " + mlist.get(i).getDistance());
                Log.d(tag, "Day : " + mlist.get(i).getSaveDate());
            }
        }

        for (int j = 0; j < 7; j++) {
            Log.d(tag, "Value of J before : " + j);
            for (int k = 0; k < mlist.size(); k++) {
                if (j == getDay(mlist.get(k).getSaveDate())) {

                    Log.d(tag, "Value of K : " + k);
                    Log.d(tag, "SIZE OF mlist(k) : " + mlist.size());
                    Log.d(tag, "DATE : " + getDay(mlist.get(k).getSaveDate()));
                    Log.d(tag, "Value of ditance : " + mlist.get(k).getDistance());

                    distanceArrayList.add(new BarEntry(j, mlist.get(k).getDistance()));

                    totalLeftXis = (int) (totalLeftXis + mlist.get(k).getDistance());
                    grapAdded = true;
                    break;
                } else {
                    grapAdded = false;
                }
            }

            if (!grapAdded) {
                // Log.d(tag, "Value of J After : " + j);
                distanceArrayList.add(new BarEntry(j, 0, getDayName(j)));
            }

            Log.d(tag, "left xis : " + totalLeftXis);
            //set Left Axis


        }

        leftAxis.setAxisMaximum(totalLeftXis + 10);

        BarDataSet setDistance;

        // create a dataset and give it a type

        setDistance = new BarDataSet(distanceArrayList, "");
//        setSteps.setAxisDependency(YAxis.AxisDependency.LEFT);
        setDistance.setColor(getResources().getColor(R.color.colorDist));
//        setSteps.setHighLightColor(Color.rgb(244, 117, 117));

        // create a data object with the datasets
        BarData data = new BarData(setDistance);
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
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;

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


    public class DistanceHistoryAdapter extends RecyclerView.Adapter<DistanceBarGraph.DistanceHistoryAdapter.MyViewHolder> {

        private Context context;

        public DistanceHistoryAdapter(Context context) {
            super();
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTimestamp, tvDistance;

            public MyViewHolder(View view) {
                super(view);
                tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);
                tvDistance = (TextView) view.findViewById(R.id.tvSteps);
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
            HistoryModel HistoryModel = historyModels.get(position);
            Log.d(tag, "Walking Steps : " + HistoryModel.getDistance());

            holder.tvTimestamp.setText(Utils.formatDate(Utils.convertStringToTimestamp1(HistoryModel.getSaveDate())));
            holder.tvDistance.setText(Utils.round(HistoryModel.getDistance(),2) + " km");
        }

        @Override
        public int getItemCount() {
            return historyModels.size();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
