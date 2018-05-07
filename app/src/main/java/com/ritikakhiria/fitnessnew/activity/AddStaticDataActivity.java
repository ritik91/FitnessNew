package com.ritikakhiria.fitnessnew.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.MyStepModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddStaticDataActivity extends AppCompatActivity {
    TextView date;
    Button setDate, save;
    EditText distance, calories, walking, running, stairs, steps;
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_data);

        date = (TextView) findViewById(R.id.date);
        setDate = (Button) findViewById(R.id.setDate);
        save = (Button) findViewById(R.id.save);
        distance = (EditText) findViewById(R.id.distance);
        calories = (EditText) findViewById(R.id.calories);
        walking = (EditText) findViewById(R.id.walking);
        running = (EditText) findViewById(R.id.running);
        stairs = (EditText) findViewById(R.id.stairs);
        steps = (EditText) findViewById(R.id.steps);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(date.getText().toString()) && !date.getText().toString().equals("Date")
                        && !TextUtils.isEmpty(distance.getText().toString())
                        && !TextUtils.isEmpty(calories.getText().toString())
                        && !TextUtils.isEmpty(walking.getText().toString())
                        && !TextUtils.isEmpty(running.getText().toString())
                        && !TextUtils.isEmpty(stairs.getText().toString())
                        && !TextUtils.isEmpty(steps.getText().toString())) {
                    MyStepModel d = new MyStepModel();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        final Date date1 = sdf.parse(date.getText().toString());
                        long startDate = date1.getTime();
                        d.setDate(startDate);
                        d.setDistance(Float.parseFloat(distance.getText().toString()));
                        d.setSteps(Long.parseLong(steps.getText().toString()));
                        d.setCaloriesBurned(Double.parseDouble(calories.getText().toString()));
                        d.setWalking(Float.parseFloat(walking.getText().toString()));
                        d.setRunning(Float.parseFloat(running.getText().toString()));
                        d.setStairs(Float.parseFloat(stairs.getText().toString()));
                        DBHelper.getInstance(AddStaticDataActivity.this).saveStaticTracking(d, getDate(date1), getMonth(date1) + "");
                        Toast.makeText(AddStaticDataActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        distance.setText("");
                        calories.setText("");
                        walking.setText("");
                        running.setText("");
                        stairs.setText("");
                        steps.setText("");
                        date.setText("");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(AddStaticDataActivity.this, "Fields can't be blank", Toast.LENGTH_SHORT).show();
                }


            }
        });
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
    }

    private void datePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        tiemPicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        date.setText(date_time + " " + hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public static String getDate(Date d) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(d);
        return formattedDate;
    }

    public static Integer getMonth(Date d) {
        Calendar cal = Calendar.getInstance();
        return Integer.valueOf(new SimpleDateFormat("MM").format(d));
    }
}

