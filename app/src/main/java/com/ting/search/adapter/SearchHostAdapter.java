package com.ting.search.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.base.BaseActivity;
import com.ting.bean.vo.HostVO;
import com.ting.util.UtilGlide;

import java.util.List;

public class SearchHostAdapter extends RecyclerView.Adapter<SearchHostAdapter.ItemViewHolder>{
    private List<HostVO> data;
    private BaseActivity mActivity;
    private ItemOnClickListener mListener;


    public SearchHostAdapter(BaseActivity activity) {
        mActivity = activity;
        this.mListener = new ItemOnClickListener();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search_host_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    public void setData(List<HostVO> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        HostVO vo = data.get(position);
        UtilGlide.loadAnchorImg(mActivity, vo.getUserImage(), holder.ivImg);
        holder.tvNickName.setText(vo.getNickname());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNickName;
        private ImageView ivImg;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tvNickName = itemView.findViewById(R.id.tv_nickname);
            ivImg = itemView.findViewById(R.id.iv_img);
        }
    }


    private class ItemOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            HostVO vo = (HostVO) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("anchorId", vo.getId());
            mActivity.intent(AnchorMainActivity.class, bundle);
        }
    }
}
