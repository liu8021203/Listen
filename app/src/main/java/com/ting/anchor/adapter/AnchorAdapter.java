package com.ting.anchor.adapter;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.HostVO;
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
 * Created by liu on 2017/11/7.
 */

public class AnchorAdapter extends RecyclerView.Adapter<AnchorAdapter.ItemViewHolder> implements StickyRecyclerHeadersAdapter<AnchorAdapter.HeaderViewHolder>{
    private LayoutInflater mInflater;
    private BaseActivity mActivity;
    private List<HostVO> data;
    private ItemOnClickListener mListener;
    private FocusOnClickListener mFocusOnClickListener;
    private HostVO curVO;


    public AnchorAdapter(BaseActivity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mListener = new ItemOnClickListener();
        mFocusOnClickListener = new FocusOnClickListener();
    }

    public void setData(List<HostVO> data) {
        this.data = data;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_anchor_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public long getHeaderId(int position) {
        return data.get(position).getFirstStr().charAt(0);
    }



    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.recycle_anchor_header_item, parent, false);
        HeaderViewHolder holder = new HeaderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        HostVO vo = data.get(position);
        holder.tvHeader.setText(vo.getFirstStr());
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        HostVO vo = data.get(position);
        UtilGlide.loadHeadImg(mActivity, vo.getUserImage(), holder.ivImg);
        holder.tvName.setText(vo.getNickname());
        holder.itemView.setTag(vo);
        holder.itemView.setOnClickListener(mListener);
        if(vo.getFocus() == 1){
            holder.rlFocus.setBackgroundResource(R.drawable.anchor_unfocus_btn);
            holder.tvFocus.setText("关注");
            holder.tvFocus.setTextColor(0xff46bafc);
            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.anchor_unfocus, 0,0,0);
        }else{
            holder.rlFocus.setBackgroundResource(R.drawable.anchor_focus_btn);
            holder.tvFocus.setText("已关注");
            holder.tvFocus.setTextColor(0xff666666);
            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.anchor_focus, 0,0,0);
        }
        holder.rlFocus.setTag(vo);
        holder.rlFocus.setOnClickListener(mFocusOnClickListener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView ivImg;
        private TextView tvName;
        private TextView tvFocus;
        private RelativeLayout rlFocus;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvFocus = itemView.findViewById(R.id.tv_focus);
            rlFocus = itemView.findViewById(R.id.rl_focus);
        }
    }


    protected class HeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvHeader;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tv_header);
        }
    }

    private class ItemOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            HostVO vo = (HostVO) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("anchorId", vo.getId());
            mActivity.intent(AnchorMainActivity.class, bundle);
        }
    }

    private class FocusOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            HostVO vo = (HostVO) v.getTag();
            if(!TokenManager.isLogin(mActivity)){
                mActivity.intent(LoginMainActivity.class);
                return;
            }else{
                curVO = vo;
                if(vo.getFocus() == 0){
                    cancel();
                }else{
                    focus();
                }
            }
        }
    }


    private void cancel(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("hostId", curVO.getId());
        BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                curVO.setFocus(1);
                notifyDataSetChanged();
            }

        };
        mActivity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).cancleFocusHost(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    private void focus(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("hostId", curVO.getId());
        BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult data) {
                super.success(data);
                curVO.setFocus(0);
                notifyDataSetChanged();
            }
        };
        mActivity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).focusHost(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
