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
import com.ting.anchor.adapter.AnchorWorkAdapter;
import com.ting.bean.anchor.AnchorMessResult;
import com.ting.base.BaseObserver;
import com.ting.bean.Work;
import com.ting.common.http.HttpService;
import com.ting.util.UtilRetrofit;
import com.ting.view.CustomItemDecoration;

import java.util.HashMap;
import java.util.Map;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gengjiajia on 15/9/1.
 * 主播作品
 */
public class AnchorWorkSubView extends LinearLayout implements OnLoadMoreListener{
    private AnchorMainActivity activity;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private LayoutInflater inflater;
    private View mView;
    private TextView anchor_work_zhang_shu;
    private int page = 1;//作品请求的页数；
    private Map<String, String> map = new HashMap<>();
    private String anchorID;
    private AnchorWorkAdapter adapter;

    public AnchorWorkSubView(AnchorMainActivity activity) {
        super(activity);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        mView = inflater.inflate(R.layout.subview_anchor_work, this);
        intView();
    }

    public void setData(Work works, String anchorID) {
        anchor_work_zhang_shu.setText(String.valueOf(works.getLenght()));
        this.anchorID = anchorID;
        adapter = new AnchorWorkAdapter(activity);
        adapter.setData(works.getData());
        mRecyclerView.setAdapter(adapter);
        isPaging(works.getLenght(), adapter.getItemCount());
        anchor_work_zhang_shu.setText(String.valueOf(works.getLenght()));
    }


    private void intView() {
        mSwipeToLoadLayout = (SwipeToLoadLayout) mView.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.swipe_target);
        anchor_work_zhang_shu = (TextView) mView.findViewById(R.id.anchor_work_zhang_shu);//章数
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
    }


    public void getData() {
        map.put("bid", anchorID);
        map.put("range", "1");
        map.put("pagew", String.valueOf(page));
        map.put("countw", "10");
        BaseObserver baseObserver = new BaseObserver<AnchorMessResult>(){
            @Override
            public void success(AnchorMessResult data) {
                super.success(data);
                mSwipeToLoadLayout.setLoadingMore(false);
                if(data.getWorks() != null) {
                    if (adapter == null) {
                        adapter = new AnchorWorkAdapter(activity);
                        adapter.setData(data.getWorks().getData());
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.addData(data.getWorks().getData());
                        adapter.notifyDataSetChanged();
                    }
                    page = data.getWorks().getPage();
                    isPaging(data.getWorks().getLenght(), adapter.getItemCount());
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
