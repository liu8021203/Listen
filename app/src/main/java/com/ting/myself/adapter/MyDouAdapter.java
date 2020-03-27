package com.ting.myself.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.vo.MoneyListVO;
import com.ting.myself.MyDouActivity;
import com.ting.myself.dialog.PayDialog;

import java.util.List;

/**
 * Created by liu on 2017/7/26.
 */

public class MyDouAdapter extends RecyclerView.Adapter<MyDouAdapter.ItemViewHolder>{
    private MyDouActivity mActivity;
    private List<MoneyListVO> data;
    private LayoutInflater mInflater;

    public MyDouAdapter(MyDouActivity activity) {
        mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
    }

    public void setData(List<MoneyListVO> data) {
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
        final MoneyListVO vo = data.get(position);
        holder.tvName.setText(vo.getMoneyDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayDialog dialog = new PayDialog(mActivity);
                dialog.setNum(vo.getMoneyNum());
                dialog.setPrice(vo.getMoneyPrice());
                dialog.setDesc(vo.getMoneyDesc());
                dialog.show();
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
