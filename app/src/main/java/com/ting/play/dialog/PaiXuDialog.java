package com.ting.play.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.play.BookDetailsActivity;
import com.ting.play.subview.PlayListSubView;

/**
 * Created by gengjiajia on 15/11/17.
 * 排序dialog
 */
public class PaiXuDialog extends Dialog implements View.OnClickListener {
    private PlayListSubView subView;
    private BookDetailsActivity activity;
    private ImageView paixu_dismiss;
    private ImageView fan_image;
    private TextView sure_xu_text;
    private LinearLayout zheng_layout;
    private ImageView zheng_image;
    private LinearLayout fan_layout;
    private Button btnCancle;
    private Button btnOk;
    private String message="asc";
    private SortCallBackListener mListener;

    public PaiXuDialog(BookDetailsActivity activity) {
        super(activity, R.style.PlayDialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_paixu);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        initView();
    }

    public void setListener(SortCallBackListener listener) {
        mListener = listener;
    }

    private void initView() {
        zheng_layout = (LinearLayout) findViewById(R.id.zheng_layout);
        zheng_image = (ImageView) findViewById(R.id.zheng_image);
        fan_layout = (LinearLayout) findViewById(R.id.fan_layout);
        fan_image = (ImageView) findViewById(R.id.fan_image);
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancle.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        zheng_layout.setOnClickListener(this);
        fan_layout.setOnClickListener(this);
        if(activity.sort.equals("asc"))
        {
            zheng_image.setImageResource(R.mipmap.blue_radio_check);
            fan_image.setImageResource(R.mipmap.white_radio_check);
        }
        else
        {
            zheng_image.setImageResource(R.mipmap.white_radio_check);
            fan_image.setImageResource(R.mipmap.blue_radio_check);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zheng_layout:
                zheng_image.setImageResource(R.mipmap.blue_radio_check);
                fan_image.setImageResource(R.mipmap.white_radio_check);
                message="asc";
                break;
            case R.id.fan_layout:
                zheng_image.setImageResource(R.mipmap.white_radio_check);
                fan_image.setImageResource(R.mipmap.blue_radio_check);
                message="desc";
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
            case R.id.btn_ok:
                if(!activity.getSort().equals(message))
                {
                    activity.setSort(message);
                    if(mListener != null){
                        mListener.callback();
                    }
                }
                dismiss();
                break;
        }
    }

    public interface SortCallBackListener
    {
        public void callback();
    }
}
