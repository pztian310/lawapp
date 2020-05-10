package com.pxxy.lawconsult.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
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
import com.pxxy.lawconsult.entity.ClientUser;
import com.pxxy.lawconsult.entity.LawerUser;
import com.pxxy.lawconsult.entity.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminUserRecyclerAdapter extends RecyclerView.Adapter<AdminUserRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<? extends User> list;
    private int userType;
    private itemOnClickListener itemOnClickListener;
    private deleteButtonOnClickListener deleteButtonOnClickListener;

    public void setDeleteButtonOnClickListener(AdminUserRecyclerAdapter.deleteButtonOnClickListener deleteButtonOnClickListener) {
        this.deleteButtonOnClickListener = deleteButtonOnClickListener;
    }

    public void setItemOnClickListener(AdminUserRecyclerAdapter.itemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public AdminUserRecyclerAdapter(Context context, List<? extends User> list, int type) {
        this.context = context;
        this.list = list;
        this.userType = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_admin_user_recycler, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (userType == AppConstant.TYPE_0) {
            ClientUser user = (ClientUser) list.get(i);
            myViewHolder.tel.setText("Tel:" + user.getTel());
            myViewHolder.type.setImageResource(R.drawable.admin_clientuser);
            //设置按钮的文字
            myViewHolder.details.setText(AppConstant.ADMIN_USER_CLIENTUSER_MESSAGE_BUTTON);
            myViewHolder.delete.setText(AppConstant.ADMIN_USER_CLIENTUSER_DELETE_BUTTON);
            //加载头像
            Glide.with(context)
                    .load(user.getImage())
                    .error(R.mipmap.lawconsult_icon)
                    .placeholder(R.mipmap.lawconsult_icon)
                    .into(myViewHolder.photo);
        } else if (userType == AppConstant.TYPE_1) {
            LawerUser user = (LawerUser) list.get(i);
            myViewHolder.tel.setText("Tel:" + user.getTel());
            myViewHolder.type.setImageResource(R.drawable.admin_laweruser);
            //设置按钮的文字
            myViewHolder.details.setText(AppConstant.ADMIN_USER_LAWYERUSER_MESSAGE_BUTTON);
            myViewHolder.delete.setText(AppConstant.ADMIN_USER_LAWYERUSER_DELETE_BUTTON);
            //加载头像
            Glide.with(context)
                    .load(user.getImage())
                    .error(R.mipmap.lawconsult_icon)
                    .placeholder(R.mipmap.lawconsult_icon)
                    .into(myViewHolder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 缓存类
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView photo;
        private TextView tel;
        private Button details, delete;
        private ImageView type;
        private RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.admin_show_user_photo);
            tel = itemView.findViewById(R.id.admin_show_user_tel);
            details = itemView.findViewById(R.id.admin_show_user_details);
            delete = itemView.findViewById(R.id.admin_show_user_delete);
            type = itemView.findViewById(R.id.admin_show_user_type);
            layout = itemView.findViewById(R.id.admin_show_user_layout);
            //设置点击item事件
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClickListener != null) {
                        itemOnClickListener.setItemOnClickListener(v, list.get(getLayoutPosition()), userType);
                    }
                }
            });

            //设置点击用户详情事件,与点击item事件一样的事件
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClickListener != null){
                        itemOnClickListener.setItemOnClickListener(v,list.get(getLayoutPosition()),userType);
                    }
                }
            });

            //设置删除用户按钮的点击事件
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteButtonOnClickListener != null){
                        deleteButtonOnClickListener.setDeleteButtonOnClickListener(v,list.get(getLayoutPosition()),userType,getLayoutPosition());
                    }
                }
            });

        }
    }

    public interface itemOnClickListener {
        void setItemOnClickListener(View view, User user, int type);
    }

    public interface deleteButtonOnClickListener{
        void setDeleteButtonOnClickListener(View view,User user,int type,int position);
    }

}
