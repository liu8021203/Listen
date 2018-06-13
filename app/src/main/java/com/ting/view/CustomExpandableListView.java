package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

/**
 * Created by gengjiajia on 15/9/18.
 */
public class CustomExpandableListView extends ExpandableListView {

    float scrollerY = -1;

    public CustomExpandableListView(Context context) {
        super(context);
    }

    public CustomExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

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
