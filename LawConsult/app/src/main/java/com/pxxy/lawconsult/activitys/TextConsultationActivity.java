package com.pxxy.lawconsult.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.PosttieData;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.SPUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TextConsultationActivity extends BaseActivity {
    private AlertDialog.Builder builder;
    private String tel;
    private String lawtype;
    private OkHttpUtil okHttpUtil;
    private CustomTitleView customTitleView;
    private String lawer_id;
    private int m=120;
    String[] single_list = {"婚姻家庭", "房产纠纷", "工伤赔偿", "交通事故", "民间借贷", "劳动纠纷", "刑事诉讼", "知识产权"};
    //index记住数组下标
    int index = 0;
    private TextView tv_lawtype;
    private Button btn_sumbit;
    private EditText editText;
    private String content,photo;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x1:
                    toastLong("发布成功");
                    handler.sendEmptyMessageDelayed(5,2000);
                    break;
                case 0x2:
                    toastShort("发布失败");
                    break;
                case 0x3:
                    toastShort("服务器繁忙");
                    break;
                case 0x4:
                    toastShort("请检查网络");
                    break;
                case 5:
                    Intent intent=new Intent(MyApp.getContext(),ReplyActivity.class);
                    PosttieData posttieData=new PosttieData(m,photo,tel,content,getDate());
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("Posttie",posttieData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    m++;
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public int initLayout() {
        return R.layout.activity_textconsultation;
    }

    @Override
    public void initView() {
        super.initView();
        //法律类型
        tv_lawtype=findViewById(R.id.textconsultation_tv_lawtype);
        //返回鍵
        customTitleView=findViewById(R.id.textconsultation_customtitleview);
        btn_sumbit=findViewById(R.id.textconsultation_bt_submit);
        editText=findViewById(R.id.textconsultation_ed);
        okHttpUtil=new OkHttpUtil();
    }

    @Override
    public void initData() {
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        lawer_id=bundle.getString("lawer_id");
        photo=bundle.getString("image");
        lawtype = tv_lawtype.getText().toString().trim();
        tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //設置監聽
        customTitleView.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_sumbit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(editText.getText().toString().trim().equals("")){
                                Toast.makeText(MyApp.getContext(),"请输入需要咨询的内容。",Toast.LENGTH_SHORT).show();
                            }else{
                                content=editText.getText().toString().trim();
                                okHttpUtil.Byconsulting(lawer_id,tel,lawtype,content,getDate(), new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        handler.sendEmptyMessage(0x4);
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String consultData = response.body().string();
                                        Map<String, String> map = JsonUtils.jsonToMap(consultData);
                                        String consultKey = map.get(HttpConstant.CONSULT_IS_SAVESUCCESS);
                                        if(HttpConstant.CONSULT_SAVESUCCESS.equals(consultKey)){
                                            handler.sendEmptyMessage(0x1);
                                        }else if(HttpConstant.CONSULT_SAVEFAIL.equals((consultKey))){
                                            handler.sendEmptyMessage(0x2);
                                        }else if(HttpConstant.CONSULT_SERVERERRROR.equals(consultKey)){
                                            handler.sendEmptyMessage(0x3);
                                        }
                                    }
                                });

                            }

                        }
                    });
                }

    private String getDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        return format;
    }


    //打开咨询类型对话框
    public void lawTypeDialog(View v) {
        //获取对话框构造器

        builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("问题类型");
        //设置单选对话框
        builder.setSingleChoiceItems(single_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                index = which;
            }
        });
        //设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_lawtype.setText(single_list[index]);
            }
        });
        //创建对话框并且显示
        builder.create().show();
    }
}
