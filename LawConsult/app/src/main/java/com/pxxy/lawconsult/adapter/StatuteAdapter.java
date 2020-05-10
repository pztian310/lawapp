package com.pxxy.lawconsult.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.entity.Statute;

import java.util.List;

public class StatuteAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private int mLayoutId;
    private List<Statute> mPlanetList;
    public StatuteAdapter(Context context, int layout_id, List<Statute> statuteList){
        mInflater=LayoutInflater.from(context);
        mContext=context;
        mLayoutId=layout_id;
        mPlanetList=statuteList;
    }
    @Override
    public int getCount() {
        return mPlanetList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlanetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //实例化内部类
        ViewHolder holder=null;
        if (convertView==null){//如果从来没有初始化过view，即缓存中都没有加载过
            holder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.adapter_statute,null);
            //把convertView中的控件保存到viewHolder中
            holder.tv_law_id=convertView.findViewById(R.id.tv_law_id);
            holder.tv_law_title=convertView.findViewById(R.id.tv_law_title);
            holder.tv_law_opendate=convertView.findViewById(R.id.tv_law_opendata);
            holder.tv_law_type=convertView.findViewById(R.id.tv_law_type);
            holder.tv_law_content=convertView.findViewById(R.id.tv_law_content);
            //通过setTag将ViewHolder与convertView绑定
            convertView.setTag(holder);
        }else{
            //通过调用缓冲视图convertView，然后就可以调用到viewHolder,viewHolder中已经绑定了各个控件，省去了findViewById的步骤
            holder= (ViewHolder) convertView.getTag();
        }
        //设置数据
            if(mPlanetList!=null) {
                Statute statute = mPlanetList.get(position);
                holder.tv_law_id.setText(String.valueOf(statute.getLaw_id()));
                holder.tv_law_title.setText(statute.getLaw_title());
                holder.tv_law_content.setText(statute.getLaw_content());
                holder.tv_law_type.setText("类型："+statute.getLaw_type());
                holder.tv_law_opendate.setText(statute.getOpendate());
            }
        return convertView;
    }
    //在外面先定义，ViewHolder静态类
    //后面就可以通过设置成员变量设置布局控件内容，不再用findViewById了。
    public final class ViewHolder{
        private TextView tv_law_id;
        private TextView tv_law_title;
        private TextView tv_law_content;
        private TextView tv_law_opendate;
        private TextView tv_law_type;
    }
}
