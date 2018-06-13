package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class OnePageGallery extends Gallery {

    public OnePageGallery(Context context, AttributeSet attrSet) {
        super(context, attrSet);
    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    private float gTouchStartX;
    private float gTouchStartY;

    public OnePageGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OnePageGallery(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                gTouchStartX = ev.getX();
                gTouchStartY = ev.getY();
                super.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                final float touchDistancesX = Math.abs(ev.getX() - gTouchStartX);
                final float touchDistancesY = Math.abs(ev.getY() - gTouchStartY);
                if (touchDistancesY * 2 >= touchDistancesX) {
                    return false;
                } else {
                    return true;
                }
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // return super.onFling(e1, e2, 0, velocityY);//方法一：只去除翻页惯性
        // return false;//方法二：只去除翻页惯性 注：没有被注释掉的代码实现了开始说的2种效果。
        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            // Check if scrolling left
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            // Otherwise scrolling right
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);
        return true;
    }

}
