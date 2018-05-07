package com.ritikakhiria.fitnessnew.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.adapter.ArticleAdapter;

import java.util.ArrayList;

public class HealthBlog extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //private String urlString = "http://www.androidcentral.com/feed";
    private String urlString = "http://rssfeedlist.org/category/health-and-fitness/feed/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_blog);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.canChildScrollUp();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                mAdapter.clearData();
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(true);
                loadFeed();

            }
        });

        if (!isNetworkAvailable()) {
            mSwipeRefreshLayout.setRefreshing(true);
            Toast.makeText(HealthBlog.this,"Please connect to internet",Toast.LENGTH_LONG).show();

        } else if (isNetworkAvailable()) {
            mSwipeRefreshLayout.setRefreshing(true);
            loadFeed();
        }
    }

    /* Loading the Health related articles using API. */
    public void loadFeed() {

        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {
            //what to do when the parsing is done
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                //list is an Array List with all article's information
                //set the adapter to recycler view
                mAdapter = new ArticleAdapter(list, R.layout.row, HealthBlog.this);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);

            }

            //what to do in case of error
            @Override
            public void onError() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(HealthBlog.this, "Unable to load data. Swipe down to retry.",
                                Toast.LENGTH_SHORT).show();
                        Log.i("Unable to load ", "articles");
                    }
                });
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onResume() {

        super.onResume();
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (mAdapter != null)
            mAdapter.clearData();
    }

}

