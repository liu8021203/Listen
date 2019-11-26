package com.ting.myself;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.myself.dialog.StudyUseAdapter;
import com.ting.view.CustomItemDecoration;

/**
 * Created by liu on 2017/8/9.
 */

public class StudyUseActivity extends BaseActivity{
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_use);
    }

    @Override
    protected String setTitle() {
        return "使用教程";
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        CustomItemDecoration decoration = new CustomItemDecoration(1);
        mRecyclerView.addItemDecoration(decoration);
        StudyUseAdapter adapter = new StudyUseAdapter(this);
        mRecyclerView.setAdapter(adapter);
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
}
