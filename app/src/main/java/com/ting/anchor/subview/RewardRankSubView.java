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
import com.ting.anchor.adapter.RewardAnchorAdapter;
import com.ting.bean.anchor.AnchorMessResult;
import com.ting.base.BaseObserver;
import com.ting.bean.anchor.Reward;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/1.
 * 打赏排名
 */
public class RewardRankSubView extends LinearLayout implements OnLoadMoreListener{
    private AnchorMainActivity activity;
    private LayoutInflater inflater;
    private View mView;
    private RecyclerView mRecyclerView;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private TextView all_reward_number;
    private String anchorID;
    private RewardAnchorAdapter adapter;
    private int page = 1;//作品请求的页数；
    private Map<String, String> map = new HashMap<>();

    public RewardRankSubView(AnchorMainActivity activity) {
        super(activity);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        mView = inflater.inflate(R.layout.subview_reward_rank, this);
        initView();
    }

    public void setData(Reward result, String anchorID) {
        this.anchorID = anchorID;
        adapter = new RewardAnchorAdapter(activity);
        adapter.setData(result.getData());
        mRecyclerView.setAdapter(adapter);
        all_reward_number.setText(String.valueOf(result.getLenght()));
        isPaging(result.getLenght(), adapter.getItemCount());
    }

    private void initView() {
        all_reward_number = (TextView) findViewById(R.id.all_reward_number);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = (SwipeToLoadLayout) mView.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);

    }

    public void getData() {
        map.put("bid", anchorID);
        map.put("range", "3");
        map.put("pager", String.valueOf(page));
        map.put("countr", "10");
        BaseObserver baseObserver = new BaseObserver<AnchorMessResult>(){
            @Override
            public void success(AnchorMessResult data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                if(data.getReward() != null) {
                    if (adapter == null) {
                        adapter = new RewardAnchorAdapter(activity);
                        adapter.setData(data.getReward().getData());
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.addData(data.getReward().getData());
                        adapter.notifyDataSetChanged();
                    }
                    page = data.getReward().getPage();
                    isPaging(data.getReward().getLenght(), adapter.getItemCount());
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



    private void isPaging(int totalSize, int haveSize) {
        if (totalSize > haveSize) {
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
