package com.ting.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ting.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class MyToast extends Toast {

	LayoutInflater inflater = null;

	private TextView messageView = null;



	public MyToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		View toastView = inflater.inflate(R.layout.toast_layout, null);
		messageView = ((TextView) toastView.findViewById(R.id.toast_message));
		setView(toastView);
	}

	@Override
	public void setText(CharSequence s) {
		// TODO Auto-generated method stub
		messageView.setText(s);
	}

	@Override
	public void setText(int resId) {
		// TODO Auto-generated method stub
		messageView.setText(resId);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

}
