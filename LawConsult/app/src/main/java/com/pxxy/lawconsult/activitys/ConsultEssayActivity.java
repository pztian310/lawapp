package com.pxxy.lawconsult.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.adapter.ConsultEssayAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.dao.ConsultDao;
import com.pxxy.lawconsult.entity.PosttieData;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class ConsultEssayActivity extends BaseActivity {

    private ListView listView;
    private Context context = ConsultEssayActivity.this;
    private CustomTitleView consult_essay_title;
    RefreshLayout refreshLayout;
    private ConsultEssayAdapter consultEssayAdapter;
    private List<PosttieData> list = null;
    private OkHttpUtil okHttpUtil;
    private String titleName;
    private int index = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x1:
                    toastShort("请检查网络");
                    //结束刷新动画
                    refreshLayout.finishRefresh(true);
                    break;
                case 0x3:
                    toastShort("暂无发布");
                    refreshLayout.finishRefresh(true);
                    break;
                case 0x4:
                    toastShort("服务器繁忙");
                    refreshLayout.finishRefresh(true);
                    break;
                case 0x5:
                    refreshSuccess(msg);
                    break;
                case 0x6:
                    loadMoreSuccess(msg);
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
        return R.layout.activity_consult_essay;
    }

    @Override
    public void initView() {
        consult_essay_title = findViewById(R.id.activity_consult_essay_title);
        listView = findViewById(R.id.activity_consult_essay_listview);
        refreshLayout = findViewById(R.id.activity_consult_essay_refreshlayout);
        //设置actionbar
        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        consult_essay_title.setCenterTextViewText(titleName);

    }

    @Override
    public void initData() {
        //先从本地获取数据
        list = ConsultDao.getAllConsultContent();
        consultEssayAdapter = new ConsultEssayAdapter(context, list);
        listView.setAdapter(consultEssayAdapter);
    }

    @Override
    public void initEvent() {
        //设置listview点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取被点击对象  传入到reply页面
                PosttieData posttie = (PosttieData) parent.getItemAtPosition(position);
                Intent intent = new Intent(ConsultEssayActivity.this, ReplyActivity.class);
                intent.putExtra("Posttie", posttie);
                startActivity(intent);
            }
        });


        refreshLayout.autoRefresh();
        //开启自动加载功能
        //   refreshLayout.setEnableAutoLoadMore(true);
        //back键,返回FragmentConsult页面
        consult_essay_title.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 刷新框架
         */
        //自动加载数据
        //  refreshLayout.setEnableLoadMore(true);

        //设置刷新事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        //设置加载事件
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (ConsultEssayActivity.this.consultEssayAdapter.getCount() > 30) {
                    //完成加载并标记没有更多数据
                    handler.sendEmptyMessage(0x3);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    //向数据库请求数据,加载更多
                    loadMore();
                    //完成加载
                    refreshLayout.finishLoadMore();
                }

            }
        });
    }

    //加载数据
    private void loadMore() {
        okHttpUtil = new OkHttpUtil();
        okHttpUtil.getConsultContent(titleName, index + "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0x1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String consultJson = response.body().string();
                if (consultJson.equals(HttpConstant.GETCONSULT_FAIL)) {
                    handler.sendEmptyMessage(0x4);
                    return;
                } if (consultJson.equals(HttpConstant.GETCONSULT_SERVERERROR)) {
                    handler.sendEmptyMessage(0x4);
                    return;
                }
                    List<PosttieData> consultList = JsonUtils.jsonToConsultList(consultJson);
                if (consultList.size()==0){
                    handler.sendEmptyMessage(0x2);
                }
                    Message msg = Message.obtain();
                    msg.what = 0x6;
                    msg.obj = consultList;
                    handler.sendMessage(msg);

            }
        });
    }

    //刷新数据
    private void refresh() {
        okHttpUtil = new OkHttpUtil();
        okHttpUtil.getConsultContent(titleName, index + "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0x1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String consultJson = response.body().string();
                if (consultJson.equals(HttpConstant.GETCONSULT_FAIL)) {
                    handler.sendEmptyMessage(0x4);
                    return;
                }
                List<PosttieData> consultList = JsonUtils.jsonToConsultList(consultJson);
                if (consultList.size() == 0) {
                    handler.sendEmptyMessage(0x3);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 0x5;
                    msg.obj = consultList;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    //刷新成功
    private void refreshSuccess(Message msg) {
        List<PosttieData> posttieList = (List<PosttieData>) msg.obj;
        //清空ListView
        if (list != null) {
            list.clear();
        }
        //将数据保存到list集合中
        for (PosttieData posttieData : posttieList) {
            list.add(posttieData);
        }
        //改变请求的位置
        index = index + posttieList.size();
        //刷新适配器
        consultEssayAdapter.notifyDataSetChanged();
        //结束刷新动画
        refreshLayout.finishRefresh(true);
        //将数据保存到本地

    }

    //加载成功
    private void loadMoreSuccess(Message msg) {
        List<PosttieData> posttieList = (List<PosttieData>) msg.obj;
        for (PosttieData posttieData : posttieList) {
            list.add(posttieData);
        }
        //改变请求的位置
        index = index + posttieList.size();
        Log.d("index", index + "");
        //刷新适配器
        consultEssayAdapter.notifyDataSetChanged();
        //结束刷新动画
        refreshLayout.finishLoadMore(true);
        //清楚本地缓存
        ConsultDao.deleteData();
        //将数据保存到本地
        ConsultDao.saveConsultContent(posttieList);
    }
}
