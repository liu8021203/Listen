package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by gengjiajia on 16/7/7.
 */
public class ScrollListview extends ListView {

    public ScrollListview(Context context) {
        super(context);
    }

    public ScrollListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
