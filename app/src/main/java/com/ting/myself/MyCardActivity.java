package com.ting.myself;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.myself.MyCardResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.constant.StaticConstant;
import com.ting.myself.adapter.ListenerCardAdapter;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 16/7/27.
 * 我的听书卡activity
 */
public class MyCardActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private ListenerCardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
    }

    @Override
    protected String setTitle() {
        return "我的听书卡";
    }
    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1, 0xfff8f8f8);
        decoration.setDividerSize(30);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(mActivity));
        map.put("page", "1");
        map.put("count", "100");
        BaseObserver baseObserver = new BaseObserver<MyCardResult>(this){
            @Override
            public void success(MyCardResult data) {
                super.success(data);
                if(data.getData() != null) {
                    showResult(data);
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
        UtilRetrofit.getInstance().create(HttpService.class).getCard(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    public void showResult(MyCardResult data) {
        adapter = new ListenerCardAdapter(this);
        adapter.setResult(data.getData());
        mRecyclerView.setAdapter(adapter);
    }
}
