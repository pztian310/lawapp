package com.pxxy.lawconsult.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxxy.lawconsult.R;


import de.hdodenhof.circleimageview.CircleImageView;

public class CustomTitleView extends RelativeLayout {
    //左右两边的图片按钮
    private ImageButton leftImageButton, rightImageButton;
    //左边的圆形ImageView
    private CircleImageView leftCircleImageView;
    //中间的标题
    private TextView centerTextView;
    //标题栏的根节点
    private RelativeLayout customTitleView;
    //左边按钮右边的TextView控件的文字
    private TextView leftTextView;

    public CustomTitleView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.custom_title_view, this, true);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_title_view, this, true);
        //初始化控件
        initView();
        //加载xml配置
        initConfig(context, attrs);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.custom_title_view, this, true);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        customTitleView = findViewById(R.id.custom_title_view);
        leftImageButton = findViewById(R.id.custom_title_view_left_button);
        rightImageButton = findViewById(R.id.custom_title_view_right_button);
        centerTextView = findViewById(R.id.custom_title_view_center_textview);
        leftCircleImageView = findViewById(R.id.custom_title_view_left_circleimage);
        leftTextView = findViewById(R.id.custom_title_view_left_text);
    }

    /**
     * 加载xml文件中的配置
     */
    private void initConfig(Context context, AttributeSet attrs) {
        //获取TypedArray
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleView);
        //判断array是否为空
        if (array != null) {
            //处理标题栏的背景颜色
            int customTitleViewBackground = array.getColor(R.styleable.CustomTitleView_title_background, Color.BLUE);
            customTitleView.setBackgroundColor(customTitleViewBackground);
            //处理左边的imageButton
            //处理leftImageButton的资源id
            int leftImageButtonResource = array.getResourceId(R.styleable.CustomTitleView_title_leftButton_resource, -1);
            if (leftImageButtonResource != -1) {
                leftImageButton.setImageResource(leftImageButtonResource);
            }
            //处理leftImageButton的可见性
            int leftImageButtonVisibility = array.getInt(R.styleable.CustomTitleView_title_leftButton_visibility, 0);
            if (leftImageButtonVisibility == 0) {
                leftImageButton.setVisibility(View.VISIBLE);
            } else if (leftImageButtonVisibility == 1) {
                leftImageButton.setVisibility(View.INVISIBLE);
            } else if (leftImageButtonVisibility == 2) {
                leftImageButton.setVisibility(View.GONE);
            }

            //处理左边CircleImageView
            //设置左边CircleImageView的图片资源
            int leftCircleImageViewResource = array.getResourceId(R.styleable.CustomTitleView_title_left_circleImage_resource, -1);
            if (leftCircleImageViewResource != -1) {
                leftCircleImageView.setImageResource(leftCircleImageViewResource);
            }
            //处理左边CircleImageView的可见性
            int leftCircleImageViewVIsibility = array.getInt(R.styleable.CustomTitleView_title_left_circleImage_visibility, 1);
            if (leftCircleImageViewVIsibility == 0) {
                leftCircleImageView.setVisibility(View.VISIBLE);
            } else if (leftCircleImageViewVIsibility == 1) {
                leftCircleImageView.setVisibility(View.INVISIBLE);
            } else if (leftCircleImageViewVIsibility == 2) {
                leftCircleImageView.setVisibility(View.GONE);
            }

            //处理按钮右边的文字
            String leftTextViewText = array.getString(R.styleable.CustomTitleView_title_leftTv_text);
            leftTextView.setText(leftTextViewText);
            //处理按钮右边的文字的颜色
            int leftTextViewTextColor = array.getColor(R.styleable.CustomTitleView_title_leftTv_textColor, Color.WHITE);
            leftTextView.setTextColor(leftTextViewTextColor);
            //处理按钮右边的文字的大小
            float leftTextViewTextSize = array.getDimension(R.styleable.CustomTitleView_title_leftTv_textSize, 20);
            leftTextView.getPaint().setTextSize(leftTextViewTextSize);

            //处理中间的TextView
            //处理中间TextView的文本
            String centerTextViewText = array.getString(R.styleable.CustomTitleView_title_center_textView_text);
            centerTextView.setText(centerTextViewText);
            //处理中间TextView的颜色
            int centerTextViewTextColor = array.getColor(R.styleable.CustomTitleView_title_center_textView_textColor, Color.WHITE);
            centerTextView.setTextColor(centerTextViewTextColor);
            //处理中间TextView的字体大小
            float centerTextViewTextSize = array.getDimension(R.styleable.CustomTitleView_title_center_textView_textSize, 20);
            centerTextView.getPaint().setTextSize(centerTextViewTextSize);
            //处理中间TextView的可见性
            int centerTextViewVisibility = array.getInt(R.styleable.CustomTitleView_title_center_textView_visibility, 0);
            if (centerTextViewVisibility == 0) {
                centerTextView.setVisibility(View.VISIBLE);
            } else if (centerTextViewVisibility == 1) {
                centerTextView.setVisibility(View.INVISIBLE);
            } else if (centerTextViewVisibility == 2) {
                centerTextView.setVisibility(View.GONE);
            }


            //处理右边的imageButton
            //设置rightImageButton的资源id
            int rightImageButtonResource = array.getResourceId(R.styleable.CustomTitleView_title_rightButton_resource, -1);
            if (rightImageButtonResource != -1) {
                rightImageButton.setImageResource(rightImageButtonResource);
            }
            //设置rightImageButton的可见性
            int rightImageButtonVisibility = array.getInt(R.styleable.CustomTitleView_title_rightButton_visibility, 0);
            if (rightImageButtonVisibility == 0) {
                rightImageButton.setVisibility(View.VISIBLE);
            } else if (rightImageButtonVisibility == 1) {
                rightImageButton.setVisibility(View.INVISIBLE);
            } else if (rightImageButtonVisibility == 2) {
                rightImageButton.setVisibility(View.GONE);
            }
            array.recycle();
        }
    }

    /**
     * 设置左边imageButton的点击事件
     *
     * @param onClickListener
     */
    public void setLeftImageButtonListener(OnClickListener onClickListener) {
        leftImageButton.setOnClickListener(onClickListener);
    }

    /**
     * 设置左边图片按钮和textview的点击事件
     * @param onClickListener
     */
    public void setLeftGroupOnClickListener(OnClickListener onClickListener){
        leftTextView.setOnClickListener(onClickListener);
        leftImageButton.setOnClickListener(onClickListener);
    }
    /**
     * 设置左边circleImageView的点击事件
     *
     * @param onClickListener
     */
    public void setLeftCircleImageViewListener(OnClickListener onClickListener) {
        leftCircleImageView.setOnClickListener(onClickListener);
    }

    /**
     * 设置左边图片的资源
     * @param resourse
     */
    public void setLeftImageButtonResourse(int resourse){
        leftImageButton.setImageResource(resourse);
    }

    /**
     * 设置右边按钮的资源id
     * @param resourse
     */
    public void setRightImageButtonResourse(int resourse){
        rightImageButton.setImageResource(resourse);
    }
    /**
     * 设置左边text的可见性
     * @param visiable
     */
    public void setLeftTextViewVisiable(int visiable){
        leftTextView.setVisibility(visiable);
    }

    /**
     * 设置右边imagebutton的可见性
     * @param visiable
     */
    public void setRightImageButtonVisiable(int visiable){
        rightImageButton.setVisibility(visiable);
    }
    /**
     * 设置右边按钮是否选中
     * @param select
     */
    public void setRightImageButtonSelect(boolean select ){
        rightImageButton.setSelected(select);
    }
    /**
     * 设置左边clircleImageView显示的资源id
     *
     * @param resource
     */
    public void setLeftCircleImageViewResource(int resource) {
        leftCircleImageView.setImageResource(resource);
    }

    public ImageButton getRightImageButton() {
        return rightImageButton;
    }

    /**
     * 设置中间TextView的
     *
     * @param text
     */
    public void setCenterTextViewText(String text) {
        centerTextView.setText(text);
    }

    /**
     * 设置textView的文字颜色
     *
     * @param color
     */
    public void setCenterTextViewTextColor(int color) {
        centerTextView.setTextColor(color);
    }

    /**
     * 设置右边按钮的点击事件
     *
     * @param onClickListener
     */
    public void setRightImageButtonListener(OnClickListener onClickListener) {
        rightImageButton.setOnClickListener(onClickListener);
    }

    /**
     * 设置left文字控件显示的文字
     *
     * @param msg
     */
    public void setLeftTextViewText(String msg) {
        leftTextView.setText(msg);
    }
}
