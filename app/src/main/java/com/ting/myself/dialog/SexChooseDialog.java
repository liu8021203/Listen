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
    private SettingActivity activity;
    private RelativeLayout choose_man_sex_layout;
    private RelativeLayout choose_women_sex_layout;
    private TextView man_choose_text;
    private TextView women_choose_text;
    private ImageView colse_dialog;

    public SexChooseDialog(BaseActivity baseActivity) {
        super(baseActivity, R.style.SettingDialog);
        this.activity = (SettingActivity)baseActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sex_choose);
        initView();
    }

    private void initView() {
        choose_man_sex_layout = (RelativeLayout) findViewById(R.id.choose_man_sex_layout);//选择男听迷
        choose_women_sex_layout = (RelativeLayout) findViewById(R.id.choose_women_sex_layout);//选择女听迷
        man_choose_text = (TextView) findViewById(R.id.man_choose_text);
        women_choose_text = (TextView) findViewById(R.id.women_choose_text);
        colse_dialog=(ImageView)findViewById(R.id.colse_dialog);
        choose_man_sex_layout.setOnClickListener(this);
        choose_women_sex_layout.setOnClickListener(this);
        if (TokenManager.getInfo(activity) != null) {
            if (TokenManager.getInfo(activity).getType() == 0) {
                man_choose_text.setText("男听迷");
                women_choose_text.setText("女听迷");
            } else if (TokenManager.getInfo(activity).getType() == 1) {
                man_choose_text.setText("男主播");
                women_choose_text.setText("女主播");
            }

        }

        colse_dialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_man_sex_layout:
                if (TokenManager.getInfo(activity) != null) {
                    if (TokenManager.getInfo(activity).getType() == 0) {
                        activity.showSexSeeting("男听迷");
                    } else if (StaticConstant.userInfoResult.getType() == 1) {
                        activity.showSexSeeting("男主播");
                    }

                }


                dismiss();
                break;
            case R.id.choose_women_sex_layout:
                if (TokenManager.getInfo(activity) != null) {
                    if (TokenManager.getInfo(activity).getType() == 0) {
                        activity.showSexSeeting("女听迷");
                    } else if (TokenManager.getInfo(activity).getType() == 1) {
                        activity.showSexSeeting("女主播");
                    }

                }

                dismiss();
                break;
            case R.id.colse_dialog:

                dismiss();
                break;
        }


    }
}
