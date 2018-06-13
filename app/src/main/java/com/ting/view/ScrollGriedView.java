package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by gengjiajia on 16/7/7.
 */
public class ScrollGriedView extends GridView {
    private boolean haveScrollbars = false;

    public ScrollGriedView(Context context) {
        super(context);
    }

    public ScrollGriedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollGriedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 默认为 true
     *
     * @param
     */
//    public void setHaveScrollbar(false) {
//        this.haveScrollbar = haveScrollbar;
//    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (haveScrollbars == false) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
