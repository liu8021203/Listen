package com.ting.myself.adapter;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.myself.MySeeInfo;
import com.ting.bean.vo.HostVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.MySeeActivity;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gengjiajia on 15/9/10.
 * 我的作品列表适配器
 */
public class MySeeAdapter extends RecyclerView.Adapter<MySeeAdapter.ItemViewHolder> {
    private LayoutInflater inflater;
    private MySeeActivity mActivity;
    private List<HostVO> result;
    private ItemOnClickListener mListener;
    private CancleFocusOnClickListener mFocusOnClickListener;

    public MySeeAdapter(MySeeActivity mActivity) {
        this.mActivity = mActivity;
        inflater = inflater.from(mActivity);
        mListener = new ItemOnClickListener();
        mFocusOnClickListener = new CancleFocusOnClickListener();
    }

    public void remove(MySeeInfo info){
        if(result != null && info != null){
            result.remove(info);
        }
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_anchor_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        HostVO info = result.get(position);
        UtilGlide.loadAnchorImg(mActivity, info.getUserImage(), holder.ivImg);


        holder.tvName.setText(info.getNickname());
        holder.itemView.setTag(info);
        holder.itemView.setOnClickListener(mListener);
        holder.rlFocus.setTag(info);
        holder.rlFocus.setOnClickListener(mFocusOnClickListener);
    }


    @Override
    public int getItemCount() {
        return result != null ? result.size() : 0;
    }


    public void setResult(List<HostVO> result) {
        this.result = result;
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView ivImg;//头像
        private TextView tvName;//名字
        private RelativeLayout rlFocus;//我的
        private TextView tvFocus;  //关注

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImg =  itemView.findViewById(R.id.iv_img);
            tvName =  itemView.findViewById(R.id.tv_name);
            rlFocus =  itemView.findViewById(R.id.rl_focus);
            tvFocus =  itemView.findViewById(R.id.tv_focus);
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


    private class CancleFocusOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final HostVO vo = (HostVO) v.getTag();
            Map<String, String> map = new HashMap<>();
            map.put("uid", TokenManager.getUid(mActivity));
            map.put("hostId", vo.getId());
            BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
                @Override
                public void success(BaseResult data) {
                    super.success(data);
                    result.remove(vo);
                    notifyDataSetChanged();
                    mActivity.updateNum(result.size());
                    if(result.isEmpty()){
                        mActivity.showEmpty();
                    }
                }

            };
            mActivity.mDisposable.add(baseObserver);
            UtilRetrofit.getInstance().create(HttpService.class).cancleFocusHost(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
        }
    }
}
