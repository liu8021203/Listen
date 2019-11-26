package com.ting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/12/15.
 */

public class MusicAnimView extends View {
    private static final int LINE_ANIM_COUNT = 5; // 参与动画的竖线总数
    //线宽
    private static final float LINE_WIDTH = 10;
    private static final float LINE_TO_LINE_DISTANCE = 3;
    private static final float LINE_HIGH = 60;
    private Paint mPaint = new Paint();
    private RefreshRunnable mRefreshRunnable = new RefreshRunnable();
    private List<Line> mLines = new ArrayList<>();


    public MusicAnimView(Context context) {
        this(context, null);
    }

    public MusicAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MusicAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void init() {
        mPaint.setColor(Color.parseColor("#FF45bbfc"));
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(LINE_WIDTH); // 画笔的粗细
        Line mLine1 = new Line();
        mLine1.x = LINE_WIDTH / 2;
        mLine1.maxY = LINE_HIGH * 0.1f;
        mLine1.minY = LINE_HIGH * 0.7f;
        mLine1.startY = LINE_HIGH;
        mLine1.endY = LINE_HIGH * 0.7f;
        mLines.add(mLine1);
        Line mLine2 = new Line();
        mLine2.x = LINE_WIDTH / 2 * 3 + LINE_TO_LINE_DISTANCE;
        mLine2.maxY = LINE_HIGH * 0f;
        mLine2.minY = LINE_HIGH * 0.5f;
        mLine2.startY = LINE_HIGH;
        mLine2.endY = LINE_HIGH * 0.2f;
        mLines.add(mLine2);
        Line mLine3 = new Line();
        mLine3.x = LINE_WIDTH / 2 * 5 + LINE_TO_LINE_DISTANCE * 2;
        mLine3.maxY = LINE_HIGH * 0.1f;
        mLine3.minY = LINE_HIGH * 0.7f;
        mLine3.startY = LINE_HIGH;
        mLine3.endY = LINE_HIGH * 0.3f;
        mLines.add(mLine3);
        Line mLine4 = new Line();
        mLine4.x = LINE_WIDTH / 2 * 7 + LINE_TO_LINE_DISTANCE * 3;
        mLine4.maxY = 0;
        mLine4.minY = LINE_HIGH * 0.7f;
        mLine4.startY = LINE_HIGH;
        mLine4.endY = 0f;
        mLines.add(mLine4);
        Line mLine5 = new Line();
        mLine5.x = LINE_WIDTH / 2 * 9 + LINE_TO_LINE_DISTANCE * 4;
        mLine5.maxY = 0;
        mLine5.minY = LINE_HIGH * 0.7f;
        mLine5.startY = LINE_HIGH;
        mLine5.endY = LINE_HIGH * 0.5f;
        mLines.add(mLine5);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < LINE_ANIM_COUNT; i++) {
            Line mLine = mLines.get(i);
            canvas.drawLine(mLine.x, mLine.startY, mLine.x, mLine.endY, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getSize(true, widthMeasureSpec), getSize(false, heightMeasureSpec));
    }

    /**
     * 重新测量view的宽高
     *
     * @param measureSpec
     * @return
     */
    private int getSize(boolean isWidth, int measureSpec) {
        int result = 0;
        // 获得测量模式
        int specMode = MeasureSpec.getMode(measureSpec);
        // 获得测量大小
        int specSize = MeasureSpec.getSize(measureSpec);
        // 重写模式执行逻辑
        switch (specMode) {
            case MeasureSpec.EXACTLY: // 精确值模式，手动指定了控件的宽高，或者指定match_parent
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED: // 任意大小，没有任何限制。情况使用较少
            case MeasureSpec.AT_MOST:// 手动指定控件的宽高为wrap_content，控件大小随着内容变化而变化，只要不超过父控件允许的最大尺寸即可
                int caclSize;
                if (isWidth) {
                    caclSize = (int) (getPaddingLeft() + getPaddingRight() + LINE_TO_LINE_DISTANCE * (LINE_ANIM_COUNT - 1) + LINE_WIDTH * LINE_ANIM_COUNT);
                } else {
                    caclSize = (int) (getPaddingTop() + getPaddingBottom() + LINE_HIGH);
                }
                if (specMode == MeasureSpec.AT_MOST)
                    result = Math.min(caclSize, specSize);
                break;
        }
        return result;
    }

    private void offset() {
        for (int i = 0; i < LINE_ANIM_COUNT; i++) {
            Line mLine = mLines.get(i);
            if (mLine.type == 0) {
//                if(i == 0 || i == 3) {
//                    mLine.endY -= 2.0f;
//                }else{
//                }
                switch (i) {
                    case 0:
                        mLine.endY -= 1.0f;
                        break;

                    case 1:
                        mLine.endY -= 1.2f;
                        break;
                    case 2:
                        mLine.endY -= 1.2f;
                        break;
                    case 3:
                        mLine.endY -= 1.2f;
                        break;
                    case 4:
                        mLine.endY -= 1.1f;
                        break;
                }

                if (mLine.endY <= mLine.maxY) {
                    mLine.endY = mLine.maxY;
                    mLine.type = 1;
                }
            } else {
                switch (i) {
                    case 0:
                        mLine.endY += 1.0f;
                        break;

                    case 1:
                        mLine.endY += 1.2f;
                        break;
                    case 2:
                        mLine.endY += 1.2f;
                        break;
                    case 3:
                        mLine.endY += 1.2f;
                        break;
                    case 4:
                        mLine.endY += 1.1f;
                        break;
                }
                if (mLine.endY > mLine.minY) {
                    mLine.endY = mLine.minY;
                    mLine.type = 0;
                }
            }
        }
    }


    private class RefreshRunnable implements Runnable {

        @Override
        public void run() {
            offset();
            invalidate();
            postDelayed(this, 25);
        }
    }

    public void start() {
//        reset();
        removeCallbacks(mRefreshRunnable);
        post(mRefreshRunnable);
    }

    public void stop() {
        reset();
        invalidate();
        removeCallbacks(mRefreshRunnable);
    }

    public void reset() {
        for (int i = 0; i < LINE_ANIM_COUNT; i++) {
            if (i == 0) {
                Line mLine1 = mLines.get(i);
                mLine1.x = LINE_WIDTH / 2;
                mLine1.maxY = LINE_HIGH * 0.1f;
                mLine1.minY = LINE_HIGH * 0.7f;
                mLine1.startY = LINE_HIGH;
                mLine1.endY = LINE_HIGH * 0.7f;
                continue;
            }
            if (i == 1) {
                Line mLine2 = mLines.get(i);
                mLine2.x = LINE_WIDTH / 2 * 3 + LINE_TO_LINE_DISTANCE;
                mLine2.maxY = LINE_HIGH * 0f;
                mLine2.minY = LINE_HIGH * 0.5f;
                mLine2.startY = LINE_HIGH;
                mLine2.endY = LINE_HIGH * 0.2f;
                continue;
            }
            if (i == 2) {
                Line mLine3 = mLines.get(i);
                mLine3.x = LINE_WIDTH / 2 * 5 + LINE_TO_LINE_DISTANCE * 2;
                mLine3.maxY = LINE_HIGH * 0.1f;
                mLine3.minY = LINE_HIGH * 0.7f;
                mLine3.startY = LINE_HIGH;
                mLine3.endY = LINE_HIGH * 0.3f;
                continue;
            }

            if (i == 3) {
                Line mLine4 = mLines.get(i);
                mLine4.x = LINE_WIDTH / 2 * 7 + LINE_TO_LINE_DISTANCE * 3;
                mLine4.maxY = 0;
                mLine4.minY = LINE_HIGH * 0.7f;
                mLine4.startY = LINE_HIGH;
                mLine4.endY = 0f;
                continue;
            }
            if (i == 4) {
                Line mLine5 = mLines.get(i);
                mLine5.x = LINE_WIDTH / 2 * 9 + LINE_TO_LINE_DISTANCE * 4;
                mLine5.maxY = 0;
                mLine5.minY = LINE_HIGH * 0.7f;
                mLine5.startY = LINE_HIGH;
                mLine5.endY = LINE_HIGH * 0.5f;
                continue;
            }
        }
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    protected class Line {
        public float x;
        public float startY;
        //最高坐标
        public float maxY;
        //最低坐标
        public float minY;
        public float endY;
        // 1:增加   2：减少
        public int type = 0;
    }
}
