package com.ting.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ting.R;
import com.ting.util.UtilSPutil;

import androidx.annotation.NonNull;

public class NoticeDialog extends Dialog{
    private TextView tvContent;
    private TextView tvClose;
    private String content;
    private Context mContext;

    public NoticeDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        mContext = context;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_notice);
        UtilSPutil.getInstance(mContext).setLong("systemTime", System.currentTimeMillis() / 1000);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }

    private void initView() {
        tvContent = findViewById(R.id.tv_content);
        tvContent.setText(content);
        tvClose = findViewById(R.id.tv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
