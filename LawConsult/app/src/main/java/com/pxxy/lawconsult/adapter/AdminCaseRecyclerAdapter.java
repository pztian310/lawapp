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

import com.bumptech.glide.Glide;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.constant.AppConstant;
import com.pxxy.lawconsult.entity.Case;

import java.util.List;

public class AdminCaseRecyclerAdapter extends RecyclerView.Adapter<AdminCaseRecyclerAdapter.MyHolder> {
    private Context context;
    private List<Case> caseList;
    private itemOnClickListener itemOnClickListener;
    private deleteButtonOnClickListener deleteButtonOnClickListener;
    private updateButtonOnClickListener updateButtonOnClickListener;

    public void setItemOnClickListener(AdminCaseRecyclerAdapter.itemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public void setDeleteButtonOnClickListener(AdminCaseRecyclerAdapter.deleteButtonOnClickListener deleteButtonOnClickListener) {
        this.deleteButtonOnClickListener = deleteButtonOnClickListener;
    }

    public void setUpdateButtonOnClickListener(AdminCaseRecyclerAdapter.updateButtonOnClickListener updateButtonOnClickListener) {
        this.updateButtonOnClickListener = updateButtonOnClickListener;
    }

    public AdminCaseRecyclerAdapter(Context context, List<Case> caseList) {
        this.context = context;
        this.caseList = caseList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_admin_case_recycler, viewGroup, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.caseTitle.setText(caseList.get(i).getTitle());
        myHolder.caseType.setText(caseList.get(i).getType());
        //加载图片
        Glide.with(context)
                .load(caseList.get(i).getImage())
                .error(R.mipmap.lawconsult_icon)
                .placeholder(R.mipmap.lawconsult_icon)
                .into(myHolder.casePhoto);
        //设置按钮的text
        myHolder.bt_caseUpdate.setText(AppConstant.ADMIN_CASE_SHOW_CASE_BT_UPDATE);
        myHolder.bt_deletecase.setText(AppConstant.ADMIN_CASE_SHOW_CASE_BT_DELETE);
    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

    /**
     * 视图缓存类
     */
    class MyHolder extends RecyclerView.ViewHolder {
        private RelativeLayout caseLayout;
        private ImageView casePhoto;
        private TextView caseTitle, caseType;
        private Button bt_deletecase, bt_caseUpdate;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);
            //获取控件id
            caseLayout = itemView.findViewById(R.id.admin_show_case_layout);
            casePhoto = itemView.findViewById(R.id.admin_show_case_photo);
            caseTitle = itemView.findViewById(R.id.admin_show_case_title);
            caseType = itemView.findViewById(R.id.admin_show_case_type);
            bt_deletecase = itemView.findViewById(R.id.admin_show_case_bt_delete);
            bt_caseUpdate = itemView.findViewById(R.id.admin_show_case_bt_update);

            //设置item的点击事件
            caseLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClickListener != null){
                        itemOnClickListener.setItemOnClickListener(v,caseList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });

            //设置删除按钮的点击事件
            bt_deletecase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteButtonOnClickListener !=null){
                        deleteButtonOnClickListener.setDeleteButtonOnClickListener(v,caseList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });

            //设置修改按钮的点击事件
            bt_caseUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updateButtonOnClickListener != null){
                        updateButtonOnClickListener.setUpdateButtonOnClickListener(v,caseList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });
        }
    }

    /**
     * 设置item和案例详情的点击事件接口
     */
    public interface itemOnClickListener {
        void setItemOnClickListener(View view, Case mCase, int position);
    }

    /**
     * 设置修改案例按钮的点击事件接口
     */
    public interface updateButtonOnClickListener {
        void setUpdateButtonOnClickListener(View view, Case mCase, int position);
    }

    /**
     * 设置删除案例按钮的点击事件接口
     */
    public interface deleteButtonOnClickListener {
        void setDeleteButtonOnClickListener(View view, Case mCase, int position);
    }
}
