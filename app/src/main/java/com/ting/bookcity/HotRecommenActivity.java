package com.ting.bookcity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.base.BaseActivity;
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
 * 精品推荐subview
 */
public class HotRecommenActivity extends BaseActivity {
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private CategoryListAdapter mAdapter;
    private int page = 1;
    private String activityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_finerecommend_subview);
    }

    @Override
    protected String setTitle() {
        return "热门推荐";
    }

    @Override
    protected void initView() {
        mRecyclerView =  findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1,0xffebebeb);
        mRecyclerView.addItemDecoration(decoration);
        mSwipeToLoadLayout = flContent.findViewById(R.id.swipeToLoadLayout);
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
        requestData(BaseObserver.MODEL_ALL, 1);
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        activityId = bundle.getString("activityId", null);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }




    private void requestData(final int model, final int p) {
        BaseObserver observer = new BaseObserver<BaseResult<CategoryListResult>>(this, model) {
            @Override
            public void success(BaseResult<CategoryListResult> data) {
                super.success(data);
                CategoryListResult result = data.getData();
                if (model == BaseObserver.MODEL_ALL) {
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
                        showErrorEmpty("木有相关热门书籍~");
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
        map.put("activityId", activityId);
        map.put("page", String.valueOf(p));
        map.put("size", "10");
        UtilRetrofit.getInstance().create(HttpService.class).hotRecommendList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);

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
