package com.ting.anchor.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ting.R;
import com.ting.bean.anchor.LiWuResult;
import com.ting.base.BaseActivity;
import com.ting.common.AppData;
import com.ting.play.adapter.GiftAdapter;

/**
 * Created by gengjiajia on 15/9/2.
 * 打赏催更dialog
 */
public class GiftDialog extends Dialog implements View.OnClickListener {
    private BaseActivity mActivity;
    private ImageView ivClose;
    private RecyclerView mRecyclerView;
    private GiftAdapter mAdapter;
    private int bookId;
    private int anchorId;
    public GiftDialog(BaseActivity activity) {
        super(activity, R.style.CustomDialog);
        this.mActivity = activity;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reward_anchor);
        initView();
    }

    private void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 4);
        mRecyclerView.setLayoutManager(manager);
        ivClose.setOnClickListener(this);
        setLiwuMess(AppData.liWuResult);
    }

    private void setLiwuMess(LiWuResult result) {
        if(mAdapter == null) {
            mAdapter = new GiftAdapter(mActivity, this);
            mAdapter.setBookId(bookId);
            mAdapter.setAnchorId(anchorId);
            mAdapter.setData(result.getData());
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setData(result.getData());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            default:
                break;
        }
    }

}