package com.ritikakhiria.fitnessnew.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.MyStepModel;
import com.ritikakhiria.fitnessnew.model.NutritionModel;

import java.util.ArrayList;

public class Splash extends Activity {

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        synchronized (this) {
            session = Session.getSession(Splash.this);
        }

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (TextUtils.isEmpty(session.getUserId())) {
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                } else {
                    startActivity(new Intent(Splash.this, MainDashboard.class));
                    finish();
                }

            }
        }.start();
    }
}
