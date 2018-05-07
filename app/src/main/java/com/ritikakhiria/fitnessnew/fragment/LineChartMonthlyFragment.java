package com.ritikakhiria.fitnessnew.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.HistoryModel;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class LineChartMonthlyFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private String tag = LineChartMonthlyFragment.class.getSimpleName();

    private LineChart mChart;
    private View view;
    private boolean grapAdded = false;
    private YAxis leftAxis;
    private int totalLeftXis;
    public FloatingActionButton fab;
    private TextView min, max, avg;
    public DiscreteSeekBar mSeekBar;
    public TextView mTextViewProgress, mMaxTextValue;
    public float limitValue, goalValue;
    Session session;
    Context context;
    //    GoalCalSeekBar mGoalCalSeekBar;
    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_line_chart_monthly, container, false);
        initChart();
        return view;
    }
    private void initChart() {
        min = (TextView) view.findViewById(R.id.minCalValue);
        max = (TextView) view.findViewById(R.id.maxCalValue);
        avg = (TextView) view.findViewById(R.id.avgCalValue);
        sharedpreferences = getActivity().getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mChart = (LineChart) view.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);



        leftAxis = mChart.getAxisLeft();

        // add data
        setData(12, 30);

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
//        l.setYOffset(11f);

        XAxis xAxis = mChart.getXAxis();

        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        // Limit line for goal calorie
        if (sharedpreferences.getFloat("goal_calories", 0) > 0) {
            leftAxis.removeAllLimitLines();
            LimitLine ll = new LimitLine(sharedpreferences.getFloat("goal_calories", 0), "Calorie Goal");
            ll.setLineColor(getResources().getColor(R.color.colorCalories));
            ll.setLineWidth(2f);
            ll.setTextColor(Color.BLACK);
            ll.setTextSize(9f);
            leftAxis.addLimitLine(ll);
//            leftAxis.enableGridDashedLine(10f,10f,0f);
        } else {
            leftAxis.removeAllLimitLines();
            mChart.invalidate();
        }

        leftAxis.setTextColor(ColorTemplate.getHoloBlue());

        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        mChart.getAxisRight().setDrawLabels(false);

        final String[] ds = new String[12];
        ds[0] = "JAN";
        ds[1] = "FAB";
        ds[2] = "MAR";
        ds[3] = "APR";
        ds[4] = "MAY";
        ds[5] = "JUN";
        ds[6] = "JUL";
        ds[7] = "AUG";
        ds[8] = "SEP";
        ds[9] = "OCT";
        ds[10] = "NOV";
        ds[11] = "DEC";

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
    }

    //Dialog box with the seekBar to choose the goal limit
    public void ShowDialog() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goal_seekbar, null);
        mTextViewProgress = (TextView) dialogView.findViewById(R.id.title_multiple_value);
        mMaxTextValue = (TextView) dialogView.findViewById(R.id.maxCalValue);
//        buttonOk = (Button) dialogView.findViewById(R.id.button_ok);
        mSeekBar = (DiscreteSeekBar) dialogView.findViewById(R.id.discrete_multiple);

        session = Session.getSession(context);

        int goal = (int) Utils.calculateCaloriesRequired(String.valueOf(session.getGender()), String.valueOf(session.getActivityLevel()),
                Float.parseFloat(session.getHeight()), Float.parseFloat(session.getWeight()), Integer.parseInt(session.getAge()));

        mMaxTextValue.setText(String.valueOf(goal));

        mSeekBar.setMin(0);
        mSeekBar.setMax(Integer.parseInt(mMaxTextValue.getText().toString()));
//        mSeekBar.setProgress(goal/20);
//        mSeekBar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
//            @Override
//            public int transform(int value) {
//                return goalValue = value;
//            }
//        });

        popDialog.setCancelable(false);
        popDialog.setView(dialogView);

        mSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mTextViewProgress.setText(String.valueOf(value));
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putFloat("goal_calories", value);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }

        });

        // Button OK
        popDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (sharedpreferences.getFloat("goal_calories", 0) > 0) {
                            leftAxis.removeAllLimitLines();
                            LimitLine ll = new LimitLine(sharedpreferences.getFloat("goal_calories", 0), "Calorie Goal");
                            ll.setLineColor(getResources().getColor(R.color.colorCalories));
                            ll.setLineWidth(2f);
                            ll.setTextColor(Color.BLACK);
                            ll.setTextSize(9f);
                            leftAxis.addLimitLine(ll);
//                            leftAxis.enableGridDashedLine(10f,10f,0f);
//                            leftAxis.setDrawLimitLinesBehindData(true);
                            mChart.invalidate();
                        } else {
                            leftAxis.removeAllLimitLines();
                            mChart.invalidate();
                        }
                    }
                });
//        popDialog.setView(dialogView);
        popDialog.create();
        popDialog.show();
    }

    float avaCal = 0;
    float minCal = 0, maxCal = 0;

    private void setData(int count, float range) {
        ArrayList<Entry> caloriesArrayList = new ArrayList<Entry>();
        ArrayList<Entry> stepsArrayList = new ArrayList<Entry>();
        ArrayList<Entry> distanceArrayList = new ArrayList<Entry>();
        ArrayList<Entry> walkingArrayList = new ArrayList<Entry>();
        ArrayList<Entry> runningArrayList = new ArrayList<Entry>();
        ArrayList<Entry> stairsArrayList = new ArrayList<Entry>();
        ArrayList<HashMap<String, String>> mlist = new ArrayList<>(DBHelper.getInstance(getActivity()).monthlyData());
        for (int i = 0; i < count; i++) {
            if (i < mlist.size()) {
                // Log.d(tag, "save Date : " + mlist.get(i).get("saveDate") + "Value of day : " + getDay(mlist.get(i).get("saveDate")));
                getDay(mlist.get(i).get("saveDate"));
                Log.d(tag, "totalSteps : " + mlist.get(i).get("totalSteps"));
                Log.d(tag, "steps : " + mlist.get(i).get("steps"));
                Log.d(tag, "Separate Month : " +Utils.getMonthFromMonthAndYear( mlist.get(i).get("month")));
                /*Log.d(tag, "data : " + mlist.get(i).get("data"));
                Log.d(tag, "calories : " + mlist.get(i).get("calories"));
                Log.d(tag, "distance : " + mlist.get(i).get("distance"));
                Log.d(tag, "walking : " + mlist.get(i).get("walking"));
                Log.d(tag, "running : " + mlist.get(i).get("running"));
                Log.d(tag, "stairs : " + mlist.get(i).get("stairs"));*/

            }
        }


        for (int j = 0; j < count; j++) {
            for (int k = 0; k < mlist.size(); k++) {
                int idOfMonth = Integer.valueOf(Utils.getMonthFromMonthAndYear(mlist.get(k).get("month"))) - 1;
                Log.d(tag, "Separate Month : " + Utils.getMonthFromMonthAndYear(mlist.get(k).get("month")));
                if (j == idOfMonth) {
                    stepsArrayList.add(new Entry(j, Float.parseFloat(mlist.get(k).get("totalSteps"))));
                    caloriesArrayList.add(new Entry(j, Float.parseFloat(mlist.get(k).get("totalCalories"))));
                    distanceArrayList.add(new Entry(j, Float.parseFloat(mlist.get(k).get("totalDistance"))));
                    walkingArrayList.add(new Entry(j, Float.parseFloat(mlist.get(k).get("totalWalking"))));
                    runningArrayList.add(new Entry(j, Float.parseFloat(mlist.get(k).get("totalRunning"))));
                    stairsArrayList.add(new Entry(j, Float.parseFloat(mlist.get(k).get("totalStairs"))));
                    totalLeftXis = totalLeftXis + Integer.parseInt(mlist.get(k).get("totalSteps"));

                    if (Float.parseFloat(mlist.get(k).get("totalCalories")) > 1) {
                        avaCal = avaCal + Float.parseFloat(mlist.get(k).get("totalCalories"));
                        avaCal = avaCal / (j + 1);
                    } else {
                        caloriesArrayList.add(new Entry(j, 0, getDayName(j)));
                    }

                    if (Float.parseFloat(mlist.get(k).get("totalCalories")) < sharedpreferences.getFloat("goal_calories", 0)){
                        minCal = Float.parseFloat(mlist.get(k).get("totalCalories"));
                    } else if (Float.parseFloat(mlist.get(k).get("totalCalories")) > sharedpreferences.getFloat("goal_calories", 0)) {
                        maxCal = Float.parseFloat(mlist.get(k).get("totalCalories"));
                    }

                    grapAdded = true;
                    break;
                } else {
                    grapAdded = false;
                }
            }


            if (!grapAdded) {
                // Log.d(tag, "Value of J After : " + j);
                caloriesArrayList.add(new Entry(j, 0, getDayName(j)));
                stepsArrayList.add(new Entry(j, 1f, getDayName(j)));
                distanceArrayList.add(new Entry(j, 2f, getDayName(j)));
                walkingArrayList.add(new Entry(j, 3f, getDayName(j)));
                runningArrayList.add(new Entry(j, 4f, getDayName(j)));
                stairsArrayList.add(new Entry(j, 5f, getDayName(j)));
            }

        }
        leftAxis.setAxisMaximum(totalLeftXis + 50);
        //leftAxis.setAxisMaximum(200f);
        min.setText(Utils.round(minCal, 2) + " Kcal");
        avg.setText(Utils.round(avaCal, 2) + " Kcal");
        max.setText(Utils.round(maxCal, 2) + " Kcal");

        LineDataSet setCalories, setSteps, setDistance, setWalking, setRunning, setStairs;

        // create a dataset and give it a type
        setCalories = new LineDataSet(caloriesArrayList, "Calories");
        setCalories.setAxisDependency(YAxis.AxisDependency.LEFT);
        setCalories.setColor(getResources().getColor(R.color.colorCal));
        setCalories.setCircleColor(getResources().getColor(R.color.colorCal));
        setCalories.setLineWidth(2f);
        setCalories.setCircleRadius(3f);
        setCalories.setFillAlpha(65);
        setCalories.setFillColor(ColorTemplate.getHoloBlue());
        setCalories.setHighLightColor(Color.rgb(244, 117, 117));
        setCalories.setDrawCircleHole(false);

        setSteps = new LineDataSet(stepsArrayList, "Steps");
        setSteps.setAxisDependency(YAxis.AxisDependency.LEFT);
        setSteps.setColor(getResources().getColor(R.color.colorStep));
        setSteps.setCircleColor(getResources().getColor(R.color.colorStep));
        setSteps.setLineWidth(2f);
        setSteps.setCircleRadius(3f);
        setSteps.setFillAlpha(65);
        setSteps.setFillColor(ColorTemplate.getHoloBlue());
        setSteps.setHighLightColor(Color.rgb(244, 117, 117));
        setSteps.setDrawCircleHole(false);


        setDistance = new LineDataSet(distanceArrayList, "Distance");
        setDistance.setAxisDependency(YAxis.AxisDependency.LEFT);
        setDistance.setColor(getResources().getColor(R.color.colorDist));
        setDistance.setCircleColor(getResources().getColor(R.color.colorDist));
        setDistance.setLineWidth(2f);
        setDistance.setCircleRadius(3f);
        setDistance.setFillAlpha(65);
        setDistance.setFillColor(ColorTemplate.getHoloBlue());
        setDistance.setHighLightColor(Color.rgb(244, 117, 117));
        setDistance.setDrawCircleHole(false);

        setWalking = new LineDataSet(walkingArrayList, "Walking");
        setWalking.setAxisDependency(YAxis.AxisDependency.LEFT);
        setWalking.setColor(getResources().getColor(R.color.colorWalk));
        setWalking.setCircleColor(getResources().getColor(R.color.colorWalk));
        setWalking.setLineWidth(2f);
        setWalking.setCircleRadius(3f);
        setWalking.setFillAlpha(65);
        setWalking.setFillColor(ColorTemplate.getHoloBlue());
        setWalking.setHighLightColor(Color.rgb(244, 117, 117));
        setWalking.setDrawCircleHole(false);

        setRunning = new LineDataSet(runningArrayList, "Running");
        setRunning.setAxisDependency(YAxis.AxisDependency.LEFT);
        setRunning.setColor(getResources().getColor(R.color.colorRun));
        setRunning.setCircleColor(getResources().getColor(R.color.colorRun));
        setRunning.setLineWidth(2f);
        setRunning.setCircleRadius(3f);
        setRunning.setFillAlpha(65);
        setRunning.setFillColor(ColorTemplate.getHoloBlue());
        setRunning.setHighLightColor(Color.rgb(244, 117, 117));
        setRunning.setDrawCircleHole(false);

        setStairs = new LineDataSet(stairsArrayList, "Stairs");
        setStairs.setAxisDependency(YAxis.AxisDependency.LEFT);
        setStairs.setColor(getResources().getColor(R.color.colorStair));
        setStairs.setCircleColor(getResources().getColor(R.color.colorStair));
        setStairs.setLineWidth(2f);
        setStairs.setCircleRadius(3f);
        setStairs.setFillAlpha(65);
        setStairs.setFillColor(ColorTemplate.getHoloBlue());
        setStairs.setHighLightColor(Color.rgb(244, 117, 117));
        setStairs.setDrawCircleHole(false);
        // create a data object with the datasets
        LineData data = new LineData(setCalories, setSteps, setDistance, setWalking, setRunning, setStairs);
        // LineData data = new LineData(setSteps);
        //LineData data = new LineData(setWalking);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
//        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

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

    String getDayName(Integer i) {
        String day = null;
        switch (i) {
            case 0:
                day = "JAN";
                break;
            case 1:
                day = "FAB";
                break;
            case 2:
                day = "MAR";
                break;
            case 3:
                day = "APR";
                break;
            case 4:
                day = "MAY";
                break;
            case 5:
                day = "JUN";
                break;
            case 6:
                day = "JUL";
                break;
            case 7:
                day = "AUG";
                break;
            case 8:
                day = "SEP";
                break;
            case 9:
                day = "OCT";
                break;
            case 10:
                day = "NOV";
                break;
            case 11:
                day = "DEC";
                break;
        }
        return day;
    }
}
