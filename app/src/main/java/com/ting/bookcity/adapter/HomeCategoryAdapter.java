package com.ting.bookcity.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.home.HomeBookVO;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 16/5/25.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ItemViewholder> {

    private List<HomeBookVO> data;
    private BaseActivity activity;
    private ItemOnClickListener listener;
    private LayoutInflater mInflater;

    public HomeCategoryAdapter(BaseActivity activity) {
        this.activity = activity;
        this.listener = new ItemOnClickListener();
        this.mInflater = LayoutInflater.from(activity);
    }

    public void setData(List<HomeBookVO> data) {
        this.data = data;
    }



    @Override
    public ItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_home_category, parent, false);
        ItemViewholder viewholder = new ItemViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ItemViewholder holder, int position) {
        HomeBookVO vo = data.get(position);
        UtilGlide.loadImg(activity, vo.getThumb(), holder.ivImg);
        holder.tvName.setText(data.get(position).getTitle());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    protected class ItemViewholder extends RecyclerView.ViewHolder{
        private ImageView ivImg;
        private TextView tvName;

        public ItemViewholder(View itemView) {
            super(itemView);
            ivImg = (ImageView) itemView
                    .findViewById(R.id.iv_img);
            tvName = (TextView) itemView
                    .findViewById(R.id.tv_name);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            HomeBookVO vo = (HomeBookVO) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putInt("bookID", vo.getId());
            activity.intent(BookDetailsActivity.class, bundle);
        }
    }
}
