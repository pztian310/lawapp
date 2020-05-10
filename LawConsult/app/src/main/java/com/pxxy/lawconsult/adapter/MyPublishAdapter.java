package com.pxxy.lawconsult.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.constant.DBConstant;

import java.util.List;
import java.util.Map;

public class MyPublishAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> list;
    private ViewHolder viewHolder;

    public MyPublishAdapter(Context context, List<Map<String,String>> list){
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
        if (convertView==null){
             convertView = LayoutInflater.from(context).inflate(R.layout.adapter_my_publish, null);
            viewHolder = new ViewHolder();
            viewHolder.date=convertView.findViewById(R.id.adapter_mypublish_date);
            viewHolder.publishcontent=convertView.findViewById(R.id.adapter_mypublish_content);
            viewHolder.type = convertView.findViewById(R.id.adapter_mypublish_type);
            //通过setTag将ViewHolder与convertView绑定
            convertView.setTag(viewHolder);
        }else{
            //通过调用缓冲视图convertView，然后就可以调用到viewHolder,viewHolder中已经绑定了各个控件，省去了findViewById的步骤
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.date.setText(list.get(position).get(DBConstant.POSTTIE_DATE));
        viewHolder.publishcontent.setText(list.get(position).get(DBConstant.POSTTIE_CONTENT));
        viewHolder.type.setText("发布类型:"+list.get(position).get(DBConstant.POSTTIE_T_TYPE));
        return convertView;
    }
    class ViewHolder{
        TextView publishcontent;    //发布内容
        TextView type;               //发布类型
        TextView date;                //发布时间

    }
}
