package com.ting.myself.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.myself.MessageJavaVO;
import com.ting.bean.vo.MessageListVO;
import com.ting.util.UtilDate;

import java.util.List;

/**
 * Created by liu on 2018/1/22.
 */

public class MessageJavaAdapter extends RecyclerView.Adapter<MessageJavaAdapter.ItemViewHolder>{
    private List<MessageListVO> data;

    public void setData(List<MessageListVO> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_message_item,parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        MessageListVO vo = data.get(position);
        holder.tvTitle.setText(vo.getTitle());
        holder.tvDesc.setText(vo.getContent());
        holder.tvTime.setText(vo.getTime());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvDesc;
        private TextView tvTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
