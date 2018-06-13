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
import com.ting.db.DBListenHistory;
import com.ting.play.BookDetailsActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilListener;

import java.util.List;


public class LastListenAdapter extends RecyclerView.Adapter<LastListenAdapter.ItemViewHolder> {
    private BaseActivity mActivity;
    private LayoutInflater inflater;
    private List<DBListenHistory> data;

    public LastListenAdapter(BaseActivity activity) {
        this.mActivity = activity;
        inflater = inflater.from(activity);
    }

    public void setData(List<DBListenHistory> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_last_listener, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final DBListenHistory vo = data.get(position);
        UtilGlide.loadImg(mActivity, vo.getPic(), holder.last_listene_image);
        holder.class_introduce_name.setText(vo.getBookname());
        holder.class_introduce_anchor.setText("最近收听至" + vo.getChapter_name());
        holder.class_introduce_zang_number.setText("主播：" + vo.getHost());
        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("bookID", vo.getBookid());
                bundle.putInt("view", 1);
                mActivity.intent(BookDetailsActivity.class, bundle);
            }
        });
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
        private ImageView last_listene_image;//图片
        private TextView class_introduce_name;//名称
        private TextView class_introduce_anchor;//描述
        private TextView class_introduce_zang_number;//主播名字

        public ItemViewHolder(View itemView) {
            super(itemView);
            last_listene_image = (ImageView) itemView.findViewById(R.id.last_listene_image);
            class_introduce_name = (TextView) itemView.findViewById(R.id.class_introduce_name);
            class_introduce_anchor = (TextView) itemView.findViewById(R.id.class_introduce_anchor);
            class_introduce_zang_number = (TextView) itemView.findViewById(R.id.class_introduce_zang_number);
        }
    }
}
