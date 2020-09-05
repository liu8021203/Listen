package com.ting.category;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.home.CategoryListResult;
import com.ting.category.adapter.CategoryListAdapter;
import com.ting.common.http.HttpService;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/11/6.
 */

public class CategoryListFragment extends BaseFragment {
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private CategoryListAdapter mAdapter;
    private String id;
    private int page = 1;
    private TTAdNative mTTAdNative;
    private View adView;

    @Override
    protected void initView() {
        mSwipeToLoadLayout = flContent.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = flContent.findViewById(R.id.swipe_target);
        final LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                requestData(BaseObserver.MODEL_ONLY_SHOW_TOAST, page);
            }
        });


    }

    @Override
    protected void initData() {
        if (Integer.parseInt(id) % 2 == 0) {
            loadAd();
        } else {
            requestData(BaseObserver.MODEL_SHOW_PROGRESSBAR_LAYOUT, 1);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category_list;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getArguments();
        id = bundle.getString("id", null);
    }

    @Override
    protected int setActionBar() {
        return 0;
    }

    @Override
    protected void reload() {
        initData();
    }

    private void requestData(final int model, final int p) {
        BaseObserver observer = new BaseObserver<BaseResult<CategoryListResult>>(this, model) {
            @Override
            public void success(BaseResult<CategoryListResult> data) {
                super.success(data);
                CategoryListResult result = data.getData();
                if (model == BaseObserver.MODEL_SHOW_PROGRESSBAR_LAYOUT) {
                    if (result.getList() != null && !result.getList().isEmpty()) {
                        if (mAdapter == null) {
                            mAdapter = new CategoryListAdapter(mActivity);
                            mAdapter.setData(result.getList());
                            mAdapter.setAdView(adView);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setAdView(adView);
                            mAdapter.setData(result.getList());
                            mAdapter.notifyDataSetChanged();
                        }
                        isPaging(result.getCount(), mAdapter.getItemCount());
                    } else {
                        errorEmpty("木有相关分类书籍~");
                    }
                } else {
                    mSwipeToLoadLayout.setLoadingMore(false);
                    if (result.getList() != null && !result.getList().isEmpty()) {
                        if (mAdapter != null) {
                            mAdapter.addData(data.getData().getList());
                            mAdapter.notifyDataSetChanged();
                        }
                        isPaging(result.getCount(), mAdapter.getItemCount());
                    }
                }
            }


            @Override
            public void error(BaseResult<CategoryListResult> value, Throwable e) {
                super.error(value, e);
                page--;
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        };
        mDisposable.add(observer);
        Map<String, String> map = new HashMap<>();
        map.put("categoryId", String.valueOf(id));
        map.put("page", String.valueOf(p));
        map.put("size", "10");
        UtilRetrofit.getInstance().

                create(HttpService.class).

                categoryList(map).

                subscribeOn(Schedulers.io()).

                observeOn(AndroidSchedulers.mainThread()).

                subscribe(observer);

    }


    /**
     * 是否分页
     */
    private void isPaging(int total, int currTotal) {
        if (total > currTotal) {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        }
    }


    private void loadAd() {
        //设置广告参数
        int screenWidth = UtilPixelTransfrom.getScreenWidth(mActivity);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("945454395") //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(UtilPixelTransfrom.px2dip(mActivity,screenWidth), 0) //必填：期望个性化模板广告view的size,单位dp
                .build();
        mTTAdNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        //加载广告
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.d("aaa", "ad加载失败======" + s);
                requestData(BaseObserver.MODEL_SHOW_PROGRESSBAR_LAYOUT, 1);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }
                TTNativeExpressAd ad = list.get(0);
                ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int i) {

                    }

                    @Override
                    public void onAdShow(View view, int i) {

                    }

                    @Override
                    public void onRenderFail(View view, String s, int i) {
                        requestData(BaseObserver.MODEL_SHOW_PROGRESSBAR_LAYOUT, 1);
                    }

                    @Override
                    public void onRenderSuccess(View view, float v, float v1) {
                        requestData(BaseObserver.MODEL_SHOW_PROGRESSBAR_LAYOUT, 1);
                        adView = view;
                    }
                });
                ad.render();
            }
        });
    }

}
