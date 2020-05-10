package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.SPUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlterNickNameActivity extends BaseActivity {

    private CustomTitleView alter_title;
    private TextView alter_nick_name;
    private EditText alter_signature;
    private String tel;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    toastShort("网络错误");
                    break;
                case 2:
                    toastShort("修改成功");
                    handler.sendEmptyMessageAtTime(5, 1000);
                    break;
                case 3:
                    toastShort("修改失败，请稍后再试");
                    handler.sendEmptyMessageAtTime(5, 1000);
                    break;
                case 4:
                    toastShort("服务器繁忙，请稍后再试");
                    handler.sendEmptyMessageAtTime(5, 1000);
                    break;
                case 5:
                    finish();
                    break;
            }
        }
    };

    //初始化


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //接收数据 用于显示
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        String signature = intent.getStringExtra("signature");
        //将数据显示到文本
        alter_nick_name.setText(nickname);
        alter_signature.setText(signature);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_alter_nick_name;
    }



    @Override
    public void initView() {
        alter_title = findViewById(R.id.alter_title);
        alter_nick_name = findViewById(R.id.alter_nick_name);
        alter_signature = findViewById(R.id.alter_et_signature);
    }

    @Override
    public void initEvent() {
        alter_title.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //按钮的点击事件
    public void alterTure(View v){
            //昵称
        String nickName = alter_nick_name.getText().toString().trim();
        String signature = alter_signature.getText().toString().trim();
        //查询本地电话号码
        if (TextUtils.isEmpty(nickName)){
            toastShort("昵称不能为空");
            return;
        }else if (TextUtils.isEmpty(signature)){
            toastShort("请输入个性签名");
            return;
        }
        //获取手机号
        tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
        //将数据提交到服务器
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.setPickName(tel, nickName, signature, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDate = response.body().string();
                Log.d("321", jsonDate);
               Map<String,String> map = JsonUtils.jsonToMap(jsonDate);
            //    Log.d("321", map.get(HttpConstant.GETPICKNAME));

               /* Gson gson=new Gson();
                Map<String,String> map = gson.fromJson(jsonDate, new TypeToken<Map<String, String>>() {
                }.getType());*/
                String s = map.get(HttpConstant.SETPICKNAME);
                if (s.equals(HttpConstant.SETPICKNAME_SUCCESS)){
                    handler.sendEmptyMessage(2);
                }else if(s.equals(HttpConstant.SETPICKNAME__FAILURE)){
                    handler.sendEmptyMessage(3);
                }else if (s.equals(HttpConstant.SETPICKNAME__SERVER)){
                    handler.sendEmptyMessage(4);
                }

            }
        });


    }



}
