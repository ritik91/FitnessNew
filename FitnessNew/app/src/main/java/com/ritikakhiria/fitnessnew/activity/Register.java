package com.ritikakhiria.fitnessnew.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Logger;
import com.ritikakhiria.fitnessnew.Utils.WebCalls;
import com.ritikakhiria.fitnessnew.model.SignUpModel;

public class Register extends AppCompatActivity implements OnWebCall {

    RadioGroup radioGroup_gender, radioGroup_foodhabit;
    Button login_signup;
    TextView already_user;
    EditText user_name, user_email, user_phone, user_weight, user_height, user_age, user_goal;
    String user_nameString, user_emailString, user_phoneString, user_weightString, user_goalString, user_heightString, user_ageString, user_allergyString,
            user_activityString, user_genderString, user_foodHabitString;
    Spinner spiActivityLevel, spiFoodHabit, spiUserAllergy;
    private final String Fill_user_name = "Please enter user name";
    //private final String Fill_user_nu_meal = "Please enter number of meal";
    private final String Fill_user_correct_email = "Please enter valid email";
    private final String Fill_user_email = "Please enter user email";
    private final String Fill_user_phone = "Please enter user phone number";
    private final String Fill_user_phone_invalid = "Please enter valid number";
    private final String Fill_user_weight = "Please enter user weight";
    private final String Fill_user_goal = "Please enter user goal";
    private final String Fill_user_height = "Please enter user height";
    private final String Fill_user_gender = "Please enter user gender";
    private final String Fill_user_age = "Please enter user age";
    private final String Fill_user_foodhabit = "Please enter user food habit";
    private final String Fill_user_allergy = "Please enter user allergy";
    private final String Fill_user_activity = "Please enter user activity level";
    private final String Network_not_connected = "Internet Not Connected! Please Try Again";
    private String TAG = Register.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void init() {

        user_name = (EditText) findViewById(R.id.user_name);
        user_email = (EditText) findViewById(R.id.user_email);
        user_phone = (EditText) findViewById(R.id.user_phone);
        user_height = (EditText) findViewById(R.id.user_height);
        user_weight = (EditText) findViewById(R.id.user_weight);
        user_age = (EditText) findViewById(R.id.user_age);
        //user_allergy = (EditText) findViewById(R.id.user_allergy);
        spiActivityLevel = (Spinner) findViewById(R.id.userActivityLevel);
        spiFoodHabit = (Spinner) findViewById(R.id.userFoodHabit);
        spiUserAllergy = (Spinner) findViewById(R.id.userAllergy);
        //user_activityLevel = (EditText) findViewById(R.id.user_activity_level);
        //user_foodHabit = (EditText) findViewById(R.id.user_food_habit);
        user_genderString = "";
        radioGroup_gender = (RadioGroup) findViewById(R.id.userGender);
        //radioGroup_foodhabit = (RadioGroup)findViewById(R.id.radioGroup_foodhabit);
        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.male) {
                    user_genderString = "male";
                    Log.d(TAG, "Male selected" + user_genderString);
                } else if (checkedId == R.id.female) {
                    user_genderString = "female";
                    Log.d(TAG, "Female selected" + user_genderString);
                } else {
                    user_genderString = "";
                    Log.d(TAG, "Nothing selected" + user_genderString);

                }
            }
        });
//        radioGroup_foodhabit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if(checkedId == R.id.radioButton_veg) {
//                    user_foodhabitString = "veg";
//                } else if(checkedId == R.id.radioButton_nonveg) {
//                    user_foodhabitString="non-veg";
//                }
//                else {
//                    user_foodhabitString="";
//                }
//            }
//        });

        already_user = (TextView) findViewById(R.id.already_user_text);
        already_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alreadyUserIntent = new Intent(Register.this, Login.class);
                startActivity(alreadyUserIntent);
            }
        });

        login_signup = (Button) findViewById(R.id.signUp_button);
        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_nameString = user_name.getText().toString().trim();
                user_emailString = user_email.getText().toString().trim();
                user_phoneString = user_phone.getText().toString().trim();
                user_weightString = user_weight.getText().toString().trim();
                user_goalString = "2400";
                user_heightString = user_height.getText().toString().trim();
                user_ageString = user_age.getText().toString().trim();
                user_allergyString = (String) spiUserAllergy.getSelectedItem();
                user_activityString = (String) spiActivityLevel.getSelectedItem();
                user_foodHabitString = (String) spiFoodHabit.getSelectedItem();

                if (user_nameString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_name);
                } else if (user_emailString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_email);
                } else if (!(isValidEmail(user_emailString))) {
                    Logger.showToast(Register.this, Fill_user_correct_email);
                } else if (user_phoneString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_phone);
                } else if (user_phoneString.length() != 10) {
                    Logger.showToast(Register.this, Fill_user_phone_invalid);
                } else if (user_weightString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_weight);
                } else if (user_goalString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_goal);
                } else if (user_heightString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_height);
                } else if (user_ageString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_age);
                } else if (user_genderString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_gender);
                } else if (user_foodHabitString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_foodhabit);
                } else if (user_allergyString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_allergy);
                } else if (user_activityString.equalsIgnoreCase("")) {
                    Logger.showToast(Register.this, Fill_user_activity);
                } else {
                    if (isOnline(Register.this)) {
                        SignUpModel model = new SignUpModel();
                        model.setEmail(user_emailString);
                        model.setContact(user_phoneString);
                        Log.d(TAG, "Model Set NAME = "+  user_nameString);
                        model.setName(user_nameString);
                        Log.d(TAG, "Model Get NAME = "+  model.getName());
                        model.setAge(user_ageString);
                        model.setHeight(user_heightString);
                        model.setWeight(user_weightString);
                        Log.d(TAG, "Model Set Gender = "+  user_genderString);
                        model.setGender(user_genderString);
                        Log.d(TAG, "Model Get Gender = "+  model.getGender());
                        //model.setFoodHabit(user_foodHabitString);
                        model.setFoodHabit(user_foodHabitString);
                        model.setAllergy(user_allergyString);
                        model.setActivityLevel(user_activityString);

                        WebCalls webCalls = new WebCalls(Register.this);
                        webCalls.setWebCallListner(Register.this);
                        webCalls.showProgress(true);
                        webCalls.SignUp(model);
                    } else {
                        Logger.showToast(Register.this, Network_not_connected);
                    }
                }

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void OnWebCallSuccess(String userFullData) {
        Log.d(TAG, "REGISTER ON WEBCALL SUCCESS == "+ userFullData);
        Logger.showToast(Register.this, userFullData);
        finish();
    }

    @Override
    public void OnWebCallError(String errorMessage) {
        Logger.showToast(Register.this, errorMessage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Register.this, Login.class));
        finish();
    }
}