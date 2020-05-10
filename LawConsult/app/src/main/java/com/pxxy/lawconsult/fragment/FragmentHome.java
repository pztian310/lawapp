package com.pxxy.lawconsult.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.activitys.ClassicCaseActivity;
import com.pxxy.lawconsult.activitys.ConsultActivity;
import com.pxxy.lawconsult.activitys.NoticeActivity;
import com.pxxy.lawconsult.activitys.SearchLawyerActivity;
import com.pxxy.lawconsult.activitys.ShowCaseActivity;
import com.pxxy.lawconsult.adapter.HomeAdapter;
import com.pxxy.lawconsult.base.BaseFragment;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.dao.CaseDao;
import com.pxxy.lawconsult.entity.Case;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentHome extends BaseFragment implements View.OnClickListener {
    private Banner banner;
    private CustomTitleView titleView;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private List<Integer> images;
    private HomeAdapter adapter;
    private List<Case> caseList;
    private int caseStart = 0;
    private LinearLayout fastconsultlayout;
    private Button fastconsultButton;
    private OkHttpUtil okHttpUtil;
    private Button bt_notice, bt_classicCase, bt_search;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //刷新成功
                    refreshSuccess(msg);
                    break;
                case 2:
                    //刷新失败
                    refreshLayout.finishRefresh(200, false);
                    Toast.makeText(MyApp.getContext(), AppConstant.FRAGMENT_HOME_REFRESH_FAILURE, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    //无数据可以更新
                    refreshLayout.finishRefresh(200, false);
                    Toast.makeText(MyApp.getContext(), AppConstant.FRAGMENT_HOME_REFRESH_NODATA, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    //加载成功
                    loadingSuccess(msg);
                    break;
                case 5:
                    //加载失败
                    Toast.makeText(MyApp.getContext(), AppConstant.FRAGMENT_HOME_LOAD_FAILURE, Toast.LENGTH_SHORT).show();
                    refreshLayout.finishLoadMore(false);
                    break;
                case 6:
                    //无数据可加载
                    Toast.makeText(MyApp.getContext(), AppConstant.FRAGMENT_HOME_LOAD_NODATA, Toast.LENGTH_SHORT).show();
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    break;
                case 404:
                    //停止刷新
                    refreshLayout.finishRefresh(false);
                    refreshLayout.finishLoadMore(false);
                    Toast.makeText(MyApp.getContext(), AppConstant.FRAGMENT_HOME_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private User user;
    private int userType;

    /**
     * 加载成功
     *
     * @param msg
     */
    private void loadingSuccess(Message msg) {
        //加载成功
        List<Case> cases = (List<Case>) msg.obj;
        //加载数据
        for (Case mCase : cases) {
            caseList.add(mCase);
        }
        //刷新适配器
        adapter.notifyDataSetChanged();
        //结束加载动画
        refreshLayout.finishLoadMore();
        //弹出吐司
        Toast.makeText(MyApp.getContext(),
                AppConstant.FRAGMENT_HOME_LOAD_SUCCESS_START + cases.size()
                        + AppConstant.FRAGMENT_HOME_LOAD_SUCCESS_END, Toast.LENGTH_SHORT).show();
        //改变下次的请求数据下标
        caseStart = caseStart + cases.size();
        //保存数据
        CaseDao.clearDB();
        CaseDao.saveCase(cases);
    }

    /**
     * 刷新成功
     *
     * @param msg
     */
    private void refreshSuccess(Message msg) {
        //刷新成功
        List<Case> cases = (List<Case>) msg.obj;
        //清除caseList数据
        if (caseList != null) {
            caseList.clear();
        }
        //刷新数据
        for (Case mCase : cases) {
            caseList.add(mCase);
        }
        //刷新适配器
        adapter.notifyDataSetChanged();
        //结束刷新动画
        refreshLayout.finishRefresh(200, true);
        //弹出吐司
        Toast.makeText(MyApp.getContext(), AppConstant.FRAGMENT_HOME_REFRESH_SUCCESS, Toast.LENGTH_SHORT).show();
        //改变下次的请求数据下标
        caseStart = caseStart + cases.size();
        //保存数据
        CaseDao.clearDB();
        saveCase();
    }

    @Override
    protected int setLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initEvent() {
        //设置按钮点击事件
        bt_notice.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        bt_classicCase.setOnClickListener(this);
        fastconsultlayout.setOnClickListener(this);
        /**
         * 轮播图
         */
        //设置图片加载器
        banner.setImageLoader(new MyImageLoader());
        //设置图片数据
        banner.setImages(images);
        //开始轮播
        banner.start();

        /**
         * recyclerview
         */
        //给recyclerview设置适配器
        adapter = new HomeAdapter(MyApp.getContext(), caseList);
        //设置item的点击事件
        adapter.setItemOnclickListener(new HomeAdapter.itemOnclickListener() {
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
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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

        //设置加载事件
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
            }
        });

        //自动刷新
        refreshLayout.autoRefresh();

        //立即发布
        fastconsultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyApp.getContext(), ConsultActivity.class));
            }
        });

    }

    /**
     * 上拉加载时调用的逻辑代码
     */
    private void loadMore() {
        okHttpUtil.getCase(caseStart, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //加载失败
                handler.sendEmptyMessage(5);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String caseJson = response.body().string();
                //没有数据可以加载
                if (caseJson.equals(HttpConstant.GETCASE_NODATA)) {
                    handler.sendEmptyMessage(6);
                } else {
                    //json转化为List<Case>对象
                    List<Case> cases = null;
                    try {
                        cases = JsonUtils.jsonToCaseList(caseJson);
                    } catch (Exception e) {
                        handler.sendEmptyMessage(404);
                        e.printStackTrace();
                        return;
                    }
                    Message msg = Message.obtain();
                    msg.what = 4;
                    msg.obj = cases;
                    //发送信息
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 刷新时调用的逻辑代码
     */
    private void refresh() {
        okHttpUtil.getCase(caseStart, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //刷新失败
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String caseJson = response.body().string();
                //如果没有数据
                if (caseJson.equals(HttpConstant.GETCASE_NODATA)) {
                    handler.sendEmptyMessage(3);
                    return;
                }
                //json转化为List<Case>对象
                List<Case> cases = null;
                try {
                    cases = JsonUtils.jsonToCaseList(caseJson);
                } catch (Exception e) {
                    handler.sendEmptyMessage(404);
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
    protected void initData() {
        //存放图片
        images = new ArrayList<>();
        images.add(R.drawable.a);
        images.add(R.drawable.b);
        images.add(R.drawable.c);
        images.add(R.drawable.f);
        images.add(R.drawable.e);


        //读取本地数据库数据
        caseList = CaseDao.getAllCase();

        //获取用户数据
        userType = (int) MyApp.getData().get(AppConstant.LOGIN_USER_TYPE);
        user = (User) MyApp.getData().get(AppConstant.USER);
        //获取收藏的案例
        okHttpUtil.getUserCollect(user.getId(), new Callback() {
            private List<Integer> collect;

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userCollectJson = response.body().string();
                Log.d("demo", userCollectJson);
                List<Integer> collect = null;
                try {
                    collect = JsonUtils.jsonToIntegerList(userCollectJson);
                } catch (Exception e) {
                    handler.sendEmptyMessage(404);
                }
                //把用户收藏数据放进myapp中
                MyApp.putData(AppConstant.USER_COLLECT, collect);
            }
        });

    }


    @Override
    protected void initView(View view) {
        banner = view.findViewById(R.id.fragment_home_banner);
        titleView = view.findViewById(R.id.fragment_home_title);
        recyclerView = view.findViewById(R.id.fragment_home_recycler);
        refreshLayout = view.findViewById(R.id.fragment_home_refreshlayout);
        fastconsultlayout = view.findViewById(R.id.fragment_home_fastconsultlayout);
        bt_notice = view.findViewById(R.id.fragment_home_bt_fastconsult_notice);
        bt_classicCase = view.findViewById(R.id.fragment_home_bt_fastconsult_classiccase);
        bt_search = view.findViewById(R.id.fragment_home_bt_fastconsult_search);
        okHttpUtil = new OkHttpUtil();
        fastconsultButton = view.findViewById(R.id.fastconsult_bt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //经典案例点击事件
            case R.id.fragment_home_bt_fastconsult_classiccase:
                startActivity(new Intent(MyApp.getContext(), ClassicCaseActivity.class));
                break;
            //通知公告点击事件
            case R.id.fragment_home_bt_fastconsult_notice:
                startActivity(new Intent(MyApp.getContext(), NoticeActivity.class));
                break;
            //搜索点击事件
            case R.id.fragment_home_bt_fastconsult_search:
                startActivity(new Intent(MyApp.getContext(), SearchLawyerActivity.class));
                break;
            //快速咨询点击事件
            case R.id.fragment_home_fastconsultlayout:
                startActivity(new Intent(MyApp.getContext(), ConsultActivity.class));
                break;
        }
    }


    /**
     * 保存案例
     */
    private void saveCase() {
        CaseDao.saveCase(caseList);
    }

    /**
     * 轮播图图片加载类
     */
    class MyImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }


}
