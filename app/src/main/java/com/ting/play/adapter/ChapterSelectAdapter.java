package com.ting.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.bean.vo.ChapterSelectVO;
import com.ting.play.dialog.ChapterSelectDialog;

import java.util.List;

/**
 * Created by liu on 2017/11/22.
 */

public class ChapterSelectAdapter extends RecyclerView.Adapter<ChapterSelectAdapter.ItemViewHolder>{
    private ChapterSelectDialog mDialog;
    private List<ChapterSelectVO> data;

    public ChapterSelectAdapter(ChapterSelectDialog dialog) {
        mDialog = dialog;
    }

    public void setData(List<ChapterSelectVO> data) {
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chapater_select_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ChapterSelectVO vo = data.get(position);
        holder.tvName.setText(vo.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(mDialog.getListener() != null){
                    mDialog.getListener().callback(vo.getPage());
                }
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
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
