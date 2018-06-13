package com.ting.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.ting.R;


public class ColorfulRingProgressView extends View {


    private float mPercent = 0;
    private float mStrokeWidth = 5;
    private int mBgColor = 0xffe1e1e1;

    private Context mContext;
    private RectF mOval;
    private Paint mPaint;
    //开始下载
    private Bitmap mBitmap;


    //图片宽度
    private int bitmapWidth;
    //图片高度
    private int bitmapHeight;

    public ColorfulRingProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.download_start);
        bitmapWidth = mBitmap.getWidth();
        bitmapHeight = mBitmap.getHeight();
    }

    private int dp2px(float dp) {
        return (int) (mContext.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mBgColor);
        canvas.drawArc(mOval, 0, 360, false, mPaint);
        mPaint.setColor(0xff707070);
        canvas.drawArc(mOval, 270, mPercent * 3.6f, false, mPaint);
        canvas.drawBitmap(mBitmap, (getWidth() - bitmapWidth) / 2, (getHeight() - bitmapHeight) / 2, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        updateOval();
    }

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float mPercent) {
        this.mPercent = mPercent;
        refreshTheLayout();
    }

    private void updateOval() {
        int xp = getPaddingLeft() + getPaddingRight();
        int yp = getPaddingBottom() + getPaddingTop();
        Logger.d("xp" + "+++++++" + xp + "+++++++++++++yp++++++++" + yp);
        mOval = new RectF(getPaddingLeft() + mStrokeWidth, getPaddingTop() + mStrokeWidth,
                getPaddingLeft() + (getWidth() - xp) - mStrokeWidth,
                getPaddingTop() + (getHeight() - yp) - mStrokeWidth);
    }

    public void setStrokeWidthDp(float dp) {
        this.mStrokeWidth = dp2px(dp);
        mPaint.setStrokeWidth(mStrokeWidth);
        updateOval();
        refreshTheLayout();
    }

    public void refreshTheLayout() {
        invalidate();
        requestLayout();
    }


    /**
     * 下载初始化
     */
    public void downloadInit() {
        mBitmap.recycle();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.download_start);
        mPercent = 0;
        refreshTheLayout();
    }


    public void downloadWait(){
        mBitmap.recycle();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.download_wait);
        mPercent = 0;
        refreshTheLayout();
    }

    /**
     * 下载暂停
     */
    public void downloadPause(float percent) {
        mBitmap.recycle();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.download_pause);
        mPercent = percent;
        refreshTheLayout();
    }

    /**
     * 下载继续
     */
    public void downloadResume(float percent) {
        mBitmap.recycle();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.download_resume);
        mPercent = percent;
        refreshTheLayout();
    }

    public void downloadComplete() {
        mBitmap.recycle();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.download_complete);
        mPercent = 0;
        refreshTheLayout();
    }

    public void downloadNo(){
        mBitmap.recycle();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.donwload_no);
        mPercent = 0;
        refreshTheLayout();
    }


}
