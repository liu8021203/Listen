package com.ting.myself;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseCommonAdapter;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.BookListResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.adapter.BookListAdapter;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BuyBookActivity extends BaseActivity{
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private BookListAdapter mAdapter;
    private Map<String, String> map = new HashMap<>();

    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
    }

    @Override
    protected String setTitle() {
        return "已购书籍";
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = manager.findLastVisibleItemPosition();
                if (lastVisibleItem + 1 >= mAdapter.getItemCount() && !mAdapter.isLoadMore() && mAdapter.isEnableLoadMore()) {
                    if (mAdapter != null) {
                        mAdapter.loadMore();
                        mAdapter.notifyDataSetChanged();
                        page++;
                        requestData(BaseObserver.MODEL_LOADMORE);
                    }
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF8C00"));
        //设置下拉刷新的监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                requestData(BaseObserver.MODEL_REFRESH);
            }
        });
    }

    @Override
    protected void initData() {
        requestData(BaseObserver.MODEL_LAYOUT);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    private void requestData(final int model) {
        map.put("page", String.valueOf(page));
        map.put("size", "10");
        map.put("uid", TokenManager.getUid(this));
        BaseObserver observer = new BaseObserver<BaseResult<BookListResult>>(this, model) {
            @Override
            public void success(BaseResult<BookListResult> data) {
                super.success(data);
                BookListResult result = data.getData();
                if (model == BaseObserver.MODEL_LAYOUT) {
                    if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                        mAdapter = new BookListAdapter(mActivity);
                        mAdapter.setData(result.getList());
                        mAdapter.setListener(new BaseCommonAdapter.LoadMoreOnClickListener() {
                            @Override
                            public void click() {
                                mAdapter.loadMore();
                                mAdapter.notifyDataSetChanged();
                                page++;
                                requestData(BaseObserver.MODEL_ONLY_SHOW_TOAST);
                            }
                        });
                        if (mAdapter.getData().size() >= result.getCount()) {
                            mAdapter.setShowFooterDesc("---- 到底了 ----");
                        } else {
                            mAdapter.setEnableLoadMore(true);
                        }
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        showErrorEmpty("还没有内容~");
                    }
                } else if (model == BaseObserver.MODEL_REFRESH) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                        if (mAdapter != null) {
                            mAdapter.setData(result.getList());
                            if (mAdapter.getData().size() >= result.getCount()) {
                                mAdapter.setShowFooterDesc("---- 到底了 ----");
                            } else {
                                mAdapter.setEnableLoadMore(true);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }else{
                        showErrorEmpty("还没有内容~");
                    }
                } else {
                    if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                        if (mAdapter != null) {
                            mAdapter.addData(result.getList());
                            mAdapter.loadMoreComplete();
                            if (mAdapter.getData().size() >= result.getCount()) {
                                mAdapter.setShowFooterDesc("---- 到底了 ----");
                            } else {
                                mAdapter.setEnableLoadMore(true);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }


            @Override
            public void error(BaseResult<BookListResult> value, Throwable e) {
                super.error(value, e);
                if (mAdapter != null) {
                    page--;
                    mAdapter.setClickLoadMore(true);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        mDisposable.add(observer);
        UtilRetrofit.getInstance().create(HttpService.class).buyBookList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    @Override
    protected void reload() {
        super.reload();
        initData();
    }
}
