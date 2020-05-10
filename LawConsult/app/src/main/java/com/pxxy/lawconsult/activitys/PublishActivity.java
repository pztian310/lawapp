package com.pxxy.lawconsult.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.adapter.ConsultEssayAdapter;
import com.pxxy.lawconsult.adapter.MyPublishAdapter;
import com.pxxy.lawconsult.adapter.ReplyAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.PosttieData;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.LoadingDialogUtils;
import com.pxxy.lawconsult.utils.SPUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PublishActivity extends BaseActivity {
    private CustomTitleView customTitleView;
    private ListView listView;
    private ReplyAdapter replyAdapter;
    private Context context = PublishActivity.this;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x1:
                    publish_iv.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    toastShort("请检查网络");
                    break;
                case 0x2:
                    getPosttieData(msg);
                    break;
                case 3:
                    toastShort("网络错误");
                    break;
                case 4:

                    getNews(msg);
                    break;
                case 5:
                    //显示暂无消息
                    publish_iv.setVisibility(View.VISIBLE);
                    publish_iv.setImageResource(R.drawable.evaluate10);
                    listView.setVisibility(View.GONE);
                    break;
            }
        }

        private void getNews(Message msg) {
            List<PosttieData> posttieDataList = (List<PosttieData>) msg.obj;
            ConsultEssayAdapter adapterPublish = new ConsultEssayAdapter(getApplication(), posttieDataList);
            listView.setAdapter(adapterPublish);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PosttieData itemAtPosition = (PosttieData) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getApplication(), ReplyActivity.class);
                    intent.putExtra("Posttie", itemAtPosition);
                    startActivity(intent);
                }
            });

        }
    };


    private ImageView publish_iv;
    private Dialog dialog;

    private void getPosttieData(Message msg) {
        List<PosttieData> list = (List<PosttieData>) msg.obj;
        if (list.size() == 0) {
            publish_iv.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            ConsultEssayAdapter consultEssayAdapter = new ConsultEssayAdapter(this, list);
            listView.setAdapter(consultEssayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PosttieData itemAtPosition = (PosttieData) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getApplication(), ReplyActivity.class);
                    intent.putExtra("Posttie", itemAtPosition);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_publish;
    }

    @Override
    public void initView() {
        //找控件
        customTitleView = findViewById(R.id.activity_publish_title);
        listView = findViewById(R.id.activity_publish_listview);
        publish_iv = findViewById(R.id.activity_publish_iv);


    }

    @Override
    public void initEvent() {
        super.initEvent();
        //返回按钮
        customTitleView.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

        //获取传过来的type值
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        if (type == 0) {
            //普通用户
            getMyPublish();
        } else {
            //律师

            getMyConsult();

        }

    }

    private void getMyConsult() {
        //标题改为消“消息”
        customTitleView.setCenterTextViewText("消息");


        //获取消息
        LawerUser user = (LawerUser) MyApp.getData().get(AppConstant.USER);
        int lawerId = user.getLawerId();
        String lawyer_id = String.valueOf(lawerId);
        new OkHttpUtil().getUserToLawyer(lawyer_id,"", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(3);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Log.d("aaaa", jsonData);
                if (jsonData == null && jsonData.length() == 0) {
                    handler.sendEmptyMessage(5);
                } else {
                    try{
                        List<PosttieData> posttieDataList = JsonUtils.jsonToConsultList(jsonData);
                        Message msg = Message.obtain();
                        msg.what = 4;
                        msg.obj = posttieDataList;
                        handler.sendMessage(msg);
                    }catch (Exception e){
                        handler.sendEmptyMessage(3);
                    }

                }

            }
        });
    }

    public void getMyPublish() {
        //获取用户手机号码
        String tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
        Log.d("tel", tel);
        new OkHttpUtil().getUserToLawyer("", tel, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0x1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myPublishJson = response.body().string();
                try{
                    List<PosttieData> posttieDataList = JsonUtils.jsonToConsultList(myPublishJson);
                    Message msg = Message.obtain();
                    msg.what = 0x2;
                    msg.obj = posttieDataList;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    handler.sendEmptyMessage(3);
                }


            }
        });

    }

}
