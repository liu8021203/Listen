package com.ting.welcome;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.common.AppData;
import com.ting.util.UtilPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    /**
     * 记录拉取广告的时间
     */
    private long fetchSplashADTime = 0;
    private int minSplashTimeWhenNoAD = 2000;

    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        isHome = true;
        isWelcome = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT >= 23) {
            if (UtilPermission.hasPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION})) {
//                onInto();
                fetchSplashAD(this, flAd, tvSkip, "1106518900", "2010283107418928", this, 0);
            } else {
                UtilPermission.requestPermissions(this, AppData.PERMISSION_CODE, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION});
            }
        } else {
//            splashAD = new SplashAD(mActivity, flAd, tvSkip, "1106518900", "2010283107418928", this, 0);
            fetchSplashAD(this, flAd, tvSkip, "1106518900", "2010283107418928", this, 0);
//            onInto();
        }
    }


    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        flAd = (FrameLayout) findViewById(R.id.splash_container);
        tvSkip = (TextView) findViewById(R.id.skip_view);
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
//        Map<String, String> map = new HashMap<>();
//        map.put("uid", TokenManager.getUid(this));
//        map.put("token", TokenManager.getToken(this));
//        BaseObserver baseObserver = new BaseObserver<UserInfoResult>() {
//            @Override
//            public void success(UserInfoResult data) {
//                super.success(data);
//                TokenManager.setInfo(WelcomeActivity.this, data);
//            }
//
//            @Override
//            public void error() {
//                super.error();
//                TokenManager.claerUid(WelcomeActivity.this);
//            }
//        };
//        UtilRetrofit.getInstance().create(HttpService.class).getMyInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
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
        if (canJump) {
            intent(MainActivity.class);
            finish();
        } else {
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
            case R.id.skip_view:
                intent(MainActivity.class);
                finish();
                break;

        }
    }

    /***
     * 进入主程序的方法
     */
    private void onInto() {
//        String code = UtilSPutil.getInstance(this).getString("code");
//        if (UtilStr.isEmpty(code)) {
//            UtilSPutil.getInstance(this).setString("code", UtilSystem.getVersionCode(this) + "");
//            intent(GuidActivity.class);
//            finish();
//        } else {
//            if (!code.equals(UtilSystem.getVersionCode(this) + "")) {
//                UtilSPutil.getInstance(this).setString("code", UtilSystem.getVersionCode(this) + "");
//                intent(GuidActivity.class);
//                finish();
//            } else {
//                intent(MainActivity.class);
//                finish();
//            }
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
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
//        onInto();
        fetchSplashAD(this, flAd, tvSkip, "1106518900", "2010283107418928", this, 0);
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
//        onInto();
    }

    @Override
    public void onADDismissed() {
        next();
    }

    @Override
    public void onNoAD(AdError error) {
        long alreadyDelayMills = System.currentTimeMillis() - fetchSplashADTime;//从拉广告开始到onNoAD已经消耗了多少时间
        long shouldDelayMills = alreadyDelayMills > minSplashTimeWhenNoAD ? 0 : minSplashTimeWhenNoAD
                - alreadyDelayMills;//为防止加载广告失败后立刻跳离开屏可能造成的视觉上类似于"闪退"的情况，根据设置的minSplashTimeWhenNoAD

        // 计算出还需要延时多久
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent(MainActivity.class);
                finish();
            }
        }, shouldDelayMills);
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

    @Override
    public void onADExposure() {
    }


    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity      展示广告的activity
     * @param adContainer   展示广告的大容器
     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId         应用ID
     * @param posId         广告位ID
     * @param adListener    广告状态监听器
     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        fetchSplashADTime = System.currentTimeMillis();
        Map<String, String> tags = new HashMap<>();
        tags.put("tag_s1", "value_s1");
        tags.put("tag_s2", "value_s2");
        splashAD = new SplashAD(activity, skipContainer, appId, posId, adListener, fetchDelay);
//        splashAD = new SplashAD(activity, skipContainer, appId, posId, adListener,
//                fetchDelay, tags);
//        LoadAdParams params = new LoadAdParams();
//        params.setLoginAppId("1106518900");
//        params.setLoginOpenid("2010283107418928");
//        params.setUin("testUin");
//        splashAD.setLoadAdParams(params);
        splashAD.fetchAndShowIn(adContainer);
        // 如果不需要传tag，使用如下构造函数

    }



    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
