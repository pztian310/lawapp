package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.Case;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShowCaseActivity extends BaseActivity {
    private CustomTitleView titleView;
    private WebView webView;
    private Intent caseData;
    private ImageButton collectButton;
    private OkHttpUtil okHttpUtil;
    private User user;
    private Case mCase;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //网络连接失败
                    Toast.makeText(ShowCaseActivity.this, AppConstant.SHOW_CASE_INTERNET_ERROR, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //添加收藏成功
                    collectButton.setSelected(true);
                    //删除本地收藏数据
                    if (collect == null){
                        getUserCollect();
                        collect = (List<Integer>) MyApp.getData().get(AppConstant.USER_COLLECT);
                    }else {
                        collect.add(mCase.getId());
                    }

                    Toast.makeText(ShowCaseActivity.this, AppConstant.SHOW_CASE_ADD_COLLECT_SUCCESS, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //添加收藏失败
                    Toast.makeText(ShowCaseActivity.this, AppConstant.SHOW_CASE_ADD_COLLECT_FAILURE, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    //服务器错误
                    Toast.makeText(ShowCaseActivity.this, AppConstant.SHOW_CASE_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    //取消收藏成功
                    collectButton.setSelected(false);
                    //删除本地收藏数据
                    if (collect == null){
                        getUserCollect();
                        collect = (List<Integer>) MyApp.getData().get(AppConstant.USER_COLLECT);
                    }else {
                        collect.remove((Integer) mCase.getId());
                    }
                    //发送消息给案例收藏，刷新数据
                    if (position != -1){
                        Message message = Message.obtain();
                        message.what = 0;
                        message.arg1 = position;
                        CaseCollectionActivity.refresh.sendMessage(message);
                    }
                    Toast.makeText(ShowCaseActivity.this, AppConstant.SHOW_CASE_DELETE_COLLECT_SUCCESS, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    //取消收藏失败
                    Toast.makeText(ShowCaseActivity.this, AppConstant.SHOW_CASE_DELETE_COLLECT_FAILURE, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private List<Integer> collect;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_show_case;
    }

    @Override
    public void initData() {
        mCase = (Case) caseData.getSerializableExtra(AppConstant.FRAGMENT_HOME_INTENT_CASEDATA);
        position = caseData.getIntExtra(AppConstant.FRAGMENT_HOME_INTENT_CASEPOSITION, -1);
        //获取用户
        user = (User) MyApp.getData().get(AppConstant.USER);
        //webview显示网页
        webView.loadUrl(mCase.getContent());
        //设置webview与js交互
        webView.getSettings().setJavaScriptEnabled(true);
        //设置默认webview打开
        webView.setWebViewClient(new WebViewClient());
        //设置是否收藏
        collect = (List<Integer>) MyApp.getData().get(AppConstant.USER_COLLECT);
        if (collect == null){
            Toast.makeText(this, AppConstant.SHOW_CASE_GET_COLLECT_FAILURE, Toast.LENGTH_SHORT).show();
            return;
        }
        if (collect != null){
            for (int a: collect){
                //如果collect中存在这个案例id，则显示收藏
                if (a == mCase.getId()){
                    collectButton.setSelected(true);
                }
            }
        }

    }

    @Override
    public void initEvent() {
        //设置返回按钮事件
        titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        //设置标题右边收藏的点击事件
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectButton.isSelected()){
                    cancelCollect();
                }else {
                    addCollect();
                }
            }
        });
    }

    /**
     * 添加收藏
     */
    private void addCollect() {
        Log.d("user", user.getId()+"");
        Log.d("case", mCase.getId()+"");
        okHttpUtil.addUserCollect(user.getId(), mCase.getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String collectJson = response.body().string();
                Log.d("show", collectJson);
                Map<String,String> map = JsonUtils.jsonToMap(collectJson);
                //判断是否成功
                String result = map.get(HttpConstant.ADD_COLLECT_RESULT);
                if (result.equals(HttpConstant.ADD_COLLECT_RESULT_SUCCESS)){
                    handler.sendEmptyMessage(1);
                }else if (result.equals(HttpConstant.ADD_COLLECT_RESULT_FAILURE)){
                    handler.sendEmptyMessage(2);
                }else if (result.equals(HttpConstant.ADD_COLLECT_RESULT_SERVER_ERROR)){
                    handler.sendEmptyMessage(3);
                }
            }
        });
    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        okHttpUtil.deleteUserCollect(user.getId(), mCase.getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String collectJson = response.body().string();
                Map<String,String> map = JsonUtils.jsonToMap(collectJson);
                //判断是否成功
                String result = map.get(HttpConstant.DELETE_COLLECT_RESULT);
                if (result.equals(HttpConstant.DELETE_COLLECT_RESULT_SUCCESS)){
                    handler.sendEmptyMessage(4);
                }else if (result.equals(HttpConstant.DELETE_COLLECT_RESULT_FAILURE)){
                    handler.sendEmptyMessage(5);
                }else if (result.equals(HttpConstant.DELETE_COLLECT_RESULT_SERVER_ERROR)){
                    handler.sendEmptyMessage(3);
                }
            }
        });
    }

    @Override
    public void initView() {
        titleView = findViewById(R.id.show_case_activity_title);
        caseData = getIntent();
        webView = findViewById(R.id.show_case_activity_webview);
        collectButton = titleView.getRightImageButton();
        okHttpUtil = new OkHttpUtil();
    }

    /**
     * 重新获取用户收藏
     */
    public void getUserCollect(){
        //获取收藏的案例
        okHttpUtil.getUserCollect(user.getId(), new Callback() {
            private List<Integer> collect;

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userCollectJson = response.body().string();
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
}
