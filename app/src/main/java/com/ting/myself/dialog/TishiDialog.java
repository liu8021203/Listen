package com.ting.myself.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ting.R;


/**
 * Created by gengjiajia on 16/1/29.
 * 找回密码提示dialog
 */
public class TishiDialog extends Dialog {
    private TextView tv_sure;
    private TextView tv_tishi_message;
    private String message;

    public TishiDialog(Context context, String message) {
        super(context, R.style.CustomDialog);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tishi);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        tv_tishi_message = (TextView) findViewById(R.id.tv_tishi_message);


        tv_sure = (TextView) findViewById(R.id.sure);

        tv_tishi_message.setText(message);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
