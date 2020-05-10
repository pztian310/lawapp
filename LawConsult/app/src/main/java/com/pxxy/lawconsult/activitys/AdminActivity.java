package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.utils.HintDialogUtils;
import com.pxxy.lawconsult.utils.SPUtils;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdminActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton setting;
    private Button bt_clientUser, bt_lawyerUser, bt_addUser, bt_case, bt_addCase, bt_law;
    private CircleImageView photo;
    private boolean isExit = false;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
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
        return R.layout.activity_admin;
    }

    @Override
    public void initEvent() {
        //设置监听
        bt_clientUser.setOnClickListener(this);
        bt_lawyerUser.setOnClickListener(this);
        bt_addUser.setOnClickListener(this);
        bt_case.setOnClickListener(this);
        bt_addCase.setOnClickListener(this);
        bt_law.setOnClickListener(this);
        setting.setOnClickListener(this);
        //获取用户数据
        User user = (User) MyApp.getData().get(AppConstant.USER);
        //加载头像
        Glide.with(this)
                .load(user.getImage())
                .error(R.mipmap.lawconsult_icon)
                .placeholder(R.mipmap.lawconsult_icon)
                .into(photo);
    }

    @Override
    public void initView() {
        setting = findViewById(R.id.admin_setting);
        bt_clientUser = findViewById(R.id.admin_clientuser);
        bt_lawyerUser = findViewById(R.id.admin_lawyeruser);
        bt_addUser = findViewById(R.id.admin_add_user);
        bt_case = findViewById(R.id.admin_case);
        bt_addCase = findViewById(R.id.admin_add_case);
        bt_law = findViewById(R.id.admin_law);
        photo = findViewById(R.id.admin_photo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_setting:
                //退出按钮
                userExit();
                break;
            case R.id.admin_clientuser:
                Intent clientIntent = new Intent(AdminActivity.this, AdminUserActivity.class);
                clientIntent.putExtra(AppConstant.ADMIN_USER_TYPE, AppConstant.ADMIN_USER_CLIENTUSER);
                startActivity(clientIntent);
                //客户管理
                break;
            case R.id.admin_lawyeruser:
                //律师管理
                Intent lawyerIntent = new Intent(AdminActivity.this, AdminUserActivity.class);
                lawyerIntent.putExtra(AppConstant.ADMIN_USER_TYPE, AppConstant.ADMIN_USER_LAWYERUSER);
                startActivity(lawyerIntent);
                break;
            case R.id.admin_add_user:
                //添加用户
                startActivity(new Intent(AdminActivity.this, AdminAddUserActivity.class));
                break;
            case R.id.admin_case:
                //案例管理
                startActivity(new Intent(AdminActivity.this,AdminCaseActivity.class));
                break;
            case R.id.admin_add_case:
                //添加案例
                startActivity(new Intent(AdminActivity.this,AdminAddCaseActivity.class));
                break;
            case R.id.admin_law:
                //法条管理
                startActivity(new Intent(AdminActivity.this, AdminStatuteActivity.class));
                break;
        }
    }

    private void userExit() {
        final HintDialogUtils dialogUtils = new HintDialogUtils();
        //初始化弹窗
        dialogUtils.initDialog(AdminActivity.this,AppConstant.ADMIN_EXIT_DIALOG_TITLE,AppConstant.ADMIN_EXIT_DIALOG_MESSAGE,true);
        //设置取消按钮
        dialogUtils.setLeftButton(AppConstant.ADMIN_EXIT_DIALOG_CANCLEBUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtils.closeDialog();
            }
        });
        //设置确定按钮
        dialogUtils.setRightButton(AppConstant.ADMIN_EXIT_DIALOG_CONFIGBUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消自动登录
                SPUtils.put(AppConstant.SP_AUTOLOGIN_ISAUTOLOGIN, false, SPUtils.FILE_AUTOLOGIN);
                //跳转登录界面
                startActivity(new Intent(AdminActivity.this,LoginActivity.class));
                //销毁当前界面
                closeActivity();
                //关闭dialog
                dialogUtils.closeDialog();
            }
        });
        //显示按钮
        dialogUtils.showDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按返回键不销毁activity
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (!isExit){
                isExit = true;
                toastShort(AppConstant.HOME_ACTIVITY_BACKTOAST);
                handler.sendEmptyMessageDelayed(1,2000);
            }else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
