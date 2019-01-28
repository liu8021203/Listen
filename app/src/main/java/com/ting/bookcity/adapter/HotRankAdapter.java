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
import com.ting.bean.vo.BookVO;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 16/5/25.
 */
public class HotRankAdapter extends RecyclerView.Adapter<HotRankAdapter.ItemViewholder> {

    private List<BookVO> data;
    private BaseActivity activity;
    private ItemOnClickListener listener;

    public HotRankAdapter(BaseActivity activity) {
        this.activity = activity;
        this.listener = new ItemOnClickListener();
    }

    public void setData(List<BookVO> data) {
        this.data = data;
    }



    @Override
    public ItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_recommend_item, parent, false);
        ItemViewholder viewholder = new ItemViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ItemViewholder holder, int position) {
        BookVO vo = data.get(position);
        UtilGlide.loadImg(activity, vo.getBookImage(), holder.ivImg);
        holder.tvTitle.setText(data.get(position).getBookTitle());
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
            BookVO vo = (BookVO) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("bookId", vo.getId());
            bundle.putString("bookTitle", vo.getBookTitle());
            activity.intent(BookDetailsActivity.class, bundle);
        }
    }
}
