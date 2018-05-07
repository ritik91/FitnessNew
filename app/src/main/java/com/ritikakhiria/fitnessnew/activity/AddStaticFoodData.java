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
import com.ritikakhiria.fitnessnew.model.NutritionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddStaticFoodData extends AppCompatActivity {
    TextView date;
    Button setDate, save;
    EditText caloriesFood, foodFat, foodCarbs, foodProtein, foodType;
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_food_data);

        date = (TextView) findViewById(R.id.date);
        setDate = (Button) findViewById(R.id.setDate);
        save = (Button) findViewById(R.id.save);
        foodType = (EditText) findViewById(R.id.food_type);
        caloriesFood = (EditText) findViewById(R.id.calories_food);
        foodFat = (EditText) findViewById(R.id.fat_food);
        foodCarbs = (EditText) findViewById(R.id.carbs_food);
        foodProtein = (EditText) findViewById(R.id.sugar_food);
//        steps = (EditText) findViewById(R.id.steps);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(date.getText().toString()) && !date.getText().toString().equals("Date")
                        && !TextUtils.isEmpty(foodType.getText().toString())
                        && !TextUtils.isEmpty(caloriesFood.getText().toString())
                        && !TextUtils.isEmpty(foodFat.getText().toString())
                        && !TextUtils.isEmpty(foodCarbs.getText().toString())
                        && !TextUtils.isEmpty(foodProtein.getText().toString())) {
                    NutritionModel n = new NutritionModel();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        final Date date1 = sdf.parse(date.getText().toString());
                        long startDate = date1.getTime();
                        n.setDate(String.valueOf(startDate));
                        n.setTypeOfFood(Integer.parseInt(foodType.getText().toString()));
//                        n.setSteps(Long.parseLong(steps.getText().toString()));
                        n.setCalories(Integer.parseInt(caloriesFood.getText().toString()));
                        n.setFat(Float.parseFloat(foodFat.getText().toString()));
                        n.setCarb(Float.parseFloat(foodCarbs.getText().toString()));
                        n.setSugar(Float.parseFloat(foodProtein.getText().toString()));
                        DBHelper.getInstance(AddStaticFoodData.this).saveStaticNutrition(n, getDate(date1), getMonth(date1) + "");
                        Toast.makeText(AddStaticFoodData.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        foodType.setText("");
                        caloriesFood.setText("");
                        foodFat.setText("");
                        foodCarbs.setText("");
                        foodProtein.setText("");
//                        steps.setText("");
                        date.setText("");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(AddStaticFoodData.this, "Fields can't be blank", Toast.LENGTH_SHORT).show();
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
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker() {
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
