package com.ting.welcome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.view.SfWebview;

/**
 * Created by liu on 2017/7/1.
 */

public class AdActivity extends BaseActivity{
    private SfWebview mWebview;
    private ImageView ivBack;
    private ImageView ivClose;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        initView();
    }

    @Override
    protected String setTitle() {
        return null;
    }
    @Override
    protected void initView() {
        mWebview = (SfWebview) findViewById(R.id.webview);
        mWebview.loadUrl(url);
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(!request.getUrl().toString().startsWith("http:") && !request.getUrl().toString().startsWith("https:") && !request.getUrl().toString().startsWith("ftp:")){
                    Intent intent = new Intent();
                    intent.setData(request.getUrl());
                    startActivity(intent);
                    return  true;
                }
                return super.shouldOverrideUrlLoading(view, request.getUrl().toString());
            }

        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url", null);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

}
