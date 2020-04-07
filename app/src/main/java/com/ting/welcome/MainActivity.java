package com.ting.welcome;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.FileProvider;

import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ting.R;
import com.ting.base.BaseObserver;
import com.ting.base.PlayerBaseActivity;
import com.ting.bean.BaseResult;
import com.ting.bean.NoticeResult;
import com.ting.bean.apk.ApkResult;
import com.ting.bookcity.HomeFragment;
import com.ting.bookcity.dialog.ExitDialog;
import com.ting.bookrack.BookRackFragment;
import com.ting.category.CategoryFragment;
import com.ting.common.TokenManager;
import com.ting.common.dialog.NoticeDialog;
import com.ting.common.http.HttpService;
import com.ting.download.receiver.ApkInstallReceiver;
import com.ting.myself.MineMainFrame;
import com.ting.search.SearchActivity;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSPutil;
import com.ting.util.UtilSystem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends PlayerBaseActivity implements View.OnClickListener {
    private FragmentManager manager;
    private LinearLayout book_city_frame;
    private LinearLayout book_record_frame;
    private LinearLayout myself_frame;
    private LinearLayout book_sort_frame;
    private ImageView book_city_image;
    private ImageView book_record_image;
    private ImageView me_image;
    private ImageView book_sort_image;
    private TextView book_city_text;
    private TextView book_sort_text;
    private TextView book_record_text;
    private TextView me_text;

    //书架
    private BookRackFragment mBookRackFragment;
    //书城
    private HomeFragment mHomeFragment;
    //分类
    private CategoryFragment mCategoryFragment;
    //我的frame
    private MineMainFrame mineMainFrame;


    private ApkInstallReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isHome = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        manager = getSupportFragmentManager();
        /*获取Intent中的Bundle对象*/
        setTabSelection(0);
        // 通知状态
        checkUpdate();
        if (!UtilNetStatus.isWifiConnection() && UtilNetStatus.isHasConnection(this)) {
            showToast("非wifi下请注意流量");
        }

        /**
         * 注册下载广播
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        receiver = new ApkInstallReceiver();
        registerReceiver(receiver, intentFilter);


        getNotice();
    }


    @Override
    protected void notifyPlay(String bookId, String bookTitle, String chapterTitle, String bookImage, long duration) {
        super.notifyPlay(bookId, bookTitle, chapterTitle, bookImage, duration);
        if (mBookRackFragment != null) {
            mBookRackFragment.startAnim();
        }
        if (mHomeFragment != null) {
            mHomeFragment.statAnim();
        }
    }

    @Override
    protected void notifyPause() {
        super.notifyPause();
        if (mBookRackFragment != null) {
            mBookRackFragment.stopAnim();
        }
        if (mHomeFragment != null) {
            mHomeFragment.stopAnim();
        }
    }

    @Override
    protected void notifyStop() {
        super.notifyStop();
        if (mBookRackFragment != null) {
            mBookRackFragment.stopAnim();
        }
        if (mHomeFragment != null) {
            mHomeFragment.stopAnim();
        }
    }


    @Override
    protected void notifyServiceConnected() {
        super.notifyServiceConnected();
        if (getPlaybackStateCompat() != null && (getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PLAYING)) {
            if (mBookRackFragment != null) {
                mBookRackFragment.startAnim();
            }
            if (mHomeFragment != null) {
                mHomeFragment.statAnim();
            }
        } else if (getPlaybackStateCompat() != null && getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PAUSED) {
            if (mBookRackFragment != null) {
                mBookRackFragment.stopAnim();
            }
            if (mHomeFragment != null) {
                mHomeFragment.stopAnim();
            }
        } else if (getPlaybackStateCompat() != null && getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_STOPPED) {
            if (mBookRackFragment != null) {
                mBookRackFragment.stopAnim();
            }
            if (mHomeFragment != null) {
                mHomeFragment.stopAnim();
            }
        } else {
            if (mBookRackFragment != null) {
                mBookRackFragment.stopAnim();
            }
            if (mHomeFragment != null) {
                mHomeFragment.stopAnim();
            }
        }

    }


    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {

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


    /**
     * 检查更新
     */
    private void checkUpdate() {
        Map<String, String> map = new HashMap<>();
        map.put("version", UtilSystem.getVersionCode(this) + "");
        BaseObserver baseObserver = new BaseObserver<BaseResult<ApkResult>>(this, BaseObserver.MODEL_NO) {
            @Override
            public void success(BaseResult<ApkResult> data) {
                super.success(data);
                Intent intent = new Intent(MainActivity.this, ApkDownloadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("vo", data.getData());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        UtilRetrofit.getInstance().create(HttpService.class).appVersionUpdate(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    private void init() {
        book_city_frame = (LinearLayout) findViewById(R.id.book_city_frame);//书城LinearLayout
        book_sort_frame = (LinearLayout) findViewById(R.id.book_sort_frame);//分类LinearLayout
        book_record_frame = (LinearLayout) findViewById(R.id.book_record_frame);//纪录LinearLayout
        myself_frame = (LinearLayout) findViewById(R.id.myself_frame);//我的LinearLayout
        book_city_image = (ImageView) findViewById(R.id.book_city_image);//书城image；
        book_sort_image = (ImageView) findViewById(R.id.book_sort_image);//分类image
        book_record_image = (ImageView) findViewById(R.id.book_record_image);//纪录image
        me_image = (ImageView) findViewById(R.id.me_image);//我的image
        book_city_text = (TextView) findViewById(R.id.book_city_text);//书城text
        book_sort_text = (TextView) findViewById(R.id.book_sort_text);//分类text
        book_record_text = (TextView) findViewById(R.id.book_record_text);//纪录text
        me_text = (TextView) findViewById(R.id.me_text);//我的text
        book_city_frame.setOnClickListener(this);
        book_sort_frame.setOnClickListener(this);
        book_record_frame.setOnClickListener(this);
        myself_frame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_city_frame:
                clickfailure(0);
                setTabSelection(0);
                break;
            case R.id.book_sort_frame:
                clickfailure(1);
                setTabSelection(1);
                break;
            case R.id.book_record_frame:
                clickfailure(3);
                setTabSelection(3);
                break;
            case R.id.myself_frame:
                clickfailure(4);
                setTabSelection(4);
                break;
        }
    }


    /**
     * 点击失效
     *
     * @param index
     */
    private void clickfailure(int index) {
        switch (index) {
            case 0:

                book_city_frame.setEnabled(false);
                book_sort_frame.setEnabled(true);
                book_record_frame.setEnabled(true);
                myself_frame.setEnabled(true);
                break;
            case 1:
                book_city_frame.setEnabled(true);
                book_sort_frame.setEnabled(false);
                book_record_frame.setEnabled(true);
                myself_frame.setEnabled(true);
                break;
            case 2:
                book_city_frame.setEnabled(true);
                book_sort_frame.setEnabled(true);
                book_record_frame.setEnabled(true);
                myself_frame.setEnabled(true);
                break;
            case 3:
                book_city_frame.setEnabled(true);
                book_sort_frame.setEnabled(true);
                book_record_frame.setEnabled(false);
                myself_frame.setEnabled(true);
                break;
            case 4:
                book_city_frame.setEnabled(true);
                book_sort_frame.setEnabled(true);
                book_record_frame.setEnabled(true);
                myself_frame.setEnabled(false);
                break;

            default:
                break;
        }
    }

    private void setTabSelection(int index) {
        //开启一个Fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        clearState();
        hideFragments(transaction);
        switch (index) {
            case 0:
                book_city_image.setImageResource(R.mipmap.main_bookrack_select);
                book_city_text.setTextColor(0xff3baef2);
                if (mBookRackFragment == null) {
                    mBookRackFragment = new BookRackFragment();
                    transaction.add(R.id.activity_main_frame, mBookRackFragment);
                } else {
                    transaction.show(mBookRackFragment);
                }
                transaction.commitAllowingStateLoss();
                break;

            case 1:
                book_sort_image.setImageResource(R.mipmap.main_home_select);
                book_sort_text.setTextColor(0xff3baef2);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.activity_main_frame, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                transaction.commitAllowingStateLoss();
                break;

            case 3:
                book_record_image.setImageResource(R.mipmap.main_anchor_select);
                book_record_text.setTextColor(0xff3baef2);
                if (mCategoryFragment == null) {
                    mCategoryFragment = new CategoryFragment();
                    transaction.add(R.id.activity_main_frame, mCategoryFragment);
                } else {
                    transaction.show(mCategoryFragment);
                }
                transaction.commitAllowingStateLoss();
                break;

            case 4:
                me_image.setImageResource(R.mipmap.main_mine_select);
                me_text.setTextColor(0xff3baef2);
                if (mineMainFrame == null) {
                    mineMainFrame = new MineMainFrame();
                    transaction.add(R.id.activity_main_frame, mineMainFrame);
                } else {
                    transaction.show(mineMainFrame);
                }
                transaction.commitAllowingStateLoss();
                break;


            default:
                break;
        }

    }


    /**
     * 清楚所有状态
     */
    private void clearState() {
        book_city_image.setImageResource(R.mipmap.main_bookrack_unselect);
        book_sort_image.setImageResource(R.mipmap.main_home_unselect);
        book_record_image.setImageResource(R.mipmap.main_anchor_unselect);
        me_image.setImageResource(R.mipmap.main_mine_unselect);
        book_city_text.setTextColor(0xffadadad);
        book_sort_text.setTextColor(0xffadadad);
        book_record_text.setTextColor(0xffadadad);
        me_text.setTextColor(0xffadadad);
    }

    /**
     * 将所有的Fragment设置为隐藏状态
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mBookRackFragment != null) {
            transaction.hide(mBookRackFragment);
        }
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mCategoryFragment != null) {
            transaction.hide(mCategoryFragment);
        }
        if (mineMainFrame != null) {
            transaction.hide(mineMainFrame);
        }

    }


    @Override
    public void onBackPressed() {
        exitAppDialog();
    }

    private void exitAppDialog() {
        ExitDialog dialog = new ExitDialog(this);
        dialog.show();
    }


    /**
     * @Title: exitApp
     * @Description: 退出APP
     */
    public void exitApp() {
        MediaControllerCompat.getMediaController(MainActivity.this).getTransportControls().stop();
        finish();
    }


    @Override
    public void onback(View view) {
        intent(SearchActivity.class);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int currIndex = bundle.getInt("index", 0);
            clickfailure(currIndex);
            setTabSelection(currIndex);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }


    /**
     * Downloadmanager下载完成接受的广播
     */
    class Recevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String serviceString = Context.DOWNLOAD_SERVICE;
            String realPath = "";
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(serviceString);
            long apkId = UtilSPutil.getInstance(context).getLong("APK_ID", -1L);
            Bundle extras = intent.getExtras();
            long doneDownloadId = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE) && apkId == doneDownloadId) {
                Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(doneDownloadId));
                if (c != null) {
                    c.moveToFirst();
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        int fileUriIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                        String fileUri = c.getString(fileUriIdx);
                        realPath = Uri.parse(fileUri).getPath();
                    } else {
                        realPath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    }
                    c.close();
                }
                Uri uri;
                Intent install = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(MainActivity.this, "com.sfbest.mapp.fileprovider", new File(realPath));
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(new File(realPath));
                }
                install.setDataAndType(uri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        }
    }


    private void getNotice() {
        Map<String, String> map = new HashMap<>();
        long systemTime = UtilSPutil.getInstance(mActivity).getLong("systemTime", -1L);
        map.put("systemTime", String.valueOf(systemTime));
        BaseObserver baseObserver = new BaseObserver<BaseResult<NoticeResult>>(this, BaseObserver.MODEL_ONLY_SHOW_DIALOG) {
            @Override
            public void success(BaseResult<NoticeResult> data) {
                super.success(data);
                NoticeResult result = data.getData();
                if(result != null){
                    NoticeDialog dialog = new NoticeDialog(mActivity);
                    dialog.setContent(result.getContent());
                    dialog.show();
                }
            }


            @Override
            public void error(BaseResult<NoticeResult> value, Throwable e) {
                super.error(value, e);
            }
        };
        mDisposable.add(baseObserver);
        UtilRetrofit.getInstance().create(HttpService.class).getNotice(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }





}