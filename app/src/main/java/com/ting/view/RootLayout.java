package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ting.R;

/**
 * Created by liu on 16/1/25.
 */
public class RootLayout extends LinearLayout{
    public RootLayout(Context context) {
        this(context, null);
    }

    public RootLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RootLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClipToPadding(false);
        setFitsSystemWindows(true);
        setBackgroundColor(0xffffffff);
    }
}
