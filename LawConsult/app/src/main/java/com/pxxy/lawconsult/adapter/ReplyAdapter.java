package com.pxxy.lawconsult.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.entity.ReplyLawyerData;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends BaseAdapter {
    private Context context;
    private List<ReplyLawyerData> list;
    public ReplyAdapter(Context context, List<ReplyLawyerData> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_activity_reply, null);
            viewHolder=new ViewHolder();
            viewHolder.lawyerAvatar=convertView.findViewById(R.id.adapter_reply_lawyer_avatar);
            viewHolder.lawyerName=convertView.findViewById(R.id.adapter_reply_lawyer_name);
            viewHolder.lawyerAddress=convertView.findViewById(R.id.adapter_reply_lawyer_workaddress);
            viewHolder.replyContent=convertView.findViewById(R.id.adapter_reply_content);
            viewHolder.replyDate=convertView.findViewById(R.id.adapter_reply_date);
           //通过setTag将ViewHolder与convertView绑定
           convertView.setTag(viewHolder);
       }else{
           //通过调用缓冲视图convertView，然后就可以调用到viewHolder,viewHolder中已经绑定了各个控件，省去了findViewById的步骤
           viewHolder= (ViewHolder) convertView.getTag();
       }
        Glide.with(context)
                .load(list.get(position)
                .getLAWYERAVATAR())
                .error(R.mipmap.lawconsult_icon)
                .placeholder(R.mipmap.lawconsult_icon)
                .into(viewHolder.lawyerAvatar);
       viewHolder.lawyerName.setText(list.get(position).getLAWYERNAME()+"律师");
       viewHolder.lawyerAddress.setText(list.get(position).getLAWYEROFFICE());
       viewHolder.replyDate.setText(list.get(position).getREPLYDATE());
       viewHolder.replyContent.setText(list.get(position).getREPLYCONTENT());
        return convertView;
    }
    class ViewHolder{
        TextView lawyerName;    //律师名字
        TextView lawyerAddress; //哪个事务所
        CircleImageView lawyerAvatar;   //律师头像
        TextView replyDate;     //回复的时间
        TextView replyContent;  //回复的内容
    }
}
