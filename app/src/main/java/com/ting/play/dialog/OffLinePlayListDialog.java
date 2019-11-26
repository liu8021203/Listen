package com.ting.play.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.db.DBChapter;
import com.ting.play.adapter.OfflinePlayListAdapter;

import java.util.List;

/**
 *
 * 离线播放列表Dialog
 * Created by liu on 2017/12/23.
 */
public class OffLinePlayListDialog extends Dialog implements View.OnClickListener{
    private ImageView ivClose;
    private RecyclerView mRecyclerView;
    private BaseActivity mActivity;
    private List<DBChapter> data;
    private OfflinePlayListAdapter mAdapter;
    private int bookId;

    public OffLinePlayListDialog(@NonNull Context context) {
        super(context, R.style.PlayListDialog);
        mActivity = (BaseActivity) context;
    }

    public void setData(List<DBChapter> data) {
        this.data = data;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_offline_play_list);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }

    private void initView() {
        ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recycle_view);
        mAdapter = new OfflinePlayListAdapter(mActivity, bookId);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        mAdapter.setData(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                dismiss();
                break;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}
