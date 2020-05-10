package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.pxxy.lawconsult.AdminChildStatureActivity;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class AdminStatuteActivity extends BaseActivity {
    private CustomTitleView titleView;
    private RelativeLayout  statureLayout;
    private SmartRefreshLayout refreshLayout;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_admin_stature;
    }

    @Override
    public void initView() {
        titleView = findViewById(R.id.admin_stature_activity_title);
        statureLayout = findViewById(R.id.admin_stature_layout);
        refreshLayout = findViewById(R.id.admin_stature_activity_refreshlayout);
    }

    @Override
    public void initEvent() {
        //设置titleView的返回点击事件
        titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        //设置刷新事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //设置可见
                statureLayout.setVisibility(View.VISIBLE);
                refreshLayout.finishRefresh();
            }
        });
        //设置自动刷新
        refreshLayout.autoRefresh();

        //设置刑法的点击事件
        statureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转界面
                startActivity(new Intent(AdminStatuteActivity.this, AdminChildStatureActivity.class));
            }
        });
    }
}
