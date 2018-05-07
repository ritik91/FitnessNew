package com.ritikakhiria.fitnessnew.Utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ritikakhiria.fitnessnew.model.BarcodeModel;
import com.ritikakhiria.fitnessnew.model.CommonFoodDetail;
import com.ritikakhiria.fitnessnew.model.EditModel;
import com.ritikakhiria.fitnessnew.model.ForgotModel;
import com.ritikakhiria.fitnessnew.model.LoginModel;
import com.ritikakhiria.fitnessnew.model.SearchModel;
import com.ritikakhiria.fitnessnew.model.SearchRecipeDetail;
import com.ritikakhiria.fitnessnew.model.SignUpModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static int foodTypeId = 1;
    private static double caloriesPerDay;
    private static double protein;
    private static double fat;

    public static String userId = "userId";
    public static String username = "username";//
    public static String contact = "contact";
    public static String age = "age";//
    public static String height = "height";
    public static String weight = "weight";//
    public static String gender = "gender";
    public static String allergy = "allergy";//
    public static String activityLevel = "activityLevel";//
    public static String foodHabit = "foodHabit";//
    //public static String nu_meal = "nu_meal";
    public static String userEmail = "userEmail";//
    public static String userProfile = "userProfile";
    public static String userProfileUrl = "userProfileUrl";//
    public static String diet = "diet";

    public static String breakfast = "breakfast";
    public static String lunch = "lunch";
    public static String dinner = "dinner";
    public static String image = "image";

    private static String BASE_URL = "http://13.56.221.121/pa/fitness/";
    private static String LOGIN = "login.php";
    private static String REGISTER = "register.php";
    private static String PROFILE = "profile.php";
    private static String FORGOT = "forget_password.php";
    private static String GET_STEPS = "steps_count.php";
    private static String SET_STEPS = "steps.php";

    private static String MEAL_SCHEDULER = "http://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=yPwTBWaTOgZpXOpkdb1RuNT82pjrNXu21SPf5r3h&nutrients=205&nutrients=204&nutrients=208&nutrients=269";
    private static String BARCODE_NUTRITION = "https://trackapi.nutritionix.com/v2/search/item";
    private static String GROCERY_LIST = "https://trackapi.nutritionix.com/v2/search/instant";
    private static String RECIPE_LIST = "https://trackapi.nutritionix.com/v2/search/instant";
//    private static String RECIPE_LIST = "https://api.edamam.com/search";
    private static String RECIPE_DETAIL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
    private static String RESTAURANT_LIST = "https://trackapi.nutritionix.com/v2/search/instant";
    private static String RECIPE_SEARCH_DETAIL = "https://trackapi.nutritionix.com/v2/search/item";

    public static final String NO_NET = "Internet is not available";

    public static String POST = "POST";
    public static String GET = "GET";
    public static String PUT = "PUT";

    private static String TAG = Utils.class.getSimpleName();

//    public static Session mSession;

    // shared methods
    public static void setShared(Context context, String name, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    // shared methods
    public static void setCalorieShared(Context context, String name, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void ClearCalorieShared(Context context, String name, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getCalorieShared(Context context, String name, String defaultValue) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getString(name, defaultValue);

    }

    public static void ClearShared(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static String getShared(Context context, String name, String defaultValue) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getString(name, defaultValue);

    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();

        return (int) d.getHeight();
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();

        return (int) d.getWidth();
    }

    static String getRegisterUrl(SignUpModel model) {
        Log.d(TAG, "UTIL REGISTER GENDER = " + model.getGender());
        return BASE_URL + REGISTER
                + "?email=" + model.getEmail()
                + "&name=" + model.getName()
                + "&contact=" + model.getContact()
                + "&age=" + model.getAge()
                + "&sex=" + model.getGender()
                + "&foodHabit=" + model.getFoodHabit()
                + "&height=" + model.getHeight()
                + "&weight=" + model.getWeight()
                + "&allergy=" + model.getAllergy()
                + "&activityLevel=" + model.getActivityLevel();

        //+"&nu_meal="+model.getNumberMeal()
    }

    static String getEditUrl(EditModel model) {
        return BASE_URL + PROFILE
                + "?email=" + model.getEmail()
                + "&name=" + model.getName()
                + "&contact=" + model.getContact()
                + "&age=" + model.getAge()
                + "&gender=" + model.getGender()
                + "&foodHabit=" + model.getFoodHabit()
                + "&height=" + model.getHeight()
                + "&weight=" + model.getWeight()
                + "&allergy=" + model.getAllergy()
                + "&activityLevel=" + model.getActivityLevel()
                + "&id=" + model.getId();
        //+"&nu_meal="+model.getNumberMeal()
    }

    static String getForgotUrl(ForgotModel model) {
        return BASE_URL + FORGOT
                + "?email=" + model.getEmail();
    }

    static String getLoginUrl(LoginModel model) {
        return BASE_URL + LOGIN
                + "?email=" + model.getEmail()
                + "&password=" + model.getPassword();
    }

    static String getAllStepsUrl(String custid) {
        return BASE_URL + GET_STEPS
                + "?custid=" + custid;
    }

    static String setStepsUrl(String steps, String custid) {
        return BASE_URL + SET_STEPS
                + "?custid=" + custid
                + "&steps=" + steps;
    }

    static String getMealScheduler() {
        return MEAL_SCHEDULER;
    }

    static String getNutritionFromBarCode(BarcodeModel barcodeModel) {
        return BARCODE_NUTRITION
                + "?upc=" + barcodeModel.getBarcode();
    }

//    static String getListOfRecipe(SearchModel searchModel) {
//        return RECIPE_LIST
//                + "?q=" + searchModel.getRecipeName() + "&app_id=552a3acf&app_key=135aa3fa384d038f949a85f5722caa16&from=0&to=10";
//    }

    static String getListOfGroceryFood(SearchModel searchModel) {
        return GROCERY_LIST
                + "?query=" + searchModel.getRecipeName() + "&branded_type=2";
    }

    static String getListOfRecipe(SearchModel searchModel) {
        return RECIPE_LIST
                + "?query=" + searchModel.getRecipeName() + "&branded=false&self=false&common=true";
    }

    static String getListOfRestaurant(SearchModel searchModel) {
        return RESTAURANT_LIST
                + "?query=" + searchModel.getRecipeName() + "&branded_type=1";
    }

    static String getRecipeSearchDetail(SearchRecipeDetail searchRecipeDetail) {
        return RECIPE_SEARCH_DETAIL
                + "?nix_item_id=" + searchRecipeDetail.getNixItemId();
    }

    static String getRecipeDetail(CommonFoodDetail commonFoodDetail) {
        return RECIPE_DETAIL
                + "?query=" + commonFoodDetail.getCommonFoodName();
    }

    //getString method
    public static String getString(String key, JSONObject jobj) {
        try {
            return jobj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static float caloriesBurnedForWalking(float weight, long steps) {
        //For moderate walking:
        //Calories burned = 0.029 x weight (lbs) x time (minutes)
        float caloriesBurned;
//        caloriesBurned = (float) ((0.029 * kgToLbs(weight)) * (walkingStepsToSecond(steps) / 60));
        caloriesBurned = (float) ((0.75 * kgToLbs(weight)) * kmToMiles(steps));
        return caloriesBurned;
    }
    /*public static double caloriesBurnedForRunning(double height, double weight, int stepsCount) {
    For Vigorous walking:
Calories burned = 0.048 x weight (lbs) x time (minutes)
    }*/

    //Converting kg to lbs
    public static double kgToLbs(double kg) {
        double lbs = kg * 2.2046226;
        return lbs;
    }

    /* Calculating kilometer to miles using total steps*/
    public static float kmToMiles(long steps){
        float miles;
        float distance = (float) (steps * 78) / (float) 100000;
        miles = (float) (distance / 1.609344);
        return miles;
    }

    /*
    * this function returning seconds of steps
    * */
    public static double walkingStepsToSecond(int steps) {
        //Walking or pushing a wheelchair at a moderate pace: 1 mile in 20 minutes = 2,000 steps
        double oneStepTime = 0.6;//one step taking in 1.6 second
        double timeOfSteps;
        timeOfSteps = oneStepTime * steps;
        return timeOfSteps;
    }

    public static double runningStepsToSecond(int steps) {
        //Walking or pushing a wheelchair at a moderate pace: 1 mile in 20 minutes = 2,000 steps
        double oneStepTime = 0.6;//one step taking in 1.6 second
        double timeOfSteps;
        timeOfSteps = oneStepTime * steps;
        return timeOfSteps;
    }

    public static String secondToMinutes() {
        int totalSeconds = 222;
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;
        String time = minutes + ":" + seconds;
        return time;
    }

    public static String formatDate(long date) {
        String formattedDate = null;
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        // DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        formattedDate = df.format(new Date(date));
        return formattedDate;
    }

    public static String getLastSevenDaysTimpStamp(int minusValue) {
        TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.DAY_OF_WEEK, minusValue);
        Date newDate = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(newDate);
        return formattedDate;
    }

    public static String todayDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String convertStringToTimestamp(String strDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();

        return String.valueOf(time);
    }

    public static long convertStringToTimestamp1(String strDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();

        return time;
    }

    public static Integer getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return Integer.valueOf(new SimpleDateFormat("MM").format(cal.getTime()));
    }


/*    public  static void setAlarm(Context context,int hour,int minute, int second, int type){
        Calendar calendar = Calendar.getInstance();

// set time by open date and time picker dialog

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        Bundle bundle = new Bundle();
        Intent intent1 = new Intent(context, NotificationPublisher.class);
        bundle.putInt("type",type);
        bundle.putString("date",getCurrentDate());
        intent1.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, type, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context
                .getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }*/

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static float isFloat(String strNo) {
        float value = 0;
        try {
            value = Float.parseFloat(strNo);
            return value;
        } catch (NumberFormatException ex) {
            // Do something smart here...
            return value;
        }
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

    // Using (@link getThisMonthFirstDate) method in Report layout
    public static String getThisMonthFirstDate() {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        return String.valueOf(c.getTimeInMillis());
    }

    /* Method to generate date format */
    public static String yyyymmdddToddmmyyyy(String strDate) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        String finalString = newFormat.format(date);
        return finalString;
    }

    /* Method to generate date format */
    public static String ddmmyyyyToyyyymmddd(String strDate) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        String finalString = newFormat.format(date);
        return finalString;
    }

    /* Using (@link setAlarm) method is use for providing timed notification for meal to add the nutrient values in the report. */
    public static void setAlarm(Context context, int hour, int minute, int second, int type, String userID) {

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, NotificationPublisher.class);
        bundle.putInt("type", type);
        bundle.putString("date", getCurrentDate());
        bundle.putString("userID", userID);
        intent.putExtras(bundle);
        Calendar breakFast = Calendar.getInstance();
        Calendar launch = Calendar.getInstance();
        Calendar dinner = Calendar.getInstance();
        Calendar extra = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();
        long currentTime = currentCal.getTimeInMillis();

        // set times
        switch (type) {
            case 1:
                breakFast.set(Calendar.HOUR_OF_DAY, hour);
                breakFast.set(Calendar.MINUTE, minute);
                breakFast.set(Calendar.SECOND, second);
                Log.d("Breakfast", "Breakfast : " + breakFast.getTimeInMillis());
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, type, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmMgr.cancel(alarmIntent);
                if (breakFast.getTimeInMillis() >= currentTime) {
                    // you can add buffer time too here to ignore some small differences in milliseconds
                    // set from today
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, breakFast.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
                } else {
                    // set from next day
                    // you might consider using calendar.add() for adding one day to the current day
                    breakFast.add(Calendar.DAY_OF_MONTH, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, breakFast.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
                }
                break;
            case 2:
                launch.set(Calendar.HOUR_OF_DAY, hour);
                launch.set(Calendar.MINUTE, minute);
                launch.set(Calendar.SECOND, second);
                PendingIntent alarmIntent2 = PendingIntent.getBroadcast(context, type, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmMgr.cancel(alarmIntent2);
                if (launch.getTimeInMillis() >= currentTime) {
                    // you can add buffer time too here to ignore some small differences in milliseconds
                    // set from today
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, launch.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent2);
                } else {
                    // set from next day
                    // you might consider using calendar.add() for adding one day to the current day
                    launch.add(Calendar.DAY_OF_MONTH, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, launch.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent2);
                }
                break;
            case 3:
                dinner.set(Calendar.HOUR_OF_DAY, hour);
                dinner.set(Calendar.MINUTE, minute);
                launch.set(Calendar.SECOND, second);
                PendingIntent alarmIntent3 = PendingIntent.getBroadcast(context, type, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmMgr.cancel(alarmIntent3);
                if (dinner.getTimeInMillis() >= currentTime) {
                    // you can add buffer time too here to ignore some small differences in milliseconds
                    // set from today
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, dinner.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent3);
                } else
                    // set from next day
                    // you might consider using calendar.add() for adding one day to the current day
                    dinner.add(Calendar.DAY_OF_MONTH, 1);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, dinner.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent3);
                break;

            case 4:
                extra.set(Calendar.HOUR_OF_DAY, hour);
                extra.set(Calendar.MINUTE, minute);
                extra.set(Calendar.SECOND, second);
                PendingIntent alarmIntent4 = PendingIntent.getBroadcast(context, type, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmMgr.cancel(alarmIntent4);
                if (extra.getTimeInMillis() >= currentTime) {
                    // you can add buffer time too here to ignore some small differences in milliseconds
                    // set from today
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, extra.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent4);
                } else
                    // set from next day
                    // you might consider using calendar.add() for adding one day to the current day
                    extra.add(Calendar.DAY_OF_MONTH, 1);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, extra.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent4);
                break;
        }
    }


    /* Using (@link calculateCaloriesRequired) used to calculate calories required per day. */
    public static double calculateCaloriesRequired(String gender, String activityLevel, double height, double weight, int age){
        double calculatedBMR;

        if (gender.equals("male")) {
            calculatedBMR = 66.5 + (13.7*weight) + (5*height) - (6.76*age);
            if (activityLevel.equals("Low")) {
                caloriesPerDay = calculatedBMR * 1.375;
            } else if (activityLevel.equals("Moderate")) {
                caloriesPerDay = calculatedBMR * 1.55;
            } else {
                caloriesPerDay = calculatedBMR * 1.725;
            }

        } else {
            calculatedBMR = 655 + (9.56*weight) + (1.8*height) - (4.68*age);
            if (activityLevel.equals("Low")) {
                caloriesPerDay = calculatedBMR * 1.375;
            } else if (activityLevel.equals("Moderate")) {
                caloriesPerDay = calculatedBMR * 1.55;
            } else {
                caloriesPerDay = calculatedBMR * 1.725;
            }
        }
        return caloriesPerDay;
    }

    /* Using (@link getProtein) used to calculate protein value required per day. */
    public static double getProtein(double bodyweight) {
        double PRO_G_PER_LB = .825;
        bodyweight = bodyweight * 2.2046226218;
        protein = PRO_G_PER_LB * bodyweight;

        return protein;
    }

    /* Using (@link getFat) used to calculate fat value required per day. */
    public static double getFat() {
        double FAT_CALS = 9;
        fat = (caloriesPerDay * .25) / FAT_CALS;
        return fat;
    }

    /* Using (@link getCarbs) used to calculate carbohydrate value required per day. */
    public static double getCarbs(){
        double PRO_CARB_CALS = 4;
        double FAT_CALS = 9;
        double remainder;
        double carbs;
        remainder = caloriesPerDay - ((PRO_CARB_CALS * protein) + (FAT_CALS * fat));
        carbs = remainder / PRO_CARB_CALS;
        return carbs;
    }

    public static int getLeftCalorie(Context context, String name, String defaultValue, float weight, int steps) {
        int left;
        left = (int) caloriesPerDay - (Integer.parseInt(getCalorieShared(context, name, defaultValue))) + ((int)caloriesBurnedForWalking(weight, steps));
//        Log.d(tag, "left calories, goal, food, exercise: "+left + goalCalorie + foodCalorie + exerciseCalorie);
        return left;
    }

    /**
     * Method to Hide Soft Input Keyboard
     */
    public static void HideKeyboardMain(Activity mContext, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            // R.id.search_img
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    public static double roundDouble(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}
