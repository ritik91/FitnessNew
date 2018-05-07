package com.ritikakhiria.fitnessnew.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Logger {
    public static void log(String message){
        Log.wtf("ritika",message);
    }

    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
