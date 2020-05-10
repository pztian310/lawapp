package com.pxxy.lawconsult.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.activitys.ConsultActivity;
import com.pxxy.lawconsult.activitys.ConsultEssayActivity;
import com.pxxy.lawconsult.base.BaseFragment;
import com.pxxy.lawconsult.view.CustomTitleView;


public class FragmentConsult extends BaseFragment implements View.OnClickListener {

    private Button bt_hy;           //婚姻家庭
    private Button bt_fc;           //房产纠纷
    private Button bt_gs;           //工伤赔偿
    private Button bt_jt;           //交通事故
    private Button bt_mj;           //民间借贷
    private Button bt_ld;           //劳动纠纷
    private Button bt_xs;           //刑事诉讼
    private Button bt_zs;           //知识产权
    private CustomTitleView title;

    @Override
    protected int setLayoutID() {
        return R.layout.fragment_consult;
    }

    @Override
    protected void initEvent() {

        title.setRightImageButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(activity(ConsultActivity.class));
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initView(View view) {
        //获取actionbar
        title = view.findViewById(R.id.fragment_consult_title);
        //获取功能模块按钮
        bt_hy = view.findViewById(R.id.fragment_consult_bt_hy);
        bt_fc = view.findViewById(R.id.fragment_consult_bt_fc);
        bt_gs = view.findViewById(R.id.fragment_consult_bt_gs);
        bt_jt = view.findViewById(R.id.fragment_consult_bt_jt);
        bt_ld = view.findViewById(R.id.fragment_consult_bt_ld);
        bt_xs = view.findViewById(R.id.fragment_consult_bt_xs);
        bt_zs = view.findViewById(R.id.fragment_consult_bt_zs);
        bt_mj = view.findViewById(R.id.fragment_consult_bt_mj);
        //控件注册监听
        bt_hy.setOnClickListener(this);
        bt_fc.setOnClickListener(this);
        bt_gs.setOnClickListener(this);
        bt_jt.setOnClickListener(this);
        bt_ld.setOnClickListener(this);
        bt_xs.setOnClickListener(this);
        bt_zs.setOnClickListener(this);
        bt_mj.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.fragment_consult_bt_hy:
            startIntent("婚姻家庭");
            break;
        case R.id.fragment_consult_bt_fc:
            startIntent("房产纠纷");
            break;
        case R.id.fragment_consult_bt_gs:
            startIntent("工伤赔偿");
            break;
        case R.id.fragment_consult_bt_jt:
            startIntent("交通事故");
            break;
        case R.id.fragment_consult_bt_mj:
            startIntent("民间借贷");
            break;
        case R.id.fragment_consult_bt_ld:
            startIntent("劳动纠纷");
            break;
        case R.id.fragment_consult_bt_xs:
            startIntent("刑事诉讼");
            break;
        case R.id.fragment_consult_bt_zs:
            startIntent("知识产权");
            break;
            default:
                break;
    }
    }

    private void startIntent(String string) {
        Intent intent = new Intent(getActivity(), ConsultEssayActivity.class);
        intent.putExtra("titleName",string);
        startActivity(intent);
    }
}
