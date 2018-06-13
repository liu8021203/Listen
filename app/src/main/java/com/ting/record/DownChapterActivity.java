package com.ting.record;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.db.DBChapter;
import com.ting.download.DownloadController;
import com.ting.record.adapter.HaveDownAdapter;
import com.ting.view.CustomItemDecoration;

import java.util.List;

/**
 * Created by liu on 15/11/11.
 */
public class DownChapterActivity extends BaseActivity{
    private RecyclerView mRecyclerView;
    private DownloadController controller;
    private HaveDownAdapter adapter;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_chapter);

    }

    @Override
    protected String setTitle() {
        return "章节列表";
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }


    @Override
    protected void initData() {
        controller = new DownloadController();
        List<DBChapter> data = controller.queryData(String.valueOf(bookId), 4+"");
        if(adapter == null)
        {
            adapter = new HaveDownAdapter(this, data);
            mRecyclerView.setAdapter(adapter);
        }
        else
        {
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void getIntentData() {
        bookId = (int) getIntent().getExtras().get("bookID");
    }
}
