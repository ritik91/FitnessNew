//package com.ritikakhiria.fitnessnew.activity;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.google.gson.Gson;
//import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
//import com.ritikakhiria.fitnessnew.R;
//import com.ritikakhiria.fitnessnew.Utils.Logger;
//import com.ritikakhiria.fitnessnew.Utils.Session;
//import com.ritikakhiria.fitnessnew.Utils.Utils;
//import com.ritikakhiria.fitnessnew.Utils.WebCalls;
//import com.ritikakhiria.fitnessnew.model.Foods;
//import com.ritikakhiria.fitnessnew.model.MealSchedulerModel;
//import com.ritikakhiria.fitnessnew.model.Nutrients;
//import com.ritikakhiria.fitnessnew.model.RecipeModel;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//
//public class RecipeSearchActivity extends AppCompatActivity {
//
//    private String tag = RecipeSearchActivity.class.getSimpleName();
//
//    View view;
//    TextView tvRecipeProductName, tvRecipeProductWeight, tvRecipeProductCalories, tvRecipeProductSugar, tvRecipeProductFat,
//            tvRecipeProductCarb, tvRecipeProductProtein, tvRecipeProductMeasure;
//    ImageView imgRecipeProductImage;
//    RelativeLayout rlRecipeProductDetail;
//
//    WebCalls webCalls;
//    MealSchedulerModel model;
//    Button btnAddFood;
//    Session session;
//    ArrayList<Foods> myFoods;
//    private Context context;
//    String from = "";
//    private Foods foods;
//    private boolean isAddedProduct = false;
//    String RecipeSearch = "";
//    private Bundle mbundle;
//
//    private RecipeModel recipeDetail;
//    private String label;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.recipe_search_activity);
//
//        int getValueFrom = 1;//Set here a value from last activity OR Fragment for breakfast, launch and dinner
//        mbundle = new Bundle();
//        mbundle = getIntent().getExtras();
//
//        if (mbundle != null) {
//            getValueFrom = mbundle.getInt("typeOfFoodID");
//
//            RecipeSearch = mbundle.getString("fromFragment");
//            assert RecipeSearch != null;
//            if (RecipeSearch.equals("searchRecipe")) {
//                label = mbundle.getString("label");;
//            }
//        }
//
//
//        init();
//        if (getValueFrom == 1) {
//            from = Utils.breakfast;
//            synchronized (this) {
//                initData();
//            }
//            getRecipeProductDetail();
//        } else if (getValueFrom == 2) {
//            from = Utils.lunch;
//            synchronized (this) {
//                initData();
//            }
//            getRecipeProductDetail();
//
//
//        } else if (getValueFrom == 3) {
//            from = Utils.dinner;
//            synchronized (this) {
//                initData();
//            }
//            getRecipeProductDetail();
//
//        }
//    }
//
//    private void getRecipeProductDetail() {
//        webCalls.showProgress(false);
//        if (RecipeSearch.equals("searchRecipe")) {
//            recipeDetail = new RecipeModel();
//            recipeDetail.setName(label);
//            webCalls.getRecipeDetail(recipeDetail);
//            // using for search recipe detail
//        }
//
//        webCalls.setWebCallListner(new OnWebCall() {
//            @Override
//            public void OnWebCallSuccess(String userFullData) {
//                webCalls.doneProgress();
//                try {
//                    Log.d(tag, "User full data :" + userFullData);
//                    JSONObject jsonObject = new JSONObject(userFullData);
//                    JSONArray jsonArray = jsonObject.getJSONArray("hits");
//                     JSONObject jsonObject1 = jsonArray.getJSONObject(0);
////                    JSONArray jsonArrayForGettingNutritonId = jsonObject1.getJSONArray("full_nutrients");
//                    //Log.d(tag,"Bar Code  Nutrition ID : "+jsonObjectForGetingNutritionId.getString("attr_id"));
//                    try {
//                            foods = new Foods();
////                            JSONObject jsonObjectItems = jsonArray.getJSONObject(i);
//                            JSONObject jsonObjectItem = jsonObject1.getJSONObject("recipe");
//
//                            foods.setName(jsonObjectItem.getString("label"));
//                            tvRecipeProductName.setText(jsonObjectItem.getString("label"));
//                            int totalWeight = jsonObjectItem.getInt("totalWeight");
//                            foods.setWeight(String.valueOf(totalWeight));
//                            tvRecipeProductMeasure.setText(String.valueOf(totalWeight));
//                            foods.setNdbno("0000");
//                            int serving = jsonObjectItem.getInt("yield");
//                            foods.setMeasure(String.valueOf(serving));
//                            tvRecipeProductWeight.setText(String.valueOf(serving));
//
//                            int caloriesCal = jsonObjectItem.getInt("calories");
//                            int caloriesPerP = (caloriesCal / serving);
//                            foods.setCalories(String.valueOf(caloriesPerP));
//                            tvRecipeProductCalories.setText(String.valueOf(caloriesPerP) + " kcal");
//
//                            foods.setImage(jsonObjectItem.getString("image"));
//                            Glide.with(RecipeSearchActivity.this)
//                                    .load(jsonObjectItem.getString("image"))
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                    .dontAnimate()
//                                    .skipMemoryCache(true)
//                                    .into(imgRecipeProductImage);
//
//
//                            JSONObject jsonObjectNutrient = jsonObjectItem.getJSONObject("totalNutrients");
//
//                            JSONObject fat = jsonObjectNutrient.getJSONObject("FAT");
//                            int fatValue = fat.getInt("quantity");
//                            foods.setFat(String.valueOf(fatValue));
//                            tvRecipeProductFat.setText(String.valueOf(fatValue) + " g");
//
//                            JSONObject carbs = jsonObjectNutrient.getJSONObject("CHOCDF");
//                            int carbsValue = carbs.getInt("quantity");
//                            foods.setCarbs(String.valueOf(carbsValue));
//                            tvRecipeProductCarb.setText(String.valueOf(carbsValue) + " g");
//
//                            JSONObject protein = jsonObjectNutrient.getJSONObject("PROCNT");
//                            int proteinValue = protein.getInt("quantity");
//                            foods.setProtein(String.valueOf(proteinValue));
//                            tvRecipeProductProtein.setText(String.valueOf(proteinValue) + " g");
//
//                            JSONObject sugar = jsonObjectNutrient.getJSONObject("SUGAR");
//                            int sugarValue = sugar.getInt("quantity");
//                            foods.setProtein(String.valueOf(sugarValue));
//                            tvRecipeProductSugar.setText(String.valueOf(sugarValue) + " g");
//
//                            myFoods.add(foods);
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            @Override
//            public void OnWebCallError(String errorMessage) {
//                Logger.showToast(context, errorMessage);
//            }
//        });
//    }
//
//    public void init() {
//        webCalls = new WebCalls(RecipeSearchActivity.this);
//        tvRecipeProductName = (TextView) findViewById(R.id.tv_recipe_product_name);
//        tvRecipeProductWeight = (TextView) findViewById(R.id.tv_recipe_product_weight);
//        tvRecipeProductMeasure = (TextView) findViewById(R.id.tv_recipe_product_measure);
//        tvRecipeProductCalories = (TextView) findViewById(R.id.tv_recipe_product_calories);
//        tvRecipeProductSugar = (TextView) findViewById(R.id.tv_recipe_product_sugar);
//        tvRecipeProductFat = (TextView) findViewById(R.id.tv_recipe_product_fat);
//        tvRecipeProductCarb = (TextView) findViewById(R.id.tv_recipe_product_carb);
//        tvRecipeProductProtein = (TextView) findViewById(R.id.tv_recipe_product_protein);
//        imgRecipeProductImage = (ImageView) findViewById(R.id.img_recipe_productImage);
//        rlRecipeProductDetail = (RelativeLayout) findViewById(R.id.rl_recipe_product_detail);
//        btnAddFood = (Button) findViewById(R.id.btn_add_food);
//        session = Session.getSession(RecipeSearchActivity.this);
//        myFoods = new ArrayList<>();
//    }
//
//    private void initData() {
//        session = Session.getSession(context);
//        model = new MealSchedulerModel();
//        btnAddFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isAddedProduct) {
//                    try {
//                        // myFoods.clear();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    Log.d(tag, "Form : " + from);
//                    myFoods.add(foods);
//                    if (from.equalsIgnoreCase(Utils.breakfast)) {
//                        Log.d(tag, "Breakfast ::" + getString(myFoods));
//                        setBreakfastView();
//                        session.setBreakfastDiet(getString(myFoods));
//                    } else if (from.equalsIgnoreCase(Utils.lunch)) {
//                        setLunchView();
//                        session.setLunchDiet(getString(myFoods));
//                    } else {
//                        setDinnerView();
//                        session.setDinnerDiet(getString(myFoods));
//                    }
//                    Toast.makeText(RecipeSearchActivity.this, "Added to your diet chart", Toast.LENGTH_LONG).show();
////                    comm.respond(4);
//                    //((DietType) BarcodeAndSearchRecipeActivity.this).viewPager.setCurrentItem(0, true);
//
//                    isAddedProduct = true;
//                } else {
//
//                    Toast.makeText(RecipeSearchActivity.this, "You are already added", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    private String getString(ArrayList<Foods> obj) {
//        Gson gson = new Gson();
//        return gson.toJson(obj);
//    }
//
//
////    public String getNutritionName(int nutritionId, int value) {
////        String nutritionName = null;
////        switch (nutritionId) {
////            case 204: //204 means Fat
////                nutritionName = "Fat";
////                tvProductFat.setText(String.valueOf(value));
////                break;
////            case 208: //208 means Calories
////                nutritionName = "Energy";
////                tvProductCalories.setText(String.valueOf(value));
////                break;
////            case 269: //269 means Sugar
////                nutritionName = "Sugar";
////                tvProductSugar.setText(String.valueOf(value));
////                break;
////            case 205: //208 means Carbohydrate
////                nutritionName = "Carbohydrate";
////                tvProductCarb.setText(String.valueOf(value));
////                break;
////        }
////        return nutritionName;
////    }
//
//    public void setBreakfastView() {
//        String foodData = session.getBreakfastDiet();
//        try {
//            JSONArray jarr = new JSONArray(foodData);
//            for (int i = 0; i < jarr.length(); i++) {
//                Log.d(tag, "value of i : " + i);
//                JSONObject jobj = jarr.getJSONObject(i);
//                Foods foods = new Foods();
//
//                String measure = jobj.getString("measure");
//                foods.setMeasure(measure);
//
//                String name = jobj.getString("name");
//                foods.setName(name);
//
//                String ndbno = jobj.getString("ndbno");
//                foods.setNdbno(ndbno);
//
//                String weight = jobj.getString("weight");
//                foods.setWeight(weight);
//
////                String image = jobj.getString("image");
////                foods.setImage(image);
//
//                JSONArray innerJar = jobj.getJSONArray("nutrients");
//                Nutrients[] nutrients = new Nutrients[innerJar.length()];
//                for (int j = 0; j < innerJar.length(); j++) {
//                    JSONObject innerJob = innerJar.getJSONObject(j);
//                    Nutrients nutrient = new Nutrients();
//
//                    String gm = innerJob.getString("gm");
//                    nutrient.setGm(gm);
//
//                    String _nutrient = innerJob.getString("nutrient");
//                    nutrient.setNutrient(_nutrient);
//
//                    String nutrient_id = innerJob.getString("nutrient_id");
//                    nutrient.setNutrient_id(nutrient_id);
//
//                    String unit = innerJob.getString("unit");
//                    nutrient.setUnit(unit);
//
//                    String value = innerJob.getString("value");
//                    nutrient.setValue(value);
//
//                    nutrients[j] = nutrient;
//                }
//                foods.setNutrients(nutrients);
//
//                myFoods.add(foods);
//            }
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//    public void setLunchView() {
//        String foodData = session.getLunchDiet();
//        try {
//            JSONArray jarr = new JSONArray(foodData);
//            for (int i = 0; i < jarr.length(); i++) {
//                JSONObject jobj = jarr.getJSONObject(i);
//                Foods foods = new Foods();
//
//                String measure = jobj.getString("measure");
//                foods.setMeasure(measure);
//
//                String name = jobj.getString("name");
//                foods.setName(name);
//
//                String ndbno = jobj.getString("ndbno");
//                foods.setNdbno(ndbno);
//
//                String weight = jobj.getString("weight");
//                foods.setWeight(weight);
//
////                String image = jobj.getString("image");
////                foods.setImage(image);
//
//                JSONArray innerJar = jobj.getJSONArray("nutrients");
//                Nutrients[] nutrients = new Nutrients[innerJar.length()];
//                for (int j = 0; j < innerJar.length(); j++) {
//                    JSONObject innerJob = innerJar.getJSONObject(j);
//                    Nutrients nutrient = new Nutrients();
//
//                    String gm = innerJob.getString("gm");
//                    nutrient.setGm(gm);
//
//                    String _nutrient = innerJob.getString("nutrient");
//                    nutrient.setNutrient(_nutrient);
//
//                    String nutrient_id = innerJob.getString("nutrient_id");
//                    nutrient.setNutrient_id(nutrient_id);
//
//                    String unit = innerJob.getString("unit");
//                    nutrient.setUnit(unit);
//
//                    String value = innerJob.getString("value");
//                    nutrient.setValue(value);
//
//                    nutrients[j] = nutrient;
//                }
//                foods.setNutrients(nutrients);
//
//                myFoods.add(foods);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//    public void setDinnerView() {
//        String foodData = session.getDinnerDiet();
//        try {
//            JSONArray jarr = new JSONArray(foodData);
//            for (int i = 0; i < jarr.length(); i++) {
//                JSONObject jobj = jarr.getJSONObject(i);
//                Foods foods = new Foods();
//
//                String measure = jobj.getString("measure");
//                foods.setMeasure(measure);
//
//                String name = jobj.getString("name");
//                foods.setName(name);
//
//                String ndbno = jobj.getString("ndbno");
//                foods.setNdbno(ndbno);
//
//                String weight = jobj.getString("weight");
//                foods.setWeight(weight);
//
////                String image = jobj.getString("image");
////                foods.setImage(image);
//
//                JSONArray innerJar = jobj.getJSONArray("nutrients");
//                Nutrients[] nutrients = new Nutrients[innerJar.length()];
//                for (int j = 0; j < innerJar.length(); j++) {
//                    JSONObject innerJob = innerJar.getJSONObject(j);
//                    Nutrients nutrient = new Nutrients();
//
//                    String gm = innerJob.getString("gm");
//                    nutrient.setGm(gm);
//
//                    String _nutrient = innerJob.getString("nutrient");
//                    nutrient.setNutrient(_nutrient);
//
//                    String nutrient_id = innerJob.getString("nutrient_id");
//                    nutrient.setNutrient_id(nutrient_id);
//
//                    String unit = innerJob.getString("unit");
//                    nutrient.setUnit(unit);
//
//                    String value = innerJob.getString("value");
//                    nutrient.setValue(value);
//
//                    nutrients[j] = nutrient;
//                }
//                foods.setNutrients(nutrients);
//
//                myFoods.add(foods);
//            }
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//}
