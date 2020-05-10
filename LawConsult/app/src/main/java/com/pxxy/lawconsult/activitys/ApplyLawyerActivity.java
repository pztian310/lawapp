package com.pxxy.lawconsult.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.SPUtils;
import com.pxxy.lawconsult.view.CustomTitleView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ApplyLawyerActivity extends BaseActivity implements View.OnClickListener {

    private EditText name;
    private RadioGroup sex;
    private EditText age;
    private EditText email;
    private EditText unit;
    private TextView adpet;
    private EditText num;
    private EditText province;
    private EditText city;
    private EditText content;
    private Button submit;
    String[] single_list = {"婚姻家庭", "房产纠纷", "工伤赔偿", "交通事故", "民间借贷", "劳动纠纷", "刑事诉讼", "知识产权"};
    private AlertDialog.Builder builder;
    //index记住数组下标
    int index = 0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    toastShort("网络错误，请检查网络");
                    break;
                case 2:
                    toastShort("申请提交成功，请重新登入");
                    //删除本地数据，重新登入
                    String tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL,"",SPUtils.FILE_AUTOLOGIN);
                    SPUtils.clear(SPUtils.FILE_AUTOLOGIN);
                    //写入电话号码
                    SPUtils.put(AppConstant.SP_AUTOLOGIN_TEL,tel,SPUtils.FILE_AUTOLOGIN);
                    sendExitBrodcast();
                    //移除清除密码配置
                    SPUtils.remove(AppConstant.SP_CONFIG_CLEARPASSWORD,SPUtils.FILE_CONFIG);
                    startActivity(new Intent(ApplyLawyerActivity.this,LoginActivity.class));
                    handler.sendEmptyMessageDelayed(5, 2000);
                    break;
                case 3:
                    toastShort("服务器繁忙，请稍后再试");
                    break;
                case 4:
                    toastShort("申请失败，请稍后再试");
                    break;
                case 5:
                    finish();
                    Log.d("exit","退出");
                    break;
            }
        }
    };
    private CustomTitleView back;

    @Override
    public int initLayout() {
        return R.layout.activity_apply_lawyer;
    }

    @Override
    public void initView() {
        //返回
        back = findViewById(R.id.Custom_casecollection_back);
        //律师名字
        name = findViewById(R.id.applylawyet_et_name);
        //性别
        sex = findViewById(R.id.applylawyet_rg_sex);
        //年龄
        age = findViewById(R.id.applylawyet_et_age);
        //学历
        email = findViewById(R.id.applylawyet_et_education);
        //所在单位
        unit = findViewById(R.id.applylawyet_et_unit);
        //擅长类别
        adpet = findViewById(R.id.applylawyet_et_adpet);
        //律师编号
        num = findViewById(R.id.applylawyet_et_num);
        //省份
        province = findViewById(R.id.applylawyet_et_province);
        //市
        city = findViewById(R.id.applylawyet_et_city);
        //介绍
        content = findViewById(R.id.applylawyet_et_content);
        //提交
        submit = findViewById(R.id.applylawyet_bt_submit);

    }

    @Override
    public void initEvent() {
        adpet.setOnClickListener(this);
        submit.setOnClickListener(this);
        back.setLeftImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.applylawyet_et_adpet:
                //弹出类型对话框
                shoutAdpet();
                break;
            case R.id.applylawyet_bt_submit:
                //获取数据
                getApplyData();
                break;
        }
    }

    private void getApplyData() {
        //获取本地用户id
        User user = (User) MyApp.getData().get(AppConstant.USER);
        int userId = user.getId();
        String userName = name.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        int sexId = sex.getCheckedRadioButtonId();
        //判断是男是女
        switch (sexId) {
            //男
            case R.id.apply_lawyer_rbtn_man:
                sexId=1;
                break;
            //女
            case R.id.apply_lawyer_rbtn_woman:
                sexId=0;
                break;
        }

        String userEmail = email.getText().toString().trim();
        String userUnit = unit.getText().toString().trim();
        String userNum = num.getText().toString().trim();
        String userProvince = province.getText().toString().trim();
        String userCity = city.getText().toString().trim();
        String userAdpet = adpet.getText().toString().trim();
        String userContent = content.getText().toString().trim();
        if (TextUtils.isEmpty(userName)){
            toastShort("请输入姓名");
            return;
        }else  if (TextUtils.isEmpty(userAge)){
            toastShort("年龄不能为空");
            return;
        }else if (TextUtils.isEmpty(userEmail)){
            toastShort("请填写邮箱");
            return;
        }else if (TextUtils.isEmpty(userUnit)){
            toastShort("请填写所在单位");
            return;
        }else if(TextUtils.isEmpty(userNum)){
            toastShort("请填写律师证编号");
            return;
        }else if(TextUtils.isEmpty(userProvince)){
            toastShort("请填写省份");
            return;
        }else if(TextUtils.isEmpty(userCity)){
            toastShort("请填写地址");
            return;
        }else  if (TextUtils.isEmpty(userContent)){
            toastShort("您需要填写个人资料，便于审核哦");
            return;
        }
        //将数据提交过去
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        //获取本地电话号码
        User tel = (User) MyApp.getData().get(AppConstant.USER);
        String phoneNum = tel.getTel();
        //判断擅长类型
        if (userAdpet==null&&userAdpet.length()==0){
            userAdpet="刑事诉讼";
        }
        //地址
       String address=userProvince+"省"+userCity+"市";
        Log.d("add", "userId:"+userId+"  userName:"+userName+"  userAge:"+userAge+"  sexId:"+sexId+"  userEmail:"+userEmail
            +"  userNum:"+userNum+"  userAdpet:"+userAdpet+"  userUnit:"+userUnit+"  userContent:"+userContent+"  address:"+address
        );
        okHttpUtil.setLaywerUser(userId, userName, userAge, sexId, userEmail, userNum, userAdpet, userUnit, userContent, address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Log.d("jsons", jsonData);
                Map<String,String> map = JsonUtils.jsonToMap(jsonData);
                String result = map.get(AppConstant.APPLYLAWYER_RESULT);
                if (result.equals(AppConstant.APPLYLAWYER_SUCCESS)){
                    //成功申请
                    handler.sendEmptyMessage(2);
                }else if (result.equals(AppConstant.APPLYLAWYER_SERVER_ERROR)){
                    //服务器繁忙
                    handler.sendEmptyMessage(3);
                }else  if (result.equals(AppConstant.APPLYLAWYER_FAILURE)){
                    handler.sendEmptyMessage(4);
                }
            }
        });

    }

    private void shoutAdpet() {
        //获取对话框构造器
        builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("问题类型");
        //设置单选对话框
        builder.setSingleChoiceItems(single_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                index = which;
            }
        });
        //设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adpet.setText(single_list[index]);
            }
        });
        //创建对话框并且显示
        builder.create().show();
    }

    /**
     * 发送广播
     */
    public void sendExitBrodcast(){
        Intent intent = new Intent();
        intent.setAction(AppConstant.EXIT_APP);
        sendBroadcast(intent);
        closeActivity();
    }

    /**
     * 销毁activity
     */
    public  void closeActivity(){
        this.finish();
    }
}
