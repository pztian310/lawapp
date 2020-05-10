package com.pxxy.lawconsult.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.pxxy.lawconsult.R;
import com.pxxy.lawconsult.entity.Case;

import java.util.List;

public class ClassicCaseRecyclerAdapter extends RecyclerView.Adapter<ClassicCaseRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<Case> caseList;
    private itemOnclickListener itemOnclickListener;
    /**
     * 设置item的点击事件
     * @param itemOnclickListener
     */
    public void setItemOnclickListener(ClassicCaseRecyclerAdapter.itemOnclickListener itemOnclickListener) {
        this.itemOnclickListener = itemOnclickListener;
    }

    public ClassicCaseRecyclerAdapter(Context context, List<Case> caseList){
        this.context = context;
        this.caseList = caseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_activity_classic_case_recyclerview,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Case mCase = caseList.get(i);
        myViewHolder.title.setText(mCase.getTitle());
        myViewHolder.type.setText(mCase.getType());
        //圆角
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(20));
        //glide加载图片
        Glide.with(context)
                .load(mCase.getImage())
                .apply(options)
                .error(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .into(myViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView image;
        private TextView type;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adapter_classic_case_recyclerview_title);
            image = itemView.findViewById(R.id.adapter_classic_case_recyclerview_image);
            type = itemView.findViewById(R.id.adapter_classic_case_recyclerview_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemOnclickListener != null){
                        //设置监听事件，getLayoutPosition获取当前点击的下标
                        itemOnclickListener.setItemOnclickListener(v,caseList.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    /**
     * 设置item点击事件接口
     */
    public interface itemOnclickListener{
        void setItemOnclickListener(View view, Case mCase);
    }

}
