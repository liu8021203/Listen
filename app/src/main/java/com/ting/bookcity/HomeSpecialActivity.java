package com.ting.bookcity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.home.HomeSpecialResult;
import com.ting.bookcity.adapter.SpecialAdapter;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/8/7.
 */

public class HomeSpecialActivity extends BaseActivity{
    private String id;
    private RecyclerView mRecyclerView;
    private SpecialAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_special);
    }

    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        BaseObserver baseObserver = new BaseObserver<HomeSpecialResult>(this){
            @Override
            public void success(HomeSpecialResult data) {
                super.success(data);
                if(mAdapter == null){
                    mAdapter = new SpecialAdapter((HomeSpecialActivity) mActivity);
                    mAdapter.setData(data.getBooklist());
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.setData(data.getBooklist());
                    mAdapter.notifyDataSetChanged();
                }
                setCenterTitle(data.getTitle());
            }

            @Override
            public void error() {
                super.error();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).get_special_info(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id", null);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }
}
