package com.pxxy.lawconsult.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.LawerUser;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class LawyerMainActivity extends BaseActivity implements View.OnClickListener{
    private com.pxxy.lawconsult.view.CustomTitleView customTitleView;
    private TextView tv_name,tv_age,tv_sex,tv_address,tv_introduction;
    private ImageView image_photo;
    private LawerUser mlawerUser;
    private Intent lawerUserData;
    private String mian_sex,tel,photo,lawer_id;
    private int sex;
    private RadioButton rb_call,rb_consult;
    private RxPermissions rxPermissions;
    @Override
    public int initLayout() {
        return R.layout.activity_lawyer_main;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        customTitleView.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rb_call.setOnClickListener(this);
        rb_consult.setOnClickListener(this);
        //创建rxPermissions 对象
        rxPermissions = new RxPermissions(this);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent=this.getIntent();
        LawerUser lawerUser= (LawerUser) intent.getSerializableExtra("lawUser");
        lawer_id=String.valueOf(lawerUser.getLawerId());
        String name=lawerUser.getLawerName();
        tel=lawerUser.getTel();
        photo=lawerUser.getImage();
        int age=lawerUser.getLawerAge();
        sex=lawerUser.getLawerSex();
        tv_name.setText(name);
        tv_age.setText(Integer.toString(age)+"岁");
        tv_address.setText(lawerUser.getAddress());
        tv_introduction.setText(lawerUser.getIntroduction());
        if(sex==0){
            mian_sex="女";
        }else{
            mian_sex="男";
        }
        tv_sex.setText(mian_sex);
        Glide.with(MyApp.getContext())
                .load(photo)
                .error(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .into(image_photo);
    }



    @Override
    public void initView() {
        super.initView();
        customTitleView = findViewById(R.id.LawyerMainActivity_customTitleView);
        tv_name = findViewById(R.id.lawyermainactivity_tv_name);
        tv_age = findViewById(R.id.lawyermainactivity_tv_age);
        tv_sex = findViewById(R.id.lawyermainactivity_tv_sex);
        tv_address = findViewById(R.id.lawyermainactivity_tv_address);
        tv_introduction = findViewById(R.id.lawyermainactivity_tv_introduction);
        image_photo = findViewById(R.id.lawyermainactivity_image_photo);
        rb_call = findViewById(R.id.lawyermainactivity_rb_call);
        rb_consult = findViewById(R.id.lawyermainactivity_rb_consult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
                //拨打律师电话
            case R.id.lawyermainactivity_rb_call:
                callLawer();
                break;
                //文字咨询律师
            case R.id.lawyermainactivity_rb_consult:
                consult();
                break;
        }
    }

    private void consult() {
        //跳转到文字咨询页面
        Intent intent=new Intent(MyApp.getContext(),TextConsultationActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("lawer_id",lawer_id);
        bundle.putSerializable("image",photo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void callLawer() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("即将跳转到拨号界面");
        //自定义布局
        TextView textView = new TextView(this);
        textView.setText("联系律师");
        textView.setPadding(10, 20, 10, 10);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        textView.setTextColor(getResources().getColor(R.color.smssdk_black));
        alertDialog.setCustomTitle(textView);
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //运行时权限
                rxPermissions.request(Manifest.permission.CALL_PHONE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean){
                                    //权限允许
                                    //律师的电话号码
                                    call(tel);
                                }else {
                                    //权限失败
                                    toastShort("权限不够 请前往设置更改拨打电话权限");
                                }
                            }
                        });
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
