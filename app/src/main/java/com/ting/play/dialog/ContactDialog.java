package com.ting.play.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ting.R;

public class ContactDialog extends Dialog implements View.OnClickListener {
    private TextView tvContact;
    private Button btnOk;
    private String nickname;
    private String contact;

    public ContactDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }

    public void setData(String nickname, String contact){
        this.nickname = nickname;
        this.contact = contact;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact);
        setCanceledOnTouchOutside(true);
        tvContact = findViewById(R.id.tv_contact);
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        if(TextUtils.isEmpty(contact)){
           contact = "";
        }
        tvContact.setText("本作品由\"" + nickname + "\"进行上传,联系方式：" + contact);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                dismiss();
                break;
        }
    }
}
