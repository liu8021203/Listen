package com.ting.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.vo.CardVO;

import java.util.List;

/**
 * Created by liu on 16/8/5.
 */
public class ListenBookAdapter extends RecyclerView.Adapter<ListenBookAdapter.ItemViewHolder>{
    private List<CardVO> data;
    private int defId = -1;
    private ItemOnClickListener listener;
    private LayoutInflater mInflater;

    public ListenBookAdapter(BaseActivity activity) {
        this.mInflater = LayoutInflater.from(activity);
        this.listener = new ItemOnClickListener();
    }


    public int getDefId() {
        return defId;
    }

    public void setData(List<CardVO> data) {
        this.data = data;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_listen_book_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        CardVO vo = data.get(position);
        holder.tvDesc.setText(vo.getCardDesc());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(listener);
        if(defId == Integer.parseInt(vo.getId()))
        {
            holder.tvDesc.setTextColor(0xff1296db);
        }
        else {
            holder.tvDesc.setTextColor(0xff000000);
        }
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
        TextView tvDesc;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvDesc =  itemView.findViewById(R.id.tv_desc);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            CardVO vo = (CardVO) v.getTag();
            if(defId == -1)
            {
                defId = Integer.parseInt(vo.getId());
                notifyDataSetChanged();
            }
            else {
                if(defId == Integer.parseInt(vo.getId()))
                {
                    defId = -1;
                    notifyDataSetChanged();
                }
                else {
                    defId = Integer.parseInt(vo.getId());
                    notifyDataSetChanged();
                }
            }
        }
    }
}
