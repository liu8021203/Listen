package com.ting.welcome;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.bean.UserInfoResult;
import com.ting.constant.StaticConstant;
import com.ting.util.UtilGlide;
import com.ting.util.UtilPermission;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSPutil;
import com.ting.util.UtilStr;
import com.ting.util.UtilSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gengjiajia on 15/10/18.
 * 启动界面
 */
public class WelcomeActivity extends BaseActivity implements UtilPermission.PermissionCallbacks, SplashADListener {
    private SplashAD splashAD;
    private FrameLayout flAd;
    private TextView tvSkip;
    private Bitmap mBitmap;
    private boolean mark = false;
    //是否加载完成
    private boolean loadComplete = false;

    private boolean canJump = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        isHome = true;
        isWelcome = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (TokenManager.isLogin(this)) {
            getData();
        }
        if(Build.VERSION.SDK_INT >= 23) {
            if (UtilPermission.hasPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION})) {
//                onInto();
                splashAD = new SplashAD(mActivity, flAd, tvSkip, "1106518900", "2010526968462954", this, 0);
            } else {
                UtilPermission.requestPermissions(this, AppData.PERMISSION_CODE, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION});
            }
        }else{
            splashAD = new SplashAD(mActivity, flAd, tvSkip, "1106518900", "2010526968462954", this, 0);
//            onInto();
        }
    }


    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        flAd = (FrameLayout) findViewById(R.id.fl_ad);
        tvSkip = (TextView) findViewById(R.id.tv_skip);
        tvSkip.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", TokenManager.getUid(this));
        map.put("token", TokenManager.getToken(this));
        BaseObserver baseObserver = new BaseObserver<UserInfoResult>() {
            @Override
            public void success(UserInfoResult data) {
                super.success(data);
                TokenManager.setInfo(WelcomeActivity.this, data);
            }

            @Override
            public void error() {
                super.error();
                TokenManager.claerUid(WelcomeActivity.this);
            }
        };
        UtilRetrofit.getInstance().create(HttpService.class).getMyInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(canJump){
            next();
        }
        canJump = true;
    }

    private void next() {
        if(canJump){
            intent(MainActivity.class);
            finish();
        }else{
            canJump = true;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_skip:
                intent(MainActivity.class);
                finish();
                break;

        }
    }

    /***
     * 进入主程序的方法
     */
    private void onInto() {
        String code = UtilSPutil.getInstance(this).getString("code");
        if (UtilStr.isEmpty(code)) {
            UtilSPutil.getInstance(this).setString("code", UtilSystem.getVersionCode(this) + "");
            intent(GuidActivity.class);
            finish();
        } else {
            if (!code.equals(UtilSystem.getVersionCode(this) + "")) {
                UtilSPutil.getInstance(this).setString("code", UtilSystem.getVersionCode(this) + "");
                intent(GuidActivity.class);
                finish();
            } else {
                intent(MainActivity.class);
                finish();
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UtilPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        onInto();
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        onInto();
    }

    @Override
    public void onADDismissed() {
        next();
    }

    @Override
    public void onNoAD(AdError error) {
        Log.d("aaa", error.getErrorMsg());
        intent(MainActivity.class);
        finish();
    }

    @Override
    public void onADPresent() {

    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADTick(long l) {
        tvSkip.setText(String.format("跳过%s", Math.round(l / 1000f)));
    }




}
