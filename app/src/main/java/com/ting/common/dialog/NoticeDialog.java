package com.ting.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ting.R;

import androidx.annotation.NonNull;

public class NoticeDialog extends Dialog implements View.OnClickListener {
    private TextView tvContent;
    private TextView tvClose;

    public NoticeDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_notice);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }

    private void initView() {
        tvContent = findViewById(R.id.tv_content);
        tvClose = findViewById(R.id.tv_close);
        tvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
