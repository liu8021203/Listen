package com.ting.myself;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.myself.MySeeInfo;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.adapter.MySeeAdapter;
import com.ting.bean.myself.MySeeResult;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.Map;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/10.
 * 我的关注frame;
 */
public class MySeeActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener{
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private MySeeAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Map<String, String> map = new HashMap<>();
    private TextView have_see_number;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_my_see);
        getData(0);
    }

    @Override
    protected String setTitle() {
        return "我的关注";
    }

    @Override
    protected void initView() {
        mSwipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        have_see_number = (TextView) findViewById(R.id.have_see_number);
        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        CustomItemDecoration decoration = new CustomItemDecoration(1, 0xffebebeb);
        decoration.setDividerSize(1);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }





    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_refresh:
                getData(0);
                break;
            default:
                break;
        }
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


    /**
     * 取消关注
     * @param mySeeInfo
     */
    public void cancleFouces(final MySeeInfo mySeeInfo) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("bid", String.valueOf(mySeeInfo.getId()));
        map.put("op", "cancel");
        BaseObserver baseObserver = new BaseObserver<BaseResult>(){

            @Override
            protected void onStart() {
                showProgressDialog();
            }

            @Override
            public void success(BaseResult data) {
                super.success(data);
                removeProgressDialog();
                if(mAdapter != null){
                    mAdapter.remove(mySeeInfo);
                    mAdapter.notifyDataSetChanged();
                    String numStr = have_see_number.getText().toString();
                    int num = Integer.valueOf(numStr) - 1;
                    have_see_number.setText(String.valueOf(num));
                }
            }

            @Override
            public void error() {
                super.error();
                removeProgressDialog();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).setFocus(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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

    /**
     * 加载数据
     * @param type 0是刷新，1是加载更多
     */
    private void getData(final int type){
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("page", String.valueOf(page));
        map.put("count", AppData.offset);
        BaseObserver baseObserver = new BaseObserver<MySeeResult>(this){

            @Override
            protected void onStart() {
            }

            @Override
            public void success(MySeeResult data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                mSwipeToLoadLayout.setRefreshing(false);
                if(data.getData() != null){
                    if(mAdapter == null){
                        mAdapter = new MySeeAdapter(MySeeActivity.this);
                        mAdapter.setResult(data.getData());
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        if(type == 0){
                            mAdapter.setResult(data.getData());
                            mAdapter.notifyDataSetChanged();
                        }else{
                            mAdapter.addResult(data.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    have_see_number.setText(mAdapter.getItemCount() + "");
                    isPaging(data.getLenght(), mAdapter.getItemCount());
                }else{
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
        UtilRetrofit.getInstance().create(HttpService.class).getMyFriend(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }



}
