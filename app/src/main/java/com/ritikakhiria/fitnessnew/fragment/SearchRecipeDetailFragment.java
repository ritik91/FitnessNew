package com.ritikakhiria.fitnessnew.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.model.RecipeModel;


public class SearchRecipeDetailFragment extends Fragment {
    View view;
    private Bundle mbundle;
    private RecipeModel recipeModel;
    private String TAG = SearchRecipeDetailFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_recipe_detail, container, false);
        mbundle = new Bundle();
        mbundle = getArguments();
        recipeModel = new RecipeModel();
        if(mbundle!=null){
            recipeModel = (RecipeModel)mbundle.getSerializable("recipeDetail");
        }
        if(recipeModel.getRecipeFoodName()!=null && !recipeModel.getRecipeFoodName().equalsIgnoreCase("")) {
            Log.d(TAG,"Food Name : "+recipeModel.getRecipeFoodName());
        }
        return view;
    }
}
