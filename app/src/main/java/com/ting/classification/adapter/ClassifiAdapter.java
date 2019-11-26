package com.ting.classification.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.classfi.ClassMainData;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/14.
 * 分类主适配
 */
public class ClassifiAdapter extends RecyclerView.Adapter<ClassifiAdapter.ItemViewHolder> {
    private LayoutInflater mInflater;
    private BaseActivity mActivity;
    private List<ClassMainData> result;

    public ClassifiAdapter(BaseActivity activity) {
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.classify_recycle_item, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ClassMainData data = result.get(position);
        holder.mTvClassify.setText(data.getName());
        if (position == 0) {
            holder.mTvClassify.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.svg_class_one,0,0);
        } else if (position == 1) {
            holder.mTvClassify.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.svg_class_two,0,0);
        } else if (position == 2) {
            holder.mTvClassify.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.svg_class_three,0,0);
        } else if (position == 3) {
            holder.mTvClassify.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.svg_class_four,0,0);
        } else if (position == 4) {
            holder.mTvClassify.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.svg_class_five,0,0);
        }
        holder.mRecyclerView.setAdapter(new ClassGriedAdapter(mActivity, data.getChildren()));
    }

    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }

    public void setResult(List<ClassMainData> result) {
        this.result = result;
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvClassify;
        private RecyclerView mRecyclerView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTvClassify = (TextView) itemView.findViewById(R.id.tv_classify_name);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycle_view);
            GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
            mRecyclerView.setLayoutManager(manager);
        }
    }
}
