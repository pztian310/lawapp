package com.pxxy.lawconsult.activitys;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.DBConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.db.AppSqlite;
import com.pxxy.lawconsult.entity.ClientUser;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.SPUtils;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.superluo.textbannerlibrary.TextBannerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ConsultActivity extends BaseActivity {

    private TextView tv_lawtype;
    String[] single_list = {"婚姻家庭", "房产纠纷", "工伤赔偿", "交通事故", "民间借贷", "劳动纠纷", "刑事诉讼", "知识产权"};
    private AlertDialog.Builder builder;
    //index记住数组下标
    int index = 0;
    private CustomTitleView title;
    private EditText consultContent;
    private String content;
    private String lawtype;
    private TextBannerView tv_banner;
    private ArrayList<String> mlist;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x1:
                    toastLong("发布成功");
                    finish();
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
                case 0x5:
                    getBannerDataSuccess(msg);
                    break;
                    default:
                        break;
            }
        }
    };


    private void getBannerDataSuccess(Message msg) {
        List<Map<String,String>> list= (List<Map<String, String>>) msg.obj;
        //用来存放图片的list,律师头像
    //    List<String> imageList = new ArrayList<>();
        for (Map<String,String> map:list){
          //  imageList.add(map.get("image"));
            mlist.add(map.get("lawyerName")+"律师抢答了****"+map.get("userTel").substring(7)+"的咨询");
        }
        tv_banner.setDatas(mlist);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_consult;
    }

    @Override
    public void initView() {
        //法律类型
        tv_lawtype = findViewById(R.id.consult_tv_lawtype);
        //发布内容
        consultContent = findViewById(R.id.consult_et_content);
        //获取标题栏
        title = findViewById(R.id.consult_title);
        //轮播文字
        tv_banner = findViewById(R.id.consult_tv_banner);
    }
    @Override
    public void initData() {

        mlist = new ArrayList<>();
        getBannerData();

    }

    private void getBannerData(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpConstant.SERVER_LAWYERBANNER).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0x4);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bannerData = response.body().string();
                Gson gson = new Gson();
                List<Map<String,String>> bannerList = gson.fromJson(bannerData, new TypeToken<List<Map<String, String>>>() {
                }.getType());
                Message msg = Message.obtain();
                msg.what=0x5;
                msg.obj=bannerList;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void initEvent() {
        //back键 ，返回FragmentConsult界面
        title.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    //发布事件
    public void questionSubmit(View v) {
        content = consultContent.getText().toString().trim();
        lawtype = tv_lawtype.getText().toString().trim();
        if (content.equals("")|| content.equals(null)){
            toastShort("请输入咨询内容");
        }else{
          posttieData();
        }
    }

    private void posttieData() {
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        String  tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
        okHttpUtil.saveConsultContent(tel, lawtype, content, getDate(), new Callback() {
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
    //获取系统当前时间
    public String getDate(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        return format;
    }
}
