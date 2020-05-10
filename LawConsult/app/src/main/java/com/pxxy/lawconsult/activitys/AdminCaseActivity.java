package com.pxxy.lawconsult.activitys;

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

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.adapter.AdminCaseRecyclerAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.Case;
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

public class AdminCaseActivity extends BaseActivity {
    private CustomTitleView titleView;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout notInternet;
    private RecyclerView recyclerView;
    private OkHttpUtil okHttpUtil;
    private static List<Case> caseList;
    private int startIndex;
    static Handler refresh = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //远程刷新数据
                    //删除原有的case数据
                    caseList.remove(msg.arg1);
                    //加载新的case数据
                    caseList.add(msg.arg1, (Case) msg.obj);
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
                    //网络错误
                    notInternet.setVisibility(View.VISIBLE);
                    //隐藏recyclerview
                    recyclerView.setVisibility(View.INVISIBLE);
                    //结束刷新动画
                    refreshLayout.finishRefresh();
                    break;
                case 1:
                    //空数据
                    toastShort(AppConstant.ADMIN_CASE_ACTIVITY_SERVER_ERROR);
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
                    //移除下标为arg1的数据
                    caseList.remove(msg.arg1);
                    //刷新recyclerView
                    adapter.notifyItemRemoved(msg.arg1);
                    //删除案例成功
                    toastShort(AppConstant.ADMIN_CASE_ACTIVITY_DELETE_CASE_SUCCESS);
                    break;
                case 5:
                    //删除案例失败
                    toastShort(AppConstant.ADMIN_CASE_ACTIVITY_DELETE_CASE_FAILURE);
                    break;
                case 6:
                    //删除案例失败，无网络连接
                    toastShort(AppConstant.ADMIN_CASE_ACTIVITY_DELETE_CASE_NOT_INTERNET);
                    break;
            }
        }
    };
    private static AdminCaseRecyclerAdapter adapter;
    private HintDialogUtils dialog;

    /**
     * 加载成功调用的方法
     *
     * @param msg
     */
    private void loadMoreSuccess(Message msg) {
        //获取加载的case
        List<Case> cases = (List<Case>) msg.obj;
        //判断cases中是否还有数据
        if (cases.size() == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            toastShort(AppConstant.ADMIN_CASE_ACTIVITY_NO_CASE_DATA);
            return;
        }
        //往caseList中添加加载的case
        for (Case mCase : cases) {
            caseList.add(mCase);
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
        return R.layout.activity_admin_case;
    }

    /**
     * 刷新成功
     */
    private void refreshSuccess() {
        //创建recyclerview的适配器
        adapter = new AdminCaseRecyclerAdapter(this, caseList);
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
     * 设置recyclerview的点击事件
     */
    private void initAdapterClickListener() {
        //设置item的点击事件
        adapter.setItemOnClickListener(new AdminCaseRecyclerAdapter.itemOnClickListener() {
            @Override
            public void setItemOnClickListener(View view, Case mCase, int position) {
                Intent intent = new Intent(AdminCaseActivity.this, AdminShowCaseActivity.class);
                intent.putExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_TYPE, AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_ITEM);
                intent.putExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_CASE, mCase);
                intent.putExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_POSITION, position);
                startActivity(intent);
            }
        });
        //设置修改案例按钮的点击事件
        adapter.setUpdateButtonOnClickListener(new AdminCaseRecyclerAdapter.updateButtonOnClickListener() {
            @Override
            public void setUpdateButtonOnClickListener(View view, Case mCase, int position) {
                Intent intent = new Intent(AdminCaseActivity.this, AdminShowCaseActivity.class);
                intent.putExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_TYPE, AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_UPDATE);
                intent.putExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_CASE, mCase);
                intent.putExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_POSITION, position);
                startActivity(intent);
            }
        });
        //设置删除案例按钮的点击事件
        adapter.setDeleteButtonOnClickListener(new AdminCaseRecyclerAdapter.deleteButtonOnClickListener() {
            @Override
            public void setDeleteButtonOnClickListener(View view, final Case mCase, final int position) {
                dialog = new HintDialogUtils();
                //初始化dialog
                dialog.initDialog(AdminCaseActivity.this,
                        AppConstant.ADMIN_CASE_ACTIVITY_DELETE_DIALOG_TITLE,
                        AppConstant.ADMIN_CASE_ACTIVITY_DELETE_DIALOG_MESSAGE,
                        true);
                //设置dialog的按钮的点击事件
                dialog.setLeftButton(AppConstant.ADMIN_CASE_ACTIVITY_DELETE_DIALOG_CANCLEBUTTON, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.closeDialog();
                    }
                });
                //设置确定按钮的点击事件
                dialog.setRightButton(AppConstant.ADMIN_CASE_ACTIVITY_DELETE_DIALOG_CONFIRMBUTTON, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确定删除案例
                        try {
                            //网络请求删除案例
                            deleteCaseOnInternet(mCase, position);
                        }catch (Exception e){
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
     * 网络请求删除案例
     * @param mCase
     * @param position
     * @throws IOException
     */
    private void deleteCaseOnInternet(final Case mCase, final int position) throws IOException {
        //网络请求删除案例
        okHttpUtil.adminDeleteCase(mCase.getId(), new Callback() {
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
                    String reslut = map.get(HttpConstant.ADMIN_DELETECASE_RESULT);
                    if (reslut.equals(HttpConstant.ADMIN_DELETECASE_RESULT_SUCCESS)){
                        //删除案例成功
                        Message msg = Message.obtain();
                        msg.what = 4;
                        msg.obj = mCase;
                        msg.arg1 = position;
                        //发送消息
                        handler.sendMessage(msg);
                    }else if (reslut.equals(HttpConstant.ADMIN_DELETECASE_RESULT_FAILURE)){
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

    @Override
    public void initView() {
        titleView = findViewById(R.id.admin_case_activity_title);
        refreshLayout = findViewById(R.id.admin_case_activity_refreshlayout);
        notInternet = findViewById(R.id.admin_case_activity_not_internet);
        recyclerView = findViewById(R.id.admin_case_activity_recyclerview);
        okHttpUtil = new OkHttpUtil();
    }

    @Override
    public void initEvent() {
        //设置title的返回事件
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
        //设置上拉加载事件
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
     * 下拉刷新调用的方法
     */
    private void refresh() {
        //初始化请求的案例下标
        startIndex = 0;
        //请求网络
        okHttpUtil.adminGetCase(startIndex, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络连接失败
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String caseJson = response.body().string();
                try {
                    caseList = JsonUtils.jsonToCaseList(caseJson);
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
     * 上拉加载调用的方法
     */
    private void loadMore() {
        //加载下一页数据
        startIndex += 10;
        //网络获取下一页数据
        okHttpUtil.adminGetCase(startIndex, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络连接失败
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String caseListJson = response.body().string();
                try {
                    List<Case> cases = JsonUtils.jsonToCaseList(caseListJson);
                    //获取消息对象
                    Message msg = Message.obtain();
                    msg.what = 3;
                    msg.obj = cases;
                    //发送消息
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        });
    }
}
