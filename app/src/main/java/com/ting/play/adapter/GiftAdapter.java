package com.ting.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.anchor.dialog.GiftDialog;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.anchor.LiWuMess;
import com.ting.bean.vo.GiftVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.play.subview.PlayIntroduceSubView;
import com.ting.base.ListenDialog;
import com.ting.util.UtilGlide;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/7/16.
 */

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ItemViewHolder> {
    private BaseActivity mActivity;
    private List<GiftVO> data;
    private LayoutInflater mInflater;
    private GiftDialog mDialog;
    private String hostId;

    public GiftAdapter(BaseActivity activity, GiftDialog dialog) {
        this.mActivity = activity;
        this.mDialog = dialog;
        mInflater = LayoutInflater.from(activity);
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public void setData(List<GiftVO> data) {
        this.data = data;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.liwu_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final GiftVO vo = data.get(position);
        UtilGlide.loadImg(mActivity, vo.getGiftImage(), holder.liwu_image);
        holder.liwu_text.setText(vo.getGiftName());
        holder.liwu_price_text.setText(vo.getGiftPrice() + "听豆");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                ListenDialog.makeListenDialog(mActivity, null, "赠送一个" + vo.getGiftName() + "将会扣除" + vo.getGiftPrice() + "听豆，是否赠送？", true, "否", true, "是", new ListenDialog.CallBackListener() {
                    @Override
                    public void callback(ListenDialog dialog, int mark) {
                        dialog.dismiss();
                        if (mark == ListenDialog.RIGHT) {
                            BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity) {
                                @Override
                                public void success(BaseResult data) {
                                    super.success(data);
                                    mActivity.showToast("谢谢您的礼物！！");
                                }
                            };
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("uid", TokenManager.getUid(mActivity));
                            map.put("giftId", vo.getId());
                            map.put("hostId", hostId);
                            mActivity.mDisposable.add(baseObserver);
                            UtilRetrofit.getInstance().create(HttpService.class).sendGift(map)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);

                        }
                    }
                }).show();
            }


        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView liwu_image;
        private TextView liwu_text;
        private TextView liwu_price_text;

        public ItemViewHolder(View itemView) {
            super(itemView);
            liwu_image = (ImageView) itemView.findViewById(R.id.liwu_image);
            liwu_text = (TextView) itemView.findViewById(R.id.liwu_text);
            liwu_price_text = (TextView) itemView.findViewById(R.id.liwu_price_text);
        }
    }
}
