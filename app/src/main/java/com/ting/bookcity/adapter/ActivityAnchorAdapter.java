package com.ting.bookcity.adapter;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * 跃主播适配器
 */
public class ActivityAnchorAdapter extends RecyclerView.Adapter<ActivityAnchorAdapter.ItemViewHolder> {
    private HotHostActivity activity;
    private LayoutInflater inflater;
    private ItemOnClickListener mItemOnClickListener;
    private FocusOnClickListener mFocusOnClickListener;

    private List<Data> data;

    public ActivityAnchorAdapter(HotHostActivity activity) {
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
        View view = inflater.inflate(R.layout.hot_anchor_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Data vo = data.get(position);
        UtilGlide.loadAnchorImg(activity, vo.getThumb(), holder.hot_anchor_image);
        holder.tv_anchor_name.setText(vo.getName());
        if (vo.isFollowed()) {
            holder.iv_fource_state.setImageResource(R.drawable.svg_have_fource);
        } else {
            holder.iv_fource_state.setImageResource(R.drawable.svg_add_fource);
        }
        holder.tv_anchor_rank.setText(data.get(position).getGroupname());
        holder.tv_work_number.setText("作品:" + data.get(position).getWorksCount());
        holder.tv_fans_number.setText("粉丝:" + data.get(position).getFocusFans());

        holder.iv_fource_state.setTag(vo);
        holder.itemView.setTag(vo.getId());
        holder.iv_fource_state.setOnClickListener(mFocusOnClickListener);
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


    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView hot_anchor_image;//主播头像
        private ImageView iv_fource_state;
        private TextView tv_anchor_name;
        private TextView tv_anchor_rank;
        private TextView tv_work_number;
        private TextView tv_fans_number;

        public ItemViewHolder(View itemView) {
            super(itemView);
            hot_anchor_image = (CircleImageView) itemView.findViewById(R.id.hot_anchor_image);
            iv_fource_state = (ImageView) itemView.findViewById(R.id.iv_fource_state);
            tv_anchor_name = (TextView) itemView.findViewById(R.id.tv_anchor_name);
            tv_anchor_rank = (TextView) itemView.findViewById(R.id.tv_anchor_rank);
            tv_work_number = (TextView) itemView.findViewById(R.id.tv_work_number);
            tv_fans_number = (TextView) itemView.findViewById(R.id.tv_fans_number);
        }
    }
}
