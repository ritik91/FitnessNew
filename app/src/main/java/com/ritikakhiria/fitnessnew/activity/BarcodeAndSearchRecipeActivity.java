package com.ritikakhiria.fitnessnew.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Logger;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.Utils.WebCalls;
import com.ritikakhiria.fitnessnew.model.BarcodeModel;
import com.ritikakhiria.fitnessnew.model.CommonFoodDetail;
import com.ritikakhiria.fitnessnew.model.Foods;
import com.ritikakhiria.fitnessnew.model.MealSchedulerModel;
import com.ritikakhiria.fitnessnew.model.Nutrients;
import com.ritikakhiria.fitnessnew.model.SearchRecipeDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarcodeAndSearchRecipeActivity extends AppCompatActivity {

    private String tag = BarcodeAndSearchRecipeActivity.class.getSimpleName();

    View view;
    TextView tvProductName, tvProductWeight, tvProductCalories, tvProductSugar,
            tvProductFat, tvProductCarb, tvBarcodeNotFound, tvProductMeasure, tvProductProtein;
    ImageView imgProductImage;
    RelativeLayout rlProductDetail;

//    String nutritionName = null;

    WebCalls webCalls;
    MealSchedulerModel model;
    Button btnAddFood;
    Session session;
    ArrayList<Foods> myFoods;
    private Context context;
    String from = "";
    private Foods foods;
    private String barcode;
    private boolean isAddedProduct = false;
    String fromBarcodeOrRecipeSearch = "";
    private Bundle mbundle;
    private BarcodeModel barcodeModel;
    private SearchRecipeDetail searchRecipeDetail;
    private CommonFoodDetail mCommonFoodDetail;
    private String nixId, commonFoodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_and_search_recipe);

        int getValueFrom = 1;//Set here a value from last activity OR Fragment for breakfast, launch and dinner
        mbundle = new Bundle();
        mbundle = getIntent().getExtras();

        if (mbundle != null) {
            getValueFrom = mbundle.getInt("typeOfFoodID");

            fromBarcodeOrRecipeSearch = mbundle.getString("fromFragment");
            if (mbundle.getString("fromFragment") != null) {
                barcode = mbundle.getString("barcode");
            }
        }

        if (fromBarcodeOrRecipeSearch.equals("searchRecipe")) {
            nixId = mbundle.getString("nixItemId");
            // using for search recipe detail
        }

        if (fromBarcodeOrRecipeSearch.equals("searchRecipes")){
            commonFoodName = mbundle.getString("commonFoodName");
        }


        init();
        if (getValueFrom == 1) {
            from = Utils.breakfast;
            synchronized (this) {
                initData();
            }
            getProductDetailFromBarcode();
        } else if (getValueFrom == 2) {
            from = Utils.lunch;
            synchronized (this) {
                initData();
            }
            getProductDetailFromBarcode();


        } else if (getValueFrom == 3) {
            from = Utils.dinner;
            synchronized (this) {
                initData();
            }
            getProductDetailFromBarcode();

        }
    }

    private void getProductDetailFromBarcode() {
        webCalls.showProgress(false);
        if (fromBarcodeOrRecipeSearch.equals("searchRecipe")) {
            // using for search nutrient details for branded and grocery food from Nutritionix API calls.
            searchRecipeDetail = new SearchRecipeDetail();
            searchRecipeDetail.setNixItemId(nixId);
            webCalls.getSearchRecipeDetail(searchRecipeDetail);
        } else if (fromBarcodeOrRecipeSearch.equals("searchRecipes")) {
            // using for search nutrient details for branded and grocery food from Nutritionix API calls.
            mCommonFoodDetail = new CommonFoodDetail();
            mCommonFoodDetail.setCommonFoodName(commonFoodName);
            webCalls.getRecipeDetail(mCommonFoodDetail);
        } else {
            // using for barcode.
            barcodeModel = new BarcodeModel();
            barcodeModel.setBarcode(barcode);
            webCalls.getNutritionFromBarCodes(barcodeModel);
        }



        webCalls.setWebCallListner(new OnWebCall() {
            @Override
            public void OnWebCallSuccess(String userFullData) {
                webCalls.doneProgress();
                try {
                    Log.d(tag, "User full data :" + userFullData);
                    JSONObject jsonObject = new JSONObject(userFullData);
                    JSONArray jsonArray = jsonObject.getJSONArray("foods");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArrayForGettingNutritonId = jsonObject1.getJSONArray("full_nutrients");
                    //Log.d(tag,"Bar Code  Nutrition ID : "+jsonObjectForGetingNutritionId.getString("attr_id"));
                    try {
                        foods = new Foods();
                        if (jsonObject1.getString("food_name") != null) {
                            rlProductDetail.setVisibility(View.VISIBLE);
                            tvBarcodeNotFound.setVisibility(View.GONE);
                        }
                        foods.setName(jsonObject1.getString("food_name"));
                        tvProductName.setText(jsonObject1.getString("food_name"));
                        foods.setMeasure(jsonObject1.getString("alt_measures"));
                        tvProductMeasure.setText(jsonObject1.getString("alt_measures"));
                        foods.setNdbno("0000");
                        foods.setWeight(jsonObject1.getString("serving_weight_grams"));
                        tvProductWeight.setText(jsonObject1.getString("serving_weight_grams"));
                        JSONObject jsonArrayPhoto = jsonObject1.getJSONObject("photo");
                        foods.setImage(jsonArrayPhoto.getString("thumb"));
                        if (jsonArrayPhoto.getString("thumb") != null) {
                            Glide.with(BarcodeAndSearchRecipeActivity.this)
                                    .load(jsonArrayPhoto.getString("thumb"))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .dontAnimate()
                                    .skipMemoryCache(true)
                                    .into(imgProductImage);
                        }
                        ArrayList<Nutrients> alNutrient = new ArrayList<>();


                        for (int i = 0; i < jsonArrayForGettingNutritonId.length(); i++) {
                            JSONObject jsonObjectForGetingNutritionId = jsonArrayForGettingNutritonId.getJSONObject(i);
                            if (jsonObjectForGetingNutritionId.getInt("attr_id") == 203 ||
                                    jsonObjectForGetingNutritionId.getInt("attr_id") == 204 ||
                                    jsonObjectForGetingNutritionId.getInt("attr_id") == 205 ||
                                    jsonObjectForGetingNutritionId.getInt("attr_id") == 208 ||
                                    jsonObjectForGetingNutritionId.getInt("attr_id") == 269) {

                                Nutrients mNutrientsModel = new Nutrients();
                                mNutrientsModel.setGm("0");
                                Log.d(tag, "GM: " + mNutrientsModel.getGm());
                                Log.d(tag, "value of nutrition :" + jsonObjectForGetingNutritionId.getInt("value"));
                                mNutrientsModel.setNutrient(getNutritionName(jsonObjectForGetingNutritionId.getInt("attr_id"), jsonObjectForGetingNutritionId.getInt("value")));
                                Log.d(tag, "Nutrient: " + mNutrientsModel.getNutrient());
                                mNutrientsModel.setNutrient_id(String.valueOf(jsonObjectForGetingNutritionId.getInt("attr_id")));
                                Log.d(tag, "Nutrient Id: " + mNutrientsModel.getNutrient_id());
                                mNutrientsModel.setUnit("0");
                                Log.d(tag, "Unit: " + mNutrientsModel.getUnit());
                                mNutrientsModel.setValue(String.valueOf(jsonObjectForGetingNutritionId.getInt("value")));
                                Log.d(tag, "Value: " + mNutrientsModel.getValue());
                                alNutrient.add(mNutrientsModel);

                            }

                        }

                        Nutrients[] nutrientses = new Nutrients[alNutrient.size()];
                        nutrientses = alNutrient.toArray(nutrientses);
                        foods.setNutrients(nutrientses);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void OnWebCallError(String errorMessage) {
                Logger.showToast(context, errorMessage);
            }
        });
        if (fromBarcodeOrRecipeSearch.equals("searchRecipe")) {
            // using for search branded & grocery food detail
            webCalls.getSearchRecipeDetail(searchRecipeDetail);
        } else if (fromBarcodeOrRecipeSearch.equals("searchRecipes")) {
            // using for search recipe detail
            webCalls.getRecipeDetail(mCommonFoodDetail);
        } else {
            // using for barcode
            webCalls.getNutritionFromBarCodes(barcodeModel);//Set barcode here
        }
    }

    public void init() {
        webCalls = new WebCalls(BarcodeAndSearchRecipeActivity.this);
        tvProductName = (TextView) findViewById(R.id.tv_product_name);
        tvProductWeight = (TextView) findViewById(R.id.tv_product_weight);
        tvProductMeasure = (TextView) findViewById(R.id.tv_product_measure);
        tvProductCalories = (TextView) findViewById(R.id.tv_product_calories);
        tvProductSugar = (TextView) findViewById(R.id.tv_product_sugar);
        tvProductFat = (TextView) findViewById(R.id.tv_product_fat);
        tvProductCarb = (TextView) findViewById(R.id.tv_product_carb);
        tvProductProtein = (TextView) findViewById(R.id.tv_product_protein);
        tvBarcodeNotFound = (TextView) findViewById(R.id.tv_barcode_not_found);
        imgProductImage = (ImageView) findViewById(R.id.img_productImage);
        rlProductDetail = (RelativeLayout) findViewById(R.id.rl_product_detail);
        btnAddFood = (Button) findViewById(R.id.btn_add_food);
        session = Session.getSession(BarcodeAndSearchRecipeActivity.this);
        myFoods = new ArrayList<>();
    }

    private void initData() {
        session = Session.getSession(context);
        model = new MealSchedulerModel();
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddedProduct) {
                    try {
                        // myFoods.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(tag, "Form : " + from);
                    myFoods.add(foods);
                    if (from.equalsIgnoreCase(Utils.breakfast)) {
                        Log.d(tag, "Breakfast ::" + getString(myFoods));
                        setBreakfastView();
                        session.setBreakfastDiet(getString(myFoods));
                    } else if (from.equalsIgnoreCase(Utils.lunch)) {
                        setLunchView();
                        session.setLunchDiet(getString(myFoods));
                    } else {
                        setDinnerView();
                        session.setDinnerDiet(getString(myFoods));
                    }
                    Toast.makeText(BarcodeAndSearchRecipeActivity.this, "Added to your diet chart", Toast.LENGTH_LONG).show();
//                    comm.respond(4);
                    //((DietType) BarcodeAndSearchRecipeActivity.this).viewPager.setCurrentItem(0, true);

                    isAddedProduct = true;
                } else {

                    Toast.makeText(BarcodeAndSearchRecipeActivity.this, "You are already added", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getString(ArrayList<Foods> obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }


    public String getNutritionName(int nutritionId, int value) {
        String nutritionName = null;
        switch (nutritionId) {
            case 204: //204 means Fat
                nutritionName = "Fat";
                tvProductFat.setText(String.valueOf(value) + " g");
                break;
            case 208: //208 means Calories
                nutritionName = "Energy";
                tvProductCalories.setText(String.valueOf(value) + " kcal");
                break;
            case 269: //269 means Sugar
                nutritionName = "Sugar";
                tvProductSugar.setText(String.valueOf(value) + " g");
                break;
            case 205: //205 means Carbohydrate
                nutritionName = "Carbohydrate";
                tvProductCarb.setText(String.valueOf(value) + " g");
                break;
            case 203: //203 means Protein
                nutritionName = "Protein";
                tvProductProtein.setText(String.valueOf(value) + " g");
                break;

        }
        return nutritionName;
    }

    public void setBreakfastView() {
        String foodData = session.getBreakfastDiet();
        try {
            JSONArray jarr = new JSONArray(foodData);
            for (int i = 0; i < jarr.length(); i++) {
                Log.d(tag, "value of i : " + i);
                JSONObject jobj = jarr.getJSONObject(i);
                Foods foods = new Foods();

                String measure = jobj.getString("measure");
                foods.setMeasure(measure);

                String name = jobj.getString("name");
                foods.setName(name);

                String ndbno = jobj.getString("ndbno");
                foods.setNdbno(ndbno);

                String weight = jobj.getString("weight");
                foods.setWeight(weight);

                foods.setImage(jobj.getString("image"));

                JSONArray innerJar = jobj.getJSONArray("nutrients");
                Nutrients[] nutrients = new Nutrients[innerJar.length()];
                for (int j = 0; j < innerJar.length(); j++) {
                    JSONObject innerJob = innerJar.getJSONObject(j);
                    Nutrients nutrient = new Nutrients();

                    String gm = innerJob.getString("gm");
                    nutrient.setGm(gm);

                    String _nutrient = innerJob.getString("nutrient");
                    nutrient.setNutrient(_nutrient);

                    String nutrient_id = innerJob.getString("nutrient_id");
                    nutrient.setNutrient_id(nutrient_id);

                    String unit = innerJob.getString("unit");
                    nutrient.setUnit(unit);

                    String value = innerJob.getString("value");
                    nutrient.setValue(value);

                    nutrients[j] = nutrient;
                }
                foods.setNutrients(nutrients);

                myFoods.add(foods);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setLunchView() {
        String foodData = session.getLunchDiet();
        try {
            JSONArray jarr = new JSONArray(foodData);
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jobj = jarr.getJSONObject(i);
                Foods foods = new Foods();

                String measure = jobj.getString("measure");
                foods.setMeasure(measure);

                String name = jobj.getString("name");
                foods.setName(name);

                String ndbno = jobj.getString("ndbno");
                foods.setNdbno(ndbno);

                String weight = jobj.getString("weight");
                foods.setWeight(weight);

                foods.setImage(jobj.getString("image"));

                JSONArray innerJar = jobj.getJSONArray("nutrients");
                Nutrients[] nutrients = new Nutrients[innerJar.length()];
                for (int j = 0; j < innerJar.length(); j++) {
                    JSONObject innerJob = innerJar.getJSONObject(j);
                    Nutrients nutrient = new Nutrients();

                    String gm = innerJob.getString("gm");
                    nutrient.setGm(gm);

                    String _nutrient = innerJob.getString("nutrient");
                    nutrient.setNutrient(_nutrient);

                    String nutrient_id = innerJob.getString("nutrient_id");
                    nutrient.setNutrient_id(nutrient_id);

                    String unit = innerJob.getString("unit");
                    nutrient.setUnit(unit);

                    String value = innerJob.getString("value");
                    nutrient.setValue(value);

                    nutrients[j] = nutrient;
                }
                foods.setNutrients(nutrients);

                myFoods.add(foods);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setDinnerView() {
        String foodData = session.getDinnerDiet();
        try {
            JSONArray jarr = new JSONArray(foodData);
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jobj = jarr.getJSONObject(i);
                Foods foods = new Foods();

                String measure = jobj.getString("measure");
                foods.setMeasure(measure);

                String name = jobj.getString("name");
                foods.setName(name);

                String ndbno = jobj.getString("ndbno");
                foods.setNdbno(ndbno);

                String weight = jobj.getString("weight");
                foods.setWeight(weight);

                foods.setImage(jobj.getString("image"));

                JSONArray innerJar = jobj.getJSONArray("nutrients");
                Nutrients[] nutrients = new Nutrients[innerJar.length()];
                for (int j = 0; j < innerJar.length(); j++) {
                    JSONObject innerJob = innerJar.getJSONObject(j);
                    Nutrients nutrient = new Nutrients();

                    String gm = innerJob.getString("gm");
                    nutrient.setGm(gm);

                    String _nutrient = innerJob.getString("nutrient");
                    nutrient.setNutrient(_nutrient);

                    String nutrient_id = innerJob.getString("nutrient_id");
                    nutrient.setNutrient_id(nutrient_id);

                    String unit = innerJob.getString("unit");
                    nutrient.setUnit(unit);

                    String value = innerJob.getString("value");
                    nutrient.setValue(value);

                    nutrients[j] = nutrient;
                }
                foods.setNutrients(nutrients);

                myFoods.add(foods);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
