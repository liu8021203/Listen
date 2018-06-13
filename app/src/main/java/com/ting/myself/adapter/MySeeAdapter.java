package com.ting.myself.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.bean.myself.MySeeInfo;
import com.ting.myself.MySeeActivity;
import com.ting.util.UtilGlide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by gengjiajia on 15/9/10.
 * 我的作品列表适配器
 */
public class MySeeAdapter extends RecyclerView.Adapter<MySeeAdapter.ItemViewHolder> {
    private LayoutInflater inflater;
    private MySeeActivity mActivity;
    private List<MySeeInfo> result;

    public MySeeAdapter(MySeeActivity mActivity) {
        this.mActivity = mActivity;
        inflater = inflater.from(mActivity);
    }

    public void remove(MySeeInfo info){
        if(result != null && info != null){
            result.remove(info);
        }
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_my_see_new, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final MySeeInfo info = result.get(position);
        UtilGlide.loadAnchorImg(mActivity, info.getThumb(), holder.my_see_image);


        holder.my_see_name.setText(info.getName());
        holder.my_see_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("anchorId", String.valueOf(info.getId()));
                mActivity.intent(AnchorMainActivity.class, bundle);
            }
        });

        holder.iv_cancle_fouce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.cancleFouces(info);
            }
        });
        holder.tv_anchor_rank.setText(info.getRankname());
        if(result.get(position).getLastupdate()!=null){
            holder.anchor_last_update.setText("最近更新:  "+result.get(position).getLastupdate().getTitle());
        }else{
            holder.anchor_last_update.setText("");
        }
    }


    @Override
    public int getItemCount() {
        return result != null ? result.size() : 0;
    }


    public void setResult(List<MySeeInfo> result) {
        this.result = result;
    }

    public void addResult(List<MySeeInfo> result){
        if(this.result != null && result != null){
            this.result.addAll(result);
        }
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView my_see_image;//头像
        private TextView my_see_name;//名字
        private RelativeLayout my_see_item;//我的
        private TextView iv_cancle_fouce;//取消关注
        private TextView tv_anchor_rank;//三星主播
        private TextView anchor_last_update;//最近收听

        public ItemViewHolder(View itemView) {
            super(itemView);
            my_see_item = (RelativeLayout) itemView.findViewById(R.id.my_see_item);
            my_see_image = (CircleImageView) itemView.findViewById(R.id.my_see_image);
            my_see_name = (TextView) itemView.findViewById(R.id.my_see_name);
            iv_cancle_fouce = (TextView) itemView.findViewById(R.id.iv_cancle_fouce);
            tv_anchor_rank = (TextView) itemView.findViewById(R.id.tv_anchor_rank);
            anchor_last_update = (TextView) itemView.findViewById(R.id.anchor_last_update);
            tv_anchor_rank = (TextView) itemView.findViewById(R.id.tv_anchor_rank);
        }
    }
}
