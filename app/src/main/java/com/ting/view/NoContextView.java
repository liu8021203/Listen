package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;

/**
 * Created by gengjiajia on 16/8/16.
 * <p/>
 * 无内容布局 网络错误布局
 */
public class NoContextView extends LinearLayout implements View.OnClickListener {


    public NoContextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
//        initView();
    }


    /**
     * 设置无数据的默认图顶部图标
     */
    public void NoDateTopImage(int id) {

        LinearLayout.LayoutParams params = new LayoutParams(202
                , 202, Gravity.CENTER_HORIZONTAL);
        params.topMargin = 80;

        ImageView iv = new ImageView(getContext());
        iv.setImageResource(id);

        addView(iv, params);
    }


    /**
     * 设置无数据的标题文本
     */

    public void NoDateTopText(String title) {
        // 设置LinearLayout的布局方向
        // 设置布局参数
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        params.topMargin = 26;


        TextView iv = new TextView(getContext());
        iv.setText(title);
        iv.setTextColor(getContext().getResources().getColor(R.color.caaaaaa));
        iv.setTextSize(15);

        addView(iv, params);
    }

    /**
     * 设置无数据的文本内容
     */

    public void NoDateTopMessage(String message) {
        // 设置LinearLayout的布局方向
        // 设置布局参数
        LinearLayout.LayoutParams params = new LayoutParams(400
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 26;


        TextView iv = new TextView(getContext());
        iv.setText(message);
        iv.setTextColor(getContext().getResources().getColor(R.color.caaaaaa));
        iv.setTextSize(13);
        iv.setGravity(Gravity.CENTER_HORIZONTAL);
        iv.setSingleLine();
        addView(iv, params);
    }


    /**
     * 测量尺寸时的回调方法
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 对子view进行布局，确定子view的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    public void onClick(View v) {

    }


}
