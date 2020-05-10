package com.pxxy.lawconsult.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.view.CustomTitleView;

public class FansActivity extends BaseActivity {

    private CustomTitleView fans_title;

    @Override
    public int initLayout() {
        return R.layout.activity_fans;
    }

    @Override
    public void initView() {
        fans_title = findViewById(R.id.activity_fans_title);
    }

    @Override
    public void initEvent() {
        fans_title.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
