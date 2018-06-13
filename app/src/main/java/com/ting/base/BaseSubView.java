package com.ting.base;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ting.R;
import com.ting.constant.CodeConstant;
import com.ting.view.BaseListView;
import com.ting.base.listener.ViewShowListener;

/**
 * 用于find模块里的子界面基类
 * 
 * @author zhangyu
 * 
 */
public class BaseSubView extends LinearLayout implements ViewShowListener {

	private Context mContext;
	private BaseFragment baseFragment;
	private LayoutInflater inflater;

	private ImageView toastImageView;

	public BaseSubView(Context context, BaseFragment baseFragment) {
		super(context);
		this.baseFragment = baseFragment;
	}

	@Override
	public void onViewShow() {
		// TODO Auto-generated method stub
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}


	/**
	 * 销毁的时候会回调
	 */
	public void onDestroyView() {

	}

}
