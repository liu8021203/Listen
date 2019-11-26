package com.ting.classification.adapter;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.classfi.ClassChilderMess;
import com.ting.classification.ClassChilActivity;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/14.
 * 分类griedview适配
 */
public class ClassGriedAdapter extends RecyclerView.Adapter<ClassGriedAdapter.ItemViewHolder> {
    private LayoutInflater mInflater;
    private BaseActivity activity;
    private List<ClassChilderMess> result;

    public ClassGriedAdapter(BaseActivity activity, List<ClassChilderMess> childer) {
        this.activity = activity;
        this.result = childer;
        this.mInflater = LayoutInflater.from(activity);
    }



    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.class_gried_adapter, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final ClassChilderMess vo = result.get(position);
        holder.mTvName.setText(vo.getName());
        holder.mTvName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("classItemID", vo.getId());
                bundle.putString("classItemName", vo.getName());
                activity.intent(ClassChilActivity.class, bundle);
            }
        });
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.class_children_name);
        }
    }
}
