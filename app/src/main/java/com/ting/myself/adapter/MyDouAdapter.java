package com.ting.myself.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.myself.DouPayRank;
import com.ting.myself.MyDouActivity;

import java.util.List;

/**
 * Created by liu on 2017/7/26.
 */

public class MyDouAdapter extends RecyclerView.Adapter<MyDouAdapter.ItemViewHolder>{
    private MyDouActivity mActivity;
    private List<DouPayRank> data;
    private LayoutInflater mInflater;

    public MyDouAdapter(MyDouActivity activity) {
        mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
    }

    public void setData(List<DouPayRank> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_my_dou, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final DouPayRank vo = data.get(position);
        holder.tvName.setText(vo.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showPayDialog(vo);
            }


        });
    }



    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.listen_dou_price);
        }
    }
}
