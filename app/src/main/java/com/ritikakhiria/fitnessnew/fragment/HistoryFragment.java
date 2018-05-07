package com.ritikakhiria.fitnessnew.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.Utils.Utils;
import com.ritikakhiria.fitnessnew.db.DBHelper;
import com.ritikakhiria.fitnessnew.model.HistoryModel;
import com.ritikakhiria.fitnessnew.widget.StatefulRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends android.support.v4.app.Fragment {

    private StatefulRecyclerView recyclerView;
    private TextView txerror;
    private List<HistoryModel> historyModels;
    private HistoryAdapter historyAdapter = null;

    private String tag = HistoryFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_log_history, container, false);

        recyclerView = (StatefulRecyclerView) view.findViewById(R.id.recycle_list);
        txerror = (TextView) view.findViewById(R.id.txerror);

        historyModels = DBHelper.getInstance(this.getActivity()).getAllTracking();
        if (historyModels == null) {
            historyModels = new ArrayList<HistoryModel>();
        } else {
            txerror.setVisibility(View.GONE);
        }

        historyAdapter = new HistoryAdapter(this.getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);

        return view;
    }

    public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

        private Context context;

        public HistoryAdapter(Context context) {
            super();
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTimestamp, tvSteps, tvDistance, tvCaloriesBurned, tvWalking, tvRunning, tvStairs;

            public MyViewHolder(View view) {
                super(view);
                tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);
                tvSteps = (TextView) view.findViewById(R.id.tvSteps);
                tvDistance = (TextView) view.findViewById(R.id.tvDistance);
                tvCaloriesBurned = (TextView) view.findViewById(R.id.tv_carbs);
                tvWalking = (TextView) view.findViewById(R.id.tv_walking_value);
                tvRunning = (TextView) view.findViewById(R.id.tv_running_value);
                tvStairs = (TextView) view.findViewById(R.id.tv_stairs_value);
            }
        }

        /* Inflating the view to show the Activity Tracker history data. */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row_history, parent, false);

            return new MyViewHolder(itemView);
        }

        /* Populating the Activity Tracker data on the View of RecycleView */
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            HistoryModel HistoryModel = historyModels.get(position);
            Log.d(tag, "Walking Steps : " + HistoryModel.getWalking());

            holder.tvTimestamp.setText(Utils.formatDate(Utils.convertStringToTimestamp1(HistoryModel.getSaveDate())));
            holder.tvDistance.setText(Utils.round(HistoryModel.getDistance(),2) + " km");
            holder.tvSteps.setText(HistoryModel.getSteps() + "");
            holder.tvCaloriesBurned.setText(Utils.roundDouble(HistoryModel.getCaloriesBurned(),1) + " Kcal");

            holder.tvWalking.setText(HistoryModel.getWalking() + " min");
            holder.tvRunning.setText(HistoryModel.getRunning() + " min");
            holder.tvStairs.setText(HistoryModel.getStairs() + " min");
        }

        @Override
        public int getItemCount() {
            return historyModels.size();
        }
    }

}
