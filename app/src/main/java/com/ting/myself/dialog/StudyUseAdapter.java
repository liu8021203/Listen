package com.ting.myself.dialog;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.myself.StudyUseActivity;
import com.ting.myself.StudyUseDetailsActivity;

/**
 * Created by liu on 2017/8/9.
 */

public class StudyUseAdapter extends RecyclerView.Adapter<StudyUseAdapter.ItemViewHolder>{
    private String[] str = {"小米手机", "华为手机"};
    private int[] code = {0,1};
    private StudyUseActivity mActivity;
    private LayoutInflater mInflater;

    public StudyUseAdapter(StudyUseActivity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_study_use, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.tvName.setText(str[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("code", code[position]);
                mActivity.intent(StudyUseDetailsActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return str.length;
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
