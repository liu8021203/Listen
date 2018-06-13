package com.ting.myself;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.record.adapter.CollectionAdapter;
import com.ting.bean.myself.CollectResult;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/8/7.
 */

public class CollectActivity extends BaseActivity implements OnRefreshListener{
    private RecyclerView mRecyclerView;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private CollectionAdapter collectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected String setTitle() {
        return "我的收藏";
    }

    @Override
    protected void initData() {
        getData(true);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }



    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1,0xffebebeb);
        mRecyclerView.addItemDecoration(decoration);
    }





    @Override
    public void onRefresh() {
        getData(false);
    }

    private void getData(boolean b){
        BaseObserver baseObserver = new BaseObserver<CollectResult>(this, b){
            @Override
            public void success(CollectResult data) {
                super.success(data);
                mSwipeToLoadLayout.setRefreshing(false);
                if(data.getData() != null && data.getData().size() > 0){
                    if(collectionAdapter == null){
                        collectionAdapter = new CollectionAdapter(mActivity);
                        collectionAdapter.setData(data.getData());
                        mRecyclerView.setAdapter(collectionAdapter);
                    }else{
                        collectionAdapter.setData(data.getData());
                        collectionAdapter.notifyDataSetChanged();
                    }
                }else{
                    errorEmpty();
                }
            }

            @Override
            public void error() {
                super.error();
                mSwipeToLoadLayout.setRefreshing(false);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getFavorite(TokenManager.getUid(mActivity)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }
}
