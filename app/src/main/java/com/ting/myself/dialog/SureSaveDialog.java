package com.ting.myself.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.myself.SettingActivity;

/**
 * Created by gengjiajia on 15/9/16.
 * 确认保存修改资料dialog
 */
public class SureSaveDialog extends Dialog implements View.OnClickListener {
    private SettingActivity settingActivity;
    private TextView no_sure_save;
    private TextView yes_sure_save;

    public SureSaveDialog(BaseActivity baseActivity) {
        super(baseActivity, R.style.SettingDialog);
        this.settingActivity = (SettingActivity) baseActivity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sure_save);
        initView();
    }

    private void initView() {
        yes_sure_save = (TextView) findViewById(R.id.yes_sure_save);//确认修改资料
        no_sure_save = (TextView) findViewById(R.id.no_sure_save);//否修改资料
        yes_sure_save.setOnClickListener(this);
        no_sure_save.setOnClickListener(this);
        System.out.println("");
        System.out.println("fenggge");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_sure_save:
                settingActivity.SaveMess();
                dismiss();

                break;
            case R.id.no_sure_save:
                dismiss();
//                settingFrame.getActivity().finish();
                break;
        }


    }
}
