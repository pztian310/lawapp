package com.pxxy.lawconsult.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.activitys.LawyerMainActivity;
import com.pxxy.lawconsult.adapter.LawyerAdapter;
import com.pxxy.lawconsult.base.BaseFragment;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.dao.LawerUserDao;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentLawyer extends BaseFragment {
    private CustomTitleView customTitleView;
    private ListView listView;
    private TextView tv_name,tv_age,tv_sex,tv_address,tv_introduction;
    private ImageView image_photo;
    private List<LawerUser> lawerUsersList;
    private RefreshLayout refreshLayout;
    private LawyerAdapter lawyerAdapter;
    private OkHttpUtil okHttpUtil;
    private int LawerUserStart=0;
    private String name,address,introduction,tel,photo;
    private int age,sex;
    private User user;
    private int userType;
    private LawerUser lawerUser;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //刷新成功
                    refreshSuccess(msg);
                    break;
                case 2:
                    //刷新失败
                    refreshLayout.finishRefresh(200,false);
                    //吐司失败结果
                    Toast.makeText(MyApp.getContext(),AppConstant.FRAGMENT_LAWYER_REFRESH_FAILURE,Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    //无数据可以更新
                    refreshLayout.finishRefresh(200,false);
                    //吐司无数据可更新
                    Toast.makeText(MyApp.getContext(),AppConstant.FRAGMENT_LAWYER_REFRESH_NODATA,Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    //加载成功
                    loadingSuccess(msg);
                    break;
                case 5:
                    //加载失败
                    //弹出吐司
                    Toast.makeText(MyApp.getContext(),AppConstant.FRAGMENT_LAWYER_LOAD_FAILURE,Toast.LENGTH_SHORT).show();
                    refreshLayout.finishLoadMore(false);
                    break;
                case 6:
                    //无数据可以加载
                    Toast.makeText(MyApp.getContext(),AppConstant.FRAGMENT_LAWYER_LOAD_NODATA,Toast.LENGTH_SHORT).show();
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    break;
                case 404:
                    refreshLayout.finishRefresh(false);
                    refreshLayout.finishLoadMore(false);
                    Toast.makeText(MyApp.getContext(), AppConstant.FRAGMENT_LAWYER_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 加载成功
     * @param msg
     */
    private void loadingSuccess(Message msg) {
        //加载成功
        List<LawerUser> lawerUsers= (List<LawerUser>) msg.obj;
        //加载数据
        for(LawerUser mlawer: lawerUsers){
            lawerUsersList.add(mlawer);
        }
        //加载适配器
        lawyerAdapter.notifyDataSetChanged();
        //结束加载动画
        refreshLayout.finishLoadMore();
        //弹出吐司
        Toast.makeText(MyApp.getContext(),
                AppConstant.FRAGMENT_LAWYER_LOAD_SUCCESS_START + lawerUsers.size()
                        + AppConstant.FRAGMENT_LAWYERR_LOAD_SUCCESS_END, Toast.LENGTH_SHORT).show();
        //改变下次的请求数据下标
        LawerUserStart = LawerUserStart + lawerUsers.size();
        //保存数据
        LawerUserDao.clearDB();
        LawerUserDao.SaveLawerUser(lawerUsers);
    }

    /**
     *刷新成功
     * @param msg
     */
    private void refreshSuccess(Message msg) {
        //刷新成功
        List<LawerUser> lawerUsers= (List<LawerUser>) msg.obj;
        //清除lawerUsersList数据
        if (lawerUsersList != null) {
            lawerUsersList.clear();
        }
        //刷新数据
        for(LawerUser mlawer: lawerUsers){
            lawerUsersList.add(mlawer);
        }
        //刷新适配器
        lawyerAdapter.notifyDataSetChanged();
        //结束刷新动画
        refreshLayout.finishRefresh(200,true);
        //弹出吐司
        Toast.makeText(MyApp.getContext(), AppConstant.FRAGMENT_LAWYER_REFRESH_SUCCESS,Toast.LENGTH_SHORT).show();
        //改变下次请求数据的下标
        LawerUserStart=LawerUserStart+lawerUsers.size();
        //保存数据
        LawerUserDao.clearDB();
        Save();
    }

    @Override
    protected int setLayoutID() {
        return R.layout.fragment_lawyer;
    }

    @Override
    protected void initEvent() {
        //设置适配器
        lawyerAdapter = new LawyerAdapter(MyApp.getContext(), R.id.adapter_lawyer,lawerUsersList);
        listView.setAdapter(lawyerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lawerUser= (LawerUser) parent.getItemAtPosition(position);
                Intent intent = new Intent(MyApp.getContext(), LawyerMainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("lawUser",lawerUser);
                intent.putExtras(bundle);
                startActivity(intent);
                }

        });

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
    }

    /**
         * 上拉加载时调用的逻辑代码
         */
        private void loadMore(){

            okHttpUtil.getLawerUserList(LawerUserStart,new Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    //加载失败
                    handler.sendEmptyMessage(5);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String LawyerUserJson=response.body().string();
                    Log.d("LawyerUser", LawyerUserJson);
                    //没有数据可以加载
                    if(LawyerUserJson.equals(HttpConstant.GETLAWERNODATA)){
                        handler.sendEmptyMessage(6);
                    }else{
                        //Json转化lawerUsers对象
                        List<LawerUser> lawerUsers=null;
                        try{
                            lawerUsers=JsonUtils.jsonToLawerUserList(LawyerUserJson);
                        }catch (Exception e){
                            handler.sendEmptyMessage(404);
                            e.printStackTrace();
                            return;
                        }
                        Message msg = Message.obtain();
                        msg.what = 4;
                        msg.obj = lawerUsers;
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
            LawerUserStart = 0;
            okHttpUtil.getLawerUserList(LawerUserStart, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //刷新失败
                    handler.sendEmptyMessage(2);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String LawyerUserJson=response.body().string();
                    Log.d("abc",LawyerUserJson);
                    //没有数据可以加载
                    if(LawyerUserJson.equals(HttpConstant.GETLAWERNODATA)){
                        handler.sendEmptyMessage(3);
                    }
                    List<LawerUser> lawerUsers=null;
                    try{
                        lawerUsers=JsonUtils.jsonToLawerUserList(LawyerUserJson);
                    }catch (Exception e){
                        handler.sendEmptyMessage(404);
                        return;
                    }
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = lawerUsers;
                    //发送信息
                    handler.sendMessage(msg);
                }
            });

        }



    @Override
    protected void initData() {
            //获取本地所有数据库
        lawerUsersList=LawerUserDao.getAllLawerUser();

        //获取用户数据
        userType = (int) MyApp.getData().get(AppConstant.LOGIN_USER_TYPE);
        user = (User) MyApp.getData().get(AppConstant.USER);

    }


    @Override
    protected void initView(View view) { customTitleView=view.findViewById(R.id.fragment_lawyer_title);
        //找到listview
        listView = view.findViewById(R.id.fragment_lawyer_listview);
        //找到刷新框架的view
        refreshLayout=view.findViewById(R.id.fragment_lawyer_refreshlayout);
        okHttpUtil=new OkHttpUtil();
    }
    /**
     * 保存律师到本地数据库
     */
    private void Save(){
        LawerUserDao.SaveLawerUser(lawerUsersList);
    }
}
