package com.pxxy.lawconsult.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    //上下文对象
    protected Context context;
    //Activity对象
    protected Activity activity;

    /**
     * 当视图加载到Activity时调用
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutID(),container,false);
        //初始化视图
        initView(view);
        //设置数据
        initData();
        //设置监听事件
        initEvent();
        return view;
    }

    /**
     * 设置布局
     * @return
     */
    protected abstract int setLayoutID();

    /**
     * 设置功能的方法
     */
    protected abstract void initEvent();

    /**
     * 设置监听的方法
     */
    protected abstract void initData();

    /**
     * 设置视图
     * @param view
     * @return
     */
    protected abstract void initView(View view);

    /**
     * 跳转界面
     * @param tClass 要跳转的界面
     * @param <T>
     * @return
     */
    protected final <T extends Activity>Intent activity(Class<T> tClass){
        return  new Intent(activity,tClass);
    }
}
