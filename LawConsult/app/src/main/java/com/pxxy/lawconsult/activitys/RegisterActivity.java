package com.pxxy.lawconsult.activitys;


import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.ClientUser;
import com.pxxy.lawconsult.entity.RegisterData;
import com.pxxy.lawconsult.fragment.FragmentHome;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.LoadingDialogUtils;
import com.pxxy.lawconsult.utils.MyCountDownTime;
import com.pxxy.lawconsult.utils.SMS;
import com.pxxy.lawconsult.utils.SPUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    private EditText et_phone;
    private EditText et_verifi;
    private EditText et_password;
    private Button bt_verifi;
    private MyCountDownTime myCountDownTime;
    private Button bt_register;
    private Dialog dialog;
    private Gson gson;
    private String verifi;
    private String phoneNumber;
    private Boolean isChecks = false;
    private String password;
    private Dialog myDialog = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int code = msg.what;
            switch (msg.what) {
                case 101:
                    toastShort("短信发送成功");
                    isChecks = true;
                    sendSuccess();
                    break;
                case 102:
                    toastShort("短信发送失败");
                    sendError();
                    break;
                case 201:
                    LoadingDialogUtils.closeDialog(myDialog);
                    // verifiSuccess();
                    //开始注册
                    successRegister(phoneNumber, password);

                    break;
                case 202:
                    toastShort("验证码输入错误，请重新输入");
                    LoadingDialogUtils.closeDialog(myDialog);
                    verifiError();
                    break;
                //网络数据请求失败
                case 1:
                    toastShort("网络错误");
                    break;
                //手机号已经注册过
                case 2:

                    toastShort("该手机号已被注册");
                    break;
                //服务器错误
                case 3:
                    toastShort("服务器繁忙，请稍后再试");
                    break;
                //手机号未注册
                case 4:
                    //发送验证码
                    //new倒计时对象 总共多少秒，每隔多少秒更新一次
                    myCountDownTime = new MyCountDownTime(60000, 1000, bt_verifi);

                    //判断是否倒计时结束，避免在倒计时时多次点击导致重复请求接口
                    if (myCountDownTime.isFinish()) {
                        myCountDownTime.start();
                    }
                    //发送短信验证
                    SMS.sendSMS(phoneNumber, handler);
                    break;
                //
                case 5:
                    LoadingDialogUtils.closeDialog(dialog);
                    toastShort("网络错误，请检查网络状态");
                    break;
                //注册成功
                case 6:
                    toastShort("注册成功");
                    //把用户数据转换为json对象存入sp中
                    RegisterData registerData = (RegisterData) msg.obj;
                    ClientUser data = registerData.getData();
                    MyApp.putData(AppConstant.USER, data);
                    MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_0);
                    String clientUserJson = JsonUtils.clientUserToJson(data);
                    //设置用户类型
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_TYPE, AppConstant.TYPE_0, SPUtils.FILE_AUTOLOGIN);
                    //json对象保存在sp中
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_USER, clientUserJson, SPUtils.FILE_AUTOLOGIN);
                    //存放头像url
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_PHOTO, data.getImage(), SPUtils.FILE_AUTOLOGIN);

                    //将数据存到本地
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_TEL, phoneNumber, SPUtils.FILE_AUTOLOGIN);
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_PASSWORD, password, SPUtils.FILE_AUTOLOGIN);

                    //sp中设置是否自动登录
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_ISAUTOLOGIN, true, SPUtils.FILE_AUTOLOGIN);
                    //加入配置文件
                    SPUtils.put(AppConstant.SP_CONFIG_CLEARPASSWORD, true, SPUtils.FILE_CONFIG); //配置自动清除密码
                    LoginActivity.closeLoginActivity();
                    //跳转页面
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                    break;
                //注册失败
                case 7:
                    toastShort("注册失败");
                    break;
                //服务器错误
                case 8:

                    toastShort("服务器繁忙，请稍后再试");
                    break;

            }
        }
    };


    /**
     * 101短信发送成功
     */
    private void sendSuccess() {
       /* //如果发生成功 手机号不能修改
        et_phone.setFocusable(false);
        //注册按钮可被点击
        bt_register.setClickable(true);*/
    }

    /**
     * 102短信发送失败
     */
    private void sendError() {
        /*//手机号可以点击
        et_phone.setClickable(true);*/
        //关闭计时器
        if (myCountDownTime != null)
            myCountDownTime.cancel();
        //十秒后可重新被点击
        new MyCountDownTime(10000, 1000, bt_verifi).start();

    }

    /**
     * 201验证码通过
     */
    private void verifiSuccess() {

        /*//跳转到home主页
        startActivity(new Intent(this, FragmentHome.class));
        toastShort("注册成功");*/
    }

    /**
     * 202验证码失败
     */
    private void verifiError() {
        //手机号可被点击

    }

    @Override
    public int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        //找到我们所关系的控件
        //手机号
        et_phone = findViewById(R.id.register_et_phone);
        //验证码的输入
        et_verifi = findViewById(R.id.register_et_verifi);
        //验证码的按钮
        bt_verifi = findViewById(R.id.register_bt_verifi);
        //密码
        et_password = findViewById(R.id.register_et_password);
        //注册按钮
        bt_register = findViewById(R.id.register_bt_register);
        gson = new Gson();
    }

    /**
     * 获取返回上一级的点击事件
     */
    public void back(View v) {

        finish();
    }

    /**
     * 获取验证码的点击事件
     */
    public void verifi(View v) {
        phoneNumber = et_phone.getText().toString().trim();
        //判断手机号是否为空
        if (TextUtils.isEmpty(phoneNumber)) {
            //Toast
            toastShort("手机号不能为空");
            return;
        } else if (phoneNumber.length() != 11) {
            toastShort("手机号格式错误，请重新检查");
            return;
        }
        //判断手机号是否已经注册

        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.isUserExist(phoneNumber, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //当连接网络错误时
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String registerJson = response.body().string();
                Log.d("a", registerJson);
                Map<String, String> map = gson.fromJson(registerJson, new TypeToken<Map<String, String>>() {
                }.getType());
                String isRegister = map.get(HttpConstant.IS_USEREXIST);
                if (isRegister.equals(HttpConstant.IS_USEREXIST_EXIST)) {
                    handler.sendEmptyMessage(2);
                } else if (isRegister.equals(HttpConstant.IS_USEREXIST_SERVERERROR)) {
                    handler.sendEmptyMessage(3);
                } else if (isRegister.equals(HttpConstant.IS_USEREXIST_NOT_EXIST)) {
                    handler.sendEmptyMessage(4);
                }
//                RegisterData registerData = gson.fromJson(registerJson, RegisterData.class);
//                String registerState = registerData.getRegisterState();
//                if (registerState.equals(RegisterData.REGISTERSTATE_FAILURE_USEREXIST)) {
//                    //已经注册
//                    handler.sendEmptyMessage(2);
//                } else if (registerState.equals(RegisterData.REGISTERSTATE_FAILURE_SERVER_ERROR)) {
//                    //服务器失败
//                    handler.sendEmptyMessage(3);
//                } else {
//                    //未注册
//                    handler.sendEmptyMessage(4);
//                }
            }
        });

    }

    /**
     * 注册的点击事件
     */
    public void register(View v) {
        password = et_password.getText().toString().trim();
        verifi = et_verifi.getText().toString().trim();
        String nowPhone = et_phone.getText().toString().trim();
        Log.d("aaa", phoneNumber + "   " + nowPhone);
        if (isChecks) {
            if (TextUtils.isEmpty(verifi)) {
                toastShort("请输入 验证码");
                return;
            } else if (TextUtils.isEmpty(password)) {
                toastShort("请输入密码");
                return;
            } else if (!phoneNumber.equals(nowPhone)) {
                toastShort("请先获取验证码");
                return;
            }

            //验证码
            SMS.verifySMS(verifi);
            myDialog = LoadingDialogUtils.showDialog(RegisterActivity.this, " 验证中");

        } else {
            toastShort("请先获取验证码");
        }
        /*if (phoneNumber==null){
            toastShort("请先输入手机号");
            return;
        }else if (nowPhone.equals(phoneNumber)) {
            if (TextUtils.isEmpty(password)) {
                toastShort("密码不能为空");
                return;
            } else if (TextUtils.isEmpty(phoneNumber)) {
                toastShort("手机号不能为空");
                return;
            } else if (TextUtils.isEmpty(verifi)) {
                toastShort("请输入验证码");
                return;
            }
            LoadingDialogUtils.showDialog(this, " ");
            //验证码
            SMS.verifySMS(verifi);
        }else{
            toastShort("请先获取验证码");
            return;
        }*/

    }

    /**
     * @param phoneNumber
     * @param password
     */
    private void successRegister(String phoneNumber, String password) {
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.register(phoneNumber, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络错误
                handler.sendEmptyMessage(5);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String registerJson = response.body().string();
                Log.d("message", registerJson);
                RegisterData registerData = gson.fromJson(registerJson, RegisterData.class);
                String registerState = registerData.getRegisterState();

                if (registerState.equals(RegisterData.REGISTERSTATE_SUCCESS)) {
                    Message msg = Message.obtain();
                    msg.obj = registerData;
                    msg.what = 6;
                    handler.sendMessage(msg);
                } else if (registerState.equals(RegisterData.REGISTERSTATE_FAILURE)) {
                    handler.sendEmptyMessage(7);
                } else if (registerState.equals(RegisterData.REGISTERSTATE_FAILURE_SERVER_ERROR)) {
                    handler.sendEmptyMessage(8);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭计时器
        if (myCountDownTime != null)
            myCountDownTime.cancel();
        //关闭短信验证接口
        SMS.closeSMS();
    }
}
