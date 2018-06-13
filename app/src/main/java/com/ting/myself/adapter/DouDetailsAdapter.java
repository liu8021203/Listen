package com.ting.myself.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.myself.DouChildrenBean;
import com.ting.myself.DouDetailsActivity;
import com.ting.myself.DouDetailsChildrenActivity;

import java.util.List;


/**
 * Created by gengjiajia on 15/9/10.
 * 听豆明细适配器
 */
public class DouDetailsAdapter extends RecyclerView.Adapter<DouDetailsAdapter.ItemViewHolder> {
    private LayoutInflater inflater;
    private DouDetailsActivity mActivity;
    private List<DouChildrenBean> data;

    public DouDetailsAdapter(DouDetailsActivity douDetailsActivity) {
        this.mActivity = douDetailsActivity;
        inflater = inflater.from(douDetailsActivity);
    }

    public void setData(List<DouChildrenBean> data) {
        this.data = data;
    }

    public void addData(List<DouChildrenBean> data){
        if(this.data != null && data != null){
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
        DouChildrenBean vo = data.get(position);
        holder.tv_pay_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.intent(DouDetailsChildrenActivity.class);

            }
        });
        holder.tv_type.setText(Html.fromHtml(vo.getAlt()).toString());
        holder.tv_time.setText(vo.getCreatetime());
        holder.tv_get_dou_number.setText(vo.getJifen());
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
        private TextView tv_pay_details;
        private TextView tv_type;
        private TextView tv_time;
        private TextView tv_get_dou_number;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_pay_details = (TextView) itemView.findViewById(R.id.tv_pay_details);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_get_dou_number = (TextView) itemView.findViewById(R.id.tv_get_dou_number);
        }
    }
}
