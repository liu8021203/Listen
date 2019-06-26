package com.ting.myself;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.bean.vo.CardListVO;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.myself.adapter.ListenerCardAdapter;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.List;
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
        mRecyclerView = findViewById(R.id.swipe_target);
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
        BaseObserver baseObserver = new BaseObserver<BaseResult<List<CardListVO>>>(this, BaseObserver.MODEL_ALL){
            @Override
            public void success(BaseResult<List<CardListVO>> data) {
                super.success(data);
                List<CardListVO> list = data.getData();
                if(list != null && !list.isEmpty()) {
                    adapter = new ListenerCardAdapter((MyCardActivity) mActivity);
                    adapter.setData(list);
                    mRecyclerView.setAdapter(adapter);
                }else{
                    showErrorEmpty("还没有主播听书卡，快去购买吧。");
                }
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).cardList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @Override
    protected void reload() {
        super.reload();
        initData();
    }
}
