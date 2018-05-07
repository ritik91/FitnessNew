package com.ritikakhiria.fitnessnew.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.db.DBHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.internal.Util;

import static com.ritikakhiria.fitnessnew.Utils.Utils.getThisMonthFirstDate;


public class NutritionWeeklyReportFragment extends Fragment implements OnChartValueSelectedListener {

    private String tag = NutritionWeeklyReportFragment.class.getSimpleName();

    private PieChart mChart;
    TextView tvFat, tvCarbs, tvProtein, tvSugar, tvOthers;
    TextView avgFat, avgCarbs, avgSugar, avgCal, avgProtein;
    View view;
    private float totalFat,totalCarbs,totalSugar,totalProtein,totalOther;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nutrition_weekly_report, container, false);
        tvFat = (TextView)view.findViewById(R.id.tv_fat);
        tvCarbs = (TextView)view.findViewById(R.id.tv_carbs);
//        tvSugar = (TextView)view.findViewById(R.id.tv_sugar);
        tvProtein = (TextView)view.findViewById(R.id.tv_protein);
        tvOthers = (TextView)view.findViewById(R.id.tv_others);

        avgFat = (TextView) view.findViewById(R.id.avg_fat_value);
        avgCarbs = (TextView) view.findViewById(R.id.avg_carbs_value);
//        avgSugar = (TextView) view.findViewById(R.id.avg_sugar_value);
        avgProtein = (TextView) view.findViewById(R.id.avg_protein_value);
        avgCal = (TextView) view.findViewById(R.id.avg_calories_value);

        mChart = (PieChart) view.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        //mChart.setCenterTextTypeface(mTfLight);
        // mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(false);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        /*mSeekBarX.setOnSeekBarChangeListener(this);
        mSeekBarY.setOnSeekBarChangeListener(this);*/

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
       /* mChart.setEntryLabelTypeface(mTfRegular);*/
        mChart.setEntryLabelTextSize(12f);
        return view;
    }

    private void setData(int count, float range) {

        float mult = range;
        float avgFatValue = 0, avgCarbsValue = 0, avgSugarValue = 0, avgCalVal = 0, avgProteinValue = 0;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        String[] mParties = new String[] {
                "", "", "", "", "", ""
        };
        ArrayList<HashMap<String, String>> mlist = new ArrayList<>(DBHelper.getInstance(getActivity()). weeklyNutritionData());
        Log.d(tag,"Size of array : "+getThisMonthFirstDate());
        for (int j=0; j < 4; j++) {
            for (int i = 0; i < mlist.size(); i++) {
                if (mlist.get(i).get("totalFat") != null) {
                    totalFat = Float.parseFloat(mlist.get(i).get("totalFat"));
                    tvFat.setText(new DecimalFormat("##.##").format(totalFat) + " g");
//                    for (int j = 0; j < 3; j++) {
                    if (totalFat > 1) {
                        avgFatValue = avgFatValue + totalFat;
                        avgFatValue = avgFatValue / (j + 1);
                    }
//                    }
                }
                if (mlist.get(i).get("totalCarbs") != null) {
                    totalCarbs = Float.parseFloat(mlist.get(i).get("totalCarbs"));
                    tvCarbs.setText(new DecimalFormat("##.##").format(totalCarbs) + " g");
                    if (totalCarbs > 1) {
                        avgCarbsValue = avgCarbsValue + totalCarbs;
                        avgCarbsValue = avgCarbsValue / (j + 1);
                    }
                }
//                if (mlist.get(i).get("totalSugar") != null) {
//                    totalSugar = Float.parseFloat(mlist.get(i).get("totalSugar"));
//                    tvSugar.setText(new DecimalFormat("##.##").format(totalSugar) + " g");
//                    if (totalSugar > 1) {
//                        avgSugarValue = avgSugarValue + totalSugar;
//                        avgSugarValue = avgSugarValue / (j + 1);
//                    }
//                }
                if (mlist.get(i).get("totalProtein") != null) {
                    totalProtein = Float.parseFloat(mlist.get(i).get("totalProtein"));
                    tvProtein.setText(new DecimalFormat("##.##").format(totalProtein) + " g");
                    if (totalProtein > 1) {
                        avgProteinValue = avgProteinValue + totalProtein;
                        avgProteinValue = avgProteinValue / (j + 1);
                    }
                }
                if (mlist.get(i).get("totalCalories") != null) {
                    totalOther = Float.parseFloat(mlist.get(i).get("totalCalories"));// - (totalFat + totalCarbs + totalProtein);
                    tvOthers.setText(new DecimalFormat("##.##").format(totalOther) + " g");
                    if (Float.parseFloat(mlist.get(i).get("totalCalories")) > 1) {
                        avgCalVal = avgCalVal + Float.parseFloat(mlist.get(i).get("totalCalories"));
                        avgCalVal = avgCalVal / (j + 1);
                    }
                }

            }
        }
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry(totalOfNutrition(i),
                    mParties[i % mParties.length],
                    getResources().getDrawable(R.drawable.ic_add_black_24dp)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        avgFat.setText(Utils.round(avgFatValue, 1) + "");
        avgCarbs.setText(Utils.round(avgCarbsValue, 1) + "");
//        avgSugar.setText(Utils.round(avgSugarValue, 1) + "");
        avgProtein.setText(Utils.round(avgProteinValue, 1) + "");
        avgCal.setText(Utils.round(avgCalVal, 1) + "");

        //dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        //dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : COLORFUL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        /*data.setValueTypeface(mTfLight);*/
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
    public static final int[] COLORFUL_COLORS = {
            Color.rgb(255,211,229),
            Color.rgb(255, 80, 80),
            Color.rgb(244,166,68),
            Color.rgb(0, 204, 153),
//            Color.rgb(201,126,	149)
    };
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {

    }

    float totalOfNutrition(int i){
        switch (i){
            case 0:
                return totalFat;
            case 1:
                return totalCarbs;
//            case 2:
//                return totalSugar;
            case 2:
                return totalProtein;
            case 3:
                return totalOther;
        }
        return 0;
    }
}
