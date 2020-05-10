package com.pxxy.lawconsult.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.utils.SPUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Button setting_bt_password;
    private Button setting_bt_relation;
    private Button setting_bt_idea;
    private Button setting_bt_phone;
    private Button setting_bt_update;
    private Button setting_bt_exit;
    private TextView setting_tv_phone;
    private CustomTitleView setting_bt_back;

    @Override
    public void initView() {
        setting_bt_password = findViewById(R.id.setting_bt_password);
        setting_bt_relation = findViewById(R.id.setting_bt_relation);
        setting_bt_idea = findViewById(R.id.setting_bt_idea);
        setting_bt_phone = findViewById(R.id.setting_bt_phone);
        setting_bt_update = findViewById(R.id.setting_bt_update);
        setting_bt_exit = findViewById(R.id.setting_bt_exit);
        setting_tv_phone = findViewById(R.id.setting_tv_phone);
        setting_bt_back = findViewById(R.id.setting_bt_back);
        setting_tv_phone.setText(getPhone());
    }

    /**
     * 获取电话号码
     * @return
     */
    public String getPhone(){
        User user = (User) MyApp.getData().get(AppConstant.USER);
        String tel = user.getTel();
        String leftTel = tel.substring(0,3);
        String rightTel = tel.substring(9);
        String newTel = leftTel+"******"+rightTel;
        return newTel;
    }

    @Override
    public void initEvent() {
        setting_bt_password.setOnClickListener(this);
        setting_bt_relation.setOnClickListener(this);
        setting_bt_idea.setOnClickListener(this);
        setting_bt_phone.setOnClickListener(this);
        setting_bt_update.setOnClickListener(this);
        setting_bt_exit.setOnClickListener(this);
        setting_bt_back.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //修改密码
            case R.id.setting_bt_password:
                startActivity(new Intent(getApplication(), ChangePasswordActivity.class));
                break;
                //联系客户
            case R.id.setting_bt_relation:
                startActivity(new Intent(getApplication(), RelationActivity.class));
                break;
                //意见反馈
            case R.id.setting_bt_idea:
                startActivity(new Intent(getApplication(), IdeaActivity.class));
                break;
                //手机号
            case R.id.setting_bt_phone:
                break;
                //版本
            case R.id.setting_bt_update:
                toastLong("已是最新版本");
                //退出
                break;
            case R.id.setting_bt_exit:
                //弹出底部对话框
                showBottomDialog();
                break;
        }
    }
    //封装dialog方法
    private void showBottomDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this,R.layout.dialog_bottom_custom,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_exit1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //sp中设置是否自动登录
                SPUtils.put(AppConstant.SP_AUTOLOGIN_ISAUTOLOGIN, false, SPUtils.FILE_AUTOLOGIN);
                startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                sendExitBrodcast();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_exit2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sendExitBrodcast();
            }
        });

    }

    /**
     * 发送广播
     */
    public void sendExitBrodcast(){
        Intent intent = new Intent();
        intent.setAction(AppConstant.EXIT_APP);
        sendBroadcast(intent);
        closeActivity();
    }

    /**
     * 销毁activity
     */
    public  void closeActivity(){
        this.finish();
    }

}
