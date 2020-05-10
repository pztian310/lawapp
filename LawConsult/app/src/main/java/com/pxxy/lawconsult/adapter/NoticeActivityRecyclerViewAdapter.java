package com.pxxy.lawconsult.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.Notice;
import com.pxxy.lawconsult.entity.Statute;

import java.util.List;

public class NoticeActivityRecyclerViewAdapter extends RecyclerView.Adapter<NoticeActivityRecyclerViewAdapter.MyHolder> {
    private Context context;
    private List<Notice> noticeList;
    private itemOnClickListener itemOnClickListener;

    public void setItemOnClickListener(NoticeActivityRecyclerViewAdapter.itemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }


    public NoticeActivityRecyclerViewAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_notice_activity_recycler, viewGroup, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.noticeTitle.setText(noticeList.get(i).getTitle());
        myHolder.noticeContent.setText(noticeList.get(i).getContent());
        myHolder.noticeTime.setText(noticeList.get(i).getTime());
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    /**
     * 视图缓存类
     */
    class MyHolder extends RecyclerView.ViewHolder {
        private RelativeLayout noticeLayout;
        private TextView noticeTitle, noticeContent,noticeTime;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);
            //获取控件id
            noticeLayout = itemView.findViewById(R.id.adapter_notice_activity_layout);
            noticeTitle = itemView.findViewById(R.id.adapter_notice_activity_title);
            noticeContent = itemView.findViewById(R.id.adapter_notice_activity_content);
            noticeTime = itemView.findViewById(R.id.adapter_notice_activity_time);

            //设置点击事件
            noticeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClickListener != null){
                        itemOnClickListener.setItemOnClickListener(v,noticeList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });



        }
    }

    /**
     * 设置item和案例详情的点击事件接口
     */
    public interface itemOnClickListener {
        void setItemOnClickListener(View view, Notice notice, int position);
    }

}
