package com.ting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by liu on 2017/7/26.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable dividerDrawable;
//    private int num;
    private int orientation = LinearLayoutManager.HORIZONTAL;

    public GridItemDecoration(Context context, int resId, int num) {
        dividerDrawable = context.getResources().getDrawable(resId);
//        this.num = num;
    }


//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        if (dividerDrawable == null) {
//            return;
//        }
//        int position = parent.getChildAdapterPosition(view);
//        if (position < 0) {
//            return;
//        }
//        if (parent.getChildAdapterPosition(view) >= num) {
//            outRect.top = dividerDrawable.getIntrinsicHeight();
//        }
//        if(position % num == 0) {
//            outRect.left = 0;
//            outRect.right = dividerDrawable.getIntrinsicWidth() / 2;
//        }else {
//            if((position + 1) % num == 0) {
//                outRect.left = dividerDrawable.getIntrinsicWidth() / 2;
//                outRect.right = 0;
//            }else{
//                outRect.left = dividerDrawable.getIntrinsicWidth() / 2;
//                outRect.right = dividerDrawable.getIntrinsicWidth() / 2;
//            }
//        }
//    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }

        if (parent.getChildLayoutPosition(view) < 1) {
            return;
        }

        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = dividerDrawable.getIntrinsicHeight();
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = dividerDrawable.getIntrinsicWidth();
        }
    }

    /**
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }

        int childCount = parent.getChildCount();
        int rightV = parent.getWidth();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int leftV = parent.getPaddingLeft() + child.getPaddingLeft();
            int bottomV = child.getTop() - params.topMargin;
            int topV = bottomV - dividerDrawable.getIntrinsicHeight();

            int topH = child.getTop() + params.topMargin;
            int bottomH = child.getBottom() + params.bottomMargin;
            int rightH = child.getLeft() - params.leftMargin;
            int leftH = rightH - dividerDrawable.getIntrinsicWidth();
            dividerDrawable.setBounds(leftH, topH, rightH, bottomH);
            dividerDrawable.draw(c);
            dividerDrawable.setBounds(leftV, topV, rightV, bottomV);
            dividerDrawable.draw(c);
        }
    }

}
