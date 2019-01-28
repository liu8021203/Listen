package com.ting.bookcity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.Data;
import com.ting.bookcity.adapter.ActivityAnchorAdapter;
import com.ting.bookcity.adapter.HotAnchorAdapter;
import com.ting.bean.home.HotAnchorResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.constant.StaticConstant;
import com.ting.util.UtilRetrofit;
import com.ting.view.GridItemDecoration;
import com.ting.view.LfLayoutManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 16/6/29.
 */
public class HotHostActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mHotRecyclerView;
    private RecyclerView mActiveRecyclerView;
    private ActivityAnchorAdapter adapter;
    private Map<String, String> map = new HashMap<>();
    private HotAnchorResult hotAnchorResult;
    private Data vo;
    private RelativeLayout rl_hot_anchor;
    private HotAnchorAdapter hotAnchorAdapter;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_host);
    }

    @Override
    protected String setTitle() {
        return "热门主播";
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void initView() {
        rl_hot_anchor = (RelativeLayout) findViewById(R.id.rl_hot_anchor);
        mHotRecyclerView = (RecyclerView) findViewById(R.id.gv_hot_anshor);
        mActiveRecyclerView = (RecyclerView) findViewById(R.id.active_recycle_view);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mHotRecyclerView.setLayoutManager(manager);
        GridItemDecoration decoration = new GridItemDecoration(mActivity, R.drawable.money_grid, 3);
        mHotRecyclerView.addItemDecoration(decoration);
        LfLayoutManager layoutManager = new LfLayoutManager(this);
        layoutManager.setScrollEnabled(false);
        mActiveRecyclerView.setLayoutManager(layoutManager);
    }

    private void getData(){
        if (TokenManager.isLogin(this)) {
            map.put("uid", TokenManager.getUid(this));
        }
        BaseObserver baseObserver = new BaseObserver<HotAnchorResult>(this){
            @Override
            public void success(HotAnchorResult data) {
                super.success(data);
                showResult(data);
            }

            @Override
            public void error() {
                super.error();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getHotBroadcaster(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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

    /**
     * 显示数据
     */
    public void showResult(HotAnchorResult result) {
        this.hotAnchorResult = result;
        if (result.getHotdata() != null && result.getHotdata().size() > 0) {

            if (hotAnchorAdapter == null) {
                hotAnchorAdapter = new HotAnchorAdapter(this);
                hotAnchorAdapter.setData(result.getHotdata());
                mHotRecyclerView.setAdapter(hotAnchorAdapter);
            } else {
                hotAnchorAdapter.setData(result.getHotdata());
                hotAnchorAdapter.notifyDataSetChanged();
            }
        } else {
            rl_hot_anchor.setVisibility(View.GONE);
        }
        if (result.getActivedata() != null && result.getActivedata().size() > 0) {
            if (adapter == null) {
                adapter = new ActivityAnchorAdapter(this);
                adapter.setData(hotAnchorResult.getActivedata());
                mActiveRecyclerView.setAdapter(adapter);
            } else {
                adapter.setData(hotAnchorResult.getActivedata());
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 关 主播
     */
    public void seeAnchor(Data vo, String type) {
        this.vo = vo;
//        map.put("uid", String.valueOf(StaticConstant.userInfoResult.getUid()));
        map.put("bid", vo.getId());
        map.put("op", "focus");
//        hotAnchorController.SeeCancleAnchor(map, "focus");
        this.type = type;

    }

    /**
     * 取 关 主播
     */
    public void CancleAnchor(Data vo, String type) {
        this.vo = vo;
//        map.put("uid", String.valueOf(StaticConstant.userInfoResult.getUid()));
        map.put("bid", vo.getId());
        map.put("op", "cancel");
//        hotAnchorController.SeeCancleAnchor(map, "cancel");
        this.type = type;

    }

    /**
     * 关 取 主播成功返回结果
     */
    public void FouCanRe() {
        if (vo.isFollowed()) {
            vo.setFocusFans(vo.getFocusFans() - 1);
            vo.setIsFollowed(false);
        } else {
            vo.setFocusFans(vo.getFocusFans() + 1);
            vo.setIsFollowed(true);
        }
        if (type.equals("hot")) {
            hotAnchorAdapter.notifyDataSetChanged();
        } else if (type.equals("activity")) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
