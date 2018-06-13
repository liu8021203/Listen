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
        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        getData(0);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    private void getData(final int type){
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("page", String.valueOf(page));
        map.put("count", "10");
        BaseObserver baseObserver = new BaseObserver<DouChildrenResult>(this){
            @Override
            public void success(DouChildrenResult data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                if(type == 0) {
                    if (adapter == null) {
                        adapter = new DouDetailsAdapter(DouDetailsActivity.this);
                        adapter.setData(data.getData());
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.setData(data.getData());
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    adapter.addData(data.getData());
                    adapter.notifyDataSetChanged();
                }
                page = data.getPage();
                isPagging(data.getLenght(), adapter.getItemCount());
            }

            @Override
            public void error() {
                if(type == 0) {
                    super.error();
                }
                if(type == 1){
                    mSwipeToLoadLayout.setLoadingMore(false);
                    page--;
                }
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).jifen_mingxi(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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
        page--;
        getData(1);
    }
}
