//package com.ritikakhiria.fitnessnew.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//
//import com.ritikakhiria.fitnessnew.R;
//import com.ritikakhiria.fitnessnew.model.MyStepModel;
//
//
//public class burnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    Context context;
//    private LayoutInflater inflater;
//    ArrayList<MyStepModel> stepItems;
//
//    public burnAdapter(Context context, ArrayList<MyStepModel> stepItems) {
//        super();
//        this.context = context;
//        this.stepItems = stepItems;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.item_steps, parent, false);
//        burnAdapter.MyHolder holder = new burnAdapter.MyHolder(view, context);
//        return holder;
//    }
//
//    // Bind data
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        // Get current position of item in RecyclerView to bind data and assign values from list
//        MyHolder myHolder = (MyHolder) holder;
//        MyStepModel current = stepItems.get(position);
//        int stepInt = current.getSteps();
//
//        stepInt = stepInt / 20;
//        myHolder.StepsTV.setText(String.valueOf(stepInt)+" Calories");
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            myHolder.DatesTV.setText(sdf.format(current.getSaveDate()));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    // return total item from List
//    @Override
//    public int getItemCount() {
//        return stepItems.size();
//    }
//
//    class MyHolder extends RecyclerView.ViewHolder {
//        TextView StepsTV, DatesTV;
//
//        // create constructor to get widget reference
//        public MyHolder(View itemView, final Context context) {
//            super(itemView);
//            final Context context1 = context;
//            StepsTV = (TextView) itemView.findViewById(R.id.textView2);
//            DatesTV = (TextView) itemView.findViewById(R.id.textView3);
//
//        }
//
//    }
//}