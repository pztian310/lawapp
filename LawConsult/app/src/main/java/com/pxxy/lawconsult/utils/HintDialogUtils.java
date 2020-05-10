package com.pxxy.lawconsult.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pxxy.lawconsult.R;

public class HintDialogUtils {

    private TextView title;
    private TextView message;
    private Button leftButton;
    private Button rightButton;
    private View centerLine;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private int onclickCount = 0; //统计设置按钮个数

    /**
     * 一个自定义dialog，按钮自己添加,setPositiveButton：设置正面按钮
     * setNegativeButton：设置反面按钮,设置按钮能链式设置
     * @param titleText dialog标题
     * @param context 标题上下文
     * @param messageText   dialog内容
     * @param Cancelable dialog 点击对话框以外的区域是否让对话框消失，如果为null，默认为true；
     * @return
     */
    public HintDialogUtils initDialog(Context context, String titleText, String messageText, boolean Cancelable){
        //初始化
        onclickCount = 0;
        //创建alertdialog对象
        dialogBuilder = new AlertDialog.Builder(context);
        //获取显示视图
        View view = View.inflate(context, R.layout.dialog_hint,null);
        //获取显示视图的控件
        title = view.findViewById(R.id.dialog_hint_title);
        message = view.findViewById(R.id.dialog_hint_message);
        leftButton = view.findViewById(R.id.dialog_hint_left_button);
        rightButton = view.findViewById(R.id.dialog_hint_right_button);
        centerLine = view.findViewById(R.id.dialog_hint_center_line);
        //隐藏按钮
        centerLine.setVisibility(View.GONE);
        leftButton.setVisibility(View.GONE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        //设置标题
        title.setText(titleText);
        //设置内容
        message.setText(messageText);
        //设置dialog视图
        dialogBuilder.setView(view);
        return this;
    }

    /**
     * 设置左边按钮
     * @param text
     * @param onClickListener
     * @return
     */
    public HintDialogUtils setLeftButton(String text, View.OnClickListener onClickListener){
        if(text != null && !TextUtils.isEmpty(text) && onClickListener != null){
            if(leftButton != null){
                leftButton.setVisibility(View.VISIBLE);
                leftButton.setOnClickListener(onClickListener);
                onclickCount++;
            }
        }
        return this;
    }

    /**
     * 设置右边按钮
     * @return
     */
    public HintDialogUtils setRightButton(String text, View.OnClickListener onClickListener){
        if(text != null && !TextUtils.isEmpty(text) && onClickListener != null){
            if(rightButton != null){
                rightButton.setVisibility(View.VISIBLE);
                rightButton.setOnClickListener(onClickListener);
                onclickCount++;
            }
        }
        return this;
    }
    /**
     * 显示alertDialog,用于关闭dialog
     */
    public HintDialogUtils showDialog(){
        if (dialogBuilder != null){
            dialog = dialogBuilder.create();
        }

        if (dialog != null && !dialog.isShowing()){
            if (onclickCount == 2){
                centerLine.setVisibility(View.VISIBLE);
            }
            dialog.show();
        }
        return this;
    }


    /**
     * 关闭dialog
     */
    public HintDialogUtils closeDialog(){
        if(dialog.isShowing() && dialog != null){
            dialog.dismiss();
        }
        return this;
    }
}
