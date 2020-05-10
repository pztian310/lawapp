package com.pxxy.lawconsult.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.HintDialogUtils;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminAddCaseActivity extends BaseActivity {
    private CustomTitleView titleView;
    private TextView tv_title, tv_type, tv_image, tv_content;
    private EditText et_title, et_type, et_image, et_content;
    private OkHttpUtil okHttpUtil;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //网络错误
                    toastShort(AppConstant.ADMIN_ADD_CASE_ACTIVITY_NOT_INTERNET);
                    break;
                case 1:
                    //服务器错误
                    addCaseFailureServerError();
                    break;
                case 2:
                    //添加案例成功
                    addCaseSuccess();
                    break;
                case 3:
                    //添加案例失败
                    addCaseFailure();
                    break;
            }
        }
    };

    /**
     * 添加案例失败(服务器错误)
     */
    private void addCaseFailureServerError() {
        HintDialogUtils dialogUtils = new HintDialogUtils();
        dialogUtils.initDialog(AdminAddCaseActivity.this,
                AppConstant.ADMIN_ADD_CASE_ACTIVITY_DIALOG_TITLE,
                AppConstant.ADMIN_ADD_CASE_ACTIVITY_DIALOG_SERVER_ERROR_MESSAGE,true).showDialog();
    }

    /**
     * 添加案例失败
     */
    private void addCaseFailure() {
        HintDialogUtils dialogUtils = new HintDialogUtils();
        dialogUtils.initDialog(AdminAddCaseActivity.this,
                AppConstant.ADMIN_ADD_CASE_ACTIVITY_DIALOG_TITLE,
                AppConstant.ADMIN_ADD_CASE_ACTIVITY_DIALOG_FAILURE_MESSAGE,true).showDialog();
    }

    /**
     * 添加案例成功
     */
    private void addCaseSuccess() {
        final HintDialogUtils dialogUtils = new HintDialogUtils();
        dialogUtils.initDialog(AdminAddCaseActivity.this,
                AppConstant.ADMIN_ADD_CASE_ACTIVITY_DIALOG_TITLE,
                AppConstant.ADMIN_ADD_CASE_ACTIVITY_DIALOG_SUCCESS_MESSAGE,false);
        //设置点击事件
        dialogUtils.setRightButton(AppConstant.ADMIN_ADD_CASE_ACTIVITY_DIALOG_BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtils.closeDialog();
                //关闭activity
                closeActivity();
            }
        });
        //显示dialog
        dialogUtils.showDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_admin_add_case;
    }

    @Override
    public void initView() {
        titleView = findViewById(R.id.admin_add_case_titleview);
        tv_title = findViewById(R.id.admin_add_case_title).findViewById(R.id.add_case_message_key);
        tv_type = findViewById(R.id.admin_add_case_type).findViewById(R.id.add_case_message_key);
        tv_image = findViewById(R.id.admin_add_case_image).findViewById(R.id.add_case_message_key);
        tv_content = findViewById(R.id.admin_add_case_content).findViewById(R.id.add_case_message_key);
        et_title = findViewById(R.id.admin_add_case_title).findViewById(R.id.add_case_message_value);
        et_type = findViewById(R.id.admin_add_case_type).findViewById(R.id.add_case_message_value);
        et_image = findViewById(R.id.admin_add_case_image).findViewById(R.id.add_case_message_value);
        et_content = findViewById(R.id.admin_add_case_content).findViewById(R.id.add_case_message_value);
        //设置提示key
        //设置数据显示
        tv_title.setText(AppConstant.ADMIN_SHOW_CASE_ACTIVITY_TITLE);
        tv_image.setText(AppConstant.ADMIN_SHOW_CASE_ACTIVITY_IMAGE);
        tv_type.setText(AppConstant.ADMIN_SHOW_CASE_ACTIVITY_TYPE);
        tv_content.setText(AppConstant.ADMIN_SHOW_CASE_ACTIVITY_CONTENT);
        //设置输入框中的提示语句
        et_title.setHint(AppConstant.ADMIN_ADD_CASE_ACTIVITY_HINT_TITLE);
        et_image.setHint(AppConstant.ADMIN_ADD_CASE_ACTIVITY_HINT_IMAGE);
        et_type.setHint(AppConstant.ADMIN_ADD_CASE_ACTIVITY_HINT_TYPE);
        et_content.setHint(AppConstant.ADMIN_ADD_CASE_ACTIVITY_HINT_CONTENT);
        //创建okhttp
        okHttpUtil = new OkHttpUtil();
    }

    @Override
    public void initEvent() {
        //设置titleView的左按钮的点击事件
        titleView.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });

        //设置titleView的右边按钮的点击事件
        titleView.setRightImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加案例
                addCaseOnInternet();
            }
        });
    }

    /**
     * 网络请求添加案例
     */
    private void addCaseOnInternet() {
        //获取输入的值
        String caseTitle = et_title.getText().toString().trim();
        String caseType = et_type.getText().toString().trim();
        String caseImage = et_image.getText().toString().trim();
        String caseContent = et_content.getText().toString().trim();
        //判断是否为空
        if (TextUtils.isEmpty(caseTitle) || TextUtils.isEmpty(caseType)|| TextUtils.isEmpty(caseImage)|| TextUtils.isEmpty(caseContent)){
            toastShort(AppConstant.ADMIN_ADD_CASE_ACTIVITY_DATA_CAN_NOT_BE_EMPTY);
            return;
        }
        //网络请求添加案例数据
        okHttpUtil.adminAddCase(caseTitle, caseType, caseImage, caseContent, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取json对象
                String addCaseJson = response.body().string();
                try{
                    //转化成map对象
                    Map<String,String> map  = JsonUtils.jsonToMap(addCaseJson);
                    String result = map.get(HttpConstant.ADMIN_ADD_CASE_RESULT);
                    if (result.equals(HttpConstant.ADMIN_ADD_CASE_RESULT_SUCCESS)) {
                        handler.sendEmptyMessage(2);
                    }else if (result.equals(HttpConstant.ADMIN_ADD_CASE_RESULT_FAILURE)){
                        handler.sendEmptyMessage(3);
                    }
                }catch (Exception e){
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        });
    }
}
