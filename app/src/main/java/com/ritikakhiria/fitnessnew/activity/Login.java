package com.ritikakhiria.fitnessnew.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Logger;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.Utils.WebCalls;
import com.ritikakhiria.fitnessnew.model.LoginModel;
import com.ritikakhiria.fitnessnew.service.AppJobManager;
import com.ritikakhiria.fitnessnew.service.GetNutritionsDataFromServerJob;
import com.ritikakhiria.fitnessnew.service.GetTrackingDataFromServer;

import org.json.JSONObject;

public class Login extends AppCompatActivity implements OnWebCall {

    Button login_button;
    EditText user_name, password;
    String userNameString = "";
    String passwordString = "";
    Session session;
    TextView forgot_password, login_signup;
    CheckBox show_hide_password;

    private String TAG = Login.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {

        session = Session.getSession(Login.this);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });

        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);

        /* Set check listener over checkbox for showing and hiding password. */
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button,
                                         boolean isChecked) {

                // If it is checked then show password else hide password
                if (isChecked) {
                    show_hide_password.setText(R.string.hide_pwd); // change checkbox text

                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); // show password
                } else {
                    show_hide_password.setText(R.string.show_pwd); // change checkbox text

                    password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance()); // hide password
                }

            }
        });

        login_signup = (TextView) findViewById(R.id.login_signup);
        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegister();

            }
        });

        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameString = user_name.getText().toString().trim();
                passwordString = password.getText().toString().trim();

                if (userNameString.equalsIgnoreCase("")) {
                    Logger.showToast(Login.this, "Please enter username");
                } else if (passwordString.equalsIgnoreCase("")) {
                    Logger.showToast(Login.this, "Please enter password");
                } else {
                    LoginModel model = new LoginModel();
                    model.setEmail(userNameString);
                    model.setPassword(passwordString);

                    WebCalls webCalls = new WebCalls(Login.this);
                    webCalls.showProgress(true);
                    webCalls.setWebCallListner(Login.this);
                    webCalls.login(model);
                }
            }
        });

    }

    private void sendToRegister() {
        startActivity(new Intent(Login.this, Register.class));
    }

    private void sendToDashBoard() {
        startActivity(new Intent(Login.this, MainDashboard.class));
        finish();
    }


    @Override
    public void OnWebCallSuccess(String userFullData) {
        try {
            synchronized (this) {
                JSONObject jobj = new JSONObject(userFullData);
                Log.d(TAG, "Login Response == " + userFullData);

//                String username = jobj.getString("name");
                String username = Utils.getString("name", jobj);
                session.setUsername(username);

//                String contact = jobj.getString("contact");
                String contact = Utils.getString("contact", jobj);
                session.setContact(contact);

//                String id = jobj.getString("id");
                String id = Utils.getString("id", jobj);
                session.setUserId(id);

//                String age = jobj.getString("age");
                String age = Utils.getString("age", jobj);
                session.setAge(age);

//                String height = jobj.getString("height");
                String height = Utils.getString("height", jobj);
                session.setHeight(height);

//                String weight = jobj.getString("weight");
                String weight = Utils.getString("weight", jobj);
                session.setWeight(weight);

                String gender = Utils.getString("gender", jobj);
                session.setGender(gender);

//                String allergy = jobj.getString("allergy");
                String allergy = Utils.getString("allergy", jobj);
                session.setAllergy(allergy);

//                String activityLevel = jobj.getString("activityLevel");
                String activityLevel = Utils.getString("activityLevel", jobj);
                session.setActivityLevel(activityLevel);

//                String foodHabit = jobj.getString("foodHabit");
                String foodHabit = Utils.getString("foodHabit", jobj);
                session.setFoodHabit(foodHabit);

//                String nu_meal = jobj.getString("nu_meal");
//                session.setNu_meal(nu_meal);

                String userProfile = Utils.getString("profileImage", jobj);
                session.setUserProfileUrl(userProfile);

                session.setUserEmail("" + userNameString);
            }
            AppJobManager.getJobManager().addJobInBackground(new GetTrackingDataFromServer(session.getUserId()));
            AppJobManager.getJobManager().addJobInBackground(new GetNutritionsDataFromServerJob(session.getUserId()));
            sendToDashBoard();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void OnWebCallError(String errorMessage) {
        Logger.showToast(Login.this, errorMessage);
    }
}