package com.ting.myself.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.bean.myself.DouPayRank;
import com.ting.myself.MyDouActivity;

/**
 * Created by gengjiajia on 15/9/16.
 * 充值dialog
 */
public class PayDialog extends Dialog implements View.OnClickListener {
    private RelativeLayout wx_pay_layout;
    private RelativeLayout ali_pay_layout;
    private MyDouActivity activity;
    private DouPayRank douPayRank;
    private boolean wifipay;//微信版本是否有微信支付

    public PayDialog(BaseActivity baseActivity, DouPayRank douPayRank,boolean wifipay) {
        super(baseActivity, R.style.SettingDialog);
        this.activity = (MyDouActivity) baseActivity;
        this.douPayRank = douPayRank;
        this.wifipay=wifipay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        wx_pay_layout = (RelativeLayout) findViewById(R.id.wx_pay_layout);//微信支付
        ali_pay_layout = (RelativeLayout) findViewById(R.id.ali_pay_layout);//支付宝支付
        wx_pay_layout.setOnClickListener(this);
        ali_pay_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_pay_layout:
                    if (wifipay) {
                        activity.toPay("wechat");
                    } else {
                        activity.showToast("微信版本不支持支付，安装微信新版本");
                    }

                    dismiss();


                break;

            case R.id.ali_pay_layout:
                dismiss();
                activity.toPay("alipay");


                break;

            default:
                break;
        }


    }


}
