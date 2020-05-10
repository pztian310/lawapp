package com.pxxy.lawconsult.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.base.BaseActivity;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.dao.CaseDao;
import com.pxxy.lawconsult.db.AppSqlite;
import com.pxxy.lawconsult.fragment.FragmentConsult;
import com.pxxy.lawconsult.fragment.FragmentHome;
import com.pxxy.lawconsult.fragment.FragmentLawyer;
import com.pxxy.lawconsult.fragment.FragmentMine;

public class HomeActivity extends BaseActivity {
    private boolean isExit = false;
    private RadioGroup homeRadioGroup;
    private FragmentManager homeManager;
    private ExitBroadcast exitBroadcast;
    Fragment fragmentHome, fragmentLawyer, fragmentConsult, fragmentMine;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    isExit = false;
                    break;
            }
        }
    };
    private AppSqlite sqlite;
    private SQLiteDatabase db;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(exitBroadcast);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载配置
        initConfig();
        //注册广播
        exitBroadcast = new ExitBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstant.EXIT_APP);
        registerReceiver(exitBroadcast,intentFilter);
    }

    /**
     * 加载配置
     */
    private void initConfig() {
        //默认选定第一个radiobutton
        fragmentHome = new FragmentHome();
        //获取布局管理器
        FragmentTransaction fTransaction = homeManager.beginTransaction();
        fTransaction.add(R.id.home_top_framelayout, fragmentHome);
        //提交
        fTransaction.commit();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        homeRadioGroup = findViewById(R.id.home_rg_radiogroup);
        homeManager = getSupportFragmentManager();
        //创建本地数据库
        sqlite = AppSqlite.getInstance(this);
        db = sqlite.getReadableDatabase();
    }

    @Override
    public void initData() {

    }


    @Override
    public void initEvent() {
        homeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = homeManager.beginTransaction();
                //隐藏所有的fragment
                hideAllFragment(transaction);
                switch (checkedId) {
                    case R.id.home_rb_home:
                        if (fragmentHome == null) {
                            fragmentHome = new FragmentHome();
                            transaction.add(R.id.home_top_framelayout, fragmentHome);
                        } else {
                            transaction.show(fragmentHome);
                        }
                        break;
                    case R.id.home_rb_lawyer:
                        if (fragmentLawyer == null) {
                            fragmentLawyer = new FragmentLawyer();
                            transaction.add(R.id.home_top_framelayout, fragmentLawyer);
                        } else {
                            transaction.show(fragmentLawyer);
                        }
                        break;
                    case R.id.home_rb_consult:
                        if (fragmentConsult == null) {
                            fragmentConsult = new FragmentConsult();
                            transaction.add(R.id.home_top_framelayout, fragmentConsult);
                        } else {
                            transaction.show(fragmentConsult);
                        }
                        break;
                    case R.id.home_rb_mine:
                        if (fragmentMine == null) {
                            fragmentMine = new FragmentMine();
                            transaction.add(R.id.home_top_framelayout, fragmentMine);
                        } else {
                            transaction.show(fragmentMine);
                        }
                        break;
                }
                transaction.commit();
            }
        });
    }

    /**
     * 隐藏所有的fragment
     *
     * @param fragmentTransaction
     */
    public void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fragmentHome != null) {
            fragmentTransaction.hide(fragmentHome);
        }

        if (fragmentLawyer != null) {
            fragmentTransaction.hide(fragmentLawyer);
        }

        if (fragmentConsult != null) {
            fragmentTransaction.hide(fragmentConsult);
        }

        if (fragmentMine != null) {
            fragmentTransaction.hide(fragmentMine);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按返回键不销毁activity
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (!isExit){
                isExit = true;
                toastShort(AppConstant.HOME_ACTIVITY_BACKTOAST);
                handler.sendEmptyMessageDelayed(1,2000);
            }else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        //重新让新的Fragment指向了原本未被销毁的fragment
        if (fragmentHome == null && fragment instanceof FragmentHome){
            fragmentHome =fragment;
        }
        if (fragmentLawyer == null && fragment instanceof FragmentLawyer){
            fragmentLawyer =fragment;
        }
        if (fragmentConsult == null && fragment instanceof FragmentConsult){
            fragmentConsult =fragment;
        }
        if (fragmentMine == null && fragment instanceof FragmentMine){
            fragmentMine =fragment;
        }
    }

    /**
     * 关闭activity
     */
    public void closeActivity(){
        this.finish();
    }

    class ExitBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstant.EXIT_APP)){
                closeActivity();
            }
        }
    }

}
