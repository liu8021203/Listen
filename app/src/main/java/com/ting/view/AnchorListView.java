package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by liu on 16/7/24.
 */
public class AnchorListView extends ListView {
    public ScrollView parentScrollView;
    int currentY;


    public AnchorListView(Context context) {
        super(context);
        setFocusable(false);
    }

    public AnchorListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
    }

    public AnchorListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(false);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (parentScrollView == null) {
            return super.onInterceptTouchEvent(ev);
        } else {
            View parentchild = parentScrollView.getChildAt(0);
            int height2 = parentchild.getMeasuredHeight();
            height2 = height2 - parentScrollView.getMeasuredHeight();
            int scrollY2 = parentScrollView.getScrollY();
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (scrollY2 >= height2) {
                    // 将父scrollview的滚动事件拦截
                    currentY = (int) ev.getY();
                    setParentScrollAble(false);
                }

            }
            else if (ev.getAction() == MotionEvent.ACTION_UP) {
                // 把滚动事件恢复给父Scrollview
                setParentScrollAble(true);
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            }
        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (parentScrollView != null) {
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {

                int y = (int) ev.getY();


                View parentchild = parentScrollView.getChildAt(0);
                int height2 = parentchild.getMeasuredHeight();
                height2 = height2 - parentScrollView.getMeasuredHeight();


                int scrollY2 = parentScrollView.getScrollY();


                if (scrollY2 >= height2) {

                    // 手指向下滑动
                    if (currentY < y) {
                        boolean result = false;
                        result = getFirstVisiblePosition()==0 ? true : false;
                        if (result) {
                            // 如果向下滑动到头，就把滚动交给父Scrollview
                            setParentScrollAble(true);
                            return false;
                        } else {
                            setParentScrollAble(false);

                        }
                    } /*else if (currentY > y) {

                    if (scrollY >= height) {
                        // 如果向上滑动到头，就把滚动交给父Scrollview
                        setParentScrollAble(true);
                        return false;
                    } else {
                        setParentScrollAble(false);

                    }

                }*/
                    currentY = y;
                }
            } /*else {
                setParentScrollAble(true);
            }*/
        }
        return super.onTouchEvent(ev);
    }


    private void setParentScrollAble(boolean flag) {

        parentScrollView.requestDisallowInterceptTouchEvent(!flag);
    }
}
