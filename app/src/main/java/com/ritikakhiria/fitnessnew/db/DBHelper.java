package com.ritikakhiria.fitnessnew.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bumptech.glide.util.Util;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.model.Foods;
import com.ritikakhiria.fitnessnew.model.HistoryModel;
import com.ritikakhiria.fitnessnew.model.MyStepModel;
import com.ritikakhiria.fitnessnew.model.NutritionModel;
//import com.ritikakhiria.fitnessnew.model.StepModel;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static com.ritikakhiria.fitnessnew.Utils.Utils.convertStringToTimestamp;
import static com.ritikakhiria.fitnessnew.Utils.Utils.convertStringToTimestamp1;
import static com.ritikakhiria.fitnessnew.Utils.Utils.getCurrentDate;
import static com.ritikakhiria.fitnessnew.Utils.Utils.getCurrentMonth;
import static com.ritikakhiria.fitnessnew.Utils.Utils.getLastSevenDaysTimpStamp;
import static com.ritikakhiria.fitnessnew.Utils.Utils.getThisMonthFirstDate;
import static com.ritikakhiria.fitnessnew.Utils.Utils.isFloat;
import static com.ritikakhiria.fitnessnew.Utils.Utils.todayDate;

/**
 * Database helper class that interacts with Content Provider to fetch data from database
 */
public class DBHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    private Context context = null;
    private static DBHelper instance = null;
    private String tag = DBHelper.class.getSimpleName();

    private DBHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }

        return instance;
    }

    // save tracking data in local database
    public void saveTracking(MyStepModel stepModel) {
        ContentValues values = new ContentValues();
        values.put(DataProviderContract.TIMESTAMP, stepModel.getDate());
        values.put(DataProviderContract.DATA, stepModel.toJson());
        values.put(DataProviderContract.SAVE_DATE, todayDate());
        values.put(DataProviderContract.CALORIES, stepModel.getCaloriesBurned());
        values.put(DataProviderContract.DISTANCE, stepModel.getDistance());
        values.put(DataProviderContract.STEPS, stepModel.getSteps());
        values.put(DataProviderContract.WALKING, stepModel.getWalking());
        values.put(DataProviderContract.RUNNING, stepModel.getRunning());
        values.put(DataProviderContract.STAIRS, stepModel.getStairs());
        values.put(DataProviderContract.MONTH, getCurrentMonth());
        values.put(DataProviderContract.STATUS, 0);
        context.getContentResolver().insert(DataProviderContract.Tracking.CONTENT_URI, values);
    }

    // insert tracking data in server side database
    public void insertTrackingNew(MyStepModel stepModel) {
        ContentValues values = new ContentValues();
        values.put(DataProviderContract.TIMESTAMP, System.currentTimeMillis());
        values.put(DataProviderContract.DATA, stepModel.toJson());
        values.put(DataProviderContract.SAVE_DATE, todayDate());
        values.put(DataProviderContract.CALORIES, stepModel.getCaloriesBurned());
        values.put(DataProviderContract.DISTANCE, stepModel.getDistance());
        values.put(DataProviderContract.STEPS, stepModel.getSteps());
        values.put(DataProviderContract.WALKING, stepModel.getWalking());
        values.put(DataProviderContract.RUNNING, stepModel.getRunning());
        values.put(DataProviderContract.STAIRS, stepModel.getStairs());
        values.put(DataProviderContract.MONTH, getCurrentMonth());
        values.put(DataProviderContract.STATUS, 0);
        context.getContentResolver().insert(DataProviderContract.Tracking.CONTENT_URI, values);
    }

    public void insertTrackingNewFromServer(List<MyStepModel> stepModel) {
        clearAllDataBase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < stepModel.size(); i++) {
            values.clear();
            //values.put(DataProviderContract.TIMESTAMP, System.currentTimeMillis());
            values.put(DataProviderContract.TIMESTAMP, stepModel.get(i).getTimestamp());
            values.put(DataProviderContract.DATA, stepModel.get(i).toJson());
            values.put(DataProviderContract.SAVE_DATE, stepModel.get(i).getSaveDate());
            values.put(DataProviderContract.CALORIES, stepModel.get(i).getCaloriesBurned());
            values.put(DataProviderContract.DISTANCE, stepModel.get(i).getDistance());
            values.put(DataProviderContract.STEPS, stepModel.get(i).getSteps());
            values.put(DataProviderContract.WALKING, stepModel.get(i).getWalking());
            values.put(DataProviderContract.RUNNING, stepModel.get(i).getRunning());
            values.put(DataProviderContract.STAIRS, stepModel.get(i).getStairs());
            values.put(DataProviderContract.MONTH, stepModel.get(i).getMonth());
            values.put(DataProviderContract.STATUS, 1);
            context.getContentResolver().insert(DataProviderContract.Tracking.CONTENT_URI, values);
        }

    }

    // Updating the tracking data in the server side database.
    public void updateTrackingNew(MyStepModel stepModel) {
        ContentValues values = new ContentValues();
        values.put(DataProviderContract.TIMESTAMP, System.currentTimeMillis());
        values.put(DataProviderContract.DATA, stepModel.toJson());
        values.put(DataProviderContract.SAVE_DATE, todayDate());
        values.put(DataProviderContract.CALORIES, stepModel.getCaloriesBurned());
        values.put(DataProviderContract.DISTANCE, stepModel.getDistance());
        values.put(DataProviderContract.STEPS, stepModel.getSteps());
        values.put(DataProviderContract.WALKING, stepModel.getWalking());
        values.put(DataProviderContract.RUNNING, stepModel.getRunning());
        values.put(DataProviderContract.STAIRS, stepModel.getStairs());
        values.put(DataProviderContract.MONTH, getCurrentMonth());
        values.put(DataProviderContract.STATUS, 0);
        context.getContentResolver().update(DataProviderContract.Tracking.CONTENT_URI, values, DataProviderContract.SAVE_DATE + "=?", new String[]{todayDate() + ""});


    }

    public void updateTrackingStatus(int status, String savedDate) {
        ContentValues values = new ContentValues();
        values.put(DataProviderContract.STATUS, status);
        context.getContentResolver().update(DataProviderContract.Tracking.CONTENT_URI, values, DataProviderContract.SAVE_DATE + "=?", new String[]{savedDate + ""});


    }

    public boolean getTodayTracking() {
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION,
                "saveDate = '" + todayDate() + "' group by saveDate",
                null,
                DataProviderContract.TIMESTAMP + " ");
        // looping through all rows and adding to list
        boolean b = false;
        if (cursor != null && cursor.getCount() > 0) {
            b = true;
        }
        cursor.close();
        return b;
    }

    // Getting the tracking data to show in the graph report.
    public List<HistoryModel> getTodayTrackingData(String saveDate) {
        List<HistoryModel> data = new ArrayList<HistoryModel>();
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION,
                DataProviderContract.SAVE_DATE + "=?",
                new String[]{saveDate},
                null);
        Log.d(tag, "Query : " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                HistoryModel item = new HistoryModel();
                item.setDate(cursor.getLong(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
                item.setDistance(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.DISTANCE)));
                item.setSteps(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STEPS)));
                item.setWalking(cursor.getInt(cursor.getColumnIndex(DataProviderContract.WALKING)));
                item.setRunning(cursor.getInt(cursor.getColumnIndex(DataProviderContract.RUNNING)));
                item.setStairs(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STAIRS)));
                item.setCaloriesBurned(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.CALORIES)));
                item.setSaveDate(cursor.getString(cursor.getColumnIndex(DataProviderContract.SAVE_DATE)));
                item.setStatus(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STATUS)));
                data.add(item);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    // saving static tracking data in local database
    public void saveTrackingNew(MyStepModel stepModel, String todayDate, String month) {
        ContentValues values = new ContentValues();
        values.put(DataProviderContract.TIMESTAMP, stepModel.getDate());
        values.put(DataProviderContract.DATA, stepModel.toJson());
        values.put(DataProviderContract.SAVE_DATE, todayDate);
        values.put(DataProviderContract.CALORIES, stepModel.getCaloriesBurned());
        values.put(DataProviderContract.DISTANCE, stepModel.getDistance());
        values.put(DataProviderContract.STEPS, stepModel.getSteps());
        values.put(DataProviderContract.WALKING, stepModel.getWalking());
        values.put(DataProviderContract.RUNNING, stepModel.getRunning());
        values.put(DataProviderContract.STAIRS, stepModel.getStairs());
        values.put(DataProviderContract.MONTH, month);
        values.put(DataProviderContract.STATUS, stepModel.getStatus());
        if (getTodayTracking()) {
            context.getContentResolver().update(DataProviderContract.Tracking.CONTENT_URI, values, DataProviderContract.SAVE_DATE + "=?", new String[]{todayDate() + ""});
        } else {
            context.getContentResolver().insert(DataProviderContract.Tracking.CONTENT_URI, values);
        }
    }

    public void saveStaticTracking(MyStepModel stepModel, String todayDate, String month) {
        ContentValues values = new ContentValues();
        values.put(DataProviderContract.TIMESTAMP, stepModel.getDate());
        values.put(DataProviderContract.DATA, stepModel.toJson());
        values.put(DataProviderContract.SAVE_DATE, todayDate);
        values.put(DataProviderContract.CALORIES, stepModel.getCaloriesBurned());
        values.put(DataProviderContract.DISTANCE, stepModel.getDistance());
        values.put(DataProviderContract.STEPS, stepModel.getSteps());
        values.put(DataProviderContract.WALKING, stepModel.getWalking());
        values.put(DataProviderContract.RUNNING, stepModel.getRunning());
        values.put(DataProviderContract.STAIRS, stepModel.getStairs());
        values.put(DataProviderContract.MONTH, month);
        values.put(DataProviderContract.STATUS, stepModel.getStatus());
        context.getContentResolver().insert(DataProviderContract.Tracking.CONTENT_URI, values);
    }

    public List<HistoryModel> getAllTracking() {
        List<HistoryModel> data = new ArrayList<HistoryModel>();

        Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION,
                null,
                null,
                DataProviderContract.TIMESTAMP + " DESC");
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex(DataProviderContract.Tracking._ID));
                //MyStepModel item = MyStepModel.getObject(cursor.getString(cursor.getColumnIndex(DataProviderContract.DATA)));
                HistoryModel item = new HistoryModel();
                item.setDate(cursor.getLong(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
                item.setDistance(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.DISTANCE)));
                item.setSteps(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STEPS)));
                item.setWalking(cursor.getInt(cursor.getColumnIndex(DataProviderContract.WALKING)));
                item.setRunning(cursor.getInt(cursor.getColumnIndex(DataProviderContract.RUNNING)));
                item.setStairs(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STAIRS)));
                item.setCaloriesBurned(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.CALORIES)));
                item.setStatus(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STATUS)));
                item.setSaveDate(cursor.getString(cursor.getColumnIndex(DataProviderContract.SAVE_DATE)));
                item.setId(_id);
                data.add(item);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return data;
    }

    public List<HistoryModel> getUnSyncedTracking() {
        List<HistoryModel> data = new ArrayList<HistoryModel>();

        Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION,
                DataProviderContract.STATUS + "=?",
                new String[]{"0"},
                null);
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex(DataProviderContract.Tracking._ID));
                HistoryModel item = new HistoryModel();
                item.setDate(cursor.getLong(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
                item.setDistance(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.DISTANCE)));
                item.setSteps(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STEPS)));
                item.setWalking(cursor.getInt(cursor.getColumnIndex(DataProviderContract.WALKING)));
                item.setRunning(cursor.getInt(cursor.getColumnIndex(DataProviderContract.RUNNING)));
                item.setStairs(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STAIRS)));
                item.setCaloriesBurned(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.CALORIES)));
                item.setMonth(cursor.getString(cursor.getColumnIndex(DataProviderContract.MONTH)));
                item.setStatus(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STATUS)));
                item.setSaveDate(cursor.getString(cursor.getColumnIndex(DataProviderContract.SAVE_DATE)));
                item.setId(_id);
                data.add(item);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return data;
    }


    // Nutrition Data

    // Inserting nutrition values in the server side database.
    public void insertNutritionNewFromServer(List<NutritionModel> nutritionModels) {
        clearAllNuDataBase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < nutritionModels.size(); i++) {
            values.clear();
            values.put(DataProviderContract.TIMESTAMP, System.currentTimeMillis());
            values.put(DataProviderContract.DATE, nutritionModels.get(i).getDate());
            values.put(DataProviderContract.CALORIES, nutritionModels.get(i).getCalories());
            values.put(DataProviderContract.CARBS, nutritionModels.get(i).getCarb());
            values.put(DataProviderContract.PORTEIN, nutritionModels.get(i).getProtein());
            values.put(DataProviderContract.FAT, nutritionModels.get(i).getFat());
            values.put(DataProviderContract.FIBER, nutritionModels.get(i).getFiber());
            values.put(DataProviderContract.TYPE_OF_FOOD, nutritionModels.get(i).getTypeOfFood());
            values.put(DataProviderContract.SUGAR, nutritionModels.get(i).getSugar());
            values.put(DataProviderContract.STATUS, 1);
            context.getContentResolver().insert(DataProviderContract.Nutrition.CONTENT_URI, values);
        }

    }


    // saving static nutrition data in local database
    public void saveStaticNutrition(NutritionModel nutritionModel,String todayDate,String month) {
        ContentValues values = new ContentValues();
        values.put(DataProviderContract.TIMESTAMP, System.currentTimeMillis());
        values.put(DataProviderContract.DATE, todayDate);
        values.put(DataProviderContract.CALORIES, nutritionModel.getCalories());
        values.put(DataProviderContract.FAT, nutritionModel.getFat());
        values.put(DataProviderContract.CARBS, nutritionModel.getCarb());
        values.put(DataProviderContract.SUGAR, nutritionModel.getSugar());
        values.put(DataProviderContract.PORTEIN, nutritionModel.getProtein());
        values.put(DataProviderContract.TYPE_OF_FOOD, nutritionModel.getTypeOfFood());
        values.put(DataProviderContract.MONTH, month);
        values.put(DataProviderContract.STATUS, 1);
        context.getContentResolver().insert(DataProviderContract.Nutrition.CONTENT_URI, values);
    }


    public void saveNutrition(NutritionModel nutritionModel) {
        Log.d(tag, "Nutrition obj : " + nutritionModel);
        if (nutritionModel.getCalories() != null) {
            ContentValues values = new ContentValues();
            values.put(DataProviderContract.TIMESTAMP, System.currentTimeMillis());
            values.put(DataProviderContract.DATE, todayDate());
            values.put(DataProviderContract.CALORIES, nutritionModel.getCalories());
            values.put(DataProviderContract.CARBS, nutritionModel.getCarb());
            values.put(DataProviderContract.PORTEIN, nutritionModel.getProtein());
            values.put(DataProviderContract.FAT, nutritionModel.getFat());
            values.put(DataProviderContract.FIBER, nutritionModel.getFiber());
            values.put(DataProviderContract.SUGAR, nutritionModel.getSugar());
            values.put(DataProviderContract.TYPE_OF_FOOD, nutritionModel.getTypeOfFood());
            values.put(DataProviderContract.MONTH, getCurrentMonth());
            values.put(DataProviderContract.STATUS, 0);

//            if (checkFood(nutritionModel.getDate(), nutritionModel)) {
//                Log.d(tag, "Update : ");
//                values.put(DataProviderContract.TIMESTAMP, convertStringToTimestamp(nutritionModel.getDate()));
//                values.put(DataProviderContract.CALORIES, nutritionModel.getCalories());
//                values.put(DataProviderContract.FAT, isFloat(String.valueOf(nutritionModel.getFat())));
//                values.put(DataProviderContract.FIBER, isFloat(String.valueOf(nutritionModel.getFiber())));
//                values.put(DataProviderContract.PORTEIN, isFloat(String.valueOf(nutritionModel.getProtein())));
//                values.put(DataProviderContract.CARBS, isFloat(String.valueOf(nutritionModel.getCarb())));
//                values.put(DataProviderContract.SUGAR, isFloat(String.valueOf(nutritionModel.getSugar())));
//                values.put(DataProviderContract.DATE, String.valueOf(nutritionModel.getDate()));
//                values.put(DataProviderContract.TYPE_OF_FOOD, nutritionModel.getTypeOfFood());
//                context.getContentResolver().update(DataProviderContract.Nutrition.CONTENT_URI, values, "date='" + nutritionModel.getDate() + "'", null);
//            } else {
//                Log.d(tag, "Insert : ");
            context.getContentResolver().insert(DataProviderContract.Nutrition.CONTENT_URI, values);
//            }
        }
    }

    public void updateNutritionStatus(int status, String savedDate) {
        ContentValues values = new ContentValues();
        values.put(DataProviderContract.STATUS, status);
        context.getContentResolver().update(DataProviderContract.Nutrition.CONTENT_URI, values, DataProviderContract.DATE + "=?", new String[]{savedDate + ""});
    }

    public List<NutritionModel> getAllNutrition() {
        List<NutritionModel> data = new ArrayList<NutritionModel>();

        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION,
                null,
                null,
                DataProviderContract.TIMESTAMP + " DESC");
        Log.d(tag, "Query : " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex(DataProviderContract.Nutrition._ID));
                NutritionModel item = new NutritionModel();
                item.setCalories(cursor.getInt(cursor.getColumnIndex(DataProviderContract.CALORIES)));
                item.setCarb(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.CARBS)));
                item.setDate(cursor.getString(cursor.getColumnIndex(DataProviderContract.DATE)));
                item.setFat(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.FAT)));
                item.setFiber(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.FIBER)));
                item.setProtein(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.PORTEIN)));
                item.setSugar(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.SUGAR)));
                item.setTypeOfFood(cursor.getInt(cursor.getColumnIndex(DataProviderContract.TYPE_OF_FOOD)));
//                item.setTimestamp(cursor.getString(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
                item.setId(_id);
                data.add(item);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<NutritionModel> getUnsyncNutrition() {
        List<NutritionModel> data = new ArrayList<NutritionModel>();
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION,
                DataProviderContract.STATUS + "=?",
                new String[]{"0"},
                null);
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex(DataProviderContract.Nutrition._ID));
                NutritionModel item = new NutritionModel();
                item.setCalories(cursor.getInt(cursor.getColumnIndex(DataProviderContract.CALORIES)));
                item.setCarb(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.CARBS)));
                item.setDate(cursor.getString(cursor.getColumnIndex(DataProviderContract.DATE)));
                item.setFat(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.FAT)));
                item.setFiber(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.FIBER)));
                item.setProtein(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.PORTEIN)));
                item.setSugar(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.SUGAR)));
                item.setTypeOfFood(cursor.getInt(cursor.getColumnIndex(DataProviderContract.TYPE_OF_FOOD)));
                item.setTimestamp(cursor.getString(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
                item.setId(_id);
                data.add(item);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    // Tracking data for weekly report.
    public ArrayList<HistoryModel> weeklyData() {
        List<HistoryModel> data = new ArrayList<HistoryModel>();
        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+"Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION_WEEKLY,
                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))) + "' and timestamp <= '" + System.currentTimeMillis() + "' group by saveDate",
                null,
                DataProviderContract.TIMESTAMP + " DESC");
       /* Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION_WEEKLY,
                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))) + "' and timestamp <= '" + System.currentTimeMillis() + "' group by saveDate",
                null, null);*/
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex(DataProviderContract.Tracking._ID));
                //MyStepModel item = MyStepModel.getObject(cursor.getString(cursor.getColumnIndex(DataProviderContract.DATA)));
                HistoryModel item = new HistoryModel();
                item.setDate(cursor.getLong(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
                item.setDistance(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.DISTANCE)));
                item.setSteps(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STEPS)));
                item.setWalking(cursor.getInt(cursor.getColumnIndex(DataProviderContract.WALKING)));
                item.setRunning(cursor.getInt(cursor.getColumnIndex(DataProviderContract.RUNNING)));
                item.setStairs(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STAIRS)));
                item.setCaloriesBurned(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.CALORIES)));
                item.setStatus(cursor.getInt(cursor.getColumnIndex(DataProviderContract.STATUS)));
                item.setSaveDate(cursor.getString(cursor.getColumnIndex(DataProviderContract.SAVE_DATE)));
                Log.e(TAG,"Date : : "+cursor.getString(cursor.getColumnIndex(DataProviderContract.SAVE_DATE)));
                Log.e(TAG,"Time Stamp : : "+cursor.getString(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
                item.setId(_id);
                data.add(item);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return (ArrayList<HistoryModel>) data;
    }

    public ArrayList<HashMap<String,String>> monthlyData() {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        int CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR);
        Log.d(TAG,"Current Day Of Year : "+(CurrentDayOfYear-(CurrentDayOfYear+CurrentDayOfYear)));
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION_MONTHLY,
                "timestamp > '" +
                        convertStringToTimestamp(getLastSevenDaysTimpStamp((CurrentDayOfYear-(CurrentDayOfYear+CurrentDayOfYear)))) +
                            "' and timestamp <= '" + System.currentTimeMillis() + "' group by month",
                null,
                DataProviderContract.TIMESTAMP + " ");
        /*Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION_MONTHLY,
                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(-365)) + "' and timestamp <= '" + System.currentTimeMillis() + "' group by month",
                null,
                DataProviderContract.TIMESTAMP + " ");*/
        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d(tag, "Column  Name : " + cursor.getColumnName(i) + " = " + cursor.getString(i));
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                maplist.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return maplist;
    }


    public ArrayList<HashMap<String, String>> nutritionWeeklyDataBreakfast() {
        ArrayList<NutritionModel> data = new ArrayList<NutritionModel>();
        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+" Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))) + "' and timestamp <= '" + System.currentTimeMillis()  + "' and typeOfFood = '" + 1 + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " ");

        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }


    public ArrayList<HashMap<String,String>> nutritionWeeklyDataLunch() {
        ArrayList<NutritionModel> data = new ArrayList<NutritionModel>();
        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+" Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))) + "' and timestamp <= '" + System.currentTimeMillis()  + "' and typeOfFood = '" + 2 + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " ");

        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }

    public ArrayList<HashMap<String,String>> nutritionWeeklyDataDinner() {
        ArrayList<NutritionModel> data = new ArrayList<NutritionModel>();
        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+" Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))) + "' and timestamp <= '" + System.currentTimeMillis()  + "' and typeOfFood = '" + 3 + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " ");

        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }

    public ArrayList<HashMap<String,String>> nutritionWeeklyDataBreakfast1() {
        ArrayList<NutritionModel> data = new ArrayList<NutritionModel>();
        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+" Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
                "date = '" + todayDate()  + "' and typeOfFood = '" + 1 + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " ");

        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }


    public ArrayList<HashMap<String,String>> nutritionWeeklyDataLunch1() {
        ArrayList<NutritionModel> data = new ArrayList<NutritionModel>();
        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+" Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
                "date = '" + todayDate() + "' and typeOfFood = '" + 2 + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " ");

        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }

    public ArrayList<HashMap<String,String>> nutritionWeeklyDataDinner1() {
        ArrayList<NutritionModel> data = new ArrayList<NutritionModel>();
        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+" Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
                "date = '" + todayDate() + "' and typeOfFood = '" + 3 + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " ");

        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }


//    public ArrayList<NutritionModel> nutritionWeeklyDataDinner() {
//        ArrayList<NutritionModel> data = new ArrayList<NutritionModel>();
//        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
//        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+" Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
//        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
//                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))) + "' and timestamp <= '" + System.currentTimeMillis()  + "' and typeOfFood = '" + 3 + "' group by date",
//                null,
//                DataProviderContract.TIMESTAMP + " DESC");
//
//        if (cursor.moveToFirst()) {
//            do {
//                int _id = cursor.getInt(cursor.getColumnIndex(DataProviderContract.Nutrition._ID));
//                NutritionModel item = new NutritionModel();
//                item.setTimestamp(cursor.getString(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
//                item.setCalories(cursor.getInt(cursor.getColumnIndex(DataProviderContract.CALORIES)));
//                item.setCarb(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.CARBS)));
//                item.setDate(cursor.getString(cursor.getColumnIndex(DataProviderContract.DATE)));
//                item.setFat(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.FAT)));
//                item.setFiber(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.FIBER)));
//                item.setProtein(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.PORTEIN)));
//                item.setSugar(cursor.getFloat(cursor.getColumnIndex(DataProviderContract.SUGAR)));
//                item.setTypeOfFood(cursor.getInt(cursor.getColumnIndex(DataProviderContract.TYPE_OF_FOOD)));
//                Log.e(TAG,"Date : : "+cursor.getString(cursor.getColumnIndex(DataProviderContract.DATE)));
//                Log.e(TAG,"Time Stamp : : "+cursor.getString(cursor.getColumnIndex(DataProviderContract.TIMESTAMP)));
//                item.setId(_id);
//                data.add(item);
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//        return (ArrayList<NutritionModel>) data;
//    }


    public boolean checkFood(String date, NutritionModel nutritionModel) {
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION,
                "date='" + date + "'",
                null, null);
        if (cursor.getCount() > 0) {
            if (cursor != null) {
                ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            map.put(cursor.getColumnName(i), cursor.getString(i));
                            Log.d(tag, "Column Name :" + cursor.getColumnName(i) + "\n Value : " + cursor.getString(i));
                            if ("protein".equals(cursor.getColumnName(i)) || "fat".equals(cursor.getColumnName(i))
                                    || "carbs".equals(cursor.getColumnName(i))
                                    || "calories".equals(cursor.getColumnName(i))
                                    || "sugar".equals(cursor.getColumnName(i))
                                    || "fiber".equals(cursor.getColumnName(i))) {
                                setAllNutrition(nutritionModel, cursor.getColumnName(i), Float.valueOf(cursor.getString(i)));
                            }
                        }
                        maplist.add(map);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

//    public ArrayList<HashMap<String, String>> weeklyNutritionDataBarGraph() {
//        Log.d(TAG,"Minus : "+(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate()))));
//        Log.d(TAG,"Current Time Stamp :"+System.currentTimeMillis()+" Past Time Stamp : "+convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))));
//        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
//                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))) + "' and timestamp <= '" + System.currentTimeMillis() + "' group by date",
//                null,
//                DataProviderContract.TIMESTAMP + " ");
//
//        Log.d(tag, "Query : " + cursor);
//        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
//        // looping through all rows and adding to list
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    for (int i = 0; i < cursor.getColumnCount(); i++) {
//                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
//                        map.put(cursor.getColumnName(i), cursor.getString(i));
//                    }
//
//                    maplist.add(map);
//                } while (cursor.moveToNext());
//            }
//        }
//        cursor.close();
//        return maplist;
//    }

    public ArrayList<HashMap<String, String>> weeklyNutritionDataInfo() {
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
                "date = '" + todayDate()  + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " ");


        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }

    public ArrayList<HashMap<String, String>> totalNutritionData() {
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_MONTHLY,
                "timestamp >= '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(-365)) + "' and timestamp <= '" + System.currentTimeMillis() + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " DESC");


        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }

    public ArrayList<HashMap<String, String>> weeklyNutritionData() {
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_WEEKLY,
                "timestamp > '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(Utils.getDay(todayDate())-(Utils.getDay(todayDate())+Utils.getDay(todayDate())))) + "' and timestamp <= '" + System.currentTimeMillis()  + "' group by date",
                null,
                DataProviderContract.TIMESTAMP + " ");


        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(tag,"Column Name : "+cursor.getColumnName(i)+ "Value of : : " + cursor.getString(i));
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    maplist.add(map);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return maplist;
    }


    //getThisMonthFirstDate()
    public ArrayList<HashMap<String, String>> monthlyNutritionData() {
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Nutrition.CONTENT_URI, DataProviderContract.Nutrition.PROJECTION_MONTHLY,
                "timestamp >= '" + convertStringToTimestamp(getLastSevenDaysTimpStamp(-365)) + "' and timestamp <= '" + System.currentTimeMillis() + "' group by month",
                null,
                DataProviderContract.TIMESTAMP + " ");
        Log.d(tag, "Query : " + cursor);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d(tag, "Column  Name : " + cursor.getColumnName(i) + " = " + cursor.getString(i));
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                maplist.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return maplist;
    }

    public ArrayList<HashMap<String, String>> getTodayBurnedCalories() {
        Cursor cursor = context.getContentResolver().query(DataProviderContract.Tracking.CONTENT_URI, DataProviderContract.Tracking.PROJECTION_DAILY_BURNED_CALORIES,
                "saveDate = '" + todayDate() + "' group by saveDate",
                null,
                DataProviderContract.TIMESTAMP + " ");
        Log.d(tag, "Query : " + cursor.getCount() + "\n Date : " + todayDate());
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list
        if (cursor.getCount() > 0) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            Log.d(tag, "Column Name : " + cursor.getColumnName(i) + " = " + cursor.getString(i));
                            map.put(cursor.getColumnName(i), cursor.getString(i));
                        }

                        maplist.add(map);
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        return maplist;
    }

    public void clearAllDataBase() {
        context.getContentResolver().delete(DataProviderContract.Tracking.CONTENT_URI, null, null);
    }

    public void clearAllNuDataBase() {
        context.getContentResolver().delete(DataProviderContract.Nutrition.CONTENT_URI, null, null);
    }

    public void setAllNutrition(NutritionModel nutritionModel, String columnName, Float value) {
        Log.d(tag, "Nutrition calories : " + nutritionModel.getFat());
        float lastValue = 0;
        switch (columnName) {
            case "protein": // 203 means protein
                if (nutritionModel.getProtein() != null) {
                    lastValue = nutritionModel.getProtein();
                }
                nutritionModel.setProtein(lastValue + value);
                break;
            case "fat": //204 means Fat
                if (nutritionModel.getFat() != null) {
                    lastValue = nutritionModel.getFat();
                }
                nutritionModel.setFat(lastValue + value);
                break;
            case "carbs": //205 means Carbohydrate
                if (nutritionModel.getCarb() != null) {
                    lastValue = nutritionModel.getCarb();
                }
                nutritionModel.setCarb(lastValue + value);
                break;
            case "calories": //208 means Calories
                if (nutritionModel.getCalories() != null) {
                    lastValue = nutritionModel.getCalories();
                }
                nutritionModel.setCalories((int) (lastValue + value));
                break;
            case "sugar": //269 means Sugar
                if (nutritionModel.getSugar() != null) {
                    lastValue = nutritionModel.getSugar();
                }
                nutritionModel.setSugar(lastValue + value);
                Log.d(tag, "final sugar value : " + nutritionModel.getSugar());
                break;
            case "fiber": // means fiber
                if (nutritionModel.getFiber() != null) {
                    lastValue = nutritionModel.getFiber();
                }
                nutritionModel.setFiber(lastValue + value);
                break;

        }


    }
}