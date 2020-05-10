package com.pxxy.lawconsult.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.entity.Case;

import java.util.List;

public class CaseAdapter extends BaseAdapter {

    private List<Case> caseList;
    private Context context;

    public CaseAdapter(Context context, List<Case> caseList) {
        this.caseList = caseList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return caseList.size();
    }

    @Override
    public Object getItem(int position) {
        return caseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_case_recyclerview, null);
        }
        Glide.with(context)
                .load(caseList.get(position).getImage())
                .error(R.drawable.loading)
                .into((ImageView) convertView.findViewById(R.id.adapter_case_recyclerview_image));
        TextView adapter_title = convertView.findViewById(R.id.adapter_case_recyclerview_title);
        TextView adapter_type = convertView.findViewById(R.id.adapter_case_recyclerview_type);

        adapter_title.setText(caseList.get(position).getTitle());
        adapter_type.setText(caseList.get(position).getType());
        return convertView;
    }
}
