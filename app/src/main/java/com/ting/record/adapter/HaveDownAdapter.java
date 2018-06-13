package com.ting.record.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.db.DBChapter;
import com.ting.download.DownloadController;
import com.ting.play.BookDetailsActivity;
import com.ting.play.dialog.DeleteDialog;
import com.ting.record.DownChapterActivity;
import com.ting.util.UtilGlide;

import java.util.List;


/**
 * Created by gengjiajia on 15/9/9.
 * 已经下载适配器
 */
public class HaveDownAdapter extends RecyclerView.Adapter<HaveDownAdapter.ItemViewHolder> {
    private DownChapterActivity activity;
    private LayoutInflater inflater;
    private List<DBChapter> data;
    private ItemOnClickListener listener;
    private DeleteOnClickListener mDeleteOnClickListener;
    private DownloadController controller;
    public HaveDownAdapter(DownChapterActivity activity, List<DBChapter> data) {
        this.activity = activity;
        inflater = inflater.from(activity);
        this.data = data;
        this.listener = new ItemOnClickListener();
        this.controller = new DownloadController();
        this.mDeleteOnClickListener = new DeleteOnClickListener();
    }

    public void setData(List<DBChapter> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_have_down, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DBChapter vo = data.get(position);
        UtilGlide.loadImg(activity, vo.getBookUrl(), holder.ivImg);
        holder.tvName.setText(vo.getChapterTitle());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(listener);
        holder.ivDelete.setTag(vo);
        holder.ivDelete.setOnClickListener(mDeleteOnClickListener);
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
        private TextView tvName;
        private ImageView ivDelete;
        private ImageView ivImg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivDelete = itemView.findViewById(R.id.down_delete);
            ivImg = itemView.findViewById(R.id.iv_img);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            DBChapter vo = (DBChapter) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            bundle.putInt("bookID", Integer.valueOf(vo.getBookId()));
            activity.intent(BookDetailsActivity.class, bundle);

        }
    }

    private class DeleteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            DBChapter vo = (DBChapter) v.getTag();
            DeleteDialog dialog = new DeleteDialog(activity);
            dialog.setListener(new DeleteDialog.CallBackListener() {
                @Override
                public void callback(DBChapter vo) {
                    controller.delete(vo);
                    data.remove(vo);
                    notifyDataSetChanged();
                }
            });
            dialog.setVo(vo, 0);
            dialog.show();
        }
    }
}
