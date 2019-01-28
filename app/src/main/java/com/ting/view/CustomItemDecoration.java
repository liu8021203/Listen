package com.ting.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by liu on 2017/4/12.
 */

public class CustomItemDecoration extends RecyclerView.ItemDecoration{
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    private int mDividerOrientation;
    private int mDividerSize = 1;
    private int color = 0xFFE0E0E0;
    private Paint mPaint;

    public CustomItemDecoration(int orientation) {
        mDividerOrientation = orientation;


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFFE0E0E0);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public CustomItemDecoration(int orientation, int color) {
        mDividerOrientation = orientation;

        mDividerSize = 1;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setDividerSize(int dividerSize) {
        mDividerSize = dividerSize;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mDividerOrientation == VERTICAL) {
            drawVertical(c, parent);     // 垂直方向间隔高度为分割线高度
        } else {
            drawHorizontal(c, parent);    // 水平方向间隔宽度为分割线宽度
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mDividerOrientation == HORIZONTAL) {
            outRect.set(0, 0, 0, mDividerSize);
        } else {
            outRect.set(0, 0, mDividerSize, 0);
        }
    }


    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        // 对于水平方向的分割线，两端的位置是不变的，可以直接通过RecyclerView来获取
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        // 这里获取的是一屏的Item数量
        int childCount = parent.getChildCount();

        // 分割线从Item的底部开始绘制，且在最后一个Item底部不绘制
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            // 有的Item布局会设置layout_marginXXX
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerSize;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    /**
     * 绘制纵向分割线原理参考上面的方法
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerSize;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
