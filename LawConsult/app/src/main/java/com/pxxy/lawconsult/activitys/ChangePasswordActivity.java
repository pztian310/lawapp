package com.pxxy.lawconsult.activitys;

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
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.MD5;
import com.pxxy.lawconsult.utils.SPUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_back;
    private EditText et_pwd;
    private EditText et_newpwd;
    private Button bt_findpwd;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //网络错误
                    toastShort("网络错误，请检查网络是否连接");
                    break;
                case 1:
                    toastShort("服务器繁忙，请稍后再试");
                    //服务器错误
                    break;
                case 2:
                    //未找到密码
                    toastShort("未找到该用户");
                    break;
                case 3:
                    //修改密码成功
                    toastShort("修改密码成功");
                    //修改密码成功后将新密码保存到本地sp
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_PASSWORD, newpwd, SPUtils.FILE_AUTOLOGIN);
                    //修改密码成功后关闭该页面
                    finish();
                    break;
                case 4:
                    toastShort("修改密码失败");
                    //修改密码失败
                    break;
            }
        }
    };
    private String newpwd;

    @Override
    public int initLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
        bt_back = findViewById(R.id.changepwd_bt_back);
        et_pwd = findViewById(R.id.changepwd_et_pwd);
        et_newpwd = findViewById(R.id.changepwd_et_newpwd);
        bt_findpwd = findViewById(R.id.changepwd_bt_findpwd);
    }

    @Override
    public void initEvent() {
        bt_back.setOnClickListener(this);
        bt_findpwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changepwd_bt_back:
                finish();
                break;
            case R.id.changepwd_bt_findpwd:
                //验证并修改密码
                changePwd();
                break;
        }
    }

    private void changePwd() {
        String pwd = et_pwd.getText().toString().trim();
        newpwd = et_newpwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            toastShort("请输入旧密码");
            return;
        }else if (TextUtils.isEmpty(newpwd)){
            toastShort("请输入新密码");
            return;
        }
        //从本地获取数据
        String tel= (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
        String password = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_PASSWORD, "", SPUtils.FILE_AUTOLOGIN);

        //获取输入密码的加密字符串
        MD5 md5 = new MD5();
        String md5Pwd = md5.getMD5x100(pwd);

        //判断旧密码是否输入正确
        if (md5Pwd.equals(password)){
            //将旧密码保存到上传到服务器
            postData(tel, newpwd);

        }else{

            toastShort("旧密码输入错误");
        }


    }

    private void postData(String tel,String newpwd) {
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        final Gson gson = new Gson();
        okHttpUtil.findPassword(tel, newpwd, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            //网络错误
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJson = response.body().string();
                Map<String, String> map = gson.fromJson(responseJson, new TypeToken<Map<String, String>>() {
                }.getType());

                String data = map.get(HttpConstant.FIND_PASSWORD );
                if (data.equals(HttpConstant.FINDPASSWORD_FAILURE_SERVER_ERROR)) {
                    handler.sendEmptyMessage(1);
                } else if (data.equals(HttpConstant.FINDPASSWORD_NOTFIND)) {
                    handler.sendEmptyMessage(2);
                } else if (data.equals(HttpConstant.FINDPASSWORD_SUCCESS)) {
                    handler.sendEmptyMessage(3);
                } else if (data.equals(HttpConstant.FINDPASSWORD_FAILURE)) {
                    handler.sendEmptyMessage(4);
                }
            }
        });
    }
}
