package com.pxxy.lawconsult.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.activitys.AlterNickNameActivity;
import com.pxxy.lawconsult.activitys.ApplyLawyerActivity;
import com.pxxy.lawconsult.activitys.AttentionActivity;
import com.pxxy.lawconsult.activitys.CaseCollectionActivity;
import com.pxxy.lawconsult.activitys.FansActivity;
import com.pxxy.lawconsult.activitys.PublishActivity;
import com.pxxy.lawconsult.activitys.SettingActivity;
import com.pxxy.lawconsult.base.BaseFragment;
import com.pxxy.lawconsult.config.MyApp;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.constant.HttpConstant;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;
import com.pxxy.lawconsult.utils.JsonUtils;
import com.pxxy.lawconsult.utils.SPUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class FragmentMine extends BaseFragment implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {


    private Button bt_setting;
    private Button btn_apply;
    private Button bt_casecollection;
    private Button btn_publish;
    private CircleImageView iv_head;
    private BottomSheetDialog bsd;
    private TextView tv_phone;
    private TextView tv_picture;
    private TextView tv_exit;
    private OkHttpUtil okHttpUtil;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private Uri uri;
    private String tel;
    private String originalPath;
    private boolean isGetData = false;
    private Map<String, String> mapNickName;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                    //显示图片
                    Toast.makeText(getContext(), originalPath, Toast.LENGTH_SHORT).show();
                    Glide.with(FragmentMine.this).load(originalPath).into(iv_head);
                    //将图片保存到本地
                    savePhoto();

                    break;
                case 2:
                    Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getContext(), "服务器繁忙", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    //将图片地址保存在本地
                    String photoUrl = (String) msg.obj;

                    SPUtils.put(AppConstant.SP_AUTOLOGIN_PHOTO, photoUrl, SPUtils.FILE_AUTOLOGIN);
                    break;
                case 6:
                    //网络错误 默认添加数据
                    tv_nickname.setText("昵称");
                    tv_signature.setText("这个家伙很懒，什么都没留下");
                    break;
                case 7:
                    //获取昵称
                    String name = mapNickName.get(HttpConstant.GETNICKNAME_NAME);
                    //获取个性签名
                    String signature = mapNickName.get(HttpConstant.GETNICKNAME_SIGNATURE);
                    tv_nickname.setText(name);
                    tv_signature.setText(signature);
                    break;

            }
        }
    };
    private Button bt_attention;
    private Button bt_fans;
    private Button bt_praise;
    private TextView tv_nickname;
    private TextView tv_signature;
    private BottomSheetDialog bsd2;
    private TextView dialog_tv_nickName;
    private TextView dialog_dialog_tv_exit;
    private int type;

    private void savePhoto() {
        okHttpUtil = new OkHttpUtil();
        okHttpUtil.getUserPhoto(tel, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(4);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String pictureGson = response.body().string();
                Gson gson = new Gson();
                Map<String, String> map = gson.fromJson(pictureGson, new TypeToken<Map<String, String>>() {
                }.getType());

                String picture = map.get(HttpConstant.GETUSERPHOTO);
                if (picture.equals(HttpConstant.GETUSERPHOTO_SUCCESS)) {
                    Message msg = handler.obtainMessage();
                    msg.obj = map.get(HttpConstant.GETUSERPHOTO_DATA);
                    msg.what = 5;
                    handler.sendMessage(msg);
                } else if (picture.equals(HttpConstant.GETUSERPHOTO_FAILURE)) {
                    handler.sendEmptyMessage(2);
                } else if (picture.equals(HttpConstant.GETUSERPHOTO_SERVER)) {
                    handler.sendEmptyMessage(3);
                }
            }
        });
    }

    private void setPhoto() {
        /*//从本地获取电话号码
        tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);*/
        //获取本地照片地址
        String photoUrl = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_PHOTO, "", SPUtils.FILE_AUTOLOGIN);
        //设置头像
        Glide.with(MyApp.getContext())
                .load(photoUrl)
                .placeholder(R.mipmap.lawconsult_icon)//图片加载时图片
                .fallback(R.mipmap.lawconsult_icon)//url为空时的图片
                .into(iv_head);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //获取本地用户类型
        User user = (User) MyApp.getData().get(AppConstant.USER);
        type = user.getType();
        getTakePhoto().onCreate(savedInstanceState);
        //初始化头像
        setPhoto();
        return view;
    }

    private void getNickName() {
        //请求数据
        if (okHttpUtil == null) {
            okHttpUtil = new OkHttpUtil();
        }
        //获取手机号
        String tels = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
        okHttpUtil.getNickName(tels, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(6);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Log.d("mine",jsonData);
                mapNickName = JsonUtils.jsonToMap(jsonData);

                //获取状态
                String s = mapNickName.get(HttpConstant.GETNICKNAME);
                if (s.equals(HttpConstant.GETPICKNAME_SUCCESS)) {
                    handler.sendEmptyMessage(7);
                } else if (s.equals(HttpConstant.GETPICKNAME__FAILURE)) {
                    handler.sendEmptyMessage(6);
                } else if (s.equals(HttpConstant.GETPICKNAME__SERVER)) {
                    handler.sendEmptyMessage(6);
                }
            }
        });
    }

    @Override
    protected int setLayoutID() {
        return R.layout.fragment_mine;
    }

    //点击事件
    @Override
    protected void initEvent() {
        //头像的点击数事件
        iv_head.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        //设置的点击事件
        bt_setting.setOnClickListener(this);
        //案例收藏的点击事件
        bt_casecollection.setOnClickListener(this);
        //发布的点击事件
        btn_publish.setOnClickListener(this);
      /*  //关注
        bt_attention.setOnClickListener(this);
        //粉丝
        bt_fans.setOnClickListener(this);
        //获赞
        bt_praise.setOnClickListener(this);*/
        //拍照
        tv_phone.setOnClickListener(this);
        //相册
        tv_picture.setOnClickListener(this);
        //取消
        tv_exit.setOnClickListener(this);
        //昵称
        tv_nickname.setOnClickListener(this);
        //个性签名
        tv_signature.setOnClickListener(this);
        //修改昵称和个性签名
        dialog_tv_nickName.setOnClickListener(this);
        //退出修改
        dialog_dialog_tv_exit.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        takePhoto = getTakePhoto();
        File file = new File(getContext().getExternalCacheDir(), System.currentTimeMillis() + ".png");
        uri = Uri.fromFile(file);


    }

    //视图初始化
    @Override
    protected void initView(View view) {

        //头像
        iv_head = view.findViewById(R.id.fragment_iv_head);
        //昵称
        tv_nickname = view.findViewById(R.id.fragment_tv_nickname);
        //个性签名
        tv_signature = view.findViewById(R.id.fragment_tv_signature);
        //申请律师界面
        btn_apply = view.findViewById(R.id.fragment_bt_applylawyer);
        //下划线
       // View v_apply = view.findViewById(R.id.fragment_v_apply);
        //设置
        bt_setting = view.findViewById(R.id.fragment_bt_setting);
        //案例收藏
        bt_casecollection = view.findViewById(R.id.fragment_bt_casecollection);
        //发布界面
        btn_publish = view.findViewById(R.id.fragment_btn_publish);
       /* //关注
        bt_attention = view.findViewById(R.id.fragment_bt_attention);
        //粉丝
        bt_fans = view.findViewById(R.id.fragment_bt_fans);
        //获赞
        bt_praise = view.findViewById(R.id.fragment_bt_praise);*/
        //底部弹出状态栏


        View views = View.inflate(getActivity(), R.layout.dialog_bottom_photo, null);
        bsd = new BottomSheetDialog(getActivity());


        tv_phone = views.findViewById(R.id.tv_photo);
        tv_picture = views.findViewById(R.id.tv_picture);
        tv_exit = views.findViewById(R.id.tv_exit);

        bsd.setContentView(views);

        //点击修改昵称和个性签名弹出
        View view2 = View.inflate(getActivity(), R.layout.dialog_bottom_nickname, null);
        bsd2 = new BottomSheetDialog(getActivity());


        dialog_tv_nickName = view2.findViewById(R.id.tv_nickName2);
        dialog_dialog_tv_exit = view2.findViewById(R.id.dialog_tv_exit);
        bsd2.setContentView(view2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_iv_head:
                bsd.show();
                break;
            case R.id.fragment_tv_nickname:
                bsd2.show();
                break;
            case R.id.fragment_tv_signature:
                bsd2.show();
                break;
            case R.id.fragment_bt_casecollection:
                getActivity().startActivity(new Intent(getContext(), CaseCollectionActivity.class));
                break;
            case R.id.fragment_bt_setting:
                getActivity().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.fragment_bt_applylawyer:

                if (type == 0) {
                    startActivity(new Intent(getActivity(), ApplyLawyerActivity.class));
                } else {
                    Toast.makeText(getContext(), "你已经是律师啦！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fragment_btn_publish:
                //将用户类型传过去
                Intent intent1 = new Intent(getActivity(), PublishActivity.class);
                intent1.putExtra("type", type);
                startActivity(intent1);
                break;
           /* case R.id.fragment_bt_attention:
                startActivity(activity(AttentionActivity.class));
                break;
            case R.id.fragment_bt_fans:
                startActivity(activity(FansActivity.class));
                break;
            case R.id.fragment_bt_praise:
                Toast.makeText(getContext(), "您还没有被点赞哦！", Toast.LENGTH_SHORT).show();
                break;*/

            case R.id.tv_photo:
                //拍照并裁剪
                takePhoto.onPickFromCapture(uri);
                bsd.cancel();
                break;
            case R.id.tv_picture:
                //从照片选择不裁剪
                takePhoto.onPickFromGallery();
                bsd.cancel();
                break;
            case R.id.tv_exit:
                bsd.cancel();
                break;
            case R.id.tv_nickName2:
                //将数据带过去
                Intent intent = new Intent(getContext(), AlterNickNameActivity.class);
                //获取昵称和个性签名的文本
                String nickname = tv_nickname.getText().toString().trim();
                String signature = tv_signature.getText().toString().trim();
                intent.putExtra("nickname", nickname);
                intent.putExtra("signature", signature);
                startActivity(intent);
                bsd2.cancel();
                break;
            case R.id.dialog_tv_exit:
                bsd2.cancel();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    public TakePhoto getTakePhoto() {
        //获得TakePhoto实例
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
       /* //设置压缩规则，最大500kb
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);*/
        return takePhoto;

    }

    @Override
    public void takeSuccess(TResult result) {
        //成功取得照片
        originalPath = result.getImage().getOriginalPath();
        int i = originalPath.lastIndexOf("/") + 1;
        //获取照片名
        String fileName = originalPath.substring(i);
        File file = new File(originalPath);
        okHttpUtil = new OkHttpUtil();
        //获取手机号
        tel = (String) SPUtils.get(AppConstant.SP_AUTOLOGIN_TEL, "", SPUtils.FILE_AUTOLOGIN);
        //上传照片
        okHttpUtil.setUserPhoto(tel, file, fileName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                handler.sendEmptyMessage(3);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String photoGson = response.body().string();
                Log.d("photo",photoGson);
                Gson gson = new Gson();
                Map<String, String> map = gson.fromJson(photoGson, new TypeToken<Map<String, String>>() {
                }.getType());

                String isPhoto = map.get(HttpConstant.SETUSERPHOTO);
                switch (isPhoto) {
                    case HttpConstant.SETUSERPHOTO_SUCCESS:
                        handler.sendEmptyMessage(1);
                        break;
                    case HttpConstant.SETUSERPHOTO_FAILURE:
                        handler.sendEmptyMessage(2);
                        break;
                    case HttpConstant.SETUSERPHOTO_SERVER:
                        handler.sendEmptyMessage(3);
                        break;
                }
            }
        });


    }

    @Override
    public void takeFail(TResult result, String msg) {
        Toast.makeText(getContext(), "拍照或获取相册失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
//            GetData();
            //显示昵称和个性签名
            //判断用户是普通用户还是律师
            if (type == 0) {
                //如果是普通用户显示
                getNickName();
            }else {
                tv_nickname.setText("律师");
                tv_signature.setText("专业认证");
                tv_nickname.setClickable(false);
                tv_signature.setClickable(false);
                //隐藏律师申请
                btn_apply.setVisibility(View.GONE);
                //我的发布变成我的消息
                btn_publish.setText("我的消息");
            }
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }


}
