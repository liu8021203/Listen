package com.ting.bookcity.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ting.R;
import com.ting.welcome.MainActivity;

/**
 * Created by gengjiajia on 15/9/16.
 * 退出应用dialog
 */
public class ExitDialog extends Dialog implements View.OnClickListener {

    private MainActivity activity;
    private TextView cancle_exit;
    private TextView hide_exit;
    private TextView sure_exit;

    public ExitDialog(MainActivity mainActivity) {
        super(mainActivity, R.style.SettingDialog);
        this.activity = mainActivity;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit);
        initView();
    }

    private void initView() {
        cancle_exit = (TextView) findViewById(R.id.cancle_exit);//取消
        hide_exit = (TextView) findViewById(R.id.hide_exit);//隐藏
        sure_exit = (TextView) findViewById(R.id.sure_exit);//确定退出


        cancle_exit.setOnClickListener(this);
        hide_exit.setOnClickListener(this);
        sure_exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle_exit:
                this.dismiss();
                break;
            case R.id.hide_exit:
                moveTaskToBack();
                dismiss();
                break;
            case R.id.sure_exit:
                 activity.exitApp();
                dismiss();
                break;
            default:
                break;
        }


    }

    /**
     * 退出应用到后台运行
     */
    public void moveTaskToBack() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(i);
//        activity.moveTaskToBack(true);
    }
}
