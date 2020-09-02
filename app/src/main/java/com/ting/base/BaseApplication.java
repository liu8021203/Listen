package com.ting.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.lechuan.midunovel.view.FoxSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.ting.R;
import com.ting.db.DaoMaster;
import com.ting.db.DaoSession;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.greenrobot.greendao.database.Database;


import androidx.multidex.MultiDex;


/**
 * Created by liu on 15/6/18.
 */
public class BaseApplication extends Application {
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    private static BaseApplication application = null;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.b24d62, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "tingshijie-db-encrypted" : "tingshijie-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        CrashReport.initCrashReport(getApplicationContext(), "d4e1ad51d9", false);

        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdSdk.init(this,
                new TTAdConfig.Builder()
                        .appId("5038728")
                        .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                        .appName("听世界听书")
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                        .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_4G) //允许直接下载的网络状态集合
                        .supportMultiProcess(true) //是否支持多进程，true支持
                        //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                        .build());
        initUM();
        FoxSDK.init(this);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void initUM() {
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5165707656240b56d6011d7f", "Umeng", 1, "f5312dffd2c94d94a2e56d42c9e76755");

        //微信 appid appsecret   9184e2475abe488cc0eacc87304539e0
        PlatformConfig.setWeixin("wx65ef305f9d0ebdec", "9184e2475abe488cc0eacc87304539e0");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("631789704", "30a378dc3e2ff7fd51d91a6ef914d170", "http://sns.whalecloud.com");
        // QQ和Qzone appid appke
        PlatformConfig.setQQZone("1101054688", "VAbMLQU8mOrf3cga");

        PushAgent pushAgent = PushAgent.getInstance(this);
        pushAgent.setDisplayNotificationNumber(5);
        pushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String s) {
                Log.i("xxx", "--->>> onSuccess, s is " + s);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i("xxx", "--->>> onFailure, s is " + s + ", s1 is " + s1);
            }

        });
        HuaWeiRegister.register(this);
        MiPushRegistar.register(this, "2882303761517189747", "5761718946747");
        OppoRegister.register(this, "bmi3rmjkRl4OSOgswk8G8c8ck", "Ad2f9eE495f040c0c7C87F883434A45a");
    }


    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static BaseApplication getInstance() {
        if (null == application) {
            application = new BaseApplication();
        }
        return application;

    }


    /**
     * 获取当前进程名
     */
    private String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }



}
