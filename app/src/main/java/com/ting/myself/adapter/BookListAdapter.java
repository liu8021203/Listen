package com.ting.myself.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseCommonAdapter;
import com.ting.bean.vo.BookVO;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 2017/11/7.
 */

public class BookListAdapter extends BaseCommonAdapter{
    private BaseActivity mActivity;
    private LayoutInflater mInflater;
    private List<BookVO> data;
    private ItemOnClickListener mListener;

    public BookListAdapter(BaseActivity activity) {
        mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.mListener = new ItemOnClickListener();
    }

    public void setData(List<BookVO> data) {
        this.data = data;
    }

    public void addData(List<BookVO> data){
        if(this.data != null && data != null){
            this.data.addAll(data);
        }
    }

    public List<BookVO> getData() {
        return data;
    }

    @Override
    protected int count() {
        return data == null ? 0 : data.size();
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.recycler_category_list_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    protected void setContentData(RecyclerView.ViewHolder holder, int position) {
        BookVO vo = data.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        UtilGlide.loadImg(mActivity, vo.getBookImage(), itemViewHolder.ivImg);
        itemViewHolder.tvTitle.setText(vo.getBookTitle());
        itemViewHolder.tvDesc.setText(vo.getBookDesc());
        itemViewHolder.tvAnchor.setText("播音：" + vo.getBookAnchor());
        if(Integer.valueOf(vo.getBookUpdateStatus()) == 1){
            itemViewHolder.tvStatus.setText("状态：" + "完结");
        }else{
            itemViewHolder.tvStatus.setText("状态：更新至" + vo.getCount() + "集");
        }
        itemViewHolder.itemView.setTag(vo);
        itemViewHolder.itemView.setOnClickListener(mListener);
    }



    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImg;
        private TextView tvTitle;
        private TextView tvDesc;
        private TextView tvAnchor;
        private TextView tvStatus;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImg =  itemView.findViewById(R.id.iv_img);
            tvTitle =  itemView.findViewById(R.id.tv_title);
            tvAnchor =  itemView.findViewById(R.id.tv_anchor);
            tvDesc =  itemView.findViewById(R.id.tv_desc);
            tvStatus =  itemView.findViewById(R.id.tv_status);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            BookVO vo = (BookVO) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("bookId", vo.getId());
            bundle.putString("bookImg", vo.getBookImage());
            bundle.putString("bookTitle", vo.getBookTitle());
            bundle.putString("bookHost", vo.getBookAnchor());
            mActivity.intent(BookDetailsActivity.class, bundle);
        }
    }
}
