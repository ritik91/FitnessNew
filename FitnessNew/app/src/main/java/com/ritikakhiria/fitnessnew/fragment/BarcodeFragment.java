package com.ritikakhiria.fitnessnew.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.ritikakhiria.fitnessnew.Interfaces.OnWebCall;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Logger;
import com.ritikakhiria.fitnessnew.Utils.Session;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.Utils.WebCalls;
import com.ritikakhiria.fitnessnew.model.BarcodeModel;
import com.ritikakhiria.fitnessnew.model.Foods;
import com.ritikakhiria.fitnessnew.model.MealSchedulerModel;
import com.ritikakhiria.fitnessnew.model.Nutrients;
import com.ritikakhiria.fitnessnew.model.SearchRecipeDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BarcodeFragment extends Fragment {

    public static final String TAG = BarcodeFragment.class.getSimpleName();
    View view;
    Button barcodeScan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_barcode, container, false);
        barcodeScan = (Button) view.findViewById(R.id.btn_barcode);
        barcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeScanner();
            }
        });
        return view;
    }
    public void barcodeScanner(){
        new IntentIntegrator(getActivity()).initiateScan(); // `this` is the current Activity

    }
}
