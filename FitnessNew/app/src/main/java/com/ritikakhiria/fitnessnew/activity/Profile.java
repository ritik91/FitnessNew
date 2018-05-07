package com.ritikakhiria.fitnessnew.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ritikakhiria.fitnessnew.Constant.Constants;
import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Logger;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.WebCalls;
import com.ritikakhiria.fitnessnew.model.EditModel;
import com.ritikakhiria.fitnessnew.response.ApiResponse;
import com.ritikakhiria.fitnessnew.retrofit.ApiClient;
import com.ritikakhiria.fitnessnew.retrofit.ApiService;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements OnWebCall {

    EditText edit_name,edit_email,edit_phone,edit_weight,edit_goal,edit_height,edit_age;
    RadioGroup radioGroup_gender;
    Session session;
    Button save;
    ImageView profileImage;
    TextView tvUserName, tvUserEmail;
    Spinner spiActivityLevel, spiFoodHabit, spiUserAllergy;
    String user_nameString,user_emailString,user_phoneString,user_weightString,user_goalString,user_heightString,user_ageString,user_foodhabitString,user_allergyString, user_activityString, user_genderString;
    private final String Fill_user_name = "Please enter user name";
    private final String Fill_user_nu_meal = "Please enter number of meal";
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
    private final String Fill_user_activityLevel = "Please enter user activity level";
    private final String Network_not_connected = "Internet Not Connected! Please Try Again";
    private String tag = Profile.class.getSimpleName();
    private String[] mActivityLevel;
    private String[] mFoodHabit;
    private String[] mUserAllergy;
    private final int PERMS_REQUEST_CODE = 1;
    private final int select_photo = 1;
    private WebCalls webCalls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init(){

        session = Session.getSession(Profile.this);

        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_email = (EditText)findViewById(R.id.edit_email);
        edit_phone = (EditText)findViewById(R.id.edit_phone);
        edit_height = (EditText)findViewById(R.id.edit_height);
        edit_weight = (EditText)findViewById(R.id.edit_weight);
        edit_age = (EditText)findViewById(R.id.edit_age);
//        edit_allergy = (EditText)findViewById(R.id.edit_allergy);
//        edit_activityLevel = (EditText)findViewById(R.id.edit_activity);
//        edit_foodHabit = (EditText)findViewById(R.id.edit_habit);
        edit_email.setEnabled(false);

        edit_name.setText(""+session.getUsername());
        edit_email.setText(""+session.getUserEmail());
        edit_phone.setText(""+session.getContact());
        edit_age.setText(""+session.getAge());
        edit_height.setText(""+session.getHeight());
//        edit_allergy.setText(""+session.getAllergy());
        edit_weight.setText(""+session.getWeight());
//        edit_foodHabit.setText(""+session.getFoodHabit());
//        edit_activityLevel.setText(""+session.getActivityLevel());
        radioGroup_gender = (RadioGroup)findViewById(R.id.radioGroup_gender);

        tvUserName = (TextView)findViewById(R.id.tv_userName);
        tvUserEmail= (TextView)findViewById(R.id.tv_user_email);
        tvUserName.setText(session.getUsername());
        tvUserEmail.setText(session.getUserEmail());

        profileImage = (ImageView)findViewById(R.id.user_profile_image);

        spiActivityLevel = (Spinner) findViewById(R.id.userActivityLevel);
        spiFoodHabit = (Spinner) findViewById(R.id.userFoodHabit);
        spiUserAllergy = (Spinner) findViewById(R.id.userAllergy);
        mActivityLevel = getResources().getStringArray(R.array.spinner_activityLevel);
        mFoodHabit = getResources().getStringArray(R.array.spinner_foodHabit);
        mUserAllergy = getResources().getStringArray(R.array.spinner_allergy);
        for(int i = 0; i < mActivityLevel.length; i++){
            if(mActivityLevel[i].equalsIgnoreCase(session.getActivityLevel())){
                spiActivityLevel.setSelection(i);
                user_activityString = mActivityLevel[i];
            }
        }

        spiActivityLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_activityString = mActivityLevel[position];
                Log.d(tag,"Activity Level changed : "+user_activityString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for(int i = 0; i < mFoodHabit.length; i++){
            if(mFoodHabit[i].equalsIgnoreCase(session.getFoodHabit())){
                spiFoodHabit.setSelection(i);
                user_foodhabitString = mFoodHabit[i];
                Log.d(tag,"Food Habbit :"+user_foodhabitString);
            }
        }
        spiFoodHabit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_foodhabitString = mFoodHabit[position];
                Log.d(tag,"mFoodHabit Level changed : "+mFoodHabit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for(int i = 0; i < mUserAllergy.length; i++){
            if(mUserAllergy[i].equalsIgnoreCase(session.getAllergy())){
                spiUserAllergy.setSelection(i);
                user_allergyString = mUserAllergy[i];
            }
        }
        spiUserAllergy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_allergyString = mUserAllergy[position];
                Log.d(tag,"mFoodHabit Level changed : "+mFoodHabit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* Use to get the image from the phone gallery. */
        profileImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (hasPermissions()) {
                            // Intent to gallery
                            Intent in = new Intent(Intent.ACTION_PICK);
                            in.setType("image/*");
                            startActivityForResult(in, select_photo); // start activity to get the image
                        } else {
                            //our app doesn't have permissions, So i m requesting permissions.
                            requestPerms();
                        }
                    }
                });

        Log.d(tag,"Url of profile : "+session.getUserProfileUrl());
        if(!session.getUserProfile().equals("")) {
            File imgFile = new  File(session.getUserProfile());

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                profileImage.setImageBitmap(myBitmap);

            }
        }else {
            Glide.with(this)
                    .load(session.getUserProfileUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .into(profileImage);
        }
        //radioGroup_foodhabit = (RadioGroup)findViewById(R.id.radioGroup_foodhabit_edit);
        user_genderString ="";
        String gender = session.getGender();
        Log.d("CJ PRINT","GENDER - "+gender.trim());
        if(gender.trim().equalsIgnoreCase("male"))
        {
            radioGroup_gender.check(R.id.male);
            user_genderString = "male";
        }else {
            radioGroup_gender.check(R.id.female);
            user_genderString = "female";
        }

//        String foodhabbit = session.getFoodHabit();
//        if(foodhabbit.trim().equals("veg"))
//        {
//            radioGroup_foodhabit.check(R.id.radioButton_veg_edit);
//            user_foodhabitString = "veg";
//        }else if(foodhabbit.trim().equals("non-veg"))
//        {
//            radioGroup_foodhabit.check(R.id.radioButton_nonveg_edit);
//            user_foodhabitString = "non-veg";
//        }

        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.male) {
                    user_genderString = "male";
                } else if(checkedId == R.id.female) {
                    user_genderString ="female";
                }
                else {
                    user_genderString ="";
                }
            }
        });
//        radioGroup_foodhabit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if(checkedId == R.id.radioButton_veg_edit) {
//                    user_foodhabitString = "veg";
//                } else if(checkedId == R.id.radioButton_nonveg_edit) {
//                    user_foodhabitString="non-veg";
//                }
//                else {
//                    user_foodhabitString="";
//                }
//            }
//        });



        save = (Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // int no_meals = Integer.parseInt(edit_diet_plan.getText().toString());
                //int setAlarm = (Integer)(18/no_meals);
                user_nameString = edit_name.getText().toString().trim();
                user_emailString = edit_email.getText().toString().trim();
                user_phoneString = edit_phone.getText().toString().trim();
                user_weightString = edit_weight.getText().toString().trim();
                user_goalString = "2400";
                user_heightString = edit_height.getText().toString().trim();
                user_ageString = edit_age.getText().toString().trim();
                /*user_allergyString = edit_allergy.getText().toString().trim();
                user_activityString = edit_activityLevel.getText().toString().trim();
                user_foodhabitString = edit_foodHabit.getText().toString().trim();*/

                if(user_nameString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_name);
                } else if(user_emailString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_email);
                } else if(!(isValidEmail(user_emailString))){
                    Logger.showToast(Profile.this,Fill_user_correct_email);
                } else if(user_phoneString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_phone);
                }else if(user_phoneString.length()!=10){
                    Logger.showToast(Profile.this,Fill_user_phone_invalid);
                }else if(user_weightString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_weight);
                }else if(user_goalString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_goal);
                }else if(user_heightString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_height);
                }else if(user_ageString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_age);
                } else if(user_genderString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this, Fill_user_gender);
                }else if(user_foodhabitString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_foodhabit);
                }else if(user_allergyString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_allergy);
                }else if(user_activityString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this, Fill_user_activityLevel);
                } else {
                    if(isOnline(Profile.this)) {
                        Log.d(tag,"Your profile updated successfully");
                        EditModel model = new EditModel();
                        model.setEmail(user_emailString);
                        model.setContact(user_phoneString);
                        model.setName(user_nameString);
                        model.setAge(user_ageString);
                        model.setHeight(user_heightString);
                        model.setWeight(user_weightString);
                        model.setGender(user_genderString);
                        // model.setFoodHabit(user_foodhabitString);
                        model.setFoodHabit(user_foodhabitString);
                        model.setAllergy(user_allergyString);
                        model.setActivityLevel(user_activityString);
                        model.setId(session.getUserId());
                        WebCalls webCalls = new WebCalls(Profile.this);
                        webCalls.setWebCallListner(Profile.this);
                        webCalls.showProgress(true);
                        webCalls.EditUser(model);
                    }
                    else
                    {
                        Logger.showToast(Profile.this,Network_not_connected);
                    }
                }
            }
        });
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
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    @Override
    public void OnWebCallSuccess(String userFullData) {
        try{
            synchronized (this) {
                Log.d(tag,"User Profile  : "+userFullData);
                JSONObject jobj = new JSONObject(userFullData);

                String username = jobj.getString("name");
                session.setUsername(username);

                String contact = jobj.getString("contact");
                session.setContact(contact);

                String id = jobj.getString("id");
                session.setUserId(id);

                String age = jobj.getString("age");
                session.setAge(age);

                String height = jobj.getString("height");
                session.setHeight(height);

                String weight = jobj.getString("weight");
                session.setWeight(weight);

                String gender = jobj.getString("gender");
                session.setGender(gender);

                String allergy = jobj.getString("allergy");
                session.setAllergy(allergy);

                String activityLevel = jobj.getString("activityLevel");
                session.setActivityLevel(activityLevel);

                String foodHabit = jobj.getString("foodHabit");
                session.setFoodHabit(foodHabit);

//                String nu_meal = jobj.getString("nu_meal");
//                session.setNu_meal(nu_meal);

                //String email = jobj.getString("email");
                //session.setUserEmail(email);

            }



        }catch (Exception ex){
            ex.printStackTrace();
        }
        startActivity(new Intent(Profile.this,MainDashboard.class));
        finish();
    }

    @Override
    public void OnWebCallError(String errorMessage) {
        Logger.showToast(Profile.this,errorMessage);
    }

    /* (@link onActivityResult) use for profile image. */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case select_photo:
                if (resultCode == RESULT_OK) {
                    try {
                        // Get intent data
                        Uri selectedImageUri = data.getData();
                        Log.d(tag, "requestCode : " + selectedImageUri);
                        Bitmap bitmap = decodeUri(Profile.this, selectedImageUri, 300);
                        File imageFile = new File(getRealPathFromURI(selectedImageUri));
                        updateProfile(session.getUserId(),getRealPathFromURI(selectedImageUri));
                        webCalls = new WebCalls(Profile.this);
                        webCalls.showProgress(true);
                        /*ProfilePictureModel model = new ProfilePictureModel();
                        model.setId(Integer.valueOf(session.getUserId()));
                        model.setProfilePath(imageFile);
                        WebCalls webCalls = new WebCalls(MainDashboard.this);
                        webCalls.showProgress(true);
                        webCalls.setWebCallListner(MainDashboard.this);
                        webCalls.setUploadProfile(model);*/
                        storeImage(bitmap);

                        // call decode uri method and Check if bitmap is not null, then set image else show toast
                        if (bitmap != null) {
                            // Toast.makeText(MainDashboard.this, "Image uploaded successfully.", Toast.LENGTH_SHORT).show();
                            Log.d(tag, "Bitmap Image : " + bitmap);
                            // Set image over bitmap
                            profileImage.setImageBitmap(bitmap);
                        }else {
                            Toast.makeText(Profile.this, "Error while decoding image.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {

                        e.printStackTrace();
                        Toast.makeText(Profile.this, "File not found.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
    private boolean hasPermissions(){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allowed = true;

        switch (requestCode){
            case PERMS_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            // Intent to gallery
            Intent in = new Intent(Intent.ACTION_PICK);
            in.setType("image/*");
            startActivityForResult(in, select_photo); // start activity to get the image
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /* Method that decode uri into bitmap. This method is necessary to decode
    * large size images to load over circular image view. */
    public static Bitmap decodeUri(Context context, Uri uri,
                                   final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o2);
    }

    /* To Save your bitmap in sdcard use the following code. */
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        session.setUserProfile(String.valueOf(pictureFile));
        Log.d(tag,"File Path : "+pictureFile);
        if (pictureFile == null) {
            Log.d(tag,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(tag, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(tag, "Error accessing file: " + e.getMessage());
        }
    }

    //To Get the Path for Image Storage
    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName="profile.jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    /* (@link getRealPathFromURI) use to get the original image path to store the user image. */
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private void updateProfile(String userId, String strImgPath) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        MultipartBody.Part body = null;

        if (!TextUtils.isEmpty(strImgPath)) {
            File file = new File(strImgPath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
            body = MultipartBody.Part.createFormData(Constants.KEY_USER_IMAGE, strImgPath, requestFile);
        }

        Map<String, RequestBody> params = new HashMap();
        params.put(Constants.KEY_ID, createPartFromString(userId));
        Call<ApiResponse> response = apiService.updateProfile(params, body);
        response.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                webCalls.doneProgress();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(tag,"Error call : "+call);
                Log.d(tag,"Error call 2: "+t);
            }
        });
    }
    private RequestBody createPartFromString(String value) {
        if (TextUtils.isEmpty(value))
            value = "";
        RequestBody requestBody = RequestBody.create(MultipartBody.FORM, value);
        return requestBody;
    }

    /* Setting-up user profile image. */
    public void setProfilePicture(){

        if(!session.getUserProfile().equals("")) {
            File imgFile = new  File(session.getUserProfile());

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                profileImage.setImageBitmap(myBitmap);

            }
        }else {
            Log.d(tag, "session Url()" + session.getUserProfileUrl());
            Glide.with(this)
                    .load(session.getUserProfileUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .placeholder(getDrawable(R.drawable.bg))
                    .skipMemoryCache(true)
                    .into(profileImage);
        }
    }

    /* Hides the soft keyboard */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}

