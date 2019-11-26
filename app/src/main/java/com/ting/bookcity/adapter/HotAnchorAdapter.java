package com.ting.bookcity.adapter;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.Data;
import com.ting.bookcity.HotHostActivity;
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
public class HotAnchorAdapter extends RecyclerView.Adapter<HotAnchorAdapter.ItemViewHolder>{
    private HotHostActivity activity;
    private LayoutInflater inflater;
    private ItemOnClickListener mItemOnClickListener;
    private FocusOnClickListener mFocusOnClickListener;
    private List<Data> data;

    public HotAnchorAdapter(HotHostActivity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.mItemOnClickListener = new ItemOnClickListener();
        this.mFocusOnClickListener = new FocusOnClickListener();
    }


    public void setData(List<Data> data) {
        this.data = data;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_gried_hot_anchor, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Data vo = data.get(position);
        UtilGlide.loadAnchorImg(activity, vo.getThumb(), holder.hot_anchor_image);
        holder.hot_anchor_name.setText(vo.getName());

        if (data.get(position).isFollowed()) {
            holder.tv_jia.setVisibility(View.GONE);
            holder.tv_fource.setTextColor(activity.getResources().getColor(R.color.c595758));
            holder.ll_fource.setBackgroundResource(R.drawable.grey_kuang);
            holder.tv_fource.setText("已关注");

        } else {
            holder.tv_jia.setVisibility(View.VISIBLE);
            holder.tv_fource.setTextColor(activity.getResources().getColor(R.color.cffaa3d));
            holder.ll_fource.setBackgroundResource(R.drawable.yello_kuang);
            holder.tv_fource.setText("关注");
        }
        holder.itemView.setTag(vo.getId());
        holder.ll_fource.setTag(vo);
        holder.itemView.setOnClickListener(mItemOnClickListener);
        holder.ll_fource.setOnClickListener(mFocusOnClickListener);
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

    private class FocusOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final Data vo = (Data) v.getTag();
            if(TokenManager.isLogin(activity)){
                if(vo.isFollowed()){
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", TokenManager.getUid(activity));
                    map.put("bid", vo.getId());
                    map.put("op","cancel");
                    BaseObserver baseObserver = new BaseObserver<BaseResult>(activity){
                        @Override
                        public void success(BaseResult data) {
                            super.success(data);
                            vo.setFocusFans(vo.getFocusFans() - 1);
                            vo.setIsFollowed(false);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void error() {
                        }
                    };
                    activity.mDisposable.add(baseObserver);
                    UtilRetrofit.getInstance().create(HttpService.class).setFocus(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                }else{
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", TokenManager.getUid(activity));
                    map.put("bid", vo.getId());
                    map.put("op","focus");
                    BaseObserver baseObserver = new BaseObserver<BaseResult>(activity){
                        @Override
                        public void success(BaseResult data) {
                            super.success(data);
                            vo.setFocusFans(vo.getFocusFans() + 1);
                            vo.setIsFollowed(true);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void error() {
                        }
                    };
                    activity.mDisposable.add(baseObserver);
                    UtilRetrofit.getInstance().create(HttpService.class).setFocus(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                }
            }else{
                Bundle bundle = new Bundle();
                activity.intent(LoginMainActivity.class, bundle);
            }
        }
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView hot_anchor_image;//主播头像
        private TextView hot_anchor_name;//主播名字
        private LinearLayout ll_fource;
        private TextView tv_jia;
        private TextView tv_fource;

        public ItemViewHolder(View itemView) {
            super(itemView);
            hot_anchor_image = (CircleImageView) itemView.findViewById(R.id.hot_anchor_image);
            hot_anchor_name = (TextView) itemView.findViewById(R.id.hot_anchor_name);
            ll_fource = (LinearLayout) itemView.findViewById(R.id.ll_fource);
            tv_jia = (TextView) itemView.findViewById(R.id.tv_jia);
            tv_fource = (TextView) itemView.findViewById(R.id.tv_fource);
        }
    }
}
