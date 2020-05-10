package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.ClientUser;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.view.CustomTitleView;


public class AdminShowUserMessageActivity extends BaseActivity {
    private CustomTitleView titleView;
    private Intent intent;
    private ClientUser clientUser;
    private LawerUser lawerUser;
    private TextView userMessage_1_key, userMessage_2_key;
    private TextView userMessage_3_key, userMessage_4_key, userMessage_5_key;
    private TextView userMessage_6_key, userMessage_7_key, userMessage_8_key;
    private TextView userMessage_9_key, userMessage_10_key, userMessage_11_key;
    private TextView userMessage_12_key, userMessage_13_key, userMessage_14_key;
    private TextView userMessage_1_value, userMessage_2_value;
    private TextView userMessage_3_value, userMessage_4_value, userMessage_5_value;
    private TextView userMessage_6_value, userMessage_7_value, userMessage_8_value;
    private TextView userMessage_9_value, userMessage_10_value, userMessage_11_value;
    private TextView userMessage_12_value, userMessage_13_value, userMessage_14_value;
    private LinearLayout lawyerUserLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_admin_show_user_message;
    }

    @Override
    public void initData() {
        //获取意图对象
        intent = getIntent();
        //获取用户类型
        int userType = intent.getIntExtra(AppConstant.ADMIN_USER_TYPE, -1);
        if (userType == AppConstant.TYPE_0) {
            clientUser = (ClientUser) intent.getSerializableExtra(AppConstant.ADMIN_USER_SHOW_USER_MESSAGE_USEROBJECT);
            //设置标题
            titleView.setCenterTextViewText(AppConstant.ADMIN_USER_CLIENTUSER_MESSAGE);
        } else if (userType == AppConstant.TYPE_1) {
            lawerUser = (LawerUser) intent.getSerializableExtra(AppConstant.ADMIN_USER_SHOW_USER_MESSAGE_USEROBJECT);
            //设置标题
            titleView.setCenterTextViewText(AppConstant.ADMIN_USER_LAWYERUSER_MESSAGE);
        }
        //设置字段名
        userMessage_1_key.setText(AppConstant.ADMIN_SHOW_USER_BASIC_USER_ID);
        userMessage_2_key.setText(AppConstant.ADMIN_SHOW_USER_BASIC_USER_TEL);
        userMessage_3_key.setText(AppConstant.ADMIN_SHOW_USER_BASIC_USER_PASSWORD);
        userMessage_4_key.setText(AppConstant.ADMIN_SHOW_USER_BASIC_USER_PHOTO);
        userMessage_5_key.setText(AppConstant.ADMIN_SHOW_USER_BASIC_USER_TYPE);
        //设置数据
        if (userType == AppConstant.TYPE_0) {
            //设置客户显示数据
            setShowClientUserData();
        } else if (userType == AppConstant.TYPE_1) {
            //设置律师显示数据
            setShowLawerUserData();
        }
    }

    /**
     * 设置律师显示数据
     */
    private void setShowLawerUserData() {
        //设置基本信息
        userMessage_1_value.setText(String.valueOf(lawerUser.getId()));
        userMessage_2_value.setText(lawerUser.getTel());
        userMessage_3_value.setText(lawerUser.getPassword());
        userMessage_4_value.setText(lawerUser.getImage());
        if (lawerUser.getType() == 0) {
            userMessage_5_value.setText("客户");
        } else {
            userMessage_5_value.setText("律师");
        }
        //设置律师key显示
        lawyerUserLayout.setVisibility(View.VISIBLE);
        //设置律师key
        userMessage_6_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_NAME);
        userMessage_7_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_AGE);
        userMessage_8_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_SEX);
        userMessage_9_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_EMAIL);
        userMessage_10_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_NUMBER);
        userMessage_11_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_SPECIALITY);
        userMessage_12_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_OFFICE);
        userMessage_13_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_INTRODUCTION);
        userMessage_14_key.setText(AppConstant.ADMIN_SHOW_USER_LAWER_USER_ADDRESS);
        //设置显示数据
        userMessage_6_value.setText(lawerUser.getLawerName());
        userMessage_7_value.setText(String.valueOf(lawerUser.getLawerAge()));
        if (lawerUser.getLawerSex() == 0){
            userMessage_8_value.setText("女");
        }else {
            userMessage_8_value.setText("男");
        }
        userMessage_9_value.setText(lawerUser.getEmail());
        userMessage_10_value.setText(lawerUser.getNumber());
        userMessage_11_value.setText(lawerUser.getSpeciality());
        userMessage_12_value.setText(lawerUser.getOffice());
        userMessage_13_value.setText(lawerUser.getIntroduction());
        userMessage_14_value.setText(lawerUser.getAddress());
    }

    /**
     * 设置用户显示数据
     */
    private void setShowClientUserData() {
        //设置基本数据
        userMessage_1_value.setText(String.valueOf(clientUser.getId()));
        userMessage_2_value.setText(clientUser.getTel());
        userMessage_3_value.setText(clientUser.getPassword());
        userMessage_4_value.setText(clientUser.getImage());
        if (clientUser.getType() == 0) {
            userMessage_5_value.setText("客户");
        } else {
            userMessage_5_value.setText("律师");
        }
        //设置客户key
        userMessage_6_key.setText(AppConstant.ADMIN_SHOW_USER_CLIENT_USER_NICKNAME);
        userMessage_7_key.setText(AppConstant.ADMIN_SHOW_USER_CLIENT_USER_SIGNATURE);
        userMessage_8_key.setText(AppConstant.ADMIN_SHOW_USER_CLIENT_USER_NAME);
        userMessage_9_key.setText(AppConstant.ADMIN_SHOW_USER_CLIENT_USER_SEX);
        userMessage_10_key.setText(AppConstant.ADMIN_SHOW_USER_CLIENT_USER_EMAIL);
        //设置客户数据
        userMessage_6_value.setText(clientUser.getNickName());
        userMessage_7_value.setText(clientUser.getSignature());
        if (clientUser.getType() == 0) {
            userMessage_9_value.setText("男");
        } else {
            userMessage_9_value.setText("女");
        }
        userMessage_10_value.setText(clientUser.getEmail());
    }

    @Override
    public void initEvent() {
        //设置标题返回按钮的点击事件
        titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    @Override
    public void initView() {
        titleView = findViewById(R.id.admin_show_user_message_activity_title);
        lawyerUserLayout = findViewById(R.id.admin_show_user_lawyer);
        // 获取键id
        userMessage_1_key = findViewById(R.id.admin_show_user_basic_message_1).findViewById(R.id.user_message_key);
        userMessage_2_key = findViewById(R.id.admin_show_user_basic_message_2).findViewById(R.id.user_message_key);
        userMessage_3_key = findViewById(R.id.admin_show_user_basic_message_3).findViewById(R.id.user_message_key);
        userMessage_4_key = findViewById(R.id.admin_show_user_basic_message_4).findViewById(R.id.user_message_key);
        userMessage_5_key = findViewById(R.id.admin_show_user_basic_message_5).findViewById(R.id.user_message_key);
        userMessage_6_key = findViewById(R.id.admin_show_user_message_6).findViewById(R.id.user_message_key);
        userMessage_7_key = findViewById(R.id.admin_show_user_message_7).findViewById(R.id.user_message_key);
        userMessage_8_key = findViewById(R.id.admin_show_user_message_8).findViewById(R.id.user_message_key);
        userMessage_9_key = findViewById(R.id.admin_show_user_message_9).findViewById(R.id.user_message_key);
        userMessage_10_key = findViewById(R.id.admin_show_user_message_10).findViewById(R.id.user_message_key);
        userMessage_11_key = findViewById(R.id.admin_show_user_message_11).findViewById(R.id.user_message_key);
        userMessage_12_key = findViewById(R.id.admin_show_user_message_12).findViewById(R.id.user_message_key);
        userMessage_13_key = findViewById(R.id.admin_show_user_message_13).findViewById(R.id.user_message_key);
        userMessage_14_key = findViewById(R.id.admin_show_user_message_14).findViewById(R.id.user_message_key);
        //获取值id
        userMessage_1_value = findViewById(R.id.admin_show_user_basic_message_1).findViewById(R.id.user_message_value);
        userMessage_2_value = findViewById(R.id.admin_show_user_basic_message_2).findViewById(R.id.user_message_value);
        userMessage_3_value = findViewById(R.id.admin_show_user_basic_message_3).findViewById(R.id.user_message_value);
        userMessage_4_value = findViewById(R.id.admin_show_user_basic_message_4).findViewById(R.id.user_message_value);
        userMessage_5_value = findViewById(R.id.admin_show_user_basic_message_5).findViewById(R.id.user_message_value);
        userMessage_6_value = findViewById(R.id.admin_show_user_message_6).findViewById(R.id.user_message_value);
        userMessage_7_value = findViewById(R.id.admin_show_user_message_7).findViewById(R.id.user_message_value);
        userMessage_8_value = findViewById(R.id.admin_show_user_message_8).findViewById(R.id.user_message_value);
        userMessage_9_value = findViewById(R.id.admin_show_user_message_9).findViewById(R.id.user_message_value);
        userMessage_10_value = findViewById(R.id.admin_show_user_message_10).findViewById(R.id.user_message_value);
        userMessage_11_value = findViewById(R.id.admin_show_user_message_11).findViewById(R.id.user_message_value);
        userMessage_12_value = findViewById(R.id.admin_show_user_message_12).findViewById(R.id.user_message_value);
        userMessage_13_value = findViewById(R.id.admin_show_user_message_13).findViewById(R.id.user_message_value);
        userMessage_14_value = findViewById(R.id.admin_show_user_message_14).findViewById(R.id.user_message_value);
    }
}
