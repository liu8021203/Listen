package com.ting.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ting.R;


/**
 * Created by liu on 2017/5/24.
 */

public class SfWebview extends WebView{
    /**
     * false是不显示进度条， true是显示进度条
     */
    private static final boolean DEFAULT_SHOW = true;

    private static final int DEFAULT_HEIGHT = 3;


    private Drawable progress_drawable = null;
    private boolean progress_show = DEFAULT_SHOW;
    private int progress_height = DEFAULT_HEIGHT;

    private ProgressBar progressBar;

    public SfWebview(Context context) {
        this(context, null);
    }

    public SfWebview(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("webViewStyle","attr","android"));
    }

    public SfWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SfWebView,defStyleAttr,0);
        progress_drawable = a.getDrawable(R.styleable.SfWebView_progress_drawable);
        progress_show = a.getBoolean(R.styleable.SfWebView_progress_show, DEFAULT_SHOW);
        progress_height = a.getInt(R.styleable.SfWebView_progress_height, DEFAULT_HEIGHT);
        a.recycle();
        init(context);
    }
    private void init(Context context) {
        if(progress_show)
        {
            progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, progress_height, 0, 0));
            if(progress_drawable != null)
            {
                progressBar.setProgressDrawable(progress_drawable);
            }
            addView(progressBar);
            setWebChromeClient(new SfWebChromeClient());
        }
    }

    private class SfWebChromeClient extends WebChromeClient
    {
        /**
         * 加载进度变化
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(GONE);
            } else {
                if (progressBar.getVisibility() == GONE)
                    progressBar.setVisibility(VISIBLE);
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(progressBar != null) {
            LayoutParams lp = (LayoutParams) progressBar.getLayoutParams();
            lp.x = l;
            lp.y = t;
            progressBar.setLayoutParams(lp);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
