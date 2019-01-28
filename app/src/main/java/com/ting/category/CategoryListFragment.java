package com.ting.category;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.home.CategoryListResult;
import com.ting.category.adapter.CategoryListAdapter;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
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
                requestData(BaseObserver.MODEL_SHOW_TOAST, page);
            }
        });
    }

    @Override
    protected void initData() {
        requestData(BaseObserver.MODEL_SHOW_PROGRESSBAR, 1);
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
        requestData(BaseObserver.MODEL_SHOW_PROGRESSBAR, 1);
    }

    private void requestData(final int model, final int p) {
        BaseObserver observer = new BaseObserver<BaseResult<CategoryListResult>>(this, model) {
            @Override
            public void success(BaseResult<CategoryListResult> data) {
                super.success(data);
                CategoryListResult result = data.getData();
                if (model == BaseObserver.MODEL_SHOW_PROGRESSBAR) {
                    if (result.getList() != null && !result.getList().isEmpty()) {
                        if (mAdapter == null) {
                            mAdapter = new CategoryListAdapter(mActivity);
                            mAdapter.setData(result.getList());
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
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
}
