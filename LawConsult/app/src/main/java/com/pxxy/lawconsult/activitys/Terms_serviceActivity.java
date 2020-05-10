package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.view.KeyEvent;
import android.webkit.WebViewClient;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;

import java.io.InputStream;

public class Terms_serviceActivity extends BaseActivity {
    private WebView webView;
    private com.pxxy.lawconsult.view.CustomTitleView customTitleView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_terms_service;
    }

    @Override
    public void initView() {
        super.initView();
        //找到返回键控件
        customTitleView=findViewById(R.id.Terms_serviceActivity_customTitleView);
        //找到WebView控件
        webView=findViewById(R.id.activity_terms_webview);
        /**
         * 显示要加载web的地址
         */
        webView.loadUrl("http://120.77.205.137/LawConsult/TermsOfService.html");
        //是否和js交互
        webView.getSettings().setJavaScriptEnabled(true);
        //连接默认在webview中打开
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //设置返回键监听
        customTitleView.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到LoginActivity中
                finish();
            }
        });

    }
    //鼠标移动事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
