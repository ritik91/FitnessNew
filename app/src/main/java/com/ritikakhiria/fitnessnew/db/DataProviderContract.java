package com.ritikakhiria.fitnessnew.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataProviderContract {

    private DataProviderContract() {
    }

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "com.ritikakhiria.fitnessnew";

    // The DataProvider content URI
    public static final Uri DATABASE_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    // The content provider database name
    public static final String DATABASE_NAME = "fitnessnew";

    // The starting version of the database
    public static final int DATABASE_VERSION = 1;

    /**
     * The MIME type for a content URI that would return multiple rows
     * <P>Type: TEXT</P>
     */
    public static final String MIME_TYPE_ROWS = "vnd.android.cursor.dir/vnd.com.ritikakhiria";
    //Table Name
    // User table name
    public static final String TRACKING_TABLE = "Tracking";
    public static final String NUTRITION_TABLE = "Nutrition";
    //Column Name
    public static final String TIMESTAMP = "timestamp";
    public static final String DATA = "data";
    public static final String STATUS = "status";
    public static final String SAVE_DATE = "saveDate";
    public static final String CALORIES = "calories";
    public static final String DISTANCE = "distance";

    public static final String RUNNING = "running";
    public static final String WALKING = "walking";
    public static final String STAIRS = "stairs";

    public static final String STEPS = "steps";
    public static final String MONTH = "month";
    public static final String FAT = "fat";
    public static final String FIBER = "fiber";
    public static final String SUGAR = "sugar";
    public static final String CARBS = "carbs";
    public static final String PORTEIN = "protein";
    public static final String DATE = "date";
    public static final String TYPE_OF_FOOD = "typeOfFood";


    //Query Function
    public static final String TOTAL_STEPS = "SUM(steps) as totalSteps";
    public static final String TOTAL_STAIRS = "SUM(stairs) as totalStairs";
    public static final String TOTAL_WALKING = "SUM(walking) as totalWalking";
    public static final String TOTAL_RUNNING = "SUM(running) as totalRunning";
    public static final String TOTAL_DISTANCE = "SUM(distance) as totalDistance";
    public static final String TOTAL_CALORIES = "SUM(calories) as totalCalories";
    public static final String TOTAL_FAT = "SUM(fat) as totalFat";
    public static final String TOTAL_FIBER = "SUM(fiber) as totalFiber";
    public static final String TOTAL_PROTEIN = "SUM(protein) as totalProtein";
    public static final String TOTAL_SUGAR = "SUM(sugar) as totalSugar";
    public static final String TOTAL_CARBS = "SUM(carbs) as totalCarbs";

    public static final class Tracking implements BaseColumns {

        // User table content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(DATABASE_URI, TRACKING_TABLE);

        // User table column names

        public static final String[] PROJECTION =
                {
                        _ID,
                        TIMESTAMP,
                        DATA,
                        STATUS,
                        SAVE_DATE,
                        STEPS,
                        DISTANCE,
                        RUNNING,
                        WALKING,
                        STAIRS,
                        CALORIES,
                        MONTH
                };
        public static final String[] PROJECTION_DAILY_BURNED_CALORIES =
                {
                        TOTAL_CALORIES,
                };

        public static final String[] PROJECTION_WEEKLY =
                {
                        _ID,
                        TIMESTAMP,
                        DATA,
                        STATUS,
                        SAVE_DATE,
                        STEPS,
                        DISTANCE,
                        RUNNING,
                        WALKING,
                        STAIRS,
                        CALORIES,
                        TOTAL_DISTANCE,
                        TOTAL_WALKING,
                        TOTAL_RUNNING,
                        TOTAL_STAIRS,
                        TOTAL_STEPS,
                        TOTAL_CALORIES
                };

        public static final String[] PROJECTION_MONTHLY =
                {
                        _ID,
                        TIMESTAMP,
                        DATA,
                        STATUS,
                        SAVE_DATE,
                        CALORIES,
                        TOTAL_DISTANCE,
                        STEPS,
                        TOTAL_WALKING,
                        TOTAL_RUNNING,
                        TOTAL_STAIRS,
                        TOTAL_STEPS,
                        TOTAL_CALORIES,
                        MONTH
                };

        // create Users table
        public static final String CREATE_TABLE =
                "CREATE TABLE Tracking ("+_ID+" INTEGER PRIMARY KEY, "+TIMESTAMP+" INTEGER, "+CALORIES+" VARCHAR(20), "+DISTANCE+" VARCHAR(20), "+WALKING+" INTEGER, "+RUNNING+" INTEGER, "+STAIRS+" INTEGER, "+DATA+" TEXT, "+SAVE_DATE+" VARCHAR(20), "+STEPS+" INTEGER, "+MONTH+" VARCHAR(20), "+STATUS+" INTEGER DEFAULT 0);";
    }

    public static final class TrackingWeekly implements BaseColumns {
        // User table content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(DATABASE_URI, TRACKING_TABLE);
        public static final String[] PROJECTION =
                {
                        _ID,
                        TIMESTAMP,
                        DATA,
                        STATUS,
                        SAVE_DATE,
                        DISTANCE,
                        STEPS,
                        RUNNING,
                        WALKING,
                        STAIRS,
                        CALORIES,
                        TOTAL_DISTANCE,
                        TOTAL_WALKING,
                        TOTAL_RUNNING,
                        TOTAL_STAIRS,
                        TOTAL_STEPS,
                        TOTAL_CALORIES
                };
    }

    public static final class TrackingMonthly implements BaseColumns {
        // User table content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(DATABASE_URI, TRACKING_TABLE);
        public static final String[] PROJECTION =
                {
                        _ID,
                        TIMESTAMP,
                        DATA,
                        STATUS,
                        SAVE_DATE,
                        CALORIES,
                        TOTAL_DISTANCE,
                        STEPS,
                        TOTAL_WALKING,
                        TOTAL_RUNNING,
                        TOTAL_STAIRS,
                        TOTAL_STEPS,
                        TOTAL_CALORIES,
                        MONTH
                };

    }

    public static final class Nutrition implements BaseColumns {

        // User table content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(DATABASE_URI, NUTRITION_TABLE);

        // User table column names

        public static final String[] PROJECTION =
                {
                        _ID,
                        TIMESTAMP,
                        STATUS,
                        CALORIES,
                        SUGAR,
                        FIBER,
                        FAT,
                        CARBS,
                        PORTEIN,
                        DATE,
                        TYPE_OF_FOOD
                };
        public static final String[] PROJECTION_WEEKLY =
                {
                        _ID,
                        TIMESTAMP,
                        STATUS,
                        CALORIES,
                        SUGAR,
                        FIBER,
                        FAT,
                        CARBS,
                        PORTEIN,
                        DATE,
                        TOTAL_CALORIES,
                        TOTAL_CARBS,
                        TOTAL_FAT,
                        TOTAL_FIBER,
                        TOTAL_SUGAR,
                        TOTAL_PROTEIN,
                        TYPE_OF_FOOD

                };
        public static final String[] PROJECTION_MONTHLY =
                {
                        _ID,
                        TIMESTAMP,
                        STATUS,
                        CALORIES,
                        SUGAR,
                        FIBER,
                        FAT,
                        CARBS,
                        PORTEIN,
                        DATE,
                        TOTAL_CALORIES,
                        TOTAL_CARBS,
                        TOTAL_FAT,
                        TOTAL_FIBER,
                        TOTAL_SUGAR,
                        TOTAL_PROTEIN,
                        TYPE_OF_FOOD

                };

        // create Users table
        public static final String CREATE_TABLE =
                "CREATE TABLE Nutrition (_id INTEGER PRIMARY KEY, timestamp INTEGER, calories VARCHAR(20), fat VARCHAR(20), fiber VARCHAR(20), sugar VARCHAR(20), carbs VARCHAR(20), protein VARCHAR(20), date VARCHAR(20), month VARCHAR(20), typeOfFood INTEGER, status INTEGER DEFAULT 0);";
    }
}