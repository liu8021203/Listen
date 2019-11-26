package com.ting.bookcity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.BookVO;
import com.ting.category.adapter.CategoryListAdapter;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/8/7.
 */

public class TeamBookListActivity extends BaseActivity{
    private String id;
    private String teamTitle;
    private RecyclerView mRecyclerView;
    private CategoryListAdapter mAdapter;

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
        setCenterTitle(teamTitle);
    }

    @Override
    protected void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("teamId", id);
        BaseObserver baseObserver = new BaseObserver<BaseResult<List<BookVO>>>(this, BaseObserver.MODEL_ALL){
            @Override
            public void success(BaseResult<List<BookVO>> data) {
                super.success(data);
                if(mAdapter == null){
                    mAdapter = new CategoryListAdapter(mActivity);
                    mAdapter.setData(data.getData());
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.setData(data.getData());
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getTeamBooks(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("teamId", null);
        teamTitle = bundle.getString("teamTitle", null);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }
}
