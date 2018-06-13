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
    private LayoutInflater inflater;
    private MyCardActivity myCardActivity;
    private List<MyCardBean> result;

    public ListenerCardAdapter(MyCardActivity myCardActivity) {
        this.myCardActivity = myCardActivity;
        inflater = inflater.from(myCardActivity);
    }




    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_my_litener_card, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final MyCardBean bean = result.get(position);
        if (!TextUtils.isEmpty(bean.getType())) {
            if (bean.getType().equals("年")) {
                holder.litener_card_item.setBackgroundResource(R.mipmap.listen_card_year);
            } else if (bean.getType().equals("月")) {
                holder.litener_card_item.setBackgroundResource(R.mipmap.listen_card_mouth);
            } else if (bean.getType().equals("季")) {
                holder.litener_card_item.setBackgroundResource(R.mipmap.listen_card_ji);
            }
        }

        holder.tv_anchor.setText(bean.getUsername());
        UtilGlide.loadAnchorImg(myCardActivity, bean.getThumb(), holder.card_image);

        holder.tv_card_qixian.setText("有效期:" + bean.getCreatetime() + "-" + bean.getExpire_time());


        holder.litener_card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("anchorId", bean.getZhubo_uid());
                myCardActivity.intent(AnchorMainActivity.class, bundle);

            }
        });
    }



    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }

    public void setResult(List<MyCardBean> result) {
        this.result = result;
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
