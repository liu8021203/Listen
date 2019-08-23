package com.ting.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.vo.BuyChapterWayVO;
import com.ting.bean.vo.CardVO;

import java.util.List;

/**
 * Created by liu on 16/8/5.
 */
public class BuyChapterWaydapter extends RecyclerView.Adapter<BuyChapterWaydapter.ItemViewHolder>{
    private List<BuyChapterWayVO> data;
    private BuyChapterWayVO defVO = null;
    private ItemOnClickListener listener;
    private BaseActivity mActivity;

    public BuyChapterWaydapter(BaseActivity activity) {
        this.listener = new ItemOnClickListener();
        this.mActivity = activity;
    }

    public BuyChapterWayVO getDefVO() {
        return defVO;
    }

    public void setData(List<BuyChapterWayVO> data) {
        this.data = data;
//        defVO = data.get(0);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_listen_book_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        BuyChapterWayVO vo = data.get(position);
        holder.tvDesc.setText(vo.getDesc());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(listener);
        if(vo == defVO)
        {
            holder.tvDesc.setTextColor(0xff1296db);
            holder.ivMark.setImageResource(R.mipmap.check_select);
        }
        else {
            holder.tvDesc.setTextColor(0xff000000);
            holder.ivMark.setImageResource(R.mipmap.check_unselect);
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
        ImageView ivMark;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tvDesc =  itemView.findViewById(R.id.tv_desc);
            ivMark = itemView.findViewById(R.id.iv_mark);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            BuyChapterWayVO vo = (BuyChapterWayVO) v.getTag();
            if(defVO == null)
            {
                defVO = vo;
                notifyDataSetChanged();
            }
            else {
                if(defVO == vo)
                {
                    defVO = null;
                    notifyDataSetChanged();
                }
                else {
                    defVO = vo;
                    notifyDataSetChanged();
                }
            }
        }
    }
}
