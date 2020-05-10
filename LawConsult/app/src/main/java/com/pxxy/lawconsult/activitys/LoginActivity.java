package com.pxxy.lawconsult.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.ClientUser;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.LoginData;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.HintDialogUtils;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.LoadingDialogUtils;
import com.pxxy.lawconsult.utils.MD5;
import com.pxxy.lawconsult.utils.SPUtils;
import com.pxxy.lawconsult.utils.TextViewSetOnClick;
import com.pxxy.lawconsult.view.CustomEditText;

import java.io.IOException;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private CircleImageView loginPhoto;
    private CustomEditText loginTel, loginPassword;
    private TextView loginForgetPassword, loginRegister, loginTerms;
    private Gson gson;
    private Dialog loadingDialog;
    private HintDialogUtils dialog = null;
    public static LoginActivity loginActivity;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //http请求失败，网络错误
                    LoadingDialogUtils.closeDialog(loadingDialog);
                    dialog.initDialog(LoginActivity.this, AppConstant.LOGIN_FAILURE_TITLE, AppConstant.LOGIN_FAILURE_MESSAGE3, true).showDialog();
                    break;
                case 2:
                    //登录成功
                    LoadingDialogUtils.closeDialog(loadingDialog);
                    //执行loginSuccess方法
                    loginSuccess((LoginData) msg.obj);
                    //销毁login界面
                    finish();
                    break;
                case 3:
                    //登录失败，用户名或密码错误
                    LoadingDialogUtils.closeDialog(loadingDialog);
                    dialog.initDialog(LoginActivity.this, AppConstant.LOGIN_FAILURE_TITLE, AppConstant.LOGIN_FAILURE_MESSAGE1, true).showDialog();
                    break;
                case 4:
                    //服务器错误
                    LoadingDialogUtils.closeDialog(loadingDialog);
                    dialog.initDialog(LoginActivity.this, AppConstant.LOGIN_FAILURE_TITLE, AppConstant.LOGIN_FAILURE_MESSAGE2, true).showDialog();
                    break;
                case 5:
                    //设置用户头像
                    String url = (String) msg.obj;
                    Glide.with(LoginActivity.this)
                            .load(url)//加载路径
                            .placeholder(R.mipmap.lawconsult_icon)//图片加载时图片
                            .fallback(R.mipmap.lawconsult_icon)//url为空时的图片
                            .into(loginPhoto);
                    break;
                case 6:
                    //还原头像,如果是网络请求完还原头像，则判断是否是当前输入的值的头像
                    // 是的话则还原，不是的话不还原，修复网络慢头像覆盖的情况
                    if (msg.obj != null) {
                        if (msg.obj.equals(loginTel.getEditTextContent())) {
                            loginPhoto.setImageResource(R.mipmap.lawconsult_icon);
                        }
                    } else {
                        loginPhoto.setImageResource(R.mipmap.lawconsult_icon);
                    }
                    break;
            }
        }
    };

    /**
     * 设置用户回显数据
     */
    private void echoUser() {
        //回显用户名和密码
        String echoTel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
        String echoPassword = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_PASSWORD, "", SPUtils.FILE_AUTOLOGIN);
        String photoUrl = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_PHOTO, "", SPUtils.FILE_AUTOLOGIN);
        //设置账号回显
        if (!echoTel.equals("")) {
            loginTel.setEditTextContent(echoTel);
            //设置光标位置
            loginTel.setSelection(echoTel.length());
        }

        //设置密码回显
        if (!echoPassword.equals("")) {
            loginPassword.setEditTextContent(echoPassword);
        }

        //设置图片
        if (!photoUrl.equals("")) {
            Glide.with(LoginActivity.this).load(photoUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(loginPhoto);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //账号回显
        echoUser();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        loginPhoto = findViewById(R.id.login_ig_photo);
        loginTel = findViewById(R.id.login_et_userName);
        loginPassword = findViewById(R.id.login_et_password);
        loginForgetPassword = findViewById(R.id.login_tv_forget);
        loginRegister = findViewById(R.id.login_tv_register);
        loginTerms = findViewById(R.id.login_bottom_terms);
        gson = new Gson();
        dialog = new HintDialogUtils();
        loginActivity = LoginActivity.this;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        //设置服务条款TextView的点击事件
        TextViewSetOnClick.setOnClick(loginTerms, new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //跳转至服务条款界面
                startActivity(new Intent(LoginActivity.this, Terms_serviceActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                //设置字体颜色
                ds.setColor(getResources().getColor(R.color.theme_color));
                //去除下划线
                ds.setUnderlineText(false);
            }
        }, 10, 14, AppConstant.LOGIN_TERMSTEXT);


        //设置清除密码
        loginPassword.setSeePasswordListener(new CustomEditText.clearEditText() {
            @Override
            public void clearEditTextListener() {
                if ((Boolean) SPUtils.get(AppConstant.SP_CONFIG_CLEARPASSWORD, false, SPUtils.FILE_CONFIG)) {
                    loginPassword.clear();
                    //移除清除密码配置
                    SPUtils.remove(AppConstant.SP_CONFIG_CLEARPASSWORD, SPUtils.FILE_CONFIG);
                    //删除sp中的密码
                    SPUtils.remove(AppConstant.SP_AUTOLOGIN_PASSWORD, SPUtils.FILE_AUTOLOGIN);
                    //清除用户数据
                    SPUtils.remove(AppConstant.SP_AUTOLOGIN_USER, SPUtils.FILE_AUTOLOGIN);
                }
            }
        });

        //设置密码框按删除键一键清除
        loginPassword.addCenterEditTextChangedListener(new CustomEditText.CustomTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s + "";
                //配置中一键清除存在时，用户修改账号输入框中的数据是，自动删除密码和用户数据
                if ((Boolean) SPUtils.get(AppConstant.SP_CONFIG_CLEARPASSWORD, false, SPUtils.FILE_CONFIG)) {
                    //删除sp中的密码
                    clearLoginSP();
                    //清除所有密码
                    loginPassword.clear();
                }

            }
        });
        //设置账号输入框的文本监听事件
        loginTel.addCenterEditTextChangedListener(new CustomEditText.CustomTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    //显示用户头像
                    showUserPhoto();
                } catch (IOException e) {
                    //还原头像
                    handler.sendEmptyMessage(6);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //清除密码框
                if ((Boolean) SPUtils.get(AppConstant.SP_CONFIG_CLEARPASSWORD, false, SPUtils.FILE_CONFIG)) {
                    //清除密码框
                    loginPassword.clear();
                    clearLoginSP();
                }
            }
        });


        //设置tel输入框一键清除头像还原
        loginTel.setRightClearListener(new CustomEditText.clearButtonListener() {
            @Override
            public void clearButtonListener() {
                //头像设为默认头像
                handler.sendEmptyMessage(6);
            }
        });

        //设置password输入框中的一键删除按钮的事件
        loginPassword.setRightClearListener(new CustomEditText.clearButtonListener() {
            @Override
            public void clearButtonListener() {
                if ((Boolean) SPUtils.get(AppConstant.SP_CONFIG_CLEARPASSWORD, false, SPUtils.FILE_CONFIG)) {
                    //删除sp中的密码
                    clearLoginSP();
                }
            }
        });


    }

    /**
     * 清除密码配置s
     */
    private void clearLoginSP() {
        //删除sp中的密码
        SPUtils.remove(AppConstant.SP_AUTOLOGIN_PASSWORD, SPUtils.FILE_AUTOLOGIN);
        //清除用户数据
        SPUtils.remove(AppConstant.SP_AUTOLOGIN_USER, SPUtils.FILE_AUTOLOGIN);
        //删除清除密码配置
        SPUtils.remove(AppConstant.SP_CONFIG_CLEARPASSWORD, SPUtils.FILE_CONFIG);
    }

    /**
     * 显示用户头像
     *
     * @throws IOException
     */
    private void showUserPhoto() throws IOException {
        final String text = loginTel.getEditTextContent();
        if (text.equals(SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN))) {
            Message msg = Message.obtain();
            msg.obj = SPUtils.get(AppConstant.SP_AUTOLOGIN_PHOTO, "", SPUtils.FILE_AUTOLOGIN);
            msg.what = 5;
            handler.sendMessage(msg);
        } else if (TextUtils.isEmpty(loginTel.getEditTextContent())) {
            handler.sendEmptyMessage(6);
        } else {
            OkHttpUtil okHttpUtil = new OkHttpUtil();
            okHttpUtil.getUserPhoto(text, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(6);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Map<String, String> photo = JsonUtils.jsonToMap(response.body().string());
                    Message msg = Message.obtain();
                    //判断用户是否存在
                    if (photo.get(HttpConstant.GETUSERPHOTO).equals(HttpConstant.GETUSERPHOTO_SUCCESS)) {
                        msg.obj = photo.get(HttpConstant.GETUSERPHOTO_DATA);
                        msg.what = 5;
                        handler.sendMessage(msg);
                    } else {
                        msg.obj = text;
                        msg.what = 6;
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }

    /**
     * 忘记密码TextView的点击事件
     *
     * @param view
     */
    public void jumpForgetPassword(View view) {
        startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
    }


    /**
     * 新用户注册TextView的点击事件
     *
     * @param view
     */
    public void jumpRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    /**
     * 登录按钮的点击事件
     *
     * @param view
     */
    public void login(View view) {
        //获取输入框中的数据
        String loginUserNameText = loginTel.getEditTextContent();
        String loginPasswordText = loginPassword.getEditTextContent();
        //获取sp中的用户名密码
        int spType = (int) SPUtils.get(AppConstant.SP_AUTOLOGIN_TYPE, -1, SPUtils.FILE_AUTOLOGIN);
        User user = null;
        if (spType == AppConstant.TYPE_0) {
            String clientUserJson = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_USER, "", SPUtils.FILE_AUTOLOGIN);
            user = JsonUtils.jsonToClientUser(clientUserJson);
        } else if (spType == AppConstant.TYPE_1) {
            String LawerUserJson = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_USER, "", SPUtils.FILE_AUTOLOGIN);
            user = JsonUtils.jsonToLawerUser(LawerUserJson);
        } else if (spType == AppConstant.TYPE_2) {
            String userJson = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_USER, "", SPUtils.FILE_AUTOLOGIN);
            user = JsonUtils.jsonToUser(userJson);
        }

        //判断输入框是否输入了数据
        if (TextUtils.isEmpty(loginUserNameText)) {
            //账号为空
            toastShort(AppConstant.LOGIN_USERNAME_EMPTY);
            return;
        } else if (TextUtils.isEmpty(loginPasswordText)) {
            //密码为空
            toastShort(AppConstant.LOGIN_PASSWORD_EMPTY);
            return;
        }

        //显示正在加载dialog
        loadingDialog = LoadingDialogUtils.showDialog(this, AppConstant.LOGIN_LOADING_DIALOG);

        //判断是否与sp中相等
        if (user != null) {
            if (loginUserNameText.equals(user.getTel()) && loginPasswordText.equals(user.getPassword())) {
                if (user instanceof ClientUser) {
                    ClientUser clientUser = (ClientUser) user;
                    //把用户数据保存至MyApp中
                    MyApp.putData(AppConstant.USER, clientUser);
                    MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_0);
                    //sp中设置是否自动登录
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_ISAUTOLOGIN, true, SPUtils.FILE_AUTOLOGIN);
                    //关闭加载dialog
                    LoadingDialogUtils.closeDialog(loadingDialog);
                    //跳转至主页面
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                    return;
                } else if (user instanceof LawerUser) {
                    LawerUser lawerUser = (LawerUser) user;
                    //把用户数据保存至MyApp中
                    MyApp.putData(AppConstant.USER, lawerUser);
                    MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_1);
                    //关闭加载dialog
                    LoadingDialogUtils.closeDialog(loadingDialog);
                    //sp中设置是否自动登录
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_ISAUTOLOGIN, true, SPUtils.FILE_AUTOLOGIN);
                    //跳转至主页面
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                    return;
                } else if (user instanceof User) {
                    //把用户数据保存至myApp中
                    MyApp.putData(AppConstant.USER, user);
                    MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_2);
                    //关闭加载dialog
                    LoadingDialogUtils.closeDialog(loadingDialog);
                    //sp中设置是否自动登录
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_ISAUTOLOGIN, true, SPUtils.FILE_AUTOLOGIN);
                    //跳转至管理员界面
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    closeActivity();
                    return;
                }
            }
        }

        //请求网络判断账号密码是否正确
        OkHttpUtil okHttpUtil = new OkHttpUtil();

        //登录请求
        okHttpUtil.login(loginUserNameText, loginPasswordText, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                requestSuccess(response);
            }
        });
    }

    /**
     * 请求成功处理数据
     *
     * @param response
     * @throws IOException
     */
    private void requestSuccess(Response response) throws IOException {
        //获取json对象
        String loginJson = response.body().string();
        //json数据解析转化成LoginData对象
        LoginData loginUser = gson.fromJson(loginJson, LoginData.class);
        //获取登录状态
        String loginState = loginUser.getLoginState();
        //登录状态验证
        if (loginState.equals(LoginData.LOGINSTATE_SUCCESS)) {
            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = loginUser;
            //发送信息
            handler.sendMessage(msg);
        } else if (loginState.equals(LoginData.LOGINSTATE_FAILURE)) {
            handler.sendEmptyMessage(3);
        } else if (loginState.equals(LoginData.LOGINSTATE_FAILURE_SERVER_ERROR)) {
            handler.sendEmptyMessage(4);
        }
    }

    /**
     * 登录成功的方法
     *
     * @param loginUser
     */
    private void loginSuccess(LoginData loginUser) {
        //sp中设置tel
        SPUtils.put(AppConstant.SP_AUTOLOGIN_TEL, loginTel.getEditTextContent(), SPUtils.FILE_AUTOLOGIN);
        SPUtils.put(AppConstant.SP_AUTOLOGIN_PASSWORD, MD5.getMD5x100(loginPassword.getEditTextContent()), SPUtils.FILE_AUTOLOGIN);
        //sp中设置是否自动登录
        SPUtils.put(AppConstant.SP_AUTOLOGIN_ISAUTOLOGIN, true, SPUtils.FILE_AUTOLOGIN);
        //加入配置文件
        SPUtils.put(AppConstant.SP_CONFIG_CLEARPASSWORD, true, SPUtils.FILE_CONFIG); //配置自动清除密码


        //把用户数据转换为json对象存入sp中
        if (loginUser.getType() == AppConstant.TYPE_0) {
            ClientUser user = loginUser.getClientUser();
            Log.d("type", "0");
            String clientUserJson = JsonUtils.clientUserToJson(user);
            Log.d("json", clientUserJson);
            //设置用户类型
            SPUtils.put(AppConstant.SP_AUTOLOGIN_TYPE, AppConstant.TYPE_0, SPUtils.FILE_AUTOLOGIN);
            //json对象保存在sp中
            SPUtils.put(AppConstant.SP_AUTOLOGIN_USER, clientUserJson, SPUtils.FILE_AUTOLOGIN);
            //存放头像url
            SPUtils.put(AppConstant.SP_AUTOLOGIN_PHOTO, user.getImage(), SPUtils.FILE_AUTOLOGIN);
            //把用户数据存入MyApp中
            MyApp.putData(AppConstant.USER, user);
            //设置类型
            MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_0);
            //跳转主界面
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        } else if (loginUser.getType() == AppConstant.TYPE_1) {
            Log.d("type", "1");
            LawerUser user = loginUser.getLawerUser();
            //json对象保存在sp中
            String lawerUserToJson = JsonUtils.lawerUserToJson(user);
            //设置用户类型
            SPUtils.put(AppConstant.SP_AUTOLOGIN_TYPE, AppConstant.TYPE_1, SPUtils.FILE_AUTOLOGIN);
            //存放对象数据
            SPUtils.put(AppConstant.SP_AUTOLOGIN_USER, lawerUserToJson, SPUtils.FILE_AUTOLOGIN);
            //存放头像url
            SPUtils.put(AppConstant.SP_AUTOLOGIN_PHOTO, user.getImage(), SPUtils.FILE_AUTOLOGIN);
            //把用户数据存入MyApp中
            MyApp.putData(AppConstant.USER, user);
            //设置类型
            MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_1);
            //跳转主界面
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        } else if (loginUser.getType() == AppConstant.TYPE_2) {
            Log.d("type", "2");
            User user = loginUser.getUser();
            //json对象保存在sp中
            String userJson = JsonUtils.userToJson(user);
            //设置用户类型
            SPUtils.put(AppConstant.SP_AUTOLOGIN_TYPE, AppConstant.TYPE_2, SPUtils.FILE_AUTOLOGIN);
            //存放对象数据
            SPUtils.put(AppConstant.SP_AUTOLOGIN_USER, userJson, SPUtils.FILE_AUTOLOGIN);
            //存放头像url
            SPUtils.put(AppConstant.SP_AUTOLOGIN_PHOTO, user.getImage(), SPUtils.FILE_AUTOLOGIN);
            //把用户数据存入MyApp中
            MyApp.putData(AppConstant.USER, user);
            //设置类型
            MyApp.putData(AppConstant.LOGIN_USER_TYPE, AppConstant.TYPE_2);
            //跳转管理员界面
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
        }

    }

    /**
     * 关闭活动
     */
    public static void closeLoginActivity() {
        loginActivity.finish();
    }
}
