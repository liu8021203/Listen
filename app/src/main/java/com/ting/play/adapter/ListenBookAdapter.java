package com.ting.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.anchor.ListenBookVO;
import com.ting.base.BaseActivity;

import java.util.List;

/**
 * Created by liu on 16/8/5.
 */
public class ListenBookAdapter extends RecyclerView.Adapter<ListenBookAdapter.ItemViewHolder>{
    private List<ListenBookVO> data;
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

    public void setData(List<ListenBookVO> data) {
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
        ListenBookVO vo = data.get(position);
        holder.tvName.setText(vo.getType() + "（" + vo.getPrice() + "听豆）");
        holder.tvName.setTag(vo);
        holder.tvName.setOnClickListener(listener);
        if(defId == Integer.parseInt(vo.getId()))
        {
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dialog_time_select, 0,0,0);
        }
        else {
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dialog_time_unselect, 0,0,0);
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
        TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            ListenBookVO vo = (ListenBookVO) v.getTag();
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
