package com.ting.anchor.subview;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ting.R;
import com.ting.anchor.AnchorMainActivity;
import com.ting.anchor.adapter.RewardAnchorAdapter;
import com.ting.bean.vo.GiftDetailVO;
import com.ting.view.CustomItemDecoration;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/1.
 * 打赏排名
 */
public class RewardRankSubView extends LinearLayout {
    private AnchorMainActivity activity;
    private LayoutInflater inflater;
    private View mView;
    private RecyclerView mRecyclerView;
    private RewardAnchorAdapter adapter;

    public RewardRankSubView(AnchorMainActivity activity) {
        super(activity);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        mView = inflater.inflate(R.layout.subview_reward_rank, this);
        initView();
    }

    public void setData(List<GiftDetailVO> giftList) {
        adapter = new RewardAnchorAdapter(activity);
        adapter.setData(giftList);
        mRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        mRecyclerView =  mView.findViewById(R.id.swipe_target);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);

    }

}
