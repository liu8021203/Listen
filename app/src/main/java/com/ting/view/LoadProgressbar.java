package com.ting.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.ting.R;


public class LoadProgressbar extends View {

	private Animation operatingAnim = null;

	public LoadProgressbar(Context context) {
		super(context);
		initAnim(context);
		// TODO Auto-generated constructor stub
	}

	public LoadProgressbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAnim(context);
		// TODO Auto-generated constructor stub
	}

	public LoadProgressbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAnim(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub

		if (visibility == View.VISIBLE) {
			startAnim();
		} else {
			cancelAnim();
		}
		super.setVisibility(visibility);
	}

	private void startAnim() {
		this.startAnimation(operatingAnim);
	}

	private void cancelAnim() {
		if (operatingAnim != null) {
			operatingAnim.cancel();
		}
		this.clearAnimation();
	}

	private void initAnim(Context context) {
		if (operatingAnim == null) {
			operatingAnim = AnimationUtils.loadAnimation(context,
					R.anim.loading_anim);
			LinearInterpolator lin = new LinearInterpolator();
			operatingAnim.setInterpolator(lin);
		}
	}

}
