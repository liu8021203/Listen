package com.ting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ting.R;


public class baseListViewNoDataFooter extends LinearLayout {

	private View mContentView;

	public baseListViewNoDataFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public baseListViewNoDataFooter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.listview_refresh_nodata_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mContentView = moreView
				.findViewById(R.id.xlistview_footer_nodata_content);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		mContentView.setVisibility(View.GONE);
	}

	/**
	 * show footer
	 */
	public void show() {
		// LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
		// mContentView
		// .getLayoutParams();
		// lp.height = LayoutParams.WRAP_CONTENT;
		// mContentView.setLayoutParams(lp);
		mContentView.setVisibility(View.VISIBLE);
	}

}
