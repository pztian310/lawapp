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
import com.pxxy.lawconsult.entity.Statute;

import java.util.List;

public class AdminStatuteRecyclerAdapter extends RecyclerView.Adapter<AdminStatuteRecyclerAdapter.MyHolder> {
    private Context context;
    private List<Statute> statuteList;
    private itemOnClickListener itemOnClickListener;
    private deleteButtonOnClickListener deleteButtonOnClickListener;
    private updateButtonOnClickListener updateButtonOnClickListener;

    public void setItemOnClickListener(AdminStatuteRecyclerAdapter.itemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public void setDeleteButtonOnClickListener(AdminStatuteRecyclerAdapter.deleteButtonOnClickListener deleteButtonOnClickListener) {
        this.deleteButtonOnClickListener = deleteButtonOnClickListener;
    }

    public void setUpdateButtonOnClickListener(AdminStatuteRecyclerAdapter.updateButtonOnClickListener updateButtonOnClickListener) {
        this.updateButtonOnClickListener = updateButtonOnClickListener;
    }

    public AdminStatuteRecyclerAdapter(Context context, List<Statute> statuteList) {
        this.context = context;
        this.statuteList = statuteList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_admin_statute_recycler, viewGroup, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String title = statuteList.get(i).getLaw_content();
        myHolder.statuteTitle.setText(title.substring(0,title.indexOf("条")+1));
        myHolder.statuteTime.setText(statuteList.get(i).getOpendate());
        //设置按钮的text
        myHolder.bt_statuteUpdate.setText(AppConstant.ADMIN_SHOW_CHILD_STATUTE_UPDATE_BT);
        myHolder.bt_deleteStatute.setText(AppConstant.ADMIN_SHOW_CHILD_STATUTE_DELETE_BT);
    }

    @Override
    public int getItemCount() {
        return statuteList.size();
    }

    /**
     * 视图缓存类
     */
    class MyHolder extends RecyclerView.ViewHolder {
        private RelativeLayout statuteLayout;
        private ImageView statutePhoto;
        private TextView statuteTitle, statuteTime;
        private Button bt_deleteStatute, bt_statuteUpdate;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);
            //获取控件id
            statuteLayout = itemView.findViewById(R.id.adapter_admin_show_statute_layout);
            statutePhoto = itemView.findViewById(R.id.adapter_admin_show_statute_photo);
            statuteTitle = itemView.findViewById(R.id.adapter_admin_show_statute_title);
            statuteTime = itemView.findViewById(R.id.adapter_admin_show_statute_time);
            bt_deleteStatute = itemView.findViewById(R.id.adapter_admin_show_statute_delete);
            bt_statuteUpdate = itemView.findViewById(R.id.adapter_admin_show_statute_update);

            //设置item的点击事件
            statuteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClickListener != null){
                        itemOnClickListener.setItemOnClickListener(v,statuteList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });

            //设置删除按钮的点击事件
            bt_deleteStatute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteButtonOnClickListener !=null){
                        deleteButtonOnClickListener.setDeleteButtonOnClickListener(v,statuteList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });

            //设置修改按钮的点击事件
            bt_statuteUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updateButtonOnClickListener != null){
                        updateButtonOnClickListener.setUpdateButtonOnClickListener(v,statuteList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });
        }
    }

    /**
     * 设置item和案例详情的点击事件接口
     */
    public interface itemOnClickListener {
        void setItemOnClickListener(View view, Statute statute, int position);
    }

    /**
     * 设置修改案例按钮的点击事件接口
     */
    public interface updateButtonOnClickListener {
        void setUpdateButtonOnClickListener(View view, Statute statute, int position);
    }

    /**
     * 设置删除案例按钮的点击事件接口
     */
    public interface deleteButtonOnClickListener {
        void setDeleteButtonOnClickListener(View view, Statute statute, int position);
    }
}
