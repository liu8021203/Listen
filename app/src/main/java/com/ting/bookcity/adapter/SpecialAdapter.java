package com.ting.bookcity.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.home.HomeSpecialVO;
import com.ting.bookcity.TeamBookListActivity;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 2017/8/7.
 */

public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.ItemViewHolder>{
    private TeamBookListActivity mActivity;
    private List<HomeSpecialVO> data;
    private LayoutInflater mInflater;
    private ItemOnClickListener mListener;

    public SpecialAdapter(TeamBookListActivity activity) {
        mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.mListener = new ItemOnClickListener();
    }

    public void setData(List<HomeSpecialVO> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_home_special, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        HomeSpecialVO vo = data.get(position);
        UtilGlide.loadImg(mActivity, vo.getThumb(), holder.ivImg);
        holder.tvBookName.setText(vo.getTitle());
        holder.tvHost.setText(vo.getUsername());
        holder.itemView.setTag(vo.getId());
        holder.itemView.setOnClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImg;
        private TextView tvBookName;
        private TextView tvHost;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvHost = (TextView) itemView.findViewById(R.id.tv_host_name);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String id = (String) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putInt("bookID", Integer.valueOf(id));
            mActivity.intent(BookDetailsActivity.class, bundle);
        }
    }
}
