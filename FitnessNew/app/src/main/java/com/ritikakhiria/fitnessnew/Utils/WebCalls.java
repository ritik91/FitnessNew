package com.ritikakhiria.fitnessnew.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
import com.ritikakhiria.fitnessnew.model.BarcodeModel;
import com.ritikakhiria.fitnessnew.model.CommonFoodDetail;
import com.ritikakhiria.fitnessnew.model.EditModel;
import com.ritikakhiria.fitnessnew.model.ForgotModel;
import com.ritikakhiria.fitnessnew.model.LoginModel;
import com.ritikakhiria.fitnessnew.model.RecipeModel;
import com.ritikakhiria.fitnessnew.model.SearchModel;
import com.ritikakhiria.fitnessnew.model.SearchRecipeDetail;
import com.ritikakhiria.fitnessnew.model.SignUpModel;

import org.json.JSONObject;

public class WebCalls implements CallService.OnServiceCall {

    private Context context;
    private ProgressDialog pd;
    private OnWebCall OnWebCall;

    private final int CASE_REGISTER = 0;
    private final int CASE_LOGIN = 1;
    private final int CASE_GET_TOTAL_STEPS = 2;
    private final int CASE_SET_TOTAL_STEPS = 3;
    private final int CASE_GET_MEAL_SCHEDULER = 4;
    private final int CASE_EDIT = 5;
    private final int CASE_FORGOT = 6;
    private final int CASE_GET_NUTRITION_FROM_BARCODE = 7;
    private final int CASE_GET_GROCERY_LIST = 8;
    private final int CASE_GET_RECIPE_LIST = 9;
    private final int CASE_GET_RECIPE_SEARCH_DETAIL = 10;
    private final int CASE_GET_RECIPE_DETAIL = 11;
    private final int CASE_GET_RESTAURANT_LIST = 12;

    private boolean autoDismiss;

    private String TAG = WebCalls.class.getSimpleName();

    public void showProgress(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
        pd = ProgressDialog.show(context, "Loading ...", "Please wait");
    }

    private void hideProgress() {
        if (autoDismiss) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

    public void doneProgress() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    public void setWebCallListner(OnWebCall onWebCall) {
        this.OnWebCall = onWebCall;
    }

    public WebCalls(Context context) {
        this.context = context;
    }

    private boolean checkNet() {
        return Utils.isNetworkAvailable(context);
    }

    public void login(LoginModel model) {
        doWebCall(CASE_LOGIN, Utils.getLoginUrl(model));
    }

    public void SignUp(SignUpModel model) {
        Log.d(TAG, "SIGN UP MODEL GENDER == "+model.getGender());
        doWebCall(CASE_REGISTER, Utils.getRegisterUrl(model));
    }

    public void EditUser(EditModel model) {
        doWebCall(CASE_EDIT, Utils.getEditUrl(model));
    }

    public void ForgotPassword(ForgotModel model) {
        doWebCall(CASE_EDIT, Utils.getForgotUrl(model));
    }

    public void getSteps(String custid) {
        doWebCall(CASE_GET_TOTAL_STEPS, Utils.getAllStepsUrl(custid));
    }

    public void setSteps(String steps, String custid) {
        doWebCall(CASE_SET_TOTAL_STEPS, Utils.setStepsUrl(steps, custid));
    }

    public void getMeals() {
        doWebCall(CASE_GET_MEAL_SCHEDULER, Utils.getMealScheduler());
    }

    public void getNutritionFromBarCodes(BarcodeModel barcodeModel) {
        doWebCall(CASE_GET_NUTRITION_FROM_BARCODE, Utils.getNutritionFromBarCode(barcodeModel));
    }

    public void getRecipeList(SearchModel searchModel) {
        doWebCall(CASE_GET_RECIPE_LIST, Utils.getListOfRecipe(searchModel));
    }

    public void getGroceryFoodList(SearchModel searchModel) {
        doWebCall(CASE_GET_RESTAURANT_LIST, Utils.getListOfGroceryFood(searchModel));
    }

    public void getRestaurantList(SearchModel searchModel) {
        doWebCall(CASE_GET_RESTAURANT_LIST, Utils.getListOfRestaurant(searchModel));
    }

    public void getSearchRecipeDetail(SearchRecipeDetail searchRecipeDetail) {
        doWebCall(CASE_GET_RECIPE_SEARCH_DETAIL, Utils.getRecipeSearchDetail(searchRecipeDetail));
    }

    // Get the nutrient information of common food from Nutritionix API.
    public void getRecipeDetail(CommonFoodDetail commonFoodDetail) {
        doWebCall1(CASE_GET_RECIPE_DETAIL, Utils.getRecipeDetail(commonFoodDetail));
    }

    private void doWebCall(int requestCode, String url) {
        Log.d(TAG, "URL == "+ url.toString());
        if (!checkNet()) {
            hideProgress();
            if (OnWebCall != null)
                OnWebCall.OnWebCallError(Utils.NO_NET);
        } else new CallService(requestCode, url, Utils.GET, WebCalls.this).execute();
    }

    private void doWebCall1(int requestCode, String url) {
        Log.d(TAG, "URL == "+ url.toString());
        if (!checkNet()) {
            hideProgress();
            if (OnWebCall != null)
                OnWebCall.OnWebCallError(Utils.NO_NET);
        } else new CallService(requestCode, url, Utils.POST, WebCalls.this).execute();
    }

    @Override
    public void onServiceCall(int requestCode, String response) {
        hideProgress();
        Logger.log(response);
        switch (requestCode) {
            case CASE_REGISTER:
                handleRegister(response);
                break;
            case CASE_LOGIN:
                Log.d(TAG, "LOGIN RESPONSE == "+response);
                handleLogin(response);
                break;
            case CASE_GET_TOTAL_STEPS:
                handleSteps(response);
                break;
            case CASE_SET_TOTAL_STEPS:
                handleSteps(response);
                break;
            case CASE_GET_MEAL_SCHEDULER:
                handleMeals(response);
                break;
            case CASE_EDIT:
                handleEdit(response);
            case CASE_FORGOT:
                handleForgot(response);
            case CASE_GET_NUTRITION_FROM_BARCODE:
                handleMeals(response);
                break;
            case CASE_GET_GROCERY_LIST:
                handleMeals(response);
                break;
            case CASE_GET_RECIPE_LIST:
                handleMeals(response);
                break;
            case CASE_GET_RECIPE_SEARCH_DETAIL:
                handleMeals(response);
                break;
            case CASE_GET_RECIPE_DETAIL:
                handleMeals(response);
                break;
            case CASE_GET_RESTAURANT_LIST:
                handleMeals(response);
                break;
        }
    }

    private void handleLogin(String response) {
        try {
            JSONObject jobj = new JSONObject(response);
            String _response = jobj.getString("response");
            String message = jobj.getString("message");
            if (OnWebCall != null) {
                if (_response.equalsIgnoreCase("200")) {
                    OnWebCall.OnWebCallSuccess(response);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (OnWebCall != null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleForgot(String response) {
        try {
            JSONObject jobj = new JSONObject(response);
            String _response = jobj.getString("response");
            String message = jobj.getString("message");
            if (OnWebCall != null) {
                if (_response.equalsIgnoreCase("200")) {
                    OnWebCall.OnWebCallSuccess(message);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (OnWebCall != null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleRegister(String response) {
        try {
            JSONObject jobj = new JSONObject(response);
            String _response = jobj.getString("response");
            String message = jobj.getString("message");
            if (OnWebCall != null) {
                if (_response.equalsIgnoreCase("200")) {
                    OnWebCall.OnWebCallSuccess(message);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            if (OnWebCall != null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleEdit(String response) {
        try {
            JSONObject jobj = new JSONObject(response);
            String _response = jobj.getString("response");
            String message = jobj.getString("message");
            if (OnWebCall != null) {
                if (_response.equalsIgnoreCase("200")) {
                    OnWebCall.OnWebCallSuccess(response);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            if (OnWebCall != null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleSteps(String response) {
        try {
            try {
                if (OnWebCall != null)
                    OnWebCall.OnWebCallSuccess(response);

            } catch (Exception ex) {
                ex.printStackTrace();
                if (OnWebCall != null)
                    OnWebCall.OnWebCallError(ex.toString());
            }
            /*JSONObject jobj = new JSONObject(response);
            String _response = jobj.getString("response");
            String message = jobj.getString("message");
            if(OnWebCall!=null){
                if(_response.equalsIgnoreCase("200")){
                    OnWebCall.OnWebCallSuccess(response);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
            if (OnWebCall != null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleMeals(String response) {
        try {
            if (OnWebCall != null)
                OnWebCall.OnWebCallSuccess(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            if (OnWebCall != null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

}
