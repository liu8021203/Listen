package com.ting.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.base.MessageEventBus;
import com.ting.bean.BaseResult;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.regist.RegistMainActivity;
import com.ting.bean.UserInfoResult;
import com.ting.util.StrUtil;
import com.ting.util.UtilPermission;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSPutil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * liu
 */
public class LoginMainActivity extends BaseActivity implements UtilPermission.PermissionCallbacks {
    private TextView login_immediately;
    private TextView to_regist_text;
    private TextView forget_password_textview;
    private ImageView qq_login;//qq登录
    private ImageView weixin_login;//微信登录
    private TextView tvPhoneLogin;
    private UMShareAPI mShareAPI;
    private String source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login);
        mShareAPI = UMShareAPI.get(this);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(mActivity).setShareConfig(config);
    }

    @Override
    protected String setTitle() {
        return "登录";
    }

    @Override
    protected void initView() {
        login_immediately = (TextView) findViewById(R.id.login_immediately);
        to_regist_text = (TextView) findViewById(R.id.to_regist_text);
        forget_password_textview = (TextView) findViewById(R.id.forget_password_textview);
        qq_login = (ImageView) findViewById(R.id.qq_login);//QQ登录
        weixin_login = (ImageView) findViewById(R.id.weixin_login);//微信登录
        qq_login.setOnClickListener(this);
        weixin_login.setOnClickListener(this);
        login_immediately.setOnClickListener(this);
        to_regist_text.setOnClickListener(this);
        forget_password_textview.setOnClickListener(this);
        tvPhoneLogin = findViewById(R.id.tv_phone_login);
        tvPhoneLogin.setOnClickListener(this);
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
            case R.id.qq_login:
                source = "2";
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.weixin_login:
                source = "1";
                UMShareAPI.get(mActivity).getPlatformInfo(mActivity, SHARE_MEDIA.WEIXIN, umAuthListener);

                break;
            case R.id.to_regist_text:
                intent(RegistMainActivity.class);
                break;
            case R.id.forget_password_textview:
                intent(FindPasswordActivity.class);
                break;

            case R.id.tv_phone_login:
                intent(PhoneLoginActivity.class);
                finish();
                break;
            default:
                break;
        }
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA media) {
            showProgressDialog();
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            showToast("授权成功");
            removeProgressDialog();
            /**
             * 授权成功，获取用户信息第三方标识，1--微信 2--QQ 3--新浪微博
             */
            String type = null;
            if (source.equals("1")) {
                type = "3";
            } else if (source.equals("2")) {
                type = "4";
            }
            String nickname = data.get("name");
            String sex;
            if (data.get("gender").equals("男")) {
                sex = "1";
            } else {
                sex = "2";
            }
            String uuid = data.get("uid");
            String thumb = data.get("iconurl");
            loginOther(type, nickname, sex, uuid, thumb);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast("登录失败");
            removeProgressDialog();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("登录取消");
            removeProgressDialog();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }




    private void loginOther(String type, String nickname, String sex, String uuid, String thumb) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("uuid", uuid);
        map.put("name", nickname);
        map.put("sex", sex);
        map.put("image", thumb);
        BaseObserver baseObserver = new BaseObserver<BaseResult<UserInfoResult>>(mActivity, BaseObserver.MODEL_SHOW_DIALOG_TOAST) {

            @Override
            public void success(BaseResult<UserInfoResult> data) {
                super.success(data);
                TokenManager.setInfo(data.getData());
                TokenManager.setUid(mActivity, data.getData().getId());
                TokenManager.setToken(mActivity, data.getData().getToken());
                EventBus.getDefault().post(new MessageEventBus(MessageEventBus.LOGIN));
                showToast("登陆成功");
                finish();
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).login(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UtilPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        Log.d("aaa", "允许");
        switch (requestCode) {
            case 1:

                break;

            case 2:

                break;

            case 3:
                source = "1";
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
        }
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        Log.d("aaa", "禁止");
    }
}
