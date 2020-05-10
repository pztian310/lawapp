package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.entity.Statute;
import com.pxxy.lawconsult.view.CustomTitleView;



public class StatutesActivity extends BaseActivity {
    private CustomTitleView customTitleView;
    private TextView tv_title,tv_content,tv_opendate,tv_type,tv_sh;
    @Override
    public int initLayout() {
        return R.layout.activity_statute;
    }

    @Override
    public void initView() {
        super.initView();
        //找到CustomTitleView
        customTitleView=findViewById(R.id.activity_statute_customtitleview);
        //找到法条title
        tv_title=findViewById(R.id.activity_statute_title);
        //找到法条content
        tv_content=findViewById(R.id.activity_statute_content);
        //找到法条类型
        tv_type=findViewById(R.id.activity_statute_type);
        //找到法条制定时间
        tv_opendate=findViewById(R.id.activity_statute_opendate);
        tv_sh=findViewById(R.id.activity_statute_sh);
    }

    @Override
    public void initEvent() {
        //返回键
        customTitleView.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = this.getIntent();
        Statute statute= (Statute) intent.getSerializableExtra("statute");
        tv_title.setText(statute.getLaw_title());
        tv_content.setText(statute.getLaw_content());
        tv_type.setText(statute.getLaw_type());
        tv_opendate.setText(statute.getOpendate());
    }
}
