package com.rajan.eliteeditor.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rajan.eliteeditor.R;

public class OutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        getSupportActionBar().setTitle("Elite Viewer");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String htmlOutput = getIntent().getStringExtra("html");
        final WebView outputView = findViewById(R.id.output);
        outputView.getSettings().setJavaScriptEnabled(true);
        outputView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                outputView.loadUrl(url);
                return true;
            }
        });
        outputView.loadData(htmlOutput,"text/html","UTF-8");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
