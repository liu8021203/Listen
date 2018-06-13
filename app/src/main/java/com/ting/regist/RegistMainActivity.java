package com.ting.regist;

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
import com.ting.util.StrUtil;
import com.ting.util.UtilIntent;
import com.ting.util.UtilRetrofit;
import com.ting.welcome.MainActivity;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gengjiajia on 15/9/10.
 * 注册主frame
 */
public class RegistMainActivity extends BaseActivity implements View.OnClickListener {
    private TextView get_identifying_code;
    private int mTime = 60;// 验证码限制时间
    private EditText phone_number_editext;
    private EditText identifying_code_editext;
    private EditText regist_passowrd_editext;
    private EditText regist_passowrd_too_editext;
    private String phoneNumber;
    private TextView regist_immediately;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (mTime > 0) {
                get_identifying_code.setText(mTime + "s");
                get_identifying_code.setEnabled(false);
                mTime--;
                mHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                get_identifying_code.setText("获取验证码");
                get_identifying_code.setEnabled(true);
                mTime = 60;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_regist);
    }

    @Override
    protected String setTitle() {
        return "注册";
    }

    @Override
    protected void initView() {
        get_identifying_code = (TextView) findViewById(R.id.get_identifying_code);//获取验证码\
        phone_number_editext = (EditText) findViewById(R.id.phone_number_editext);//输入手机号
        identifying_code_editext = (EditText) findViewById(R.id.identifying_code_editext);//输入验证码
        regist_passowrd_editext = (EditText) findViewById(R.id.regist_passowrd_editext);//输入密码
        regist_passowrd_too_editext = (EditText) findViewById(R.id.regist_passowrd_too_editext);//再次输入密码
        regist_immediately = (TextView) findViewById(R.id.regist_immediately);//立即注册
        get_identifying_code.setOnClickListener(this);
        regist_immediately.setOnClickListener(this);
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
            case R.id.regist_immediately: {
                String phoneNumber = phone_number_editext.getText().toString().trim();
                String prepassword = regist_passowrd_editext.getText().toString().trim();
                String afterpassword = regist_passowrd_too_editext.getText().toString().trim();
                String code = identifying_code_editext.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    showToast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showToast("请获取验证码");
                    return;
                }
                if (TextUtils.isEmpty(prepassword) || TextUtils.isEmpty(afterpassword)) {
                    showToast("请输入密码");
                    return;
                }
                if (!prepassword.equals(afterpassword)) {
                    showToast("两个密码不一致，请确认");
                    return;
                }
                register(phoneNumber, code, StrUtil.md5(afterpassword));
            }
                break;
            case R.id.get_identifying_code: {
                String phoneNumber = phone_number_editext.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    showToast("手机号不能为空");
                    return;
                }
                getAuctoCode(phoneNumber);
            }
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getAuctoCode(String phone) {
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this) {

            @Override
            protected void onStart() {
                super.onStart();
                get_identifying_code.setEnabled(false);
            }

            @Override
            public void success(BaseResult data) {
                super.success(data);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void error() {
                removeProgressDialog();
                get_identifying_code.setEnabled(true);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getCellPhoneValidation(phone).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    private void register(String phone, String code, String password) {
        BaseObserver baseObserver = new BaseObserver<BaseResult>(this) {

            @Override
            protected void onStart() {
                super.onStart();
                regist_immediately.setEnabled(false);
            }

            @Override
            public void success(BaseResult data) {
                super.success(data);
                regist_immediately.setEnabled(true);
                UtilIntent.intentDIY(RegistMainActivity.this, MainActivity.class);
                finish();
            }

            @Override
            public void error() {
                removeProgressDialog();
                regist_immediately.setEnabled(true);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getUserRegister(phone, password, code).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

}
