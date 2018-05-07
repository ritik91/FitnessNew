package com.ritikakhiria.fitnessnew.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class MyContentProvider extends ContentProvider {

    // Database specific constant declarations
    private SQLiteDatabase db;
    public static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DataProviderContract.AUTHORITY, DataProviderContract.TRACKING_TABLE, Constants.TRACKING);
        uriMatcher.addURI(DataProviderContract.AUTHORITY, DataProviderContract.NUTRITION_TABLE, Constants.NUTRITION);
    }

    private String TAG = MyContentProvider.class.getSimpleName();

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        SQLiteDatabase.loadLibs(context);
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getReadableDatabase("123456789");
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case Constants.TRACKING:
                count = db.delete(DataProviderContract.TRACKING_TABLE, selection, selectionArgs);
                break;
            case Constants.NUTRITION:
                count = db.delete(DataProviderContract.NUTRITION_TABLE, selection, selectionArgs);
                break;
        }

        if (count > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return DataProviderContract.MIME_TYPE_ROWS;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;

        switch (uriMatcher.match(uri)) {
            case Constants.TRACKING:
                // Inserts the row into the table and returns the new row's _id value
                id = db.insert(DataProviderContract.TRACKING_TABLE, "", values);

                // If the insert succeeded, notify a change and return the new row's content URI.
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {
                    throw new SQLiteException("Insert error:" + uri);
                }
            case Constants.NUTRITION:
                // Inserts the row into the table and returns the new row's _id value
                id = db.insert(DataProviderContract.NUTRITION_TABLE, "", values);

                // If the insert succeeded, notify a change and return the new row's content URI.
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {
                    throw new SQLiteException("Insert error:" + uri);
                }
            default:
                throw new IllegalArgumentException("Insert: Invalid URI" + uri);

        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor returnCursor = null;
        switch (uriMatcher.match(uri)) {
            case Constants.TRACKING:
                // Does the query against a read-only version of the database
                returnCursor = db.query(
                        DataProviderContract.TRACKING_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                // Sets the ContentResolver to watch this content URI for data changes
                Log.d(TAG,"returnCursor :"+returnCursor.getCount());
                returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return returnCursor;
            case Constants.NUTRITION:
                // Does the query against a read-only version of the database
                returnCursor = db.query(
                        DataProviderContract.NUTRITION_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                // Sets the ContentResolver to watch this content URI for data changes
                returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return returnCursor;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case Constants.TRACKING:
                count = db.update(DataProviderContract.TRACKING_TABLE, values, selection, selectionArgs);
                break;
            case Constants.NUTRITION:
                count = db.update(DataProviderContract.NUTRITION_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If the update succeeded, notify a change and return the number of updated rows.
        if (0 != count) {
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } else {
            throw new SQLiteException("Update error:" + uri);
        }
    }

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DataProviderContract.DATABASE_NAME, null, DataProviderContract.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataProviderContract.Tracking.CREATE_TABLE);
            db.execSQL(DataProviderContract.Nutrition.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
