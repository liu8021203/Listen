package com.ting.anchor.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.bean.anchor.Reward;
import com.ting.bean.anchor.RewardMess;
import com.ting.bean.vo.GiftDetailVO;
import com.ting.util.UtilGlide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gengjiajia on 15/9/1.
 * 打赏主播listview适配器
 */
public class RewardAnchorAdapter extends RecyclerView.Adapter<RewardAnchorAdapter.ItemViewHolder> {
    private AnchorMainActivity mActivity;
    private LayoutInflater inflater;
    private List<GiftDetailVO> data;

    public RewardAnchorAdapter(AnchorMainActivity activity) {
        this.mActivity = activity;
        this.inflater = LayoutInflater.from(mActivity);
    }

    public void setData(List<GiftDetailVO> data) {
        this.data = data;
    }



    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_reward_anchor, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        GiftDetailVO vo = data.get(position);
        UtilGlide.loadAnchorImg(mActivity, vo.getUserImage(), holder.reward_touxiang);
        if(!TextUtils.isEmpty(vo.getNickname())) {
            holder.reward_name.setText(vo.getNickname());
        }else{
            holder.reward_name.setText("佚名");
        }
        holder.reward_paiming.setText("TOP：" + (position + 1));
        holder.reward_number.setText(String.valueOf(vo.getTotalPrice()));
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
        private CircleImageView reward_touxiang;//打赏头像
        private TextView reward_name;//打赏名字
        private TextView reward_paiming;//打赏排名
        private TextView reward_number;//打赏听豆

        public ItemViewHolder(View itemView) {
            super(itemView);
            reward_touxiang =  itemView.findViewById(R.id.reward_touxiang);
            reward_name =  itemView.findViewById(R.id.reward_name);
            reward_paiming =  itemView.findViewById(R.id.reward_paiming);
            reward_number =  itemView.findViewById(R.id.reward_number);
        }
    }

}
