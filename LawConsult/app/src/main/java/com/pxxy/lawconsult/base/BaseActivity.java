package com.pxxy.lawconsult.base;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


public abstract class BaseActivity extends AppCompatActivity {
    //是否显示标题栏
    private boolean isShowTitle = false;
    //是否显示状态栏
    private boolean isShowState = true;
    //封装Toast对象
    private static Toast toast = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //只允许竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //判断是否显示标题栏
        if (!isShowTitle) {
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.hide();
            }
        }

        //判断是否显示状态栏
        if (!isShowState) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(initLayout());
        //初始化控件
        initView();
        //设置数据
        initData();
        //初始化事件
        initEvent();
        //log打印activity位置
        Log.d("BaseActivity", getClass().getSimpleName());
    }

    /**
     * 设置视图
     * @return
     */
    public abstract  int initLayout();
    /**
     * 设置数据
     */
    public void initData(){

    }

    /**
     * 初始化控件
     */
    public void initView(){

    }

    /**
     * 初始化事件
     */
    public void initEvent(){

    }

    /**
     * 设置是否显示标题栏
     *
     * @param isShow
     */
    public void setTitle(boolean isShow) {
        isShowTitle = isShow;
    }

    /**
     * 设置是否显示状态栏
     *
     * @param isShow
     */
    public void setState(boolean isShow) {
        isShowState = isShow;
    }

    /**
     * 显示长Toast
     *
     * @param msg 要显示的信息
     */
    public void toastLong(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this,msg,Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();

    }

    /**
     * 显示短Toast
     * @param msg 要显示的信息
     */
    public void toastShort(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toast != null){
            toast.cancel();
        }
    }

    /**
     * 关闭activity
     */
    public void closeActivity(){
        this.finish();
    }
}