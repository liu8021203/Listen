package com.ting.classification.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.base.BaseObserver;
import com.ting.classification.adapter.ClassMoreNewAdapter;
import com.ting.bean.classfi.ClassIntroduceResult;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/7/7.
 */

public class ClassifyFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ClassMoreNewAdapter mAdapter;
    private Map<String, String> map = new HashMap<>();
    private int page = 1;
    private String requestType;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = bundle.getString("cateId", null);
        requestType = bundle.getString("requestType", null);
    }

    @Override
    protected void initView() {
        mSwipeToLoadLayout = (SwipeToLoadLayout) flContent.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = (RecyclerView) flContent.findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1, 0xffebebeb);
        decoration.setDividerSize(1);
        mRecyclerView.addItemDecoration(decoration);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("aaa", "onActivityCreated" + requestType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("aaa", "onDestroyView" + requestType);
    }

    public static ClassifyFragment getInstance() {
        ClassifyFragment fragment = new ClassifyFragment();
        return fragment;
    }

    @Override
    protected void initData() {
        map.put("cateID", id);
        map.put("requestType", requestType);
        map.put("page", String.valueOf(page));
        map.put("count", "10");
        BaseObserver baseObserver = new BaseObserver<ClassIntroduceResult>(this) {


            @Override
            public void onComplete() {
                super.onComplete();
            }

            @Override
            public void success(ClassIntroduceResult data) {
                super.success(data);

                if (data.getData() != null) {
                    mAdapter = new ClassMoreNewAdapter(mActivity);
                    mAdapter.setData(data.getData());
                    mRecyclerView.setAdapter(mAdapter);
                    isPaging(data.getLenght(), mAdapter.getItemCount());
                } else {
                    errorEmpty();
                }
            }

            @Override
            public void error() {
                super.error();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getCategoryData(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    private void getData(final int type) {
        map.put("cateID", id);
        map.put("requestType", requestType);
        map.put("page", String.valueOf(page));
        map.put("count", "10");
        BaseObserver baseObserver = new BaseObserver<ClassIntroduceResult>() {


            @Override
            public void onComplete() {
                super.onComplete();
                mSwipeToLoadLayout.setLoadingMore(false);
                mSwipeToLoadLayout.setRefreshing(false);
            }

            @Override
            public void success(ClassIntroduceResult data) {
                super.success(data);

                if (data.getData() != null) {
                    if (mAdapter == null) {
                        mAdapter = new ClassMoreNewAdapter(mActivity);
                        mAdapter.setData(data.getData());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        if (type == 0) {
                            mAdapter.setData(data.getData());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.addData(data.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    isPaging(data.getLenght(), mAdapter.getItemCount());
                } else {
                    errorEmpty();
                }
            }

            @Override
            public void error() {
                super.error();
                page--;
                mSwipeToLoadLayout.setLoadingMore(false);
                mSwipeToLoadLayout.setRefreshing(false);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getCategoryData(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classify;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setActionBar() {
        return 0;
    }

    @Override
    protected void reload() {

    }


    @Override
    public void onLoadMore() {
        page++;
        getData(1);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData(0);
    }
}
