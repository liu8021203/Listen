package com.ting.bookcity.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.Data;
import com.ting.bean.home.HotAnchorVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.login.LoginMainActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/8/27.
 * 门主播适配器
 */
public class HomeAnchorAdapter extends RecyclerView.Adapter<HomeAnchorAdapter.ItemViewHolder>{
    private BaseActivity activity;
    private LayoutInflater inflater;
    private ItemOnClickListener mItemOnClickListener;
    private List<HotAnchorVO> data;

    public HomeAnchorAdapter(BaseActivity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.mItemOnClickListener = new ItemOnClickListener();
    }


    public void setData(List<HotAnchorVO> data) {
        this.data = data;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_home_anchor, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        HotAnchorVO vo = data.get(position);
        UtilGlide.loadAnchorImg(activity, vo.getThumb(), holder.hot_anchor_image);
        holder.hot_anchor_name.setText(vo.getUsername());
        holder.itemView.setTag(vo.getId());
        holder.itemView.setOnClickListener(mItemOnClickListener);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String id = (String) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("anchorId", id);
            activity.intent(AnchorMainActivity.class, bundle);
        }
    }



    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView hot_anchor_image;//主播头像
        private TextView hot_anchor_name;//主播名字

        public ItemViewHolder(View itemView) {
            super(itemView);
            hot_anchor_image = (CircleImageView) itemView.findViewById(R.id.hot_anchor_image);
            hot_anchor_name = (TextView) itemView.findViewById(R.id.hot_anchor_name);
        }
    }
}
