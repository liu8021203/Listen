package com.ting.record;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.db.DBBook;
import com.ting.download.DownloadController;
import com.ting.record.adapter.DownBookAdapter;
import com.ting.view.CustomItemDecoration;

import java.util.List;

/**
 * Created by liu on 2018/1/29.
 */

public class DownloadActivity extends BaseActivity{
    private RecyclerView mRecyclerView;
    private DownBookAdapter adapter;
    private DownloadController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

    }

    @Override
    protected String setTitle() {
        return "下载列表";
    }

    @Override
    protected void initView() {
        mRecyclerView =  flContent.findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1, 0xffebebeb);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        controller = new DownloadController();
        showProgressDialog();
        List<DBBook> data = controller.queryBook();
        removeProgressDialog();
        if(data != null && data.size() > 0) {
            if (adapter == null) {
                adapter = new DownBookAdapter(mActivity);
                adapter.setData(data);
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }else{
            errorEmpty("还没有下载的书籍~");
        }
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }
}