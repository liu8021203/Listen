package com.ting.record.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.db.DBBook;
import com.ting.download.DownloadController;
import com.ting.play.BookDetailsActivity;
import com.ting.base.ListenDialog;
import com.ting.record.DownChapterActivity;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by liu on 15/11/11.
 */
public class DownBookAdapter extends RecyclerView.Adapter<DownBookAdapter.ItemViewHolder>{
    private List<DBBook> data;
    private DeleteListener listener;
    private ItemOnClickListener itemOnClickListener;
    private BaseActivity mActivity;
    private LayoutInflater mInflater;
    private DownloadController mController;

    public DownBookAdapter(BaseActivity activity)
    {
        this.listener = new DeleteListener();
        this.itemOnClickListener = new ItemOnClickListener();
        mInflater = LayoutInflater.from(activity);
        this.mActivity = activity;
    }



    public void setData(List<DBBook> data) {
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
        DBBook vo = data.get(position);
        UtilGlide.loadImg(mActivity,vo.getBookUrl(),holder.iv_book_url);
        holder.tv_book_name.setText(vo.getBookName());
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
        ImageView iv_book_url;
        TextView tv_book_name;
        TextView tv_host_name;
        ImageView iv_book_delete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_book_url =  itemView.findViewById(R.id.iv_book_url);
            iv_book_delete =  itemView.findViewById(R.id.iv_book_delete);
            tv_book_name =  itemView.findViewById(R.id.tv_book_name);
            tv_host_name =  itemView.findViewById(R.id.tv_host_name);
        }
    }

    private class DeleteListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            final DBBook vo = (DBBook) v.getTag();
            ListenDialog.makeListenDialog(mActivity, "提示", "是否要删除" + vo.getBookName(), true, "否", true, "是", new ListenDialog.CallBackListener() {
                @Override
                public void callback(ListenDialog dialog, int mark) {
                    dialog.dismiss();
                    if(mark == ListenDialog.RIGHT){
                        if(mController == null){
                            mController = new DownloadController();
                        }
                        mController.deleteBook(vo);
                        data.remove(vo);
                        notifyDataSetChanged();
                    }
                }
            }).show();
        }
    }

    private class ItemOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            DBBook vo = (DBBook) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putInt("bookID", Integer.valueOf(vo.getBookId()));
            mActivity.intent(DownChapterActivity.class, bundle);
        }
    }
}
