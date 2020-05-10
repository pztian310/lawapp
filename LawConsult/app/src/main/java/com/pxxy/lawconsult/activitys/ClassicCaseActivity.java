package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.adapter.ClassicCaseRecyclerAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.Case;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ClassicCaseActivity extends BaseActivity {
    private SmartRefreshLayout refreshLayout;
    private CustomTitleView titleView;
    private RecyclerView recyclerView;
    private List<Case> caseList;
    private LinearLayout notInternet;
    private int startCase = 5;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //关闭刷新动画
                    refreshLayout.finishRefresh();
                    //隐藏recyclerview
                    recyclerView.setVisibility(View.INVISIBLE);
                    //显示无网络
                    notInternet.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    List<Case> cases = (List<Case>) msg.obj;
                    //添加数据
                    for (Case mCase:cases){
                        caseList.add(mCase);
                    }
                    //刷新适配器
                    adapter.notifyDataSetChanged();
                    //关闭刷新动画
                    refreshLayout.finishRefresh();
                    break;
            }
        }
    };
    private OkHttpUtil okHttpUtil;
    private ClassicCaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_classic_case;
    }

    @Override
    public void initEvent() {
        //设置title返回按钮的事件
        titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        //设置recyclerview的适配器
        adapter = new ClassicCaseRecyclerAdapter(this, caseList);
        //设置item的点击事件
        adapter.setItemOnclickListener(new ClassicCaseRecyclerAdapter.itemOnclickListener() {
            @Override
            public void setItemOnclickListener(View view, Case mCase) {
                Intent intent = new Intent(MyApp.getContext(), ShowCaseActivity.class);
                intent.putExtra(AppConstant.FRAGMENT_HOME_INTENT_CASEDATA, mCase);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        //设置点击效果
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(MyApp.getContext(), DividerItemDecoration.VERTICAL));
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApp.getContext(), LinearLayoutManager.VERTICAL, false));

        /**
         * 刷新框架
         */
        //开启自动加载功能
        refreshLayout.setEnableAutoLoadMore(true);
        //设置刷新事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });

        //自动刷新
        refreshLayout.autoRefresh();
    }

    /**
     * 刷新时调用
     */
    private void refresh() {
        okHttpUtil.getCase(startCase, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //刷新失败
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String caseJson = response.body().string();
                //没有数据可以加载
                //json转化为List<Case>对象
                List<Case> cases = null;
                try {
                    cases = JsonUtils.jsonToCaseList(caseJson);
                } catch (Exception e) {
                    handler.sendEmptyMessage(0);
                    e.printStackTrace();
                    return;
                }
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = cases;
                //发送信息
                handler.sendMessage(msg);
            }
    });
}

    @Override
    public void initView() {
        refreshLayout = findViewById(R.id.classic_case_refreshlayout);
        titleView = findViewById(R.id.classic_case_activity_title);
        recyclerView = findViewById(R.id.classic_case_activity_recyclerview);
        okHttpUtil = new OkHttpUtil();
        caseList = new ArrayList<>();
        notInternet = findViewById(R.id.classiccase_activity_not_internet);
    }
}
