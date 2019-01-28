package com.ting.myself.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.common.TokenManager;
import com.ting.constant.StaticConstant;
import com.ting.myself.SettingActivity;

/**
 * Created by gengjiajia on 15/9/16.
 * 性别选择dialog
 */
public class SexChooseDialog extends Dialog implements View.OnClickListener {
    private RelativeLayout choose_man_sex_layout;
    private RelativeLayout choose_women_sex_layout;
    private TextView man_choose_text;
    private TextView women_choose_text;
    private ImageView colse_dialog;
    private SexChooseCallBackListener mListener;

    public SexChooseDialog(BaseActivity baseActivity) {
        super(baseActivity, R.style.SettingDialog);
    }


    public void setListener(SexChooseCallBackListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sex_choose);
        initView();
    }

    private void initView() {
        choose_man_sex_layout =  findViewById(R.id.choose_man_sex_layout);//选择男听迷
        choose_women_sex_layout =  findViewById(R.id.choose_women_sex_layout);//选择女听迷
        man_choose_text =  findViewById(R.id.man_choose_text);
        women_choose_text =  findViewById(R.id.women_choose_text);
        colse_dialog=findViewById(R.id.colse_dialog);
        choose_man_sex_layout.setOnClickListener(this);
        choose_women_sex_layout.setOnClickListener(this);
        colse_dialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_man_sex_layout:
                if(mListener != null){
                    mListener.callback(1);
                }


                dismiss();
                break;
            case R.id.choose_women_sex_layout:

                if(mListener != null){
                    mListener.callback(2);
                }
                dismiss();
                break;
            case R.id.colse_dialog:

                dismiss();
                break;
        }
    }


    public interface SexChooseCallBackListener{
        void callback(int sex);
    }
}
