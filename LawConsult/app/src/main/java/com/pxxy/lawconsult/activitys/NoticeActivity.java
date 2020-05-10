package com.pxxy.lawconsult.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.adapter.AdminCaseRecyclerAdapter;
import com.pxxy.lawconsult.adapter.NoticeActivityRecyclerViewAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.Notice;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NoticeActivity extends BaseActivity {
    private CustomTitleView titleView;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private List<Notice> noticeList;
    private OkHttpUtil okHttpUtil;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //网络未连接
                    toastShort(AppConstant.ADMIN_ADD_CASE_ACTIVITY_NOT_INTERNET);
                    refreshLayout.finishRefresh();
                    break;
                case 1:
                    //创建recyclerview的适配器
                    adapter = new NoticeActivityRecyclerViewAdapter(NoticeActivity.this,noticeList);
                    //设置适配器
                    recyclerView.setAdapter(adapter);
                    //设置recyclerView的item增加和删除动画
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    //设置布局管理器
                    recyclerView.setLayoutManager(new LinearLayoutManager(MyApp.getContext(), LinearLayoutManager.VERTICAL, false));
                    //关闭刷新动画
                    refreshLayout.finishRefresh();
                    //请求成功
                    break;
                case 2:
                    //请求失败
                    toastShort(AppConstant.ADMIN_CASE_ACTIVITY_SERVER_ERROR);
                    refreshLayout.finishRefresh();
                    break;
            }
        }
    };
    private NoticeActivityRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_notice;
    }

    @Override
    public void initEvent() {
        //设置titleview左边组合的返回事件
        titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        //设置refresh刷新事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        //设置自动刷新
        refreshLayout.autoRefresh();
    }

    /**
     * 刷新时调用
     */
    public void refresh(){
        okHttpUtil.getAllNotice(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String noticeJson = response.body().string();
                //转化成List<Notice>
                try {
                    noticeList = JsonUtils.jsonToLawerNoticeList(noticeJson);
                    handler.sendEmptyMessage(1);
                }catch (Exception e){
                    handler.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void initView() {
        titleView = findViewById(R.id.notice_activity_title);
        recyclerView = findViewById(R.id.notice_activity_recyclerview);
        refreshLayout = findViewById(R.id.notice_activity_refreshlayout);
        okHttpUtil = new OkHttpUtil();
    }
}
