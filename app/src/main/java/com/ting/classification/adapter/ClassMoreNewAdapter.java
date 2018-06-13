package com.ting.classification.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.classfi.ClassIntroMess;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilIntent;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/8.
 */
public class ClassMoreNewAdapter extends RecyclerView.Adapter<ClassMoreNewAdapter.ItemViewHolder> {
    private LayoutInflater inflater;
    private BaseActivity mActivity;
    private List<ClassIntroMess> data;

    public ClassMoreNewAdapter(BaseActivity mActivity) {
        this.mActivity = mActivity;
        inflater = inflater.from(mActivity);
    }


    public void setData(List<ClassIntroMess> data) {
        this.data = data;
    }

    public void addData(List<ClassIntroMess> data){
        if(this.data != null && data != null){
            this.data.addAll(data);
        }
    }



    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_last_listener, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ClassIntroMess vo = data.get(position);
        UtilGlide.loadImg(mActivity, vo.getThumb(), holder.last_listene_image);
        holder.class_introduce_name.setText(vo.getTitle());
        holder.class_introduce_anchor.setText("主播：" + vo.getAuthor());
        holder.class_introduce_zang_number.setText("章数：" + vo.getLastUpdate() + "章");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("bookID", vo.getId());
                UtilIntent.intentDIY(mActivity,BookDetailsActivity.class, bundle);
            }
        });
    }



    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }



    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView last_listene_image;//分类详情图片
        private TextView class_introduce_name;//分类小说名
        private TextView class_introduce_anchor;//分类主播
        private TextView class_introduce_zang_number;//章数
        public ItemViewHolder(View itemView) {
            super(itemView);
            last_listene_image = (ImageView) itemView.findViewById(R.id.last_listene_image);
            class_introduce_name = (TextView) itemView.findViewById(R.id.class_introduce_name);
            class_introduce_anchor = (TextView) itemView.findViewById(R.id.class_introduce_anchor);
            class_introduce_zang_number = (TextView) itemView.findViewById(R.id.class_introduce_zang_number);
        }
    }
}
