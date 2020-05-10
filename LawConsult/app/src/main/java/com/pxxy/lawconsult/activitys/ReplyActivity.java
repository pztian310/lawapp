package com.pxxy.lawconsult.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.adapter.ReplyAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.DBConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.PosttieData;
import com.pxxy.lawconsult.entity.ReplyLawyerData;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.SPUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ReplyActivity extends BaseActivity {

    private TextView name;
    private TextView date;
    private TextView content;
    private CircleImageView userAvatar;
    private ListView listview;
    private Context context = ReplyActivity.this;
    private TextView replyNumber;
    private CustomTitleView title;
    private EditText et_content;
    private Button btn_reply;
    private String tel;
    private PosttieData posttie;
    private List<ReplyLawyerData> mlist;
    private ReplyAdapter replyAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x1:
                    toastShort("网络异常");
                    break;
                case 0x2:
                    et_content.setVisibility(View.VISIBLE);
                    btn_reply.setVisibility(View.VISIBLE);
                    break;
                case 0x3:
                    toastShort("回复成功");
                    et_content.setText(null);
                    btn_reply.setClickable(false);
                    //更新律师回复
                    getLawyerData();
                    break;
                case 0x4:
                    toastShort("暂无律师回复");
                    break;
                case 0x5:
                    getLawyerDataSuccess(msg);
                    break;
                case 0x6:
                   toastShort("服务器繁忙");
                default:
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
        return R.layout.activity_reply;
    }


    @Override
    public void initView() {
        //找到控件的id
        listview = findViewById(R.id.reply_listview);
        userAvatar = findViewById(R.id.reply_photo_avatar);
        content = findViewById(R.id.reply_tv_consultcontent);
        date = findViewById(R.id.reply_tv_date);
        name = findViewById(R.id.reply_tv_name);
        replyNumber = findViewById(R.id.reply_tv_replynum);
        title = findViewById(R.id.reply_consult_title);
        //获取回复框
        et_content = findViewById(R.id.reply_et_content);
        btn_reply = findViewById(R.id.reply_btn_reply);

    }

    @Override
    public void initData() {
        //获取consult_essay页面传过来的值
        Intent intent = getIntent();
        posttie = (PosttieData) intent.getSerializableExtra("Posttie");
        Glide.with(this).load(posttie.getUserAvatar()).error(R.drawable.head_photo).into(userAvatar);
        content.setText(posttie.getContent());
        date.setText(posttie.getDate());
        if (posttie.getNickName().length() > 10) {
            name.setText("****" + posttie.getNickName().substring(7));
        } else {
            name.setText(posttie.getNickName());
        }
        //设置适配器
        mlist = new ArrayList<>();
        replyAdapter = new ReplyAdapter(context, mlist);
        listview.setAdapter(replyAdapter);
        //获取回帖的律师信息
        getLawyerData();


    }

    @Override
    public void initEvent() {
        //back键   返回到ConsultEssayActivity页面
        title.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //监听EditText控件
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn_reply.setBackground(getResources().getDrawable(R.drawable.loign_activity_login_button));
                btn_reply.setClickable(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    btn_reply.setClickable(false);
                    btn_reply.setBackgroundColor(Color.parseColor("#ededed"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //回帖事件
        btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastShort("点击了");
                //获取回复的内容
                String content = et_content.getText().toString();
                //获取发帖的文章编号
                int id= posttie.getT_ID();
               //获取系统当前时间
                String date = getDate();
                //获取律师手机号
                String  tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
                OkHttpUtil okHttpUtil = new OkHttpUtil();
                okHttpUtil.saveLawyerReply(id, content, date, tel, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(0x1);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //保存成功 服务器返回success ,失败返回error
                        String resutlt = response.body().string();
                        Log.d("321", resutlt);
                        if(resutlt.equals("success")){
                            handler.sendEmptyMessage(0x3);
                        }else if(resutlt.equals("error")){
                            handler.sendEmptyMessage(0x6);
                        }
                    }
                });

            }
        });
        //判断用户是不是律师
        judgeLawyer();

    }

    private void judgeLawyer() {
        tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
        new OkHttpUtil().request(tel, HttpConstant.SERVER_ISLAWYER, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0x1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String type = response.body().string();
                //律师返回Lawyer 普通用户返回Normal 管理员返回Manager
                if (type.equals("Lawyer")) {
                    handler.sendEmptyMessage(0x2);
                }
            }
        });
    }

    private void getLawyerData() {
        //获取当前文章编号
        int t_id = posttie.getT_ID();
        new OkHttpUtil().getLawyerData(t_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0x1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String replyLawyerJson = response.body().string();
                if (replyLawyerJson.equals("204")) {
                    //暂无律师回复
                    handler.sendEmptyMessage(0x4);
                } else {
                    List<ReplyLawyerData> replyLawyerData = JsonUtils.jsonToReplyLawyerList(replyLawyerJson);
                    Message msg = Message.obtain();
                    msg.what = 0x5;
                    msg.obj = replyLawyerData;
                    handler.sendMessage(msg);
                }
            }
        });

    }

    //成功获取律师回复信息
    private void getLawyerDataSuccess(Message msg) {
        List<ReplyLawyerData> list = (List<ReplyLawyerData>) msg.obj;
        if (mlist!=null){
            mlist.clear();
        }
        for (ReplyLawyerData replyLawyerData:list){
            mlist.add(replyLawyerData);
        }
        replyNumber.setText("回复"+mlist.size());
        replyAdapter.notifyDataSetChanged();

    }
    //获取系统当前时间
    public String getDate(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        return format;
    }
}
