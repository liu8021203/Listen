package com.ting.play.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.listener.CallBackListener;
import com.ting.db.DBChapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterBuyAdapter extends RecyclerView.Adapter<ChapterBuyAdapter.ItemViewHolder>{
    private List<DBChapter> data;
    private Context mContext;
    private List<DBChapter> selectData = new ArrayList<>();
    private CallBackListener mListener;

    public ChapterBuyAdapter(Context context) {
        mContext = context;
    }

    public void setListener(CallBackListener listener) {
        mListener = listener;
    }

    public void setData(List<DBChapter> data) {
        this.data = data;
        selectData.clear();
    }

    public List<DBChapter> getSelectData() {
        return selectData;
    }

    public List<DBChapter> getData() {
        return data;
    }

    /**
     * 全选
     */
    public void allSelect(){
        for (int i = 0; i < data.size(); i++){
            if(TextUtils.isEmpty(data.get(i).getUrl())){
                selectData.add(data.get(i));
            }
        }
    }

    /**
     * 全部取消
     */
    public void allUnSelect(){
        selectData.clear();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chapter_buy_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final DBChapter vo = data.get(position);
        holder.tvChapterName.setText(vo.getTitle());
        if(!TextUtils.isEmpty(vo.getUrl())){
            holder.ivCheck.setImageResource(R.mipmap.check_noselect);
            holder.itemView.setOnClickListener(null);
            holder.tvPrice.setText("本章已解锁");
            holder.tvPrice.setTextColor(0xff69af00);
        }else{
            holder.tvPrice.setText(vo.getPrice() + "听豆");
            holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.price_color));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectData.contains(vo)){
                        selectData.remove(vo);
                        notifyDataSetChanged();
                    }else{
                        selectData.add(vo);
                        notifyDataSetChanged();
                    }
                    if(mListener != null){
                        mListener.callback();
                    }
                }
            });
            if(selectData.contains(vo)){
                holder.ivCheck.setImageResource(R.mipmap.check_select);
            }else{
                holder.ivCheck.setImageResource(R.mipmap.check_unselect);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvChapterName;
        private ImageView ivCheck;
        private TextView tvPrice;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tvChapterName = itemView.findViewById(R.id.tv_chapter_name);
            ivCheck = itemView.findViewById(R.id.iv_check);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
