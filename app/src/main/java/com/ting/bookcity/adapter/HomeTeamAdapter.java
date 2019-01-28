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
import com.ting.bean.vo.TeamVO;
import com.ting.bookcity.TeamBookListActivity;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 16/5/25.
 */
public class HomeTeamAdapter extends RecyclerView.Adapter<HomeTeamAdapter.ItemViewholder> {

    private List<TeamVO> data;
    private BaseActivity activity;
    private ItemOnClickListener listener;

    public HomeTeamAdapter(BaseActivity activity) {
        this.activity = activity;
        this.listener = new ItemOnClickListener();
    }

    public void setData(List<TeamVO> data) {
        this.data = data;
    }



    @Override
    public ItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_team_item, parent, false);
        ItemViewholder viewholder = new ItemViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ItemViewholder holder, int position) {
        TeamVO vo = data.get(position);
        UtilGlide.loadImg(activity, vo.getImage(), holder.ivImg);
        holder.tvTitle.setText(vo.getName());
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
        private TextView tvTitle;

        public ItemViewholder(View itemView) {
            super(itemView);
            ivImg =  itemView
                    .findViewById(R.id.iv_img);
            tvTitle =  itemView
                    .findViewById(R.id.tv_title);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TeamVO vo = (TeamVO) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("teamId", vo.getId());
            bundle.putString("teamTitle", vo.getName());
            activity.intent(TeamBookListActivity.class, bundle);
        }
    }
}
