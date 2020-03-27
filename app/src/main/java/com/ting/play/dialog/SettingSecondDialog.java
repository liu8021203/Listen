package com.ting.play.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.util.UtilSPutil;

import androidx.annotation.NonNull;

public class SettingSecondDialog extends Dialog implements View.OnClickListener {
    private EditText mEditText;
    private Button btnCancle;
    private Button btnDefault;
    private Button btnOk;
    private BaseActivity mActivity;

    public SettingSecondDialog(@NonNull BaseActivity context) {
        super(context, R.style.CustomDialog);
        mActivity = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_second);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        initView();
    }

    private void initView() {
        mEditText = findViewById(R.id.et_second);
        btnCancle = findViewById(R.id.btn_cancle);
        btnDefault = findViewById(R.id.btn_default);
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnDefault.setOnClickListener(this);
        long num = UtilSPutil.getInstance(mActivity).getLong("skip_second", -1L);
        if(num != - 1){
            mEditText.setText(String.valueOf(num / 1000));
            mEditText.setSelection(mEditText.getText().toString().length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                String str = mEditText.getText().toString().trim();
                try{
                    int num = Integer.valueOf(str);
                    if(num > 60){
                        mActivity.showToast("不能超过60秒");
                        return;
                    }
                    mActivity.showToast("设置成功");
                    UtilSPutil.getInstance(mActivity).setLong("skip_second", num * 1000);
                    dismiss();
                }catch (Exception e){
                    mActivity.showToast("请输入数字");
                }
                break;

            case R.id.btn_cancle:
                dismiss();
                break;

            case R.id.btn_default:
                mActivity.showToast("设置成功");
                UtilSPutil.getInstance(mActivity).setLong("skip_second", -1L);
                dismiss();
                break;
        }
    }
}
