package com.ting.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;

/**
 * Created by liu on 2017/7/4.
 */

public class BaseErrorRelativeLayout extends RelativeLayout{
    private View mView;
    private LinearLayout mLlErrorService;
    private TextView mTvErrorEmpty;
    private Button mBtnErrorRefresh;
    private OnClickListener mListener;

    public BaseErrorRelativeLayout(Context context) {
        this(context, null);
    }

    public BaseErrorRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseErrorRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = LayoutInflater.from(context).inflate(R.layout.base_error_layout, this);
        initView();
    }

    public void setListener(OnClickListener listener) {
        mListener = listener;
        mBtnErrorRefresh.setOnClickListener(mListener);
    }

    private void initView() {
        mLlErrorService = (LinearLayout) mView.findViewById(R.id.ll_error_service);
        mTvErrorEmpty = (TextView) mView.findViewById(R.id.tv_error_empty);
        mBtnErrorRefresh = (Button) mView.findViewById(R.id.btn_refresh);
    }

    /**
     * 显示数据为空
     */
    protected void errorEmpty(){
        mTvErrorEmpty.setVisibility(View.VISIBLE);
        mLlErrorService.setVisibility(View.GONE);
    }

    /**
     * 显示服务异常
     */
    protected void errorService(){
        mLlErrorService.setVisibility(View.VISIBLE);
        mTvErrorEmpty.setVisibility(View.GONE);
    }
}
