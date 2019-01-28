package com.ting.bookcity.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.home.SpecialVO;
import com.ting.bookcity.TeamBookListActivity;
import com.ting.util.UtilGlide;
import com.ting.welcome.MainActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liu on 2017/9/15.
 */

public class HomeSpecialAdapter extends RecyclerView.Adapter<HomeSpecialAdapter.ItemViewHolder>{
    private MainActivity mActivity;
    private LayoutInflater mInflater;
    private List<SpecialVO> data;

    public HomeSpecialAdapter(MainActivity activity) {
        mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
    }

    public void setData(List<SpecialVO> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_home_special_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final SpecialVO vo = data.get(position);
        holder.tvName.setText(vo.getTitle());
        UtilGlide.loadImg(mActivity, vo.getThumb(), holder.civImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", vo.getId());
                mActivity.intent(TeamBookListActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView civImg;
        private TextView tvName;


        public ItemViewHolder(View itemView) {
            super(itemView);
            civImg =  itemView.findViewById(R.id.civ_img);
            tvName =  itemView.findViewById(R.id.tv_name);
        }
    }
}
