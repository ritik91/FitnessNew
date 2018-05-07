package com.ritikakhiria.fitnessnew.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ritikakhiria.fitnessnew.R;

public class TrackMeNewActivity extends AppCompatActivity {

    Button btnActivityReport, btnNutritionReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_me_new);

        btnActivityReport = (Button) findViewById(R.id.btn_activty_report);
        btnActivityReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackMeNewActivity.this,HistoryChartActivity.class);
                startActivity(intent);
            }
        });

        btnNutritionReport = (Button) findViewById(R.id.btn_food_and_nutreport);
        btnNutritionReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackMeNewActivity.this,NutritionChartActivity.class);
                startActivity(intent);
            }
        });
    }
}
