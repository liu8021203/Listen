package com.ting.record.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.record.CollectInfo;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilListener;

import java.util.List;


/**
 * Created by gengjiajia on 15/9/8.
 * 收藏适配器
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ItemViewHolder>{
    private BaseActivity mActivity;
    private LayoutInflater inflater;
    private List<CollectInfo> data;

    public CollectionAdapter(BaseActivity activity) {
        this.mActivity = activity;
        inflater = inflater.from(activity);
    }

    public void setData(List<CollectInfo> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_last_listener, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final CollectInfo vo = data.get(position);
        UtilGlide.loadImg(mActivity, vo.getThumb(), holder.last_listene_image);
        holder.class_introduce_name.setText(vo.getTitle());
        holder.class_introduce_anchor.setText("主播：" + vo.getName());
        holder.class_introduce_zang_number.setText("集数：" + vo.getLast());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("bookID", vo.getTarget_id());
                mActivity.intent(BookDetailsActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView last_listene_image;//图片
        private TextView class_introduce_name;//小说名
        private TextView class_introduce_anchor;//主播
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
