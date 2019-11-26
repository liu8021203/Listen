package com.ting.play.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ting.R;
import com.ting.common.AppData;


public class SpeedDialog extends Dialog implements View.OnClickListener{
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tvClose;
    private SpeedCallBackListener mListener;

    public SpeedDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }

    public void setListener(SpeedCallBackListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_speed);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }

    private void initView() {
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tvClose = findViewById(R.id.tv_close);
        tvClose.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        switch (AppData.speedType){
            case 1:
                tv1.setTextColor(getContext().getResources().getColor(R.color.c28abff));
                break;

            case 2:
                tv2.setTextColor(getContext().getResources().getColor(R.color.c28abff));
                break;

            case 3:
                tv3.setTextColor(getContext().getResources().getColor(R.color.c28abff));
                break;

            case 4:
                tv4.setTextColor(getContext().getResources().getColor(R.color.c28abff));
                break;

            case 5:
                tv5.setTextColor(getContext().getResources().getColor(R.color.c28abff));
                break;

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_1:
                if(mListener != null){
                    AppData.speedType = 1;
                    mListener.speed(1.0f);
                }
                break;

            case R.id.tv_2:
                if(mListener != null){
                    AppData.speedType = 2;
                    mListener.speed(1.25f);
                }
                break;

            case R.id.tv_3:
                if(mListener != null){
                    AppData.speedType = 3;
                    mListener.speed(1.5f);
                }
                break;

            case R.id.tv_4:
                if(mListener != null){
                    AppData.speedType = 4;
                    mListener.speed(1.75f);
                }
                break;

            case R.id.tv_5:
                if(mListener != null){
                    AppData.speedType = 5;
                    mListener.speed(2.0f);
                }
                break;

        }
        dismiss();
    }


    public interface SpeedCallBackListener{
        void speed(float speed);
    }
}
