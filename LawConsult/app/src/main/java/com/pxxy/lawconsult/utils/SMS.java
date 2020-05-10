package com.pxxy.lawconsult.utils;

import android.os.Handler;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SMS {
    //地区码，中国(86)
    public static final String COUNTRY = "86";
    //电话号码
    private static String smsPhone;
    //要传入handler对象
    private static Handler smsHandler;
    //事件回调
    static EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int i, int i1, Object o) {
            if (i == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    smsHandler.sendEmptyMessage(101);
                } else {
                    // TODO 处理错误的结果
                    smsHandler.sendEmptyMessage(102);
                }
            } else if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证码验证通过的结果
                    smsHandler.sendEmptyMessage(201);
                } else {
                    // TODO 处理错误的结果
                    smsHandler.sendEmptyMessage(202);
                }
            }
        }
    };

    /**
     * 发送短信验证码
     * handler中加入what值判断，用于反馈短信验证结果
     * 101:短信验证码发送成功,102:短信验证码发送失败,201:短信验证码验证成功,202:短信验证码验证失败
     * @param phone 验证的手机号
     * @param handler 传入用于更新ui的Handler对象
     *
     */
    public static void sendSMS(String phone, Handler handler) {
        smsPhone = phone;
        smsHandler= handler;
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(eventHandler);
        //发送验证码
        SMSSDK.getVerificationCode(COUNTRY, phone);
    }

    /**
     * 验证验证码是否正确
     *
     * @param code
     */
    public static void verifySMS(String code) {
        SMSSDK.submitVerificationCode(COUNTRY, smsPhone, code);
    }

    /**
     * 注销所有注册的回调方法
     */
    public static void closeSMS() {
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
        if (smsPhone != null) {
            smsPhone = null;
        }
        if (smsHandler != null) {
            smsHandler = null;
        }
    }

}

