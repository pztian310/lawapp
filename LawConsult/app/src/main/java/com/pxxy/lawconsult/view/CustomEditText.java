package com.pxxy.lawconsult.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pxxy.lawconsult.R;

public class CustomEditText extends LinearLayout {
    //左边的文字提示
    private TextView leftTextTip;
    //中间的输入框
    private EditText centerEditText;
    //右边清除按钮和查看密码按钮
    private ImageView rightClear, rightPassword;
    //输入框下方的横线
    private View buttomLine;
    //存放输入类型,0为phone输入,1为密码输入,2为文本输入,3为数字输入
    private int inputType;
    private EditTextWatcher editTextWatcher;
    //记录是否显示clear按钮和password按钮,-1为默认，0为可见，1为不可见，2位不可见也不占位置,clear默认显示,password默认不显示
    private int clearVisibility, passwordVisibility;

    public CustomEditText(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.custom_edittext, this, true);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_edittext, this, true);
        //加载控件
        initView();
        //加载配置
        initConfig(context, attrs);
        //设置事件
        initEvent();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.custom_edittext, this, true);
    }

    /**
     * 加载控件
     */
    private void initView() {
        leftTextTip = findViewById(R.id.custom_edittext_left_tip);
        centerEditText = findViewById(R.id.custom_edittext_center_edittext);
        rightClear = findViewById(R.id.custom_edittext_right_clear);
        rightPassword = findViewById(R.id.custom_edittext_issee_password);
        buttomLine = findViewById(R.id.custom_edittext_buttom_line);
    }

    /**
     * 加载配置
     *
     * @param context
     * @param attrs
     */
    private void initConfig(Context context, AttributeSet attrs) {
        //获取TypedArray
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        //判断array是否为空
        if (array != null) {
            //处理文字提示
            String leftTextTipText = array.getString(R.styleable.CustomEditText_editText_leftTipText);
            leftTextTip.setText(leftTextTipText);
            //处理文字提示的颜色
            int leftTextTipTextColor = array.getColor(R.styleable.CustomEditText_editText_leftTipTextColor, Color.BLACK);
            leftTextTip.setTextColor(leftTextTipTextColor);
            //处理文字提示的文字大小
            float leftTextTipTextSize = array.getDimension(R.styleable.CustomEditText_editText_leftTipTextSize, 20);
            if (leftTextTipTextSize != -1) {
                leftTextTip.getPaint().setTextSize(leftTextTipTextSize);
            }

            //处理中间EditText的hint提示
            String centerEditTextHintText = array.getString(R.styleable.CustomEditText_editText_centerEditTextHint);
            centerEditText.setHint(centerEditTextHintText);
            //设置中间EditText的hint文字颜色
            int centerEditTextHintTextColor = array.getColor(R.styleable.CustomEditText_editText_centerEditTextHintColor, -1);
            if (centerEditTextHintTextColor != -1) {
                centerEditText.setHintTextColor(centerEditTextHintTextColor);
            }

            //设置清除按钮的可见性
            int rightClearVisibility = array.getInt(R.styleable.CustomEditText_editText_rightClearVisibility, -1);
            if (rightClearVisibility == 0) {
                //记录用户定义celar的可见性
                clearVisibility = 0;
                //设置可见时就隐藏clear按钮，使应用刚进入时不出现clear按钮
                rightClear.setVisibility(View.INVISIBLE);
            } else if (rightClearVisibility == 1) {
                clearVisibility = 1;
                rightClear.setVisibility(View.INVISIBLE);
            } else if (rightClearVisibility == 2) {
                clearVisibility = 2;
                rightClear.setVisibility(View.GONE);
            } else if (rightClearVisibility == -1) {
                clearVisibility = -1;
                //默认会显示clear按钮,设置为INVISIBLE让应用刚进入时不显示clear按钮
                rightClear.setVisibility(View.INVISIBLE);
            }

            //设置显示密码按钮的可见性
            int rightPasswordVisibility = array.getInt(R.styleable.CustomEditText_editText_rightPasswordVisibility, -1);
            if (rightPasswordVisibility == 0) {
                //记录用户定义的password的可见性
                passwordVisibility = 0;
                //设置可见时就隐藏password按钮，使应用刚进入时不出现password按钮
                rightPassword.setVisibility(View.INVISIBLE);
            } else if (rightPasswordVisibility == 1) {
                passwordVisibility = 1;
                rightPassword.setVisibility(View.INVISIBLE);
            } else if (rightPasswordVisibility == 2) {
                passwordVisibility = 2;
                rightPassword.setVisibility(View.GONE);
            } else if (rightPasswordVisibility == -1) {
                passwordVisibility = -1;
                //默认不会显示密码按钮
                rightPassword.setVisibility(View.GONE);
            }

            //获取输入类型
            int editTextInputType = array.getInt(R.styleable.CustomEditText_editText_inputType, 0);
            if (editTextInputType == 0) {
                //phone
                inputType = 0;
                centerEditText.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_TEXT);
            } else if (editTextInputType == 1) {
                //密码类型,隐藏内容
                inputType = 1;
                //如果没有设置显示密码的可见性，默认显示可见
                if (passwordVisibility == -1) {
                    passwordVisibility = 0;
                }
                rightPassword.setVisibility(View.INVISIBLE);
                centerEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else if (editTextInputType == 2) {
                //text类型
                inputType = 2;
                centerEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (editTextInputType == 3) {
                //number类型
                inputType = 3;
                centerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            //设置输入栏底部横线的颜色
            int buttomLineColor = array.getColor(R.styleable.CustomEditText_editText_buttomLineColor, -1);
            if (buttomLineColor != -1) {
                buttomLine.setBackgroundColor(buttomLineColor);
            }

            array.recycle();
        }
    }

    /**
     * 设置监听事件
     */
    private void initEvent() {
        //设置rightclear的点击事件
        setRightClearListener(null);
        //设置密码查看事件
        setSeePasswordListener(null);

        //设置centerEditText获取焦点监听事件
        centerEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //获取输入框中是否有内容
                String editTextContent = centerEditText.getText().toString();
                //判断输入框中是什么inputType
                if (inputType == 1) {
                    //密码类型
                    if (hasFocus && editTextContent.length() > 0) {
                        if (clearVisibility == 0 || clearVisibility == -1) {
                            //如果未指定clear可见性和制定了clear可见性为可见时
                            rightClear.setVisibility(View.VISIBLE);
                        }
                        if (passwordVisibility == 0) {
                            //指定了password的可见性
                            rightPassword.setVisibility(View.VISIBLE);
                        }
                    } else if (hasFocus) {
                        if (passwordVisibility == 0) {
                            //指定了password的可见性
                            rightPassword.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (clearVisibility == 0 || clearVisibility == -1) {
                            //如果未指定clear可见性和制定了clear可见性为可见时
                            rightClear.setVisibility(View.INVISIBLE);
                        } else {
                            rightClear.setVisibility(View.GONE);
                        }
                        if (passwordVisibility == 0) {
                            rightPassword.setVisibility(View.INVISIBLE);
                        } else {
                            rightPassword.setVisibility(View.GONE);
                        }
                    }
                } else {
                    //非密码类型
                    if (hasFocus && editTextContent.length() > 0) {
                        if (clearVisibility == -1 || clearVisibility == 0) {
                            rightClear.setVisibility(View.VISIBLE);
                        } else {
                            rightClear.setVisibility(View.GONE);
                        }
                    } else {
                        if (clearVisibility == -1 || clearVisibility == 0) {
                            rightClear.setVisibility(View.INVISIBLE);
                        } else {
                            rightClear.setVisibility(View.GONE);
                        }
                    }
                }

            }
        });


        //创建EditTextWatcher
        editTextWatcher = new EditTextWatcher();
        //设置EditText文本内容变化监听事件
        centerEditText.addTextChangedListener(editTextWatcher);

    }

    /**
     * 设置光标位置
     * @param selection
     */
    public void setSelection(int selection){
        centerEditText.setSelection(selection);
    }
    /**
     * 设置密码查看事件
     */
    public void setSeePasswordListener(final clearEditText clearEditText){
        //设置查看按钮的点击事件
        rightPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightPassword.isSelected()) {
                    //如果密码为显示，设置密码不显示
                    rightPassword.setSelected(false);
                    //设置密码不显示
                    centerEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    //设置光标在末尾
                    centerEditText.setSelection(centerEditText.getText().toString().length());
                } else {
                    //密码不显示，设置密码显示
                    rightPassword.setSelected(true);
                    //添加代码
                    if (clearEditText != null){
                        clearEditText.clearEditTextListener();
                    }
                    //设置密码显示
                    centerEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //设置光标在末尾
                    centerEditText.setSelection(centerEditText.getText().toString().length());
                }
            }
        });
    }

    /**
     * 清除文本
     */
    public void clear(){
        centerEditText.setText(null);
    }

    /**
     * 添加EditText文本内容变化的监听事件
     *
     * @param customTextWatcher
     */
    public void addCenterEditTextChangedListener(final CustomTextWatcher customTextWatcher) {
        //注销之前的内容监听事件
        centerEditText.removeTextChangedListener(editTextWatcher);
        //重新设置centerEditText的内容监听事件
        centerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                customTextWatcher.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customTextWatcher.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //内容变化之后
                String text = s + "";
                if (text.length() > 0 && centerEditText.isFocused()) {
                    if (clearVisibility == -1 || clearVisibility == 0) {
                        rightClear.setVisibility(VISIBLE);
                    }
                    customTextWatcher.afterTextChanged(s);
                } else {
                    if (clearVisibility == -1 || clearVisibility == 0) {
                        rightClear.setVisibility(View.INVISIBLE);
                    } else {
                        rightClear.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    /**
     * 获取文本输入框输入的内容
     * @return
     */
    public String getEditTextContent(){
        return centerEditText.getText().toString().trim();
    }

    public void setRightClearListener(final clearButtonListener clearListener){
        //设置clearImageView的点击事件
        rightClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除输入框中的内容
                centerEditText.setText(null);
                if (clearListener != null){
                    clearListener.clearButtonListener();
                }

            }
        });
    }

    /**
     * 设置文本输入框中的内容
     * @param text 要显示的内容
     */
    public void setEditTextContent(String text){
        centerEditText.setText(text);
    }
    //editText的内容监听事件
    class EditTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //内容变化之后
            String text = s + "";
            if (text.length() > 0 && centerEditText.isFocused()) {
                if (clearVisibility == -1 || clearVisibility == 0) {
                    rightClear.setVisibility(VISIBLE);
                }
            } else {
                if (clearVisibility == -1 || clearVisibility == 0) {
                    rightClear.setVisibility(View.INVISIBLE);
                } else {
                    rightClear.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 设置文本变化事件
     */
    public interface CustomTextWatcher {
        void beforeTextChanged(CharSequence s, int start, int count, int after);
        void onTextChanged(CharSequence s, int start, int before, int count);
        void afterTextChanged(Editable s);
    }

    /**
     * 设置输入框中数据发生变化的接口
     */
    public interface clearEditText{
        void clearEditTextListener();
    }

    /**
     * 设置一键清除按钮的点击接口
     */
    public interface clearButtonListener{
        void clearButtonListener();
    }
}


