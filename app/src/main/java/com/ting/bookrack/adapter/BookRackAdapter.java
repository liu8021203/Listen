package com.ting.bookrack.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseApplication;
import com.ting.db.DBBook;
import com.ting.db.DBListenHistory;
import com.ting.db.DBListenHistoryDao;
import com.ting.play.BookDetailsActivity;
import com.ting.play.controller.MusicDBController;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 2017/11/27.
 */

public class BookRackAdapter extends RecyclerView.Adapter<BookRackAdapter.ItemViewHolder> {
    private BaseActivity mActivity;
    private LayoutInflater mInflater;
    private List<DBListenHistory> data;
    private ItemOnClickListener mItemOnClickListener;
    private DeleteOnClickListener mDeleteOnClickListener;
    //0  是正常显示   1  是编辑显示
    private int state = 0;


    public BookRackAdapter(BaseActivity activity) {
        mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.mItemOnClickListener = new ItemOnClickListener();
        this.mDeleteOnClickListener = new DeleteOnClickListener();
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setData(List<DBListenHistory> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_bookrack_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        DBListenHistory vo = data.get(position);
        holder.tvTitle.setText(vo.getBookname());
        UtilGlide.loadImg(mActivity, vo.getPic(), holder.ivImg);
        if(state == 0){
            holder.ivImg.setAlpha(1.0f);
            holder.ivDelete.setVisibility(View.GONE);
            holder.itemView.setTag(vo);
            holder.itemView.setOnClickListener(mItemOnClickListener);
        }else{
            holder.ivImg.setAlpha(0.5f);
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.itemView.setTag(vo);
            holder.itemView.setOnClickListener(mDeleteOnClickListener);
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivImg;
        private ImageView ivDelete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivImg = itemView.findViewById(R.id.iv_img);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            DBListenHistory vo = (DBListenHistory) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putInt("bookID", vo.getBookid());
            mActivity.intent(BookDetailsActivity.class, bundle);
        }
    }

    private class DeleteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            DBListenHistory vo = (DBListenHistory) v.getTag();
            DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
            dao.delete(vo);
            data.remove(vo);
            notifyDataSetChanged();
        }
    }
}
