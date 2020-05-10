package com.pxxy.lawconsult.activitys;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.view.CustomTitleView;

public class IdeaActivity extends BaseActivity implements View.OnClickListener {

    private CustomTitleView bt_title;
    private EditText et_content;
    private Button bt_submit;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                finish();
            }
        }
    };

    @Override
    public int initLayout() {
        return R.layout.activity_idea;
    }

    @Override
    public void initView() {
        bt_title = findViewById(R.id.idea_bt_title);
        et_content = findViewById(R.id.idea_et_content);
        bt_submit = findViewById(R.id.idea_bt_submit);
    }

    @Override
    public void initEvent() {
        bt_title.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.idea_bt_submit:
                //将用户输入的数据提交到服务端
                setData();
                break;
        }
    }
/**
 * 将用户数据上交
 * */
    private void setData() {
        //用户数据
        String ideaData = et_content.getText().toString().trim();
        if (ideaData.length()<10){
            toastShort("文字不能少于10个字");
        }else{
            //将数据提交到服务器
            toastShort("提交成功，我们将尽快解决问题，祝你玩得开心");
            //俩秒钟退出活动
            handler.sendEmptyMessageDelayed(1,2000);
        }

    }
}
