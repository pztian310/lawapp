package com.pxxy.lawconsult.config;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    //定义全局的context
    private static Context context;
    private static Map<String,Object> data;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        data = new HashMap<>();
    }

    /**
     *
     * @return ApplicationContext
     */
    public static Context getContext(){
        return context;
    }

    /**
     * 获取用户map
     * @return
     */
    public static Map<String,Object> getData(){
        return data;
    }

    /**
     * 添加数据
     * @param string
     * @param object
     */
    public static void putData(String key,Object value){
        data.put(key,value);
    }

    /**
     * 移除数据
     * @param key
     */
    public static void removeData(String key){
        data.remove(key);
    }

    /**
     * 清除数据
     */
    public static void clearData(){
        data.clear();
    }
}
