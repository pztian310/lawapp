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

import com.pxxy.lawconsult.AdminChildStatureActivity;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.Statute;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminShowStatuteActivity extends BaseActivity {
    private CustomTitleView titleView;
    private TextView tv_title, tv_type, tv_id, tv_content, tv_opendate;
    private EditText et_title, et_type, et_id, et_content, et_opendate;
    private Intent intent;
    private int position;
    private Statute statute;
    private int type;
    private OkHttpUtil okHttpUtil;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //网络连接失败
                    Toast.makeText(AdminShowStatuteActivity.this, AppConstant.ADMIN_CASE_ACTIVITY_NOT_INTERNET, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //修改成功
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = statute;
                    message.arg1 = position;
                    //向adminCaseActivity发送信息更新数据
                    AdminChildStatureActivity.refresh.sendMessage(message);
                    Toast.makeText(AdminShowStatuteActivity.this, AppConstant.ADMIN_STATUTE_ACTIVITY_UPDATE_STATUTE_SUCCESS, Toast.LENGTH_LONG).show();
                    //关闭activity
                    closeActivity();
                    break;
                case 2:
                    //修改失败
                    Toast.makeText(AdminShowStatuteActivity.this, AppConstant.ADMIN_STATUTE_ACTIVITY_UPDATE_STATUTE_FAILURE, Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    //服务器错误
                    Toast.makeText(AdminShowStatuteActivity.this, AppConstant.ADMIN_STATUTE_ACTIVITY_UPDATE_SERVER_ERROR, Toast.LENGTH_LONG).show();
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
        return R.layout.activity_admin_show_statute;
    }

    @Override
    public void initView() {
        titleView = findViewById(R.id.admin_show_statute_activity_titleview);

        tv_title = findViewById(R.id.admin_show_statute_activity_title).findViewById(R.id.show_statute_message_key);
        tv_type = findViewById(R.id.admin_show_statute_activity_type).findViewById(R.id.show_statute_message_key);
        tv_id = findViewById(R.id.admin_show_statute_activity_id).findViewById(R.id.show_statute_message_key);
        tv_content = findViewById(R.id.admin_show_statute_activity_content).findViewById(R.id.show_statute_message_key);
        tv_opendate = findViewById(R.id.admin_show_statute_activity_opendate).findViewById(R.id.show_statute_message_key);

        et_title = findViewById(R.id.admin_show_statute_activity_title).findViewById(R.id.show_statute_message_value);
        et_type = findViewById(R.id.admin_show_statute_activity_type).findViewById(R.id.show_statute_message_value);
        et_id = findViewById(R.id.admin_show_statute_activity_id).findViewById(R.id.show_statute_message_value);
        et_content = findViewById(R.id.admin_show_statute_activity_content).findViewById(R.id.show_statute_message_value);
        et_opendate = findViewById(R.id.admin_show_statute_activity_opendate).findViewById(R.id.show_statute_message_value);

        okHttpUtil = new OkHttpUtil();
        //获取意图对象
        intent = getIntent();
        //获取statute对象
        statute = (Statute) intent.getSerializableExtra(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_STATUTE);
        //获取statute对象的位置
        position = intent.getIntExtra(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_POSITION, -1);

    }

    @Override
    public void initEvent() {
        //初始化title
        initTitleView();
        //id不可变
        et_id.setEnabled(false);
        //如果是查看模式，禁用编辑
        if (type == 0) {
            et_title.setEnabled(false);
            et_type.setEnabled(false);
            et_content.setEnabled(false);
            et_opendate.setEnabled(false);
        }
    }

    @Override
    public void initData() {
        //设置数据显示
        tv_title.setText(AppConstant.ADMIN_SHOW_STATUTE_ACTIVITY_TITLE);
        tv_id.setText(AppConstant.ADMIN_SHOW_STATUTE_ACTIVITY_ID);
        tv_type.setText(AppConstant.ADMIN_SHOW_STATUTE_ACTIVITY_TYPE);
        tv_content.setText(AppConstant.ADMIN_SHOW_STATUTE_ACTIVITY_CONTENT);
        tv_opendate.setText(AppConstant.ADMIN_SHOW_STATUTE_ACTIVITY_OPENDATE);
        //设置案例数据
        et_title.setText(statute.getLaw_title());
        et_type.setText(statute.getLaw_type());
        et_content.setText(statute.getLaw_content());
        et_id.setText(String.valueOf(statute.getLaw_id()));
        et_opendate.setText(statute.getOpendate());
    }

    /**
     * 初始化标题栏
     */
    private void initTitleView() {
        //获取类型
        String mType = intent.getStringExtra(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_TYPE);

        if (mType.equals(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_ITEM)) {
            //设置为查看状态
            type = 0;
            //设置标题
            titleView.setCenterTextViewText(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_ITEM);
            //设置title的按钮
            titleView.setLeftGroupOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeActivity();
                }
            });
        } else if (mType.equals(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_UPDATE)) {
            //设置为编辑状态
            type = 1;
            titleView.setCenterTextViewText(AppConstant.ADMIN_STATUTE_ACTIVITY_JUMP_STATUTE_MESSAGE_UPDATE);
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
                    updateStatute();
                }
            });
        }
    }

    /**
     * 更新法条数据
     */
    private void updateStatute() {
        //获取输入框中的数据
        final String statuteTitle = et_title.getText().toString().trim();
        final String statuteType = et_type.getText().toString().trim();
        final String statuteContent = et_content.getText().toString();
        final String statuteOpendate = et_opendate.getText().toString().trim();
        //判断内容是否为空
        if (TextUtils.isEmpty(statuteTitle) || TextUtils.isEmpty(statuteType)
              || TextUtils.isEmpty(statuteContent) || TextUtils.isEmpty(statuteOpendate)) {
            toastShort(AppConstant.ADMIN_CASE_ACTIVITY_UPDATE_CASE_IS_EMPTY);
            return;
        }
        //修改后的数据
        String changeStatuteTitle = "";
        String changeStatuteType = "";
        String changeStatuteContent = "";
        String changeStatuteOpendate = "";
        //判断输入框中的数据是否变化
        if (!statuteTitle.equals(statute.getLaw_title())) {
            changeStatuteTitle = statuteTitle;
        }
        if (!statuteType.equals(statute.getLaw_type())) {
            changeStatuteType = statuteType;
        }
        if (!statuteContent.equals(statute.getLaw_content())) {
            changeStatuteContent = statuteContent;
        }
        if (!statuteOpendate.equals(statute.getOpendate())){
            changeStatuteOpendate = statuteOpendate;
        }

        //没有变化
        if (TextUtils.isEmpty(changeStatuteTitle) && TextUtils.isEmpty(changeStatuteType) && TextUtils.isEmpty(changeStatuteContent) && TextUtils.isEmpty(changeStatuteOpendate)){
            closeActivity();
            return;
        }

        //进行网络请求更新数据
        okHttpUtil.adminUpdateStatute(statute.getLaw_id(), changeStatuteTitle, changeStatuteType, changeStatuteOpendate, changeStatuteContent, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String updateCaseJson = response.body().string();
                try {
                    //转化成map对象
                    Map<String, String> map = JsonUtils.jsonToMap(updateCaseJson);
                    String result = map.get(HttpConstant.ADMIN_UPDATE_STATUTE);
                    //判断是否修改成功
                    if (result.equals(HttpConstant.ADMIN_UPDATE_STATUTE_SUCCESS)) {
                        //修改数据
                        statute.setLaw_title(statuteTitle);
                        statute.setLaw_content(statuteContent);
                        statute.setLaw_type(statuteType);
                        statute.setOpendate(statuteOpendate);
                        //发送消息
                        handler.sendEmptyMessage(1);
                    } else if (result.equals(HttpConstant.ADMIN_UPDATE_STATUTE_FAILURE)) {
                        handler.sendEmptyMessage(2);
                    }
                }catch (Exception e){
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                }
            }
        });
    }
}
