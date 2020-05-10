package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.MyCountDownTime;
import com.pxxy.lawconsult.utils.SMS;
import com.pxxy.lawconsult.utils.SPUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class FindPasswordActivity extends BaseActivity {
    private MyCountDownTime myCountDownTime;
    private OkHttpUtil okHttpUtil;
    private Gson gson;
    private String phoneNumber;
    private EditText et_phone;
    private EditText et_verifi;
    private EditText et_newpwd;
    private Button bt_getverifi;
    private String newpwd;
    private String phone;
    private String verifi;
    //判断是否获取验证码
    public boolean isClick=false;
    /*
     * 101:短信验证码发送成功
     * 102:短信验证码发送失败
     * 201:短信验证码验证成功
     * 202:短信验证码验证失败
     * 001:服务器繁忙
     * 002:用户不存在
     * 003:找回密码成功
     * 004:找回密码失败
     * 005:请检查网络
     * 006:发送验证码
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                    case 101:
                    toastShort("请注意查收");
                    break;
                case 102:
                    toastShort("服务器繁忙");
                    break;
                case 201:
                    //向服务器发送phone newpwd
                    alterPassword();
                    break;
                case 202:
                    toastShort("验证码有误");
                    break;
                case 001:
                    toastShort("服务器繁忙");
                    break;
                case 002:
                    toastShort("用户不存在");
                    break;
                case 003:
                    toastLong("修改密码成功");
                    inputSP(phone,newpwd);
                    finish();
                    break;
                case 004:
                    toastShort("修改密码失败");
                    break;
                case 005:
                    toastShort("请检查网络");
                    break;
                case 006:
                    isClick=true;
                    SMS.sendSMS(phoneNumber, handler);
                    //判断是否倒计时结束，避免在倒计时时多次点击导致重复请求接口
                    if (myCountDownTime.isFinish()) {
                        myCountDownTime.start();
                    }
                    break;
                default:
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_find_password;
    }


    @Override
    public void initView() {
        //输入手机号码控件
        et_phone = findViewById(R.id.findpwd_et_phone);
        //输入验证码控件
        et_verifi = findViewById(R.id.findpwd_et_verifi);
        //输入新密码控件
        et_newpwd = findViewById(R.id.findpwd_et_newpwd);
        //获取验证码控件
        bt_getverifi = findViewById(R.id.findpwd_bt_getverifi);

    }
    @Override
    public void initData() {
        //获取Gson对象
        gson = new Gson();
        //new倒计时对象 总共多少秒，每隔多少秒更新一次
        myCountDownTime = new MyCountDownTime(60000, 1000, bt_getverifi);
        //获取okHttpUtil对象
        okHttpUtil = new OkHttpUtil();
    }
    //返回登陆界面
    public void back(View v) {
        finish();
    }
    //获取验证码Button
    public void getVerifi(View v) {
        //获取手机号码
        phoneNumber = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            toastShort("请输入手机号");
            return;
        }else{
            okHttpUtil.isUserExist(phoneNumber, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(005);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String isexits = response.body().string();
                    Map<String, String> map = JsonUtils.jsonToMap(isexits);
                    String isUserExist = map.get(HttpConstant.IS_USEREXIST);
                    if (isUserExist.equals(HttpConstant.IS_USEREXIST_NOT_EXIST)){
                        handler.sendEmptyMessage(002);
                    }else if (isUserExist.equals(HttpConstant.IS_USEREXIST_SERVERERROR)){
                        handler.sendEmptyMessage(001);
                    }else if (isUserExist.equals(HttpConstant.IS_USEREXIST_EXIST)){
                        handler.sendEmptyMessage(006);
                    }
                }

            });
        }
    }
    //找回密码Button
    public void findPwd(View v) {
        //获取用户输入的数据
        newpwd = et_newpwd.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        verifi = et_verifi.getText().toString().trim();
        if(!valueIsEmpty()){
            if(isClick){
            //验证验证码
            SMS.verifySMS(verifi);
            } else{
                toastShort("请先获取验证码");
            }
        }
    }
    //网络请求
    private void alterPassword() {

        //向服务器提交数据
        okHttpUtil.findPassword(phone, newpwd, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(005);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取服务器返回的gson数据
                String findpasswordDate = response.body().string();
                //按照map对象去解析gson数据
                Map<String, String> map = JsonUtils.jsonToMap(findpasswordDate);
                String data = map.get(HttpConstant.FIND_PASSWORD );
                if (data.equals(HttpConstant.FINDPASSWORD_FAILURE_SERVER_ERROR)) {
                    handler.sendEmptyMessage(001);
                } else if (data.equals(HttpConstant.FINDPASSWORD_NOTFIND)) {
                    handler.sendEmptyMessage(002);
                } else if (data.equals(HttpConstant.FINDPASSWORD_SUCCESS)) {
                    handler.sendEmptyMessage(003);
                } else if (data.equals(HttpConstant.FINDPASSWORD_FAILURE)) {
                    handler.sendEmptyMessage(004);
                }
            }
        });
    }
    /**
     *
     * @return boolean 为空返回true,非空返回false
     */
    public boolean valueIsEmpty() {
        if (phone.equals("")) {
            toastShort("请输入手机号");
            return true;
        } else if (verifi.equals("")) {
            toastShort("请输入验证码");
            return true;
        } else if (newpwd.equals("")) {
            toastShort("请输入新密码");
            return true;
        } else if (!phone.equals(phoneNumber)) {
            toastShort("请先获取验证码");
            et_verifi.setText(null);
            et_newpwd.setText(null);
            return true;
        }
       return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不注销,可能会出现内存泄露
        SMS.closeSMS();
        //关闭计时器
        if (myCountDownTime != null) {
            myCountDownTime.cancel();
        }
    }
    public void inputSP(String tel,String password){
        SPUtils.put(AppConstant.SP_AUTOLOGIN_TEL,tel,SPUtils.FILE_AUTOLOGIN);
        SPUtils.put(AppConstant.SP_AUTOLOGIN_PASSWORD,password,SPUtils.FILE_AUTOLOGIN);
    }
}
