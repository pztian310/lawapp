package com.pxxy.lawconsult.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.view.CustomTitleView;

public class AttentionActivity extends BaseActivity {


    private CustomTitleView attention_title;

    @Override
    public int initLayout() {
        return R.layout.activity_attention;
    }

    @Override
    public void initView() {
        attention_title = findViewById(R.id.activity_attention_title);
    }

    @Override
    public void initEvent() {
        attention_title.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
