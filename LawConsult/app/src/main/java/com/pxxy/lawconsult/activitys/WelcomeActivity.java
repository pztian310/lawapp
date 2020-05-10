package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.ClientUser;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.SPUtils;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置无状态栏
        setState(false);
        super.onCreate(savedInstanceState);
        final String userJson = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_USER, "", SPUtils.FILE_AUTOLOGIN);
        final boolean isAutoLogin = (boolean) SPUtils.get(AppConstant.SP_AUTOLOGIN_ISAUTOLOGIN, false, SPUtils.FILE_AUTOLOGIN);
        final int userType = (int) SPUtils.get(AppConstant.SP_AUTOLOGIN_TYPE, -1, SPUtils.FILE_AUTOLOGIN);
        //设置延时跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoLogin(userJson, isAutoLogin, userType);
            }
        }, 2000);
    }

    /**
     * 自动登录
     *
     * @param userJson
     * @param isAutoLogin
     * @param userType
     */
    private void autoLogin(String userJson, boolean isAutoLogin, int userType) {
        if (!userJson.equals("") && isAutoLogin) {
            if (userType == AppConstant.TYPE_0) {
                //转化为clientUser对象
                ClientUser clientUser = JsonUtils.jsonToClientUser(userJson);
                //把clientUser对象放入MyApp中
                MyApp.putData(AppConstant.USER, clientUser);
                //MyApp中设置用户类型
                MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_0);
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                //销毁Activity
                finish();
            } else if (userType == AppConstant.TYPE_1) {
                //转化为LawerUser对象
                LawerUser lawerUser = JsonUtils.jsonToLawerUser(userJson);
                //把clientUser对象放入MyApp中
                MyApp.putData(AppConstant.USER, lawerUser);
                //MyApp中设置用户类型
                MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_1);
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                //销毁Activity
                finish();
            }else if (userType == AppConstant.TYPE_2){
                //转化成User对象
                User user = JsonUtils.jsonToUser(userJson);
                //把user对象放入myapp中
                MyApp.putData(AppConstant.USER,user);
                //设置用户类型
                MyApp.putData(AppConstant.LOGIN_USER_TYPE,AppConstant.TYPE_2);
                //跳转至管理员界面
                startActivity(new Intent(WelcomeActivity.this,AdminActivity.class));
                //销毁activity
                finish();
            }

            else if (userType == -1) {
                //清除自动登录配置
                SPUtils.clear(SPUtils.FILE_AUTOLOGIN);
                //跳转登录界面
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_welcome;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
