package com.ting.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.bean.BaseResult;
import com.ting.common.http.HttpService;
import com.ting.bean.FindPasswordResult;
import com.ting.util.StrUtil;
import com.ting.util.UtilRetrofit;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/11.
 * frame之找回密码
 */
public class FindPasswordActivity extends BaseActivity{
    private TextView find_password_getcode;
    private TextView find_password_reset;
    private int mTime = 60;// 验证码限制时间
    private String phoneNumber;
    private EditText find_password_mobilephone;
    private EditText find_password_code;
    private EditText regist_passowrd_editext;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
                    if (mTime > 0) {
                        find_password_getcode.setText(mTime + "s");
                        mTime--;
                        mHandler.sendEmptyMessageDelayed(0,1000);
                    } else {
                        find_password_getcode.setText("获取验证码");
                        find_password_getcode.setEnabled(true);
                        mTime = 60;
                    }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
    }

    @Override
    protected String setTitle() {
        return "找回密码";
    }

    @Override
    protected void initView() {
        find_password_mobilephone = (EditText) findViewById(R.id.find_password_mobilephone);//手机号输入框
        find_password_code = (EditText) findViewById(R.id.find_password_code);//验证码输入框
        find_password_getcode = (TextView) findViewById(R.id.find_password_getcode);//获取验证码
        regist_passowrd_editext = (EditText) findViewById(R.id.regist_passowrd_editext);
        find_password_reset = (TextView) findViewById(R.id.find_password_reset);//更改密码
        find_password_getcode.setOnClickListener(this);
        find_password_reset.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.find_password_getcode:
                phoneNumber = find_password_mobilephone.getText().toString().trim();
                if (phoneNumber.equals("")) {
                    showToast("手机号不能为空");
                    return;
                }
                getAuctoCode(phoneNumber);
                break;
            case R.id.find_password_reset:
                String phone = find_password_mobilephone.getText().toString().trim();
                String code = find_password_code.getText().toString().trim();
                String password = regist_passowrd_editext.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    showToast("请输入手机号");
                    return;
                }
                if(TextUtils.isEmpty(code)){
                    showToast("请输入验证码");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    showToast("请输入密码");
                    return;
                }
                modifyPassword(phone, code, StrUtil.md5(password));
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getAuctoCode(String tel) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", tel);
        find_password_getcode.setEnabled(false);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this){

            @Override
            public void success(BaseResult data) {
                super.success(data);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void error(BaseResult value, Throwable e) {
                super.error(value, e);
                find_password_getcode.setEnabled(true);
            }

        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).sendSms(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    private void modifyPassword(String phone, String code, String password){
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("code", code);
        find_password_reset.setEnabled(false);
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this, BaseObserver.MODEL_SHOW_DIALOG_TOAST){

            @Override
            public void success(BaseResult data) {
                super.success(data);
                onBackPressed();
            }

            @Override
            public void error(BaseResult value, Throwable e) {
                super.error(value, e);
                find_password_reset.setEnabled(true);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).findPassword(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
