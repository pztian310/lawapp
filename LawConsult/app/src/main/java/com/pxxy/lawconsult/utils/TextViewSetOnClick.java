package com.pxxy.lawconsult.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.widget.TextView;

public class TextViewSetOnClick {

    /**
     * 给TextView的部分文字添加点击事件
     * @param textView 要设置点击事件的TextView
     * @param clickableSpan 接口，要重写onclick方法，重写updateDrawState方法，setUnderlineText(false)删除下划线
     *                           setTextColor来设置字体颜色
     * @param start 可点击文字的开始位置,不包含
     * @param end   可点击文字的末尾，包含
     * @param content 输入textView要显示的文字
     */
    public static void setOnClick(TextView textView,ClickableSpan clickableSpan ,int start, int end,String content){
        //创建SpannableString对象
        SpannableStringBuilder builder = new SpannableStringBuilder();
        //向Spannable添加content内容
        builder.append(content);
        //设置文字的点击事件
        builder.setSpan(clickableSpan,start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置textView
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        //设置可点击的文本
        textView.setText(builder);
        //设置点击后的颜色为透明
        textView.setHighlightColor(Color.TRANSPARENT);
    }
}
