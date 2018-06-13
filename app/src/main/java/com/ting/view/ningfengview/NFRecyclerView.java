package com.ting.view.ningfengview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class NFRecyclerView extends RecyclerView {


    public ScrollView parentScrollView;
    public int HeaderId;

    public int ContentContainerId;
    private int lastScrollDelta = 0;

    private boolean flag = false;

    public NFRecyclerView(Context context) {
        super(context);
        setFocusable(false);
    }

    public NFRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
    }

    public NFRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(false);
    }


    public void resume() {
        overScrollBy(0, -lastScrollDelta, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
        lastScrollDelta = 0;
    }

    int mTop = 10;


    public void scrollTo(View targetView) {

        int oldScrollY = getScrollY();
        int top = targetView.getTop() - mTop;
        int delatY = top - oldScrollY;
        lastScrollDelta = delatY;
        overScrollBy(0, delatY, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getHeight() - (getHeight()));
        }
        return scrollRange;
    }

    int currentY;

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

            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
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
                        LayoutManager manager = getLayoutManager();
                        if (manager instanceof GridLayoutManager) {
                            GridLayoutManager gm = (GridLayoutManager) manager;
                            result = gm.findViewByPosition(gm.findFirstVisibleItemPosition()).getTop() == 0 && gm.findFirstVisibleItemPosition() == 0;
                        } else if (manager instanceof LinearLayoutManager) {
                            LinearLayoutManager lm = (LinearLayoutManager) manager;
                            result = lm.findViewByPosition(lm.findFirstVisibleItemPosition()).getTop() == 0 && lm.findFirstVisibleItemPosition() == 0;
                        }
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