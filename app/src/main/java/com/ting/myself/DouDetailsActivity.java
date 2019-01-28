package com.ting.myself;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.ExpenseResult;
import com.ting.bean.myself.DouChildrenBean;
import com.ting.bean.myself.DouChildrenResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.adapter.DouDetailsAdapter;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gengjiajia on 16/7/27.
 * <p/>
 * 听豆明细activity
 */
public class DouDetailsActivity extends BaseActivity implements OnLoadMoreListener{
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private DouDetailsAdapter adapter;
    private Map<String, String> map = new HashMap<>();
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dou_details);
    }

    @Override
    protected String setTitle() {
        return "听豆明细";
    }
    @Override
    protected void initView() {
        mRecyclerView =  findViewById(R.id.swipe_target);
        mSwipeToLoadLayout =  findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        getData(BaseObserver.MODEL_ALL);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    private void getData(final int model){
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("page", String.valueOf(page));
        map.put("size", "10");
        BaseObserver baseObserver = new BaseObserver<BaseResult<ExpenseResult>>(this, model){
            @Override
            public void success(BaseResult<ExpenseResult> data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                ExpenseResult result = data.getData();
                if(result != null) {
                    if (model == BaseObserver.MODEL_ALL) {
                        if (adapter == null) {
                            adapter = new DouDetailsAdapter(DouDetailsActivity.this);
                            adapter.setData(result.getList());
                            mRecyclerView.setAdapter(adapter);
                        } else {
                            adapter.setData(result.getList());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        adapter.addData(result.getList());
                        adapter.notifyDataSetChanged();
                    }
                    page = result.getPage();
                    isPagging(result.getCount(), adapter.getItemCount());
                }
            }


            @Override
            public void error(BaseResult<ExpenseResult> value, Throwable e) {
                super.error(value, e);
                if(model != BaseObserver.MODEL_ALL){
                    mSwipeToLoadLayout.setLoadingMore(false);
                    page--;
                }
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).expense(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }



    private void isPagging(int total, int size) {
        if (total > size) {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        getData(BaseObserver.MODEL_SHOW_TOAST);
    }
}
