package com.ting.bookcity.adapter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.base.BaseActivity;
import com.ting.bean.vo.HostVO;
import com.ting.util.UtilGlide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HostAdapter extends RecyclerView.Adapter<HostAdapter.ItemViewHolder>{
    private List<HostVO> data;
    private BaseActivity mActivity;
    private ItemOnClickListener mListener;

    public HostAdapter(BaseActivity activity) {
        mActivity = activity;
        this.mListener = new ItemOnClickListener();
    }

    public void setData(List<HostVO> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_host_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        HostVO vo = data.get(position);
        UtilGlide.loadHeadImg(mActivity, vo.getUserImage(), holder.ivImg);
        holder.tvName.setText(vo.getNickname());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView ivImg;
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
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
