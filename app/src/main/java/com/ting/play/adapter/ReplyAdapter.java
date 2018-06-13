package com.ting.play.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.play.ReplyVO;
import com.ting.util.UtilDate;
import com.ting.util.UtilGlide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liu on 2017/6/25.
 */

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Activity mActivity;
    private List<ReplyVO> data;
    private LayoutInflater mInflater;

    public ReplyAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    public void setData(List<ReplyVO> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_reply_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ReplyVO vo = data.get(position);
        UtilGlide.loadHeadImg(mActivity, vo.getThumb(), itemViewHolder.mCivImg);
        itemViewHolder.mTvName.setText(vo.getUsername());
        itemViewHolder.mTvPosition.setText("第" + (position + 1) + "楼");
        itemViewHolder.mTvContent.setText(vo.getContent());
        itemViewHolder.mTvTime.setText(UtilDate.getDifferNowTime(Long.valueOf(vo.getCreatetime()), System.currentTimeMillis()));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvName;
        private TextView mTvPosition;
        private CircleImageView mCivImg;
        private TextView mTvContent;
        private TextView mTvTime;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvPosition = (TextView) itemView.findViewById(R.id.tv_position);
            mCivImg = (CircleImageView) itemView.findViewById(R.id.civ_img);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
