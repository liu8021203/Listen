package com.ting.view;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by liu on 2017/8/2.
 */

public class NoScrollGridLayoutManager extends GridLayoutManager{
    private boolean isScrollEnabled = true;

    public NoScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NoScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NoScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled;
    }
}
