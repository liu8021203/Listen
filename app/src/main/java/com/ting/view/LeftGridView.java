package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class LeftGridView extends GridView {

	public LeftGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LeftGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LeftGridView(Context context) {
		super(context);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
