package com.pxxy.lawconsult.activitys;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tbruyelle.rxpermissions2.RxPermissionsFragment;

import io.reactivex.functions.Consumer;

public class RelationActivity extends BaseActivity {

    private CustomTitleView title;
    private RxPermissions rxPermissions;

    @Override
    public int initLayout() {
        return R.layout.activity_relation;
    }

    @Override
    public void initView() {
        //创建rxPermissions 对象
        rxPermissions = new RxPermissions(this);
        title = findViewById(R.id.activity_relation_title);
    }

    @Override
    public void initEvent() {
        //back 返回到FragmentMine
        title.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 联系客服的点击数事件
     */
    public void relation(View v) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("即将跳转到拨号界面");
        //自定义布局
        TextView textView = new TextView(this);
        textView.setText("联系客服");
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
                                   //聿言的电话号码
                                   call("15869122349");
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

    //跳过拨号界面 直接拨打电话
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
