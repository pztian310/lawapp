package com.pxxy.lawconsult.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.entity.PosttieData;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultEssayAdapter extends BaseAdapter {
private Context context;
private List <PosttieData>list;
public ConsultEssayAdapter(Context context,List <PosttieData>list){
    this.context=context;
    this.list=list;
}
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
           convertView = LayoutInflater.from(context).inflate(R.layout.adapter_activity_consultessay, null);
            //获取ViewHolder实例
          viewHolder = new ViewHolder();
          viewHolder.consult_essay_photo_avatar=convertView.findViewById(R.id.consult_essay_photo_avatar);
          viewHolder.consult_essay_tv_name=convertView.findViewById(R.id.consult_essay_tv_name);
          viewHolder.consult_essay_tv_consultcontent=convertView.findViewById(R.id.consult_essay_tv_consultcontent);
          viewHolder.consult_essay_tv_date=convertView.findViewById(R.id.consult_essay_tv_date);
          viewHolder.consult_essay_tv_reply=convertView.findViewById(R.id.consult_essay_tv_reply);
          //通过setTag将ViewHolder与convertView绑定
            convertView.setTag(viewHolder);
        }else{
            //通过调用缓冲视图convertView，然后就可以调用到viewHolder,viewHolder中已经绑定了各个控件，省去了findViewById的步骤
            viewHolder= (ViewHolder) convertView.getTag();
        }
    //   viewHolder.consult_essay_photo_avatar.setImageResource();
        Glide.with(context).load(list.get(position).getUserAvatar()).error(R.mipmap.lawconsult_icon).placeholder(R.mipmap.lawconsult_icon).into(viewHolder.consult_essay_photo_avatar);
        viewHolder.consult_essay_tv_consultcontent.setText(list.get(position).getContent());
        viewHolder.consult_essay_tv_date.setText(list.get(position).getDate());
        if(list.get(position).getNickName().length()>10){
            //显示后4位电话号码
            viewHolder.consult_essay_tv_name.setText("****"+list.get(position).getNickName().substring(7));
        }else{
            viewHolder.consult_essay_tv_name.setText(list.get(position).getNickName());
        }
        return convertView;
    }
    private class ViewHolder{
   //获取控件id
    CircleImageView consult_essay_photo_avatar;   //头像
        TextView consult_essay_tv_name;   //用户名
        TextView consult_essay_tv_reply;    // 回复
        TextView consult_essay_tv_consultcontent;    //咨询内容
        TextView consult_essay_tv_date; //发布时间
    }
}
