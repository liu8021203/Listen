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
    private List<LiWuMess> data;
    private LayoutInflater mInflater;
    private GiftDialog mDialog;
    private int bookId;
    private int anchorId;
    private PlayIntroduceSubView mSubView;

    public GiftAdapter(BaseActivity activity, GiftDialog dialog) {
        this.mActivity = activity;
        this.mDialog = dialog;
        mInflater = LayoutInflater.from(activity);
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public void setData(List<LiWuMess> data) {
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
        final LiWuMess vo = data.get(position);
        UtilGlide.loadImg(mActivity, vo.getThumb(), holder.liwu_image);
        holder.liwu_text.setText(vo.getName());
        holder.liwu_price_text.setText(vo.getPrice() + "听豆");
        if (position == 3 || position == 7) {
            holder.v_line_right.setVisibility(View.GONE);
        } else {
            holder.v_line_right.setVisibility(View.VISIBLE);
        }
        if (position == 4 || position == 5 || position == 6 || position == 7) {
            holder.v_line_bottom.setVisibility(View.GONE);
        } else {
            holder.v_line_bottom.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                ListenDialog.makeListenDialog(mActivity, null, "赠送一个" + vo.getName() + "将会扣除" + vo.getPrice() + "听豆，是否赠送？", true, "否", true, "是", new ListenDialog.CallBackListener() {
                    @Override
                    public void callback(ListenDialog dialog, int mark) {
                        dialog.dismiss();
                        if (mark == ListenDialog.RIGHT) {
                            if (mActivity instanceof AnchorMainActivity) {
                                BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity) {
                                    @Override
                                    public void success(BaseResult data) {
                                        super.success(data);
                                        mActivity.showToast("谢谢您的礼物！！");
                                    }

                                    @Override
                                    public void error() {
                                    }
                                };
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("uid", TokenManager.getUid(mActivity));
                                map.put("cid", String.valueOf(anchorId));
                                map.put("price", String.valueOf(vo.getPrice()));
                                mActivity.mDisposable.add(baseObserver);
                                UtilRetrofit.getInstance().create(HttpService.class).setGift(map)
                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                            } else {
                                BaseObserver baseObserver = new BaseObserver<BaseResult>(mActivity) {
                                    @Override
                                    public void success(BaseResult data) {
                                        super.success(data);
                                        mActivity.showToast("谢谢您的礼物！！");
                                    }

                                    @Override
                                    public void error() {
                                    }
                                };
                                mActivity.mDisposable.add(baseObserver);
                                UtilRetrofit.getInstance().create(HttpService.class).setForce(TokenManager.getUid(mActivity), String.valueOf(bookId), String.valueOf(vo.getId()))
                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
                            }
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
        private View v_line_bottom;
        private View v_line_right;

        public ItemViewHolder(View itemView) {
            super(itemView);
            liwu_image = (ImageView) itemView.findViewById(R.id.liwu_image);
            liwu_text = (TextView) itemView.findViewById(R.id.liwu_text);
            liwu_price_text = (TextView) itemView.findViewById(R.id.liwu_price_text);
            v_line_bottom = itemView.findViewById(R.id.v_line_bottom);
            v_line_right = itemView.findViewById(R.id.v_line_right);
        }
    }
}
