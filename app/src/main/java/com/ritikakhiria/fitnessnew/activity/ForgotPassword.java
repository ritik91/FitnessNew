package com.ritikakhiria.fitnessnew.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Logger;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.Utils.WebCalls;
import com.ritikakhiria.fitnessnew.model.ForgotModel;
import com.ritikakhiria.fitnessnew.model.SignUpModel;

public class ForgotPassword extends AppCompatActivity implements OnWebCall {

    EditText email;
    Button submit;
    private final String Fill_user_email = "Please enter user email";
    private final String Fill_user_correct_email = "Please enter valid email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = (EditText) findViewById(R.id.email_forgot);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString().trim();
                if(emailStr.equalsIgnoreCase("")){
                    Logger.showToast(ForgotPassword.this,Fill_user_email);
                } else if(!(Utils.isValidEmail(emailStr))){
                    Logger.showToast(ForgotPassword.this,Fill_user_correct_email);
                } else {
                    ForgotModel model = new ForgotModel();
                    model.setEmail(emailStr);
                    WebCalls webCalls = new WebCalls(ForgotPassword.this);
                    webCalls.setWebCallListner(ForgotPassword.this);
                    webCalls.showProgress(true);
                    webCalls.ForgotPassword(model);

                }
            }
        });

    }
    @Override
    public void OnWebCallSuccess(String userFullData) {
        try{
            synchronized (this){
                Logger.showToast(ForgotPassword.this,"Password Sent to your email id.");
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void OnWebCallError(String errorMessage) {
        Logger.showToast(ForgotPassword.this,errorMessage);
    }
}