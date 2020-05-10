package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.adapter.CaseAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.Case;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CaseCollectionActivity extends BaseActivity {
    private static ListView iv_case;
    private CustomTitleView case_back;
    private ImageView case_image;
    static Handler refresh = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //取消收藏
                    caselist.remove(msg.arg1);
                    caseAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //访问出错
                    toastShort("请检查网络");
                    case_image.setVisibility(View.VISIBLE);
                    iv_case.setVisibility(View.GONE);
                    break;
                case 2:
                    toastShort("暂无收藏");
                    iv_case.setVisibility(View.GONE);
                    case_image.setVisibility(View.VISIBLE);
                    //该用户暂无案例收藏
                    break;
                case 3:
                    case_image.setVisibility(View.GONE);
                    //有案例收藏
                    getCaseSuccess(msg);
                    break;
            }
        }
    };
    private static List<Case> caselist;
    private static  CaseAdapter caseAdapter;


    private void getCaseSuccess(Message msg) {
        caselist = (List<Case>) msg.obj;
       //设置数组适配器 用于显示
        caseAdapter = new CaseAdapter(this, caselist);
        iv_case.setAdapter(caseAdapter);


    }

    @Override
    public int initLayout() {
        return R.layout.activity_case_collection;
    }

    @Override
    public void initView() {
        case_image = findViewById(R.id.iv_case_image);
        case_back = findViewById(R.id.custom_case_back);
        iv_case = findViewById(R.id.lv_case);

    }

    @Override
    public void initEvent() {
        //back 事件 点击返回FragmentMine页面
        case_back.setLeftImageButtonListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                finish();
            }

        });
        //listView点击事件
        iv_case.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Case mCase = (Case) parent.getItemAtPosition(position);
                Intent intent = new Intent(MyApp.getContext(), ShowCaseActivity.class);
                intent.putExtra(AppConstant.FRAGMENT_HOME_INTENT_CASEDATA, mCase);
                intent.putExtra(AppConstant.FRAGMENT_HOME_INTENT_CASEPOSITION,position);
                startActivity(intent);
            }
        });
        Log.d("321", "123");


    }
//
    @Override
    public void initData() {
        //获取收藏案例
        getCollectionCase();


    }

    private void getCollectionCase()  {
        User user = (User) MyApp.getData().get(AppConstant.USER);
        int user_id = user.getId();
        Log.d("idid", String.valueOf(user_id));
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.getUserCase(user_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                List<Case> casesList = JsonUtils.jsonToCaseList(jsonData);
                if (casesList.size()==0){
                    handler.sendEmptyMessage(2);
                }else{
                    Message msg = Message.obtain();
                    msg.what=3;
                    msg.obj=casesList;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
