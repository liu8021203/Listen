package com.ting.myself.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.bean.myself.MyCardBean;
import com.ting.bean.vo.CardListVO;
import com.ting.myself.MyCardActivity;
import com.ting.util.UtilDate;
import com.ting.util.UtilGlide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by gengjiajia on 15/9/10.
 * 我的作品列表适配器
 */
public class ListenerCardAdapter extends RecyclerView.Adapter<ListenerCardAdapter.ItemViewHolder> {
    private MyCardActivity myCardActivity;
    private List<CardListVO> data;

    public ListenerCardAdapter(MyCardActivity myCardActivity) {
        this.myCardActivity = myCardActivity;
    }




    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_litener_card, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final CardListVO bean = data.get(position);
        holder.tv_anchor.setText("主播听书卡《" + bean.getHostName() + "》");
        UtilGlide.loadAnchorImg(myCardActivity, bean.getHostImage(), holder.card_image);
        holder.tv_card_qixian.setText("有效期:" + bean.getStartTime() + "---" + bean.getEndTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("anchorId", bean.getHostId());
                myCardActivity.intent(AnchorMainActivity.class, bundle);

            }
        });
    }



    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<CardListVO> result) {
        this.data = result;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout litener_card_item;
        private CircleImageView card_image;
        private TextView tv_anchor;
        private TextView tv_card_qixian;


        public ItemViewHolder(View itemView) {
            super(itemView);
            litener_card_item =  itemView.findViewById(R.id.litener_card_item);
            card_image =  itemView.findViewById(R.id.card_image);
            tv_card_qixian =  itemView.findViewById(R.id.tv_card_qixian);
            tv_anchor =  itemView.findViewById(R.id.tv_anchor);
        }
    }
}
