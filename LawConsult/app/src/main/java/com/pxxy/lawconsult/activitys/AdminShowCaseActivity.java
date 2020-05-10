package com.pxxy.lawconsult.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.Case;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminShowCaseActivity extends BaseActivity {
    private CustomTitleView titleView;
    private TextView tv_title, tv_type, tv_image, tv_content;
    private EditText et_title, et_type, et_image, et_content;
    private Intent intent;
    private int type;
    private Case mCase;
    private int casePosition;
    private OkHttpUtil okHttpUtil;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //网络连接失败
                    Toast.makeText(AdminShowCaseActivity.this, AppConstant.ADMIN_CASE_ACTIVITY_NOT_INTERNET, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //修改成功
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = mCase;
                    message.arg1 = casePosition;
                    //向adminCaseActivity发送信息更新数据
                    AdminCaseActivity.refresh.sendMessage(message);
                    Toast.makeText(AdminShowCaseActivity.this, AppConstant.ADMIN_CASE_ACTIVITY_UPDATE_CASE_SUCCESS, Toast.LENGTH_LONG).show();
                    //关闭activity
                    closeActivity();
                    break;
                case 2:
                    //修改失败
                    Toast.makeText(AdminShowCaseActivity.this, AppConstant.ADMIN_CASE_ACTIVITY_UPDATE_CASE_FAILURE, Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_admin_show_case;
    }

    @Override
    public void initView() {
        titleView = findViewById(R.id.admin_show_case_activity_titleview);
        tv_title = findViewById(R.id.admin_show_case_activity_title).findViewById(R.id.show_case_message_key);
        tv_type = findViewById(R.id.admin_show_case_activity_type).findViewById(R.id.show_case_message_key);
        tv_image = findViewById(R.id.admin_show_case_activity_image).findViewById(R.id.show_case_message_key);
        tv_content = findViewById(R.id.admin_show_case_activity_content).findViewById(R.id.show_case_message_key);
        et_title = findViewById(R.id.admin_show_case_activity_title).findViewById(R.id.show_case_message_value);
        et_type = findViewById(R.id.admin_show_case_activity_type).findViewById(R.id.show_case_message_value);
        et_image = findViewById(R.id.admin_show_case_activity_image).findViewById(R.id.show_case_message_value);
        et_content = findViewById(R.id.admin_show_case_activity_content).findViewById(R.id.show_case_message_value);
        //获取Intent
        intent = getIntent();
        //设置case和当前case的位置
        mCase = (Case) intent.getSerializableExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_CASE);
        casePosition = intent.getIntExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_POSITION, -1);

        okHttpUtil = new OkHttpUtil();
    }

    @Override
    public void initEvent() {
        //设置titleView
        initTitleView();
        //如果是查看案例,设置value不可编辑
        if (type == 0) {
            //设置不可编辑
            et_title.setEnabled(false);
            et_type.setEnabled(false);
            et_image.setEnabled(false);
            et_content.setEnabled(false);
        }
    }

    @Override
    public void initData() {
        //设置数据显示
        tv_title.setText(AppConstant.ADMIN_SHOW_CASE_ACTIVITY_TITLE);
        tv_image.setText(AppConstant.ADMIN_SHOW_CASE_ACTIVITY_IMAGE);
        tv_type.setText(AppConstant.ADMIN_SHOW_CASE_ACTIVITY_TYPE);
        tv_content.setText(AppConstant.ADMIN_SHOW_CASE_ACTIVITY_CONTENT);
        //设置案例数据
        et_title.setText(mCase.getTitle());
        et_type.setText(mCase.getType());
        et_image.setText(mCase.getImage());
        et_content.setText(mCase.getContent());
    }

    /**
     * 初始化titleview
     */
    private void initTitleView() {
        String titleText = intent.getStringExtra(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_TYPE);
        //初始化titleview
        if (titleText.equals(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_ITEM)) {
            type = 0;
            titleView.setCenterTextViewText(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_ITEM);
            //设置title的按钮
            titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeActivity();
                }
            });
        } else if (titleText.equals(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_UPDATE)) {
            type = 1;
            titleView.setCenterTextViewText(AppConstant.ADMIN_CASE_ACTIVITY_JUMP_CASE_MESSAGE_UPDATE);
            //设置title的按钮图片id
            titleView.setLeftTextViewVisiable(View.INVISIBLE);
            titleView.setLeftImageButtonResourse(R.drawable.close);
            titleView.setRightImageButtonResourse(R.drawable.check);
            //设置按钮的点击事件
            titleView.setLeftImageButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeActivity();
                }
            });
            //设置右边按钮的点击事件
            titleView.setRightImageButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        updateCase();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    /**
     * 更新case数据
     * @throws IOException
     */
    private void updateCase() throws IOException {
        //获取输入框中的数据
        final String caseTitle = et_title.getText().toString().trim();
        final String caseType = et_type.getText().toString().trim();
        final String caseImage = et_image.getText().toString().trim();
        final String caseContent = et_content.getText().toString().trim();
        //判断内容是否为空
        if (TextUtils.isEmpty(caseTitle) || TextUtils.isEmpty(caseType) || TextUtils.isEmpty(caseImage) || TextUtils.isEmpty(caseContent)) {
            toastShort(AppConstant.ADMIN_CASE_ACTIVITY_UPDATE_CASE_IS_EMPTY);
            return;
        }
        //修改后的数据
        String changeCaseTitle = "";
        String changeCaseType = "";
        String changeCaseImage = "";
        String changeCaseContent = "";
        //判断输入框中的数据是否变化
        if (!caseTitle.equals(mCase.getTitle())) {
            changeCaseTitle = caseTitle;
        }
        if (!caseType.equals(mCase.getType())) {
            changeCaseType = caseType;
        }
        if (!caseImage.equals(mCase.getImage())) {
            changeCaseImage = caseImage;
        }
        if (!caseContent.equals(mCase.getContent())) {
            changeCaseContent = caseContent;
        }

        //进行网络请求更新数据
        okHttpUtil.adminUpdateCase(mCase.getId(), changeCaseTitle, changeCaseType, changeCaseImage, changeCaseContent, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String updateCaseJson = response.body().string();
                //转化成map对象
                Map<String, String> map = JsonUtils.jsonToMap(updateCaseJson);
                String result = map.get(HttpConstant.ADMIN_UPDATE_CASE);
                //判断是否修改成功
                if (result.equals(HttpConstant.ADMIN_UPDATE_CASE_SUCCESS)) {
                    //修改数据
                    mCase.setTitle(caseTitle);
                    mCase.setImage(caseImage);
                    mCase.setContent(caseContent);
                    mCase.setType(caseType);
                    handler.sendEmptyMessage(1);
                } else if (result.equals(HttpConstant.ADMIN_UPDATE_CASE_FAILURE)) {
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }
}
