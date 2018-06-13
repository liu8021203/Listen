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
import com.ting.bean.home.SuperMarketVO;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 16/5/25.
 */
public class HotRankAdapter extends RecyclerView.Adapter<HotRankAdapter.ItemViewholder> {

    private List<SuperMarketVO> data;
    private BaseActivity activity;
    private ItemOnClickListener listener;
    private LayoutInflater mInflater;

    public HotRankAdapter(BaseActivity activity) {
        this.activity = activity;
        this.listener = new ItemOnClickListener();
        this.mInflater = LayoutInflater.from(activity);
    }

    public void setData(List<SuperMarketVO> data) {
        this.data = data;
    }



    @Override
    public ItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recommend_gridview_item, parent, false);
        ItemViewholder viewholder = new ItemViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ItemViewholder holder, int position) {
        SuperMarketVO vo = data.get(position);
        UtilGlide.loadImg(activity, vo.getThumb(), holder.iv_icon);
        holder.name.setText(data.get(position).getTitle());
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
        private ImageView iv_icon;
        private TextView name;

        public ItemViewholder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView
                    .findViewById(R.id.iv_recommend_gridview);
            name = (TextView) itemView
                    .findViewById(R.id.tv_recommend_gridview);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SuperMarketVO vo = (SuperMarketVO) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putInt("bookID", vo.getId());
            activity.intent(BookDetailsActivity.class, bundle);
        }
    }
}
