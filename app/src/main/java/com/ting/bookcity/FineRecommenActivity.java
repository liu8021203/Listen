package com.ting.bookcity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bookcity.adapter.FineRecomListAdapter;
import com.ting.bean.home.FineRecommendResult;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 精品推荐subview
 */
public class FineRecommenActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private FineRecomListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_finerecommend_subview);
    }

    @Override
    protected String setTitle() {
        return "精品推荐";
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1,0xffebebeb);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        getData();
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    private void getData() {
        BaseObserver baseObserver = new BaseObserver<FineRecommendResult>(mActivity){
            @Override
            public void success(FineRecommendResult data) {
                super.success(data);
                if(data.getRecommend() != null && data.getRecommend().size() > 0){
                    if(mAdapter == null){
                        mAdapter = new FineRecomListAdapter(mActivity);
                        mAdapter.setData(data.getRecommend());
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        mAdapter.setData(data.getRecommend());
                        mAdapter.notifyDataSetChanged();
                    }
                }else{
                    errorEmpty();
                }
            }

            @Override
            public void error() {
                super.error();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getRecommend().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

}
