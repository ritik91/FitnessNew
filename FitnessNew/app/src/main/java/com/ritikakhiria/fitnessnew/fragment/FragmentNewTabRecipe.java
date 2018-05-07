package com.ritikakhiria.fitnessnew.fragment;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Logger;
import com.ritikakhiria.fitnessnew.Utils.WebCalls;
import com.ritikakhiria.fitnessnew.activity.BarcodeAndSearchRecipeActivity;
import com.ritikakhiria.fitnessnew.adapter.RecipeAdapter;
import com.ritikakhiria.fitnessnew.model.RecipeModel;
import com.ritikakhiria.fitnessnew.model.SearchModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.ritikakhiria.fitnessnew.Utils.Utils.foodTypeId;

public class FragmentNewTabRecipe extends Fragment implements View.OnClickListener {

    public static final String TAG = FragmentNewTabRecipe.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ProgressDialog pDialog;
    private RecipeAdapter mRecipeAdapter;
    private List<RecipeModel> recipeFoodList;

    View view;

    public LinearLayoutManager linearLayoutManager;

    private SearchView foodSearch;
    public ImageButton foodVoiceButton;
    WebCalls webCalls;
    Context context;

    private int clickedPos;
    private final int SPEECH_RECOGNITION_CODE = 100;
    private static int CURRENT_PAGE = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_food, container, false);
        foodSearch = (SearchView) view.findViewById(R.id.restaurant_search);
        foodVoiceButton = (ImageButton) view.findViewById(R.id.voice_restaurant);
        foodVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });
        initAdapter();
        searchRecipe("soup");

        foodSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                //Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();
            }
        });

        foodSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // Reset SearchView
                recipeFoodList.clear();
                searchRecipe(query);
                foodSearch.clearFocus();
                foodSearch.setQuery("", false);
                foodSearch.setIconified(true);
//                foodSearch.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {

//                if (query.length() > 3)
//                {
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
//                    fetchRestaurant(query);
//                }
//                else
//                {
////                    mRecyclerView.setVisibility(View.INVISIBLE);
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//                progressBar.setVisibility(View.GONE);
                return false;
            }
        });

        return view;
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext().getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initAdapter() {
        recipeFoodList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecipeAdapter = new RecipeAdapter(getActivity(), recipeFoodList, this);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }
    private void searchRecipe(String recipeName) {
        SearchModel searchModel = new SearchModel();
        searchModel.setRecipeName(recipeName);
        webCalls = new WebCalls(getActivity());
        webCalls.showProgress(false);
        webCalls.getRecipeList(searchModel);
        webCalls.setWebCallListner(new OnWebCall() {

            @Override
            public void OnWebCallSuccess(String userFullData) {
                //Log.d(TAG,"Recipe search : "+userFullData);
                try {
                    JSONObject jsonObject = new JSONObject(userFullData);
                    JSONArray jsonArray = jsonObject.getJSONArray("common");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RecipeModel recipeFoodModel = new RecipeModel();
                        JSONObject jsonObjectItems = jsonArray.getJSONObject(i);
                        recipeFoodModel.setRecipeFoodName(jsonObjectItems.getString("food_name"));
                        recipeFoodModel.setServingQuanity(jsonObjectItems.getString("serving_qty"));
                        recipeFoodModel.setServingUnits(jsonObjectItems.getString("serving_unit"));
//                        genericFoodModel.setGenericFoodCal(jsonObjectItems.getString("nf_calories"));
//                        genericFoodModel.setGenericFoodSubTitle(jsonObjectItems.getString("brand_name"));
                        recipeFoodModel.setRecipeTagID(jsonObjectItems.getString("tag_id"));
                        Log.d(TAG,"Food Name : "+jsonArray.length());
                        JSONObject jsonObjectPhoto = jsonObjectItems.getJSONObject("photo");
                        Log.d(TAG,"Food Name : "+jsonObjectItems.getString("food_name")+"\n  Food Image Url : "+jsonObjectPhoto.getString("thumb"));
                        recipeFoodModel.setRecipeFoodImage(jsonObjectPhoto.getString("thumb"));
                        recipeFoodList.add(recipeFoodModel);
                    }
                } catch (Exception e) {
                    Log.d(TAG,"Exception msg : "+e.getMessage());
                }
                mRecipeAdapter.notifyDataSetChanged();
                webCalls.doneProgress();
            }

            @Override
            public void OnWebCallError(String errorMessage) {
                Logger.showToast(context, errorMessage);
                Log.d(TAG,"Recipe search Error : "+errorMessage);
            }
        });
        //webCalls.getRecipeList(searchModel);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            clickedPos = (int) v.getTag();
        }
        switch (v.getId()){
            case R.id.ll_particular_product_row_recipe:
                Bundle mbundle = new Bundle();
                mbundle.putString("fromFragment","searchRecipes");
                mbundle.putInt("typeOfFoodID",foodTypeId);
                mbundle.putString("commonFoodName", recipeFoodList.get(clickedPos).getRecipeFoodName());
                Intent intent = new Intent(getActivity(), BarcodeAndSearchRecipeActivity.class);
                intent.putExtras(mbundle);
                startActivity(intent);
                break;
        }
    }

    /**
     * Callback for speech recognition activity
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    recipeFoodList.clear();
                    searchRecipe(text);
                }
                break;
            }
        }
    }
}
