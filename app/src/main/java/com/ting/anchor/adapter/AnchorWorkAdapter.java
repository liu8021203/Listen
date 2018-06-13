package com.ting.anchor.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.bean.AnchorWork;
import com.ting.play.BookDetailsActivity;
import com.ting.util.StrUtil;
import com.ting.util.UtilGlide;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/1.
 */
public class AnchorWorkAdapter extends RecyclerView.Adapter<AnchorWorkAdapter.ItemViewHolder> {
    private AnchorMainActivity activity;
    private LayoutInflater inflater;
    private List<AnchorWork> data;

    public AnchorWorkAdapter(AnchorMainActivity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
    }

    public void setData(List<AnchorWork> data) {
        this.data = data;
    }

    public void addData(List<AnchorWork> result)
    {
        if(this.data != null && result != null)
        {
            this.data.addAll(result);
        }
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_anchor_work, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final AnchorWork vo = data.get(position);
        UtilGlide.loadImg(activity, vo.getThumb(), holder.anchor_work_image);
        holder.anchor_work_name.setText(vo.getTitle());
        holder.anchor_work_listener_number.setText("收听人数：" + vo.getListened());
        holder.anchor_work_update.setText("最近更新：" + StrUtil.toDate(vo.getLastUpdate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("bookID", vo.getId());
                activity.intent(BookDetailsActivity.class, bundle);
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
        private ImageView anchor_work_image;//作品头像
        private TextView anchor_work_name;//作品名
        private TextView anchor_work_listener_number;//收听人数
        private TextView anchor_work_update;//更新时间


        public ItemViewHolder(View itemView) {
            super(itemView);
            anchor_work_image = (ImageView) itemView.findViewById(R.id.anchor_work_image);
            anchor_work_name = (TextView) itemView.findViewById(R.id.anchor_work_name);
            anchor_work_listener_number = (TextView) itemView.findViewById(R.id.anchor_work_listener_number);
            anchor_work_update = (TextView) itemView.findViewById(R.id.anchor_work_update);
        }
    }

}
