package com.pxxy.lawconsult.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.RegisterData;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.HintDialogUtils;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomEditText;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminAddUserActivity extends BaseActivity {
    private CustomEditText userTel, userPassword;
    private CustomTitleView titleView;
    private OkHttpUtil okHttpUtil;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //网络连接失败
                    Toast.makeText(AdminAddUserActivity.this, AppConstant.ADMIN_USER_ADDUSER_TOAST_NOT_INTERNET, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //用户已存在
                    userExist();
                    break;
                case 2:
                    //用户不存在
                    userNotExist();
                    break;
                case 3:
                    //服务器错误
                    Toast.makeText(AdminAddUserActivity.this,AppConstant.ADMIN_USER_ADDUSER_TOAST_SERVER_ERROR,Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    addUserSuccess();
                    break;
                case 5:
                    //添加失败
                    toastShort(AppConstant.ADMIN_USER_ADDUSER_FAILURE);
                    break;
            }
        }

        /**
         * 添加用户成功
         */
        private void addUserSuccess() {
            //用户添加成功
            final HintDialogUtils dialog = new HintDialogUtils();
            dialog.initDialog(AdminAddUserActivity.this,
                    AppConstant.ADMIN_USER_ADDUSER_DIALOG_SUCCESS_TITLE,
                    AppConstant.ADMIN_USER_ADDUSER_SUCCESS,false)
                    .setRightButton(AppConstant.ADMIN_USER_ADDUSER_CONFIRM_BUTTON, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //关闭dialog
                            dialog.closeDialog();
                            //关闭activity
                            closeActivity();
                        }
                    }).showDialog();
        }
    };

    /**
     * 用户不存在
     */
    private void userNotExist() {
        //获取用户名和密码
        String Tel = userTel.getEditTextContent();
        String password = userPassword.getEditTextContent();
        //进行用户注册
        okHttpUtil.register(Tel, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String registerJson = response.body().string();
                //获取registerdata对象
                try {
                    RegisterData registerData = JsonUtils.jsonToRegisterData(registerJson);
                    if (registerData.getRegisterState().equals(RegisterData.REGISTERSTATE_SUCCESS)){
                        handler.sendEmptyMessage(4);
                    }else if (registerData.getRegisterState().equals(RegisterData.REGISTERSTATE_FAILURE)){
                        handler.sendEmptyMessage(5);
                    }else if (registerData.getRegisterState().equals(RegisterData.REGISTERSTATE_FAILURE_SERVER_ERROR)){
                        handler.sendEmptyMessage(3);
                    }
                }catch (Exception e){
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 用户存在
     */
    private void userExist() {
        //弹出dialog提示用户已存在
        HintDialogUtils dialogUtils = new HintDialogUtils();
        dialogUtils.initDialog(AdminAddUserActivity.this,
                AppConstant.ADMIN_USER_ADDUSER_DIALOG_FAILURE_TITLE,
                AppConstant.ADMIN_USER_ADDUSER_DIALOG_FAILURE_MESSAGE,
                true).showDialog();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_admin_add_user;
    }


    @Override
    public void initView() {
        userTel = findViewById(R.id.admin_add_user_tel);
        userPassword = findViewById(R.id.admin_add_user_password);
        titleView = findViewById(R.id.admin_add_user_title);
        okHttpUtil = new OkHttpUtil();
    }

    @Override
    public void initEvent() {
        //设置title的返回事件
        titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    /**
     * 添加用户按钮的点击事件
     *
     * @param view
     */
    public void addUser(View view) {
        //获取用户名和密码
        String tel = userTel.getEditTextContent();
        String password = userPassword.getEditTextContent();

        //判断是否为空
        if (TextUtils.isEmpty(tel)) {
            toastShort(AppConstant.ADMIN_USER_ADDUSER_TOAST_TEL);
            return;
        } else if (TextUtils.isEmpty(password)) {
            toastShort(AppConstant.ADMIN_USER_ADDUSER_TOAST_PASSWORD);
            return;
        }

        //判断用户是否存在
        okHttpUtil.isUserExist(tel, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络连接失败
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String isUserExistJson = response.body().string();
                Map<String, String> map = JsonUtils.jsonToMap(isUserExistJson);
                //获取返回数据
                String result = map.get(HttpConstant.IS_USEREXIST);
                if (result.equals(HttpConstant.IS_USEREXIST_EXIST)) {
                    handler.sendEmptyMessage(1);
                } else if (result.equals(HttpConstant.IS_USEREXIST_NOT_EXIST)) {
                    handler.sendEmptyMessage(2);
                } else if (result.equals(HttpConstant.IS_USEREXIST_SERVERERROR)) {
                    handler.sendEmptyMessage(3);
                }
            }
        });
    }
}
