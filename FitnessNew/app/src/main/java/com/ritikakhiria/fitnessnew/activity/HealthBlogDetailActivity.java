package com.ritikakhiria.fitnessnew.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ritikakhiria.fitnessnew.R;

public class HealthBlogDetailActivity extends AppCompatActivity {
    WebView articleView;
    String content = null;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_blog_detail);
        articleView = (WebView) findViewById(R.id.webview);
        articleView.getSettings().setLoadWithOverviewMode(true);

        articleView.getSettings().setJavaScriptEnabled(true);
        articleView.setHorizontalScrollBarEnabled(false);
        articleView.setWebChromeClient(new WebChromeClient());
        Intent intent = getIntent();
        content = intent.getStringExtra("HealtDetail");
        articleView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +

                "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n"  + content, null, "utf-8", null);
    }
}
