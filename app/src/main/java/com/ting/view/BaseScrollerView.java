package com.ting.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class BaseScrollerView extends ScrollView {

    public BaseScrollerView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public BaseScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public BaseScrollerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
//
//        if (y - oldy > 50) {
//            getContext().sendBroadcast(new Intent(BaseListView.UPSLIDE));
//        } else if (y - oldy < -20) {
//            getContext().sendBroadcast(new Intent(BaseListView.DOWNSLIDE));
//        }

    }

    float scrollerY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollerY == -1) {
            scrollerY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scrollerY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
//                if (scrollerY - ev.getRawY() < -80) {
//                    getContext().sendBroadcast(new Intent(BaseListView.DOWNSLIDE));
//                    scrollerY = ev.getRawY();
//                } else if (scrollerY - ev.getRawY() > 50) {
//                    getContext().sendBroadcast(new Intent(BaseListView.UPSLIDE));
//                    scrollerY = ev.getRawY();
//                }

                break;
            default:
                scrollerY = -1;

                break;
        }
        return super.onTouchEvent(ev);
    }

}
