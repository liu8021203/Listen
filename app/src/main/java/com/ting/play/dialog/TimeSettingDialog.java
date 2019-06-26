package com.ting.play.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ting.R;
import com.ting.common.AppData;
import com.ting.play.PlayActivity;

/**
 * Created by gengjiajia on 15/11/17.
 * 时间定时器dialog
 */
public class TimeSettingDialog extends Dialog implements View.OnClickListener {
    private PlayActivity activity;
    private RadioGroup radioGroup;
    private RadioButton mRadioButton;
    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private Button btnCancle;
    private Button btnOK;
    private int id;
    private int timer = -1;
    private SettingTimingCallBackListener mListener;
    //是否选择
    private boolean isSelect = false;


    public TimeSettingDialog(PlayActivity activity) {
        super(activity, R.style.PlayDialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        initView();
    }


    public void setListener(SettingTimingCallBackListener listener) {
        mListener = listener;
    }

    private void initView() {
        radioGroup =  findViewById(R.id.rg_play_dialog_shut_down_radio_group);
        radioButton0 =  findViewById(R.id.rb_play_dialog_shut_down_radio_button0);
        radioButton1 =  findViewById(R.id.rb_play_dialog_shut_down_radio_button1);
        radioButton2 =  findViewById(R.id.rb_play_dialog_shut_down_radio_button2);
        radioButton3 =  findViewById(R.id.rb_play_dialog_shut_down_radio_button3);
        radioButton4 =  findViewById(R.id.rb_play_dialog_shut_down_radio_button4);
        mRadioButton =  findViewById(R.id.rb_play_dialog_shut_down_radio_button);
        btnCancle =  findViewById(R.id.btn_cancle);
        btnOK =  findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_ok:

                turnOnShutDown();
                if (isSelect) {
                    if(mListener != null){
                        mListener.callback(AppData.shutDownTimer);
                    }
                    dismiss();
                } else{
                    activity.showToast("您还没对定时进行设置");
                }
                break;

            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }

    private void turnOnShutDown() {
        id = radioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rb_play_dialog_shut_down_radio_button:
                isSelect = true;
                timer = -1;
                break;
            case R.id.rb_play_dialog_shut_down_radio_button0:
                timer = 2 * 60;
                isSelect = true;
                break;
            case R.id.rb_play_dialog_shut_down_radio_button1:
                timer = 30 * 60;
                isSelect = true;
                break;
            case R.id.rb_play_dialog_shut_down_radio_button2:
                timer = 60 * 60;
                isSelect = true;
                break;
            case R.id.rb_play_dialog_shut_down_radio_button3:
                timer = 90 * 60;
                isSelect = true;
                break;
            case R.id.rb_play_dialog_shut_down_radio_button4:
                timer = 120 * 60;
                isSelect = true;
                break;
            default:
                timer = -1;
                break;
        }
        AppData.shutDownTimer = timer;
    }


    public interface SettingTimingCallBackListener{
        void callback(int timing);
    }
}

