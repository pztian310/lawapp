package com.pxxy.lawconsult.utils;

import android.os.CountDownTimer;
import android.widget.Button;

public class MyCountDownTime extends CountDownTimer {



    private Boolean isfinish=true;//定义一个参数，判断验证是否完成默认true
    private Button myButton;

/**
 * @param millisInFuture 要设置倒计时总时间
 * @param countDownInterval  设置更新UI时长（一般为1000毫秒）
 * @param myButton  你所点击的验证码控件(button)
 * */
    public MyCountDownTime(long millisInFuture, long countDownInterval,Button myButton) {
        super(millisInFuture, countDownInterval);
        this.myButton=myButton;
    }

    //计时过程
    @Override
    public void onTick(long millisUntilFinished) {
        //未结束
        isfinish=false;
        //防止点击多次
        myButton.setClickable(false);
        //设置显示文字
        myButton.setText(millisUntilFinished/1000+"秒后重试");
    }

    //计时结束
    @Override
    public void onFinish() {
        //结束
        isfinish=true;
        //可点击
        myButton.setClickable(true);
        myButton.setText("重新发送");
    }
    public Boolean isFinish() {
        return isfinish;
    }
}
