package com.ting.record;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.common.AppData;
import com.ting.db.DBChapter;
import com.ting.download.DownloadController;
import com.ting.record.adapter.HaveDownAdapter;
import com.ting.util.UtilFileManage;
import com.ting.util.UtilMD5Encryption;
import com.ting.view.CustomItemDecoration;

import java.util.List;

/**
 * Created by liu on 15/11/11.
 */
public class DownChapterActivity extends BaseActivity{
    private RecyclerView mRecyclerView;
    private DownloadController controller;
    private HaveDownAdapter adapter;
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_chapter);
        showRightText("全部删除");
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
        List<DBChapter> data = controller.queryData(bookId, 4+"");
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
        Bundle bundle  = getIntent().getExtras();
        bookId = bundle.getString("bookId");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_right:
                if(adapter != null){
                    for (int i = 0; i < adapter.getData().size(); i++){
                        controller.delete(adapter.getData().get(i));
                        UtilFileManage.delete(AppData.FILE_PATH + adapter.getData().get(i).getBookId() + "/" + UtilMD5Encryption.getMd5Value(adapter.getData().get(i).getChapterId()) + ".tsj");
                    }
                    showEmpty();
                }
                break;
        }
    }

    public void showEmpty(){
        errorEmpty("还没有下载的章节");
    }
}
