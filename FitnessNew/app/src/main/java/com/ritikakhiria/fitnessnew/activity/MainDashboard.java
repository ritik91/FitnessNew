package com.ritikakhiria.fitnessnew.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Logger;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.WebCalls;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.service.AppJobManager;
import com.ritikakhiria.fitnessnew.service.SendNutritionsDataToServerJob;
import com.ritikakhiria.fitnessnew.service.SendTrackingDataToServerJob;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ritikakhiria.fitnessnew.Utils.Utils.setAlarm;

public class MainDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String tag = MainDashboard.class.getName();

    CuboidButton activityTracker, mealPlanner, trackMe, healthBlog;
    CircleImageView profileImageOnNavigation;
    CircleImageView profileImage;
    Session session;
    Context context;
    private WebCalls webCalls;
    private SharedPreferences sharedpreferences;

    private void init() {
        session = Session.getSession(MainDashboard.this);
        Logger.log("user id:::" + session.getUserId());

        mealPlanner = ((CuboidButton) findViewById(R.id.meal_cuboid_button));
        mealPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, DietType.class));
            }
        });

        activityTracker = ((CuboidButton) findViewById(R.id.activity_cuboid_button));
        activityTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, MySteps.class));
            }
        });


        trackMe = ((CuboidButton) findViewById(R.id.report_cuboid_button));
        trackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, TrackMeNewActivity.class));
            }
        });

        healthBlog = ((CuboidButton) findViewById(R.id.blog_cuboid_button));
        healthBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, HealthBlog.class));
            }
        });

        context = this;

        // Calling method for notification.
        sharedpreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Log.d(tag, "before alarm set :" + sharedpreferences.getBoolean("alarmSet", false));
        setAlarm(context, 23, 59, 59, 4, session.getUserId());//For Sync
        setAlarm(context, 12, 43, 0, 1, null);//For Breakfast
        if (!sharedpreferences.getBoolean("alarmSet", false)) {
            setAlarm(context, 9, 0, 0, 1, null);//For Breakfast
            setAlarm(context, 13, 0, 0, 2, null);//For launch
            setAlarm(context, 17, 0, 0, 3, null);//For Dinner
            editor.putBoolean("alarmSet", true);
            editor.commit();
            Log.d(tag, "alarms have been set :" + sharedpreferences.getBoolean("alarmSet", false));
        }
        Log.d(tag, "after alarm set :" + sharedpreferences.getBoolean("alarmSet", false));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        synchronized (this) {
            init();
        }

        /* Setting up the navigation bar. */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        profileImageOnNavigation= (CircleImageView) header.findViewById(R.id.img_profile_picture_on_navigation);
        TextView name = (TextView) header.findViewById(R.id.name);
        TextView email = (TextView) header.findViewById(R.id.email_id);

        profileImage = (CircleImageView)findViewById(R.id.img_profile_picture);

        name.setText(session.getUsername());
        email.setText(session.getUserEmail());
        setProfilePicture(); //

        if (DBHelper.getInstance(MainDashboard.this).getUnSyncedTracking().size() > 0) {
            AppJobManager.getJobManager().addJobInBackground(new SendTrackingDataToServerJob(session.getUserId()));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
        * automatically handle clicks on the Home/Up button, so long
        * as you specify a parent activity in AndroidManifest.xml. */
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            startActivity(new Intent(MainDashboard.this, MainDashboard.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (id == R.id.my_account) {
            startActivity(new Intent(MainDashboard.this, Profile.class));
        } else if (id == R.id.my_activity) {
            startActivity(new Intent(MainDashboard.this, MySteps.class));
//        } else if (id == R.id.my_activity) {
//            startActivity(new Intent(MainDashboard.this, HistoryActivity.class));
        } else if (id == R.id.my_diet) {
            startActivity(new Intent(MainDashboard.this, DietType.class));
        } else if (id == R.id.track_me_report) {
            startActivity(new Intent(MainDashboard.this, TrackMeNewActivity.class));
        } else if (id == R.id.blog_post) {
            startActivity(new Intent(MainDashboard.this, HealthBlog.class));
        } else if (id == R.id.my_log) {
            startActivity(new Intent(MainDashboard.this, UserLogActivity.class));
        } else if (id == R.id.help) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "feedback@admin.address" });
            startActivity(Intent.createChooser(intent, ""));
        } else if (id == R.id.nav_logout) {

            AppJobManager.getJobManager().addJobInBackground(new SendTrackingDataToServerJob(session.getUserId()));
            AppJobManager.getJobManager().addJobInBackground(new SendNutritionsDataToServerJob(session.getUserId()));
            session.setUserId("");
            startActivity(new Intent(MainDashboard.this, Login.class));
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /* (@link setProfilePicture) method use to setup the profile picture */
    public void setProfilePicture(){

        if(!session.getUserProfile().equals("")) {
            File imgFile = new  File(session.getUserProfile());

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                profileImageOnNavigation.setImageBitmap(myBitmap);
                profileImage.setImageBitmap(myBitmap);
            }
        }else {
            Glide.with(this)
                    .load(session.getUserProfileUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .placeholder(getDrawable(R.drawable.bg))
                    .skipMemoryCache(true)
                    .into(profileImageOnNavigation);

            Glide.with(this)
                    .load(session.getUserProfileUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .placeholder(getDrawable(R.drawable.bg))
                    .skipMemoryCache(true)
                    .into(profileImage);

        }
    }
}
