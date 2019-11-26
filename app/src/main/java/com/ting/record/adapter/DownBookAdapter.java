package com.ting.record.adapter;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.db.DBChapter;
import com.ting.download.DownloadController;
import com.ting.base.ListenDialog;
import com.ting.record.DownChapterActivity;
import com.ting.record.DownloadActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 15/11/11.
 */
public class DownBookAdapter extends RecyclerView.Adapter<DownBookAdapter.ItemViewHolder>{
    private List<DBChapter> data;
    private DeleteListener listener;
    private ItemOnClickListener itemOnClickListener;
    private DownloadActivity mActivity;
    private LayoutInflater mInflater;
    private DownloadController mController;

    public DownBookAdapter(DownloadActivity activity)
    {
        this.listener = new DeleteListener();
        this.itemOnClickListener = new ItemOnClickListener();
        mInflater = LayoutInflater.from(activity);
        this.mActivity = activity;
    }



    public void setData(List<DBChapter> data) {
        this.data = data;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.download_book_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        DBChapter vo = data.get(position);
        UtilGlide.loadImg(mActivity,vo.getBookImage(),holder.ivImg);
        holder.tv_book_name.setText(vo.getBookTitle());
        holder.tv_host_name.setText(vo.getBookHost());
        holder.iv_book_delete.setTag(vo);
        holder.iv_book_delete.setOnClickListener(listener);
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(itemOnClickListener);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivImg;
        TextView tv_book_name;
        TextView tv_host_name;
        ImageView iv_book_delete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImg =  itemView.findViewById(R.id.iv_img);
            iv_book_delete =  itemView.findViewById(R.id.iv_book_delete);
            tv_book_name =  itemView.findViewById(R.id.tv_book_name);
            tv_host_name =  itemView.findViewById(R.id.tv_host_name);
        }
    }

    private class DeleteListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            final DBChapter vo = (DBChapter) v.getTag();
            ListenDialog.makeListenDialog(mActivity, "提示", "是否要删除" + vo.getBookTitle(), true, "否", true, "是", new ListenDialog.CallBackListener() {
                @Override
                public void callback(ListenDialog dialog, int mark) {
                    dialog.dismiss();
                    if(mark == ListenDialog.RIGHT){
                        if(mController == null){
                            mController = new DownloadController();
                        }
                        mController.deleteBook(vo.getBookId());
                        data.remove(vo);
                        notifyDataSetChanged();
                        if(data.size() == 0){
                            mActivity.showEmpty();
                        }
                    }
                }
            }).show();
        }
    }

    private class ItemOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            DBChapter vo = (DBChapter) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("bookId", vo.getBookId());
            mActivity.intent(DownChapterActivity.class, bundle);
        }
    }
}
