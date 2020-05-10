package com.pxxy.lawconsult.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.User;
import com.pxxy.lawconsult.okhttp3.OkHttpUtil;

import java.util.List;
import java.util.Map;

public class LawyerAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private int mLayoutId;
    private List<LawerUser> mPlanetList;
    public LawyerAdapter(Context context, int layout_id, List<LawerUser> lawyerList){
        mInflater=LayoutInflater.from(context);
        mContext=context;
        mLayoutId=layout_id;
        mPlanetList=lawyerList;
    }

    //需要返回的次数
    @Override
    public int getCount() {
        return mPlanetList.size();
    }
    //根据ListView所在位置返回View
    @Override
    public Object getItem(int position) {
        return mPlanetList.get(position);
    }
    //根据ListView位置得到数据源集合中的Id
    @Override
    public long getItemId(int position) {
        return position;
    }
    //重写adapter最重要的就是重写此方法，此方法也是决定listview界面的样式的
    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //实例化内部类
        ViewHolder holder=null;
        if(convertView==null){//如果从来没有初始化过view，即缓存中都没有加载过
        holder=new ViewHolder();
        convertView=mInflater.inflate(R.layout.adapter_lawyer,null);
            //把convertView中的控件保存到viewHolder中
        holder.textView_lawerName=convertView.findViewById(R.id.adapter_lawyername);
        holder.textView_lawyercase1=convertView.findViewById(R.id.adapter_lawyercase1);
        holder.imageView_lawyer=convertView.findViewById(R.id.adapter_lawyerpicture);
        holder.textView_lawyeroffice=convertView.findViewById(R.id.adapter_lawyeroffice);
        holder.textView_lawerAge=convertView.findViewById(R.id.adapter_lawyeryear);
        holder.textView_lawyerposition=convertView.findViewById(R.id.adapter_lawyerposition);
            //通过setTag将ViewHolder与convertView绑定
        convertView.setTag(holder);
        }else{
            //通过调用缓冲视图convertView，然后就可以调用到viewHolder,viewHolder中已经绑定了各个控件，省去了findViewById的步骤
            holder= (ViewHolder) convertView.getTag();
        }
        LawerUser lawerUser=mPlanetList.get(position);
        holder.textView_lawerName.setText(lawerUser.getLawerName());
        holder.textView_lawyeroffice.setText(lawerUser.getOffice());
        holder.textView_lawerAge.setText("年龄："+lawerUser.getLawerAge()+"岁");
        holder.textView_lawyerposition.setText(lawerUser.getAddress());
        holder.textView_lawyercase1.setText(lawerUser.getSpeciality());
//        holder.imageView_lawyer.setImageResource(R.drawable.head_photo);
        Glide.with(mContext).load(lawerUser.getImage())
                .error(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .into(holder.imageView_lawyer);
        return convertView;

    }

    //在外面先定义，ViewHolder静态类
    //后面就可以通过设置成员变量设置布局控件内容，不再用findViewById了。
    public final class ViewHolder{
    private LinearLayout linearLayout;
    private ImageView imageView_lawyer;
    private TextView textView_lawerName;
    private TextView textView_lawyeroffice;
    private TextView textView_lawyercase1;
    private TextView textView_lawyerposition;
    private TextView textView_lawerAge;
    }
}
