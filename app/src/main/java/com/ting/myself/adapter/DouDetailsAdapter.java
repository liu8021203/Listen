package com.ting.myself.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.vo.ExpenseVO;
import com.ting.myself.DouDetailsActivity;

import java.util.List;


/**
 * Created by gengjiajia on 15/9/10.
 * 听豆明细适配器
 */
public class DouDetailsAdapter extends RecyclerView.Adapter<DouDetailsAdapter.ItemViewHolder> {
    private LayoutInflater inflater;
    private DouDetailsActivity mActivity;
    private List<ExpenseVO> data;

    public DouDetailsAdapter(DouDetailsActivity douDetailsActivity) {
        this.mActivity = douDetailsActivity;
        inflater = inflater.from(douDetailsActivity);
    }

    public void setData(List<ExpenseVO> data) {
        this.data = data;
    }

    public void addData(List<ExpenseVO> data) {
        if (this.data != null && data != null) {
            this.data.addAll(data);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dou_details, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ExpenseVO vo = data.get(position);
        holder.tvTime.setText(vo.getDate());
        holder.tvDesc.setText(vo.getOrderDesc());
        holder.tvMoney.setText(vo.getOrderExpense());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDesc;
        private TextView tvMoney;
        private TextView tvTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvDesc =  itemView.findViewById(R.id.tv_desc);
            tvMoney =  itemView.findViewById(R.id.tv_money);
            tvTime =  itemView.findViewById(R.id.tv_time);
        }
    }
}
