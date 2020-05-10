package com.pxxy.lawconsult;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.pxxy.lawconsult.activitys.AdminShowStatuteActivity;
import com.pxxy.lawconsult.adapter.AdminStatuteRecyclerAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.Statute;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.HintDialogUtils;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminChildStatureActivity extends BaseActivity {
    private CustomTitleView titleView;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout notIntentLayout;
    private RecyclerView recyclerView;
    private static AdminStatuteRecyclerAdapter adapter;
    private int startIndex = 0;
    private OkHttpUtil okHttpUtil;
    private static List<Statute> statuteList;
    public static Handler refresh = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //远程刷新数据
                    //删除原有的case数据
                    statuteList.remove(msg.arg1);
                    //加载新的case数据
                    statuteList.add(msg.arg1, (Statute) msg.obj);
                    //刷新recyclerView
                    adapter.notifyItemChanged(msg.arg1);
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //网络未连接
                    notIntentLayout.setVisibility(View.VISIBLE);
                    //隐藏recyclerview
                    recyclerView.setVisibility(View.INVISIBLE);
                    //结束刷新动画
                    refreshLayout.finishRefresh();
                    break;
                case 1:
                    //服务器错误
                    toastShort(AppConstant.ADMIN_STATUTE_ACTIVITY_SERVER_ERROR);
                    break;
                case 2:
                    //刷新成功
                    refreshSuccess();
                    break;
                case 3:
                    //加载成功
                    loadMoreSuccess(msg);
                    break;
                case 4:
                    //法条删除成功
                    //移除下标为arg1的数据
                    statuteList.remove(msg.arg1);
                    //刷新recyclerView
                    adapter.notifyItemRemoved(msg.arg1);
                    //删除案例成功
                    toastShort(AppConstant.ADMIN_CASE_ACTIVITY_DELETE_STATUTE_SUCCESS);
                    break;
                case 5:
                    //法条删除失败
                    toastShort(AppConstant.ADMIN_CASE_ACTIVITY_DELETE_STATUTE_FAILURE);
                    break;
                case 6:
                    //删除失败，无网络
                    toastShort(AppConstant.ADMIN_CASE_ACTIVITY_DELETE_STATUTE_NOT_INTERNET);
                    break;
            }
        }
    };
    private HintDialogUtils dialog;

    /**
     * 加载成功时调用的方法
     *
     * @param msg
     */
    private void loadMoreSuccess(Message msg) {
        //获取加载的case
        List<Statute> statutes = (List<Statute>) msg.obj;
        //判断cases中是否还有数据
        if (statutes.size() == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            toastShort(AppConstant.ADMIN_STATUTE_ACTIVITY_NO_STATUTE_DATA);
            return;
        }
        //往caseList中添加加载的case
        for (Statute statute : statutes) {
            statuteList.add(statute);
        }
        //刷新recyclerview
        adapter.notifyDataSetChanged();
        //关闭加载动画
        refreshLayout.finishLoadMore();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_admin_child_statute;
    }

    @Override
    public void initView() {
        titleView = findViewById(R.id.admin_child_statute_activity_title);
        refreshLayout = findViewById(R.id.admin_child_statute_refreshlayout);
        notIntentLayout = findViewById(R.id.admin_child_statute_not_internet);
        recyclerView = findViewById(R.id.admin_child_statute_recyclerview);
        okHttpUtil = new OkHttpUtil();
    }

    @Override
    public void initEvent() {
        //设置标题栏返回按钮的点击事件
        titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
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
        //设置自动刷新
        refreshLayout.autoRefresh();

    }

    /**
     * 上拉加载调用的方法
     */
    private void loadMore() {
        //加载下一页数据
        startIndex += 10;
        //网络获取下一页数据
        okHttpUtil.adminGetStatute(startIndex, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络连接失败
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String statuteListJson = response.body().string();
                try {
                    List<Statute> statutes = JsonUtils.jsonToStatuteList(statuteListJson);
                    //获取消息对象
                    Message msg = Message.obtain();
                    msg.what = 3;
                    msg.obj = statutes;
                    //发送消息
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 下拉刷新时调用的方法
     */
    private void refresh() {
        //初始化请求的案例下标
        startIndex = 0;
        //请求网络
        okHttpUtil.adminGetStatute(startIndex, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络连接失败
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String statuteJson = response.body().string();
                try {
                    statuteList = JsonUtils.jsonToStatuteList(statuteJson);
                    //创建消息对象
                    handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 刷新成功时调用的方法
     */
    private void refreshSuccess() {
        //创建recyclerview的适配器
        adapter = new AdminStatuteRecyclerAdapter(this, statuteList);
        //设置recycler的点击事件(设置adapter)
        initAdapterClickListener();
        //设置适配器
        recyclerView.setAdapter(adapter);
        //设置recyclerView的item增加和删除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApp.getContext(), LinearLayoutManager.VERTICAL, false));
        //关闭刷新动画
        refreshLayout.finishRefresh();
    }

    /**
     * 设置recycler的点击事件
     */
    private void initAdapterClickListener() {
        //设置item的点击事件
        adapter.setItemOnClickListener(new AdminStatuteRecyclerAdapter.itemOnClickListener() {
            @Override
            public void setItemOnClickListener(View view, Statute statute, int position) {
                Intent intent = new Intent(AdminChildStatureActivity.this, AdminShowStatuteActivity.class);
                intent.putExtra(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_TYPE, AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_ITEM);
                intent.putExtra(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_STATUTE, statute);
                intent.putExtra(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_POSITION, position);
                startActivity(intent);
            }
        });
        //设置更新按钮的点击事件
        adapter.setUpdateButtonOnClickListener(new AdminStatuteRecyclerAdapter.updateButtonOnClickListener() {
            @Override
            public void setUpdateButtonOnClickListener(View view, Statute statute, int position) {
                Intent intent = new Intent(AdminChildStatureActivity.this, AdminShowStatuteActivity.class);
                intent.putExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_TYPE, AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_UPDATE);
                intent.putExtra(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_STATUTE, statute);
                intent.putExtra(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_POSITION, position);
                startActivity(intent);
            }
        });
        //设置删除按钮的点击事件
        adapter.setDeleteButtonOnClickListener(new AdminStatuteRecyclerAdapter.deleteButtonOnClickListener() {
            @Override
            public void setDeleteButtonOnClickListener(View view, final Statute statute, final int position) {
                dialog = new HintDialogUtils();
                //初始化dialog
                dialog.initDialog(AdminChildStatureActivity.this,
                        AppConstant.ADMIN_STATUTE_ACTIVITY_DELETE_DIALOG_TITLE,
                        AppConstant.ADMIN_STATUTE_ACTIVITY_DELETE_DIALOG_MESSAGE,
                        true);
                //设置dialog的按钮的点击事件
                dialog.setLeftButton(AppConstant.ADMIN_STATUTE_ACTIVITY_DELETE_DIALOG_CANCLEBUTTON, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.closeDialog();
                    }
                });
                //设置确定按钮的点击事件
                dialog.setRightButton(AppConstant.ADMIN_STATUTE_ACTIVITY_DELETE_DIALOG_CONFIRMBUTTON, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确定删除案例
                        try {
                            //网络请求删除案例
                            deleteStatuteOnInternet(statute, position);
                        } catch (Exception e) {
                            handler.sendEmptyMessage(5);
                            e.printStackTrace();
                        }
                        //关闭dialog
                        dialog.closeDialog();
                    }
                });
                //显示对话框
                dialog.showDialog();
            }
        });
    }

    /**
     * 删除法条
     *
     * @param statute
     * @param position
     */
    private void deleteStatuteOnInternet(final Statute statute, final int position) {
        //网络请求删除案例
        okHttpUtil.adminDeleteStatute(statute.getLaw_id(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(6);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取json数据
                String resultJson = response.body().string();
                try {
                    //转化成map对象
                    Map<String,String> map  = JsonUtils.jsonToMap(resultJson);
                    //判断是否删除成功
                    String reslut = map.get(HttpConstant.ADMIN_DELETESTATUTE_RESULT);
                    if (reslut.equals(HttpConstant.ADMIN_DELETESTATUTE_RESULT_SUCCESS)){
                        //删除案例成功
                        Message msg = Message.obtain();
                        msg.what = 4;
                        msg.obj = statute;
                        msg.arg1 = position;
                        //发送消息
                        handler.sendMessage(msg);
                    }else if (reslut.equals(HttpConstant.ADMIN_DELETESTATUTE_RESULT_FAILURE)){
                        //删除案例失败
                        handler.sendEmptyMessage(5);
                    }
                }catch (Exception e){
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        });
    }
}
