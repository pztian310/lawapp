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
import com.pxxy.lawconsult.adapter.AdminUserRecyclerAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.ClientUser;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.User;
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

public class AdminUserActivity extends BaseActivity {
    private CustomTitleView titleView;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout not_internet;
    private Intent intent;
    private int userType;
    private OkHttpUtil okHttpUtil;
    private AdminUserRecyclerAdapter adapter;
    private List<ClientUser> clientUsers;
    private List<LawerUser> lawerUsers;
    private HintDialogUtils deleteDialog;
    private int startIndex;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //加载或刷新失败
                    loadAndRefreshError();
                    break;
                case 1:
                    //刷新成功
                    refreshSuccess(msg);
                    break;
                case 2:
                    //加载成功
                    loadMoreSuccess(msg);
                    break;
                case 3:
                    //删除失败
                    deleteUserFailure();
                    break;
                case 4:
                    //删除用户成功
                    deleteUserSuccess(msg);
                    break;
            }
        }
    };

    /**
     * 删除用户失败
     */
    private void deleteUserFailure() {
        //删除用户失败
        if (userType == AppConstant.TYPE_0){
            toastShort(AppConstant.ADMIN_USER_CLIENTUSER_DELETE_FAILURE);
        }else if (userType == AppConstant.TYPE_1){
            toastShort(AppConstant.ADMIN_USER_LAWYERUSER_DELETE_FAILURE);
        }
        return;
    }

    /**
     * 删除用户成功
     * @param msg
     */
    private void deleteUserSuccess(Message msg) {
        int position = (int) msg.obj;
        if (userType == AppConstant.TYPE_0){
            //刷新数据
            clientUsers.remove(position);
            //刷新适配器
            adapter.notifyItemRemoved(position);
            //弹出吐司
            toastShort(AppConstant.ADMIN_USER_CLIENTUSER_DELETE_SUCCESS);
        }else if (userType == AppConstant.TYPE_1){
            //刷新数据
            lawerUsers.remove(position);
            //刷新适配器
            adapter.notifyItemRemoved(position);
            //弹出吐司
            toastShort(AppConstant.ADMIN_USER_LAWYERUSER_DELETE_SUCCESS);
        }
    }



    /**
     * 刷新失败或加载失败调用的方法
     */
    private void loadAndRefreshError() {
        //隐藏recyclerview
        recyclerView.setVisibility(View.INVISIBLE);
        //显示网络错误
        not_internet.setVisibility(View.VISIBLE);
        //停止刷新
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    /**
     * 刷新成功调用的方法
     *
     * @param msg
     */
    private void refreshSuccess(Message msg) {
        //加载recyclerview
        if (userType == AppConstant.TYPE_0) {
            clientUsers = (List<ClientUser>) msg.obj;
            adapter = new AdminUserRecyclerAdapter(MyApp.getContext(), clientUsers, userType);
        } else if (userType == AppConstant.TYPE_1) {
            lawerUsers = (List<LawerUser>) msg.obj;
            adapter = new AdminUserRecyclerAdapter(MyApp.getContext(), lawerUsers, userType);
        }
        //设置recycler的点击事件(设置adapter)
        initAdapterClickListener();
        //设置适配器
        recyclerView.setAdapter(adapter);
        //设置recyclerView的item增加和删除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApp.getContext(), LinearLayoutManager.VERTICAL, false));
        //关闭刷新
        refreshLayout.finishRefresh();
    }

    /**
     * 设置recycler的点击事件(设置adapter)
     */
    private void initAdapterClickListener() {
        //设置item和用户详情点击事件
        adapter.setItemOnClickListener(new AdminUserRecyclerAdapter.itemOnClickListener() {
            @Override
            public void setItemOnClickListener(View view, User user, int type) {
                Intent itemMsg = new Intent(AdminUserActivity.this, AdminShowUserMessageActivity.class);
                itemMsg.putExtra(AppConstant.ADMIN_USER_SHOW_USER_MESSAGE_USEROBJECT, user);
                itemMsg.putExtra(AppConstant.ADMIN_USER_TYPE, type);
                startActivity(itemMsg);
            }
        });

        //设置删除用户的点击事件
        adapter.setDeleteButtonOnClickListener(new AdminUserRecyclerAdapter.deleteButtonOnClickListener() {
            @Override
            public void setDeleteButtonOnClickListener(View view, User user, int type,int position) {
                //设置删除时弹出的dialog
                initDeleteDialog(user,position);
                //显示dialog
                deleteDialog.showDialog();
            }
        });
    }

    /**
     * 设置删除时弹出的对话框
     */
    private void initDeleteDialog(final User user, final int position) {
        //初始化dialog
        if (userType == 0) {
            deleteDialog.initDialog(AdminUserActivity.this,
                    AppConstant.ADMIN_USER_CLIENTUSER_DELETE_BUTTON_HINT_TITLE,
                    AppConstant.ADMIN_USER_CLIENTUSER_DELETE_BUTTON_HINT_MESSAGE,
                    true);
        } else if (userType == 1) {
            deleteDialog.initDialog(AdminUserActivity.this,
                    AppConstant.ADMIN_USER_LAWYERUSER_DELETE_BUTTON_HINT_TITLE,
                    AppConstant.ADMIN_USER_LAWYERUSER_DELETE_BUTTON_HINT_MESSAGE,
                    true);
        }
        //设置取消按钮及点击事件
        deleteDialog.setLeftButton(AppConstant.ADMIN_USER_DELETE_CANCLE_BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭dialog
                deleteDialog.closeDialog();
            }
        });
        //设置确定按钮及点击事件
        deleteDialog.setRightButton(AppConstant.ADMIN_USER_DELETE_CONFIRM_BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭dialog
                okHttpUtil.adminDeleteUser(user.getId(), user.getType(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(3);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resultJson = response.body().string();
                        //转换成map对象
                        Map<String,String> map = JsonUtils.jsonToMap(resultJson);
                        String result = map.get(HttpConstant.ADMIN_DELETEUSER_RESULT);
                        if (result.equals(HttpConstant.ADMIN_DELETEUSER_RESULT_SUCCESS)){
                            //删除用户成功
                            Message msg = Message.obtain();
                            msg.what = 4;
                            msg.obj = position;
                            handler.sendMessage(msg);
                        }else{
                            //删除用户失败
                            handler.sendEmptyMessage(3);
                        }
                    }
                });
                deleteDialog.closeDialog();
            }
        });
    }

    /**
     * 加载成功调用的方法
     *
     * @param msg
     */
    private void loadMoreSuccess(Message msg) {
        if (userType == AppConstant.TYPE_0) {
            List<ClientUser> users = (List<ClientUser>) msg.obj;
            if (users.size() == 0) {
                toastShort(AppConstant.ADMIN_USER_LOAD_CLIENTUSER_END);
                //设置没有数据可刷新
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                //添加数据
                for (ClientUser user : users) {
                    clientUsers.add(user);
                }
                //停止加载动画
                refreshLayout.finishLoadMore();
                //刷新适配器
                adapter.notifyDataSetChanged();
            }
        } else if (userType == AppConstant.TYPE_1) {
            List<LawerUser> users = (List<LawerUser>) msg.obj;
            if (users.size() == 0) {
                toastShort(AppConstant.ADMIN_USER_LOAD_LAWYERUSER_END);
                //设置没有数据可刷新
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                //添加数据
                for (LawerUser user : users) {
                    lawerUsers.add(user);
                }
                //停止加载动画
                refreshLayout.finishLoadMore();
                //刷新适配器
                adapter.notifyDataSetChanged();
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_admin_user;
    }

    @Override
    public void initView() {
        titleView = findViewById(R.id.admin_activity_user_title);
        refreshLayout = findViewById(R.id.admin_activity_user_refreshlayout);
        recyclerView = findViewById(R.id.admin_activity_user_recyclerview);
        not_internet = findViewById(R.id.admin_activity_user_not_internet);
        okHttpUtil = new OkHttpUtil();
        intent = getIntent();
        deleteDialog = new HintDialogUtils();
        //获取用户类型
        String type = intent.getStringExtra(AppConstant.ADMIN_USER_TYPE);
        //判断用户
        if (type.equals(AppConstant.ADMIN_USER_CLIENTUSER)) {
            //设置类型
            userType = 0;
            //设置标题
            titleView.setCenterTextViewText(AppConstant.ADMIN_USER_CLIENTUSER);
        } else if (type.equals(AppConstant.ADMIN_USER_LAWYERUSER)) {
            //设置类型
            userType = 1;
            //设置标题
            titleView.setCenterTextViewText(AppConstant.ADMIN_USER_LAWYERUSER);
        }
    }

    @Override
    public void initData() {


    }

    /**
     * 上拉加载事件
     */
    private void loadMore() {
        //加载下一页数据
        startIndex += 10;
        //网络获取用户数据
        okHttpUtil.adminGetUser(startIndex, userType, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取json对象
                String userJson = response.body().string();
                try {
                    //创建消息对象
                    Message msg = Message.obtain();
                    if (userType == AppConstant.TYPE_0) {
                        List<ClientUser> clientUsers = JsonUtils.jsonToClientUserList(userJson);
                        msg.what = 2;
                        msg.obj = clientUsers;
                    } else if (userType == AppConstant.TYPE_1) {
                        List<LawerUser> lawerUsers = JsonUtils.jsonToLawerUserList(userJson);
                        msg.what = 2;
                        msg.obj = lawerUsers;
                    }

                    //发送信息
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(0);
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 刷新时调用的方法
     */
    private void refresh() {
        //初始化
        startIndex = 0;
        //网络获取用户数据
        okHttpUtil.adminGetUser(startIndex, userType, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取json对象
                String userJson = response.body().string();
                try {
                    //创建消息对象
                    Message msg = Message.obtain();

                    if (userType == AppConstant.TYPE_0) {
                        List<ClientUser> clientUsers = JsonUtils.jsonToClientUserList(userJson);
                        msg.what = 1;
                        msg.obj = clientUsers;
                    } else if (userType == AppConstant.TYPE_1) {
                        List<LawerUser> lawerUsers = JsonUtils.jsonToLawerUserList(userJson);
                        msg.what = 1;
                        msg.obj = lawerUsers;
                    }

                    //发送信息
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(0);
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void initEvent() {
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
        //自动刷新
        refreshLayout.autoRefresh();
        //设置标题的返回按钮事件
        titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });


    }
}
