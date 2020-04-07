package com.ting.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.base.MessageEventBus;
import com.ting.bean.BaseResult;
import com.ting.bean.UserInfoResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.regist.RegistMainActivity;
import com.ting.util.StrUtil;
import com.ting.util.UtilIntent;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSPutil;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhoneLoginActivity extends BaseActivity {
    private TextView login_immediately;
    private TextView to_regist_text;
    private TextView forget_password_textview;
    private EditText user_name_editext;
    private EditText password_editext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
    }

    @Override
    protected String setTitle() {
        return "账号登录";
    }

    @Override
    protected void initView() {
        login_immediately = (TextView) findViewById(R.id.login_immediately);
        to_regist_text = (TextView) findViewById(R.id.to_regist_text);
        forget_password_textview = (TextView) findViewById(R.id.forget_password_textview);
        user_name_editext = (EditText) findViewById(R.id.user_name_editext);//用户名
        password_editext = (EditText) findViewById(R.id.password_editext);//密码
        login_immediately.setOnClickListener(this);
        to_regist_text.setOnClickListener(this);
        forget_password_textview.setOnClickListener(this);
        String name = UtilSPutil.getInstance(mActivity).getString("user_name");
        if (!TextUtils.isEmpty(name)) {
            user_name_editext.setText(name);
            user_name_editext.setSelection(name.length());
        }
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
            case R.id.login_immediately:
                String user_name = user_name_editext.getText().toString().trim();
                String password = password_editext.getText().toString();
                if (TextUtils.isEmpty(user_name)) {
                    showToast("请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("请输入密码");
                    return;
                }
                Login(user_name, password);

                break;
            case R.id.to_regist_text:
                intent(RegistMainActivity.class);
                break;
            case R.id.forget_password_textview:
                intent(FindPasswordActivity.class);
                break;


            default:
                break;
        }
    }



    private void Login(String user_name, String password) {
        Map<String, String> map = new HashMap<>();
        String name = null;
        String pwd = StrUtil.md5(password);
        UtilSPutil.getInstance(mActivity).setString("user_name", user_name);
        try {
            name = URLEncoder.encode(user_name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("phone", name);
        map.put("type", "1");
        map.put("password", pwd);
        BaseObserver baseObserver = new BaseObserver<BaseResult<UserInfoResult>>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {
            @Override
            public void success(BaseResult<UserInfoResult> data) {
                super.success(data);
                UserInfoResult result = data.getData();
                if(result != null){
                    TokenManager.setInfo(result);
                    TokenManager.setUid(mActivity, String.valueOf(result.getId()));
                    TokenManager.setToken(mActivity, result.getToken());
                    EventBus.getDefault().post(new MessageEventBus(MessageEventBus.LOGIN));
                    showToast("登陆成功");
                    finish();
                }
            }

            @Override
            public void error(BaseResult<UserInfoResult> value, Throwable e) {
                super.error(value, e);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).login(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mActivity, LoginMainActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(UtilIntent.FINISH_DEFAULT_ENTER_ANIM, UtilIntent.FINISH_DEFAULT_EXIT_ANIM);
        finish();
    }
}
