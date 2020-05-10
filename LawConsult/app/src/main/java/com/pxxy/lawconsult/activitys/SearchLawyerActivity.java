package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.adapter.StatuteAdapter;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.DBConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.dao.StatuteDao;
import com.pxxy.lawconsult.db.AppSqlite;
import com.pxxy.lawconsult.entity.Statute;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchLawyerActivity extends BaseActivity implements View.OnClickListener {

    private List<Statute> statutesList;

    @Override
    public int initLayout() {
        return R.layout.activity_search;
    }
    private ImageView imageView;//返回键的图片
    private ListView listView;//搜索框返回的律师信息listview
    private StatuteAdapter statuteAdapter;
    private EditText appCompatAutoCompleteTextView;
    private StatuteAdapter adapter;
    public static AppSqlite sqlite = AppSqlite.getInstance(MyApp.getContext());
    public static SQLiteDatabase db = null;
    private String tv_title,tv_content,tv_type,tv_opendate;
    private int StatuteStart = 0;
    private OkHttpUtil okHttpUtil;
    private String type,content;
    private TextView textView_title,textView_content,textView_type,textView_opendate;
    private Spinner spinner;
    private Button btn_search;

    @Override
    public void initView() {
        super.initView();
        //找到搜索图片按钮
        imageView = findViewById(R.id.activity_search_img);
        listView = findViewById(R.id.activity_search_list);
        appCompatAutoCompleteTextView=findViewById(R.id.activity_search_autotext);
        spinner=findViewById(R.id.activity_search_spinner);
        btn_search=findViewById(R.id.activity_serach_btn);
        db = sqlite.getWritableDatabase();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        btn_search.setOnClickListener(this);
        //实例化okHttpUtil对象
        okHttpUtil = new OkHttpUtil();
        imageView.setOnClickListener(this);//设置还回键的监听
    }



    @Override
    public void initData() {
        //设置自动搜索框
        appCompatAutoCompleteTextView.setOnClickListener(this);
        statutesList = new ArrayList<>();
        statuteAdapter = new StatuteAdapter(MyApp.getContext(), R.layout.adapter_statute, statutesList);
        listView.setAdapter(statuteAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Statute statute= (Statute) parent.getItemAtPosition(position);
                Log.d("123", statute.getLaw_content());
                Intent intent=new Intent(MyApp.getContext(),StatutesActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("statute",statute);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //保存数据
        StatuteDao.clearDB();
        SaveStatute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_search_img:
                finish();
                break;
            case R.id.activity_serach_btn:
                search();
                break;
                default:
                    break;
        }
    }

    private void search(){
        //按類型查詢
        String search_content=appCompatAutoCompleteTextView.getText().toString().trim();
        if(search_content==null||search_content.equals("")){
            Toast.makeText(MyApp.getContext(),"请输入查询的内容。",Toast.LENGTH_SHORT).show();
        }
        else{
            //获取Spinner的item
            String sp_content = (String) spinner.getSelectedItem();
            if (sp_content.equals("按内容查询")) {
                //按內容查詢
                okHttpUtil.getFuzzyStatuteList(search_content, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(MyApp.getContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String StartJson = response.body().string();
                        Log.d("ADR", StartJson);
                        List<Statute> statutes = null;
                        statutes = JsonUtils.jsonToStatuteList(StartJson);
                        if(statutesList!=null){
                            statutesList.clear();
                        }
                        //加载数据
                        for (Statute mstatute : statutes) {
                            System.out.print(mstatute.toString());
                            statutesList.add(mstatute);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statuteAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                if(statutesList.size()==0){

                }else {
                    Toast.makeText(MyApp.getContext(), "按内容查询到" + statutesList.size() + "条数据", Toast.LENGTH_SHORT).show();
                }
            } else {
                //按类型查询
                okHttpUtil.getStatuteList(search_content, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(MyApp.getContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String StartJson = response.body().string();
                        Log.d("ADR", StartJson);
                        List<Statute> statutes = null;
                        statutes = JsonUtils.jsonToStatuteList(StartJson);
                        //加载数据
                        if(statutesList!=null){
                            statutesList.clear();
                        }
                        for (Statute mstatute : statutes) {
                            System.out.print(mstatute.toString());
                            statutesList.add(mstatute);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statuteAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                if(statutesList.size()==0){

                }else {
                    Toast.makeText(MyApp.getContext(), "按类型查询到" + statutesList.size() + "条数据", Toast.LENGTH_SHORT).show();
                }
            }
        }
        }
    //保存法条
    private void SaveStatute() {
        StatuteDao.SaveStatute(statutesList);
    }
}
