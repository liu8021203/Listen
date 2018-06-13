package com.ting.anchor.subview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.anchor.adapter.FollowAnchorAdapter;
import com.ting.bean.anchor.AnchorMessResult;
import com.ting.base.BaseObserver;
import com.ting.bean.anchor.Fans;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/1.
 * 关注他的
 */
public class FollowAnchorSubView extends LinearLayout implements OnLoadMoreListener{
    private AnchorMainActivity activity;
    private LayoutInflater inflater;
    private View mView;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private TextView follow_anchor_number;
    private String anchorID;
    private FollowAnchorAdapter adapter;
    private int page = 1;//作品请求的页数；
    private Map<String, String> map = new HashMap<>();

    public FollowAnchorSubView(AnchorMainActivity activity) {
        super(activity);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        mView = inflater.inflate(R.layout.subview_follow_anchor, this);
        initView();
    }

    public void setData(Fans fans, String anchorID) {
        this.anchorID = anchorID;
        adapter = new FollowAnchorAdapter(activity);
        adapter.setData(fans.getData());
        mRecyclerView.setAdapter(adapter);
        follow_anchor_number.setText(String.valueOf(fans.getLenght()));
        isPaging(fans.getLenght(), adapter.getItemCount());
    }


    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = (SwipeToLoadLayout) mView.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
        follow_anchor_number = (TextView) mView.findViewById(R.id.follow_anchor_number);//粉丝数量

    }


    public void getData() {
        map.put("bid", anchorID);
        map.put("range", "2");
        map.put("pagef", String.valueOf(page));
        map.put("countf", "10");
        BaseObserver baseObserver = new BaseObserver<AnchorMessResult>(){
            @Override
            public void success(AnchorMessResult data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                if(data.getFans() != null) {
                    if (adapter == null) {
                        adapter = new FollowAnchorAdapter(activity);
                        adapter.setData(data.getFans().getData());
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.addData(data.getFans().getData());
                        adapter.notifyDataSetChanged();
                    }
                    page = data.getFans().getPage();
                    isPaging(data.getFans().getLenght(), adapter.getItemCount());
                }
            }

            @Override
            public void error() {
                super.error();
                page--;
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        };
        activity.mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getBroadcasterInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }




    private void isPaging(int total, int count) {
        if (total > count) {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        }
    }


    @Override
    public void onLoadMore() {
        page++;
        getData();
    }
}
