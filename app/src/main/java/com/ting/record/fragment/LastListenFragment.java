package com.ting.record.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ting.R;
import com.ting.base.BaseFragment;
import com.ting.play.controller.MusicDBController;
import com.ting.record.adapter.LastListenAdapter;
import com.ting.view.CustomItemDecoration;

/**
 * Created by gengjiajia on 15/9/8.
 * Modify liu
 * 最近收听subview
 */
public class LastListenFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private LastListenAdapter lastListenerAdapter;
    private MusicDBController controller;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new MusicDBController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("aaa","onCreateView最近");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("aaa","onActivityCreated最近");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("aaa","onDestroyView最近");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("aaa","onDestroy最近");

    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) flContent.findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1, 0xffebebeb);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        if(controller.getListenHistory() != null && controller.getListenHistory().size() > 0) {
            if (lastListenerAdapter == null) {
                lastListenerAdapter = new LastListenAdapter(mActivity);
                lastListenerAdapter.setData(controller.getListenHistory());
                mRecyclerView.setAdapter(lastListenerAdapter);
            } else {
                lastListenerAdapter.setData(controller.getListenHistory());
                lastListenerAdapter.notifyDataSetChanged();
            }
        }else{
            errorEmpty();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_last_listen;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setActionBar() {
        return 0;
    }

    @Override
    protected void reload() {

    }
}
