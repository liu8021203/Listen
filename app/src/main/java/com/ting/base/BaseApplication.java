package com.ting.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;

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
import com.umeng.socialize.PlatformConfig;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liu on 15/6/18.
 */
public class BaseApplication extends Application {
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    private static BaseApplication application = null;
    private List<Activity> activityList = new ArrayList<Activity>();

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
        initUM();
        if(shouldInit()) {
            MiPushClient.registerPush(this, "2882303761517189747", "5761718946747");
        }
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "tingshijie-db-encrypted" : "tingshijie-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        CrashReport.initCrashReport(getApplicationContext(), "d4e1ad51d9", false);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void initUM() {

        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5165707656240b56d6011d7f", "listen", UMConfigure.DEVICE_TYPE_PHONE, "");

        //微信 appid appsecret   9184e2475abe488cc0eacc87304539e0
        PlatformConfig.setWeixin("wx65ef305f9d0ebdec", "9184e2475abe488cc0eacc87304539e0");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("631789704", "30a378dc3e2ff7fd51d91a6ef914d170","http://sns.whalecloud.com");
        // QQ和Qzone appid appke
        PlatformConfig.setQQZone("1101054688", "VAbMLQU8mOrf3cga");

    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }




}
