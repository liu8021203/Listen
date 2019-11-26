package com.ting.anchor.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.bean.anchor.FansMess;
import com.ting.util.UtilGlide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gengjiajia on 15/9/1.
 * 关注他的listview适配器
 */
public class FollowAnchorAdapter extends RecyclerView.Adapter<FollowAnchorAdapter.ItemViewHolder> {
    private AnchorMainActivity mActivity;
    private LayoutInflater inflater;
    private List<FansMess> data;

    public FollowAnchorAdapter(AnchorMainActivity activity) {
        this.mActivity = activity;
        this.inflater = LayoutInflater.from(activity);
    }

    public void setData(List<FansMess> data) {
        this.data = data;
    }

    public void addData(List<FansMess> result)
    {
        if(this.data != null && result != null)
        {
            this.data.addAll(result);
        }
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_follow_anchor, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        FansMess vo = data.get(position);
        UtilGlide.loadAnchorImg(mActivity, vo.getThumb(), holder.follower_touxiang);
        if(!TextUtils.isEmpty(vo.getName())) {
            holder.fans_name.setText(vo.getName());
        }else{
            holder.fans_name.setText("佚名");
        }
        if(vo.getHistory() != null) {
            holder.fans_last_listener.setText("最近收听:" + vo.getHistory().getTitle());
        }
        else {
            holder.fans_last_listener.setText("最近没有收听");
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView follower_touxiang;//头像
        private TextView fans_name;//粉丝名字
        private TextView fans_last_listener;//粉丝最近收听

        public ItemViewHolder(View itemView) {
            super(itemView);
            follower_touxiang = (CircleImageView) itemView.findViewById(R.id.follower_touxiang);
            fans_name = (TextView) itemView.findViewById(R.id.fans_name);
            fans_last_listener = (TextView) itemView.findViewById(R.id.fans_last_listener);
        }
    }

}
