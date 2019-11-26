package com.ting.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.ting.R;

/**
 * Created by Acorn on 2017/6/26.
 */

public class StoreHeader extends LinearLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private ProgressBar loaddingPb;
    private ImageView successIv;
    private TextView tv;

    public StoreHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_store_header, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        loaddingPb = (ProgressBar) findViewById(R.id.loadding_pb);
        successIv = (ImageView) findViewById(R.id.load_success_iv);
        tv = (TextView) findViewById(R.id.load_tv);
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {

    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        loaddingPb.setVisibility(GONE);
        successIv.setVisibility(VISIBLE);
        tv.setText("刷新成功");
    }

    @Override
    public void onReset() {
        loaddingPb.setVisibility(VISIBLE);
        successIv.setVisibility(GONE);
        tv.setText("下拉刷新");
    }

    @Override
    public void onRefresh() {
    }
}
