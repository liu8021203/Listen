package com.ting.play;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.ting.R;
import com.ting.base.BaseActivity;
import com.ting.base.BaseObserver;
import com.ting.base.PlayerBaseActivity;
import com.ting.bean.BaseResult;
import com.ting.bean.ChapterResult;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.download.DownloadController;
import com.ting.play.controller.MusicDBController;
import com.ting.play.dialog.PlayListDialog;
import com.ting.play.dialog.SettingSecondDialog;
import com.ting.play.dialog.ShareDialog;
import com.ting.play.dialog.SpeedDialog;
import com.ting.play.dialog.TimeSettingDialog;
import com.ting.play.receiver.PlayerReceiver;
import com.ting.play.service.MusicService;
import com.ting.util.UtilDate;
import com.ting.util.UtilGlide;
import com.ting.util.UtilIntent;
import com.ting.util.UtilListener;
import com.ting.util.UtilNetStatus;
import com.ting.util.UtilPixelTransfrom;
import com.ting.util.UtilRetrofit;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu on 2017/12/9.
 */

public class PlayActivity extends PlayerBaseActivity {
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    private ImageView ivImg;
    private ImageView ivClose;
    private ImageView ivPrevious;
    private ImageView ivNext;
    private ImageView ivPlay;
    private ImageView ivList;
    private TextView tvTiming;
    private TextView play_name;
    private TextView program_number;
    private TextView tvShare;
    private TextView tvSpeed;
    private TextView tvSetting;
    private SeekBar music_seekbar;
    private TextView tv_current_time;
    private TextView tv_total_time;
    private ProgressBar music_progress;
    //书籍ID
    private String bookId;

    private CardView mCardView;
    private ImageView ivAdImg;
    private TextView tvAdTitle;
    private ImageView ivAdClose;
    private RelativeLayout rlAdLayout;

    private TTAdNative mTTAdNative;


    private int position = 1;

    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mScheduleFuture;
    private final Handler mHandler = new Handler();

    private ScheduledFuture<?> mTimerScheduleFuture;
    private final Runnable mUpdateTimerTask = new Runnable() {
        @Override
        public void run() {
            updateTimer();
        }
    };

    //是否进来播放
    private boolean isPlay = false;
    //播放数据列表
    private List<DBChapter> playQueue = null;
    private int timer = -1;

    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }


    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        ivClose = flContent.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        ivPrevious = flContent.findViewById(R.id.iv_previous);
        ivPrevious.setOnClickListener(this);
        ivNext = flContent.findViewById(R.id.iv_next);
        ivNext.setOnClickListener(this);
        ivPlay = flContent.findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(this);
        program_number = flContent.findViewById(R.id.program_number);//播放集数
        play_name = flContent.findViewById(R.id.play_name);//播放小说名
        play_name.setOnClickListener(this);
        tvTiming = flContent.findViewById(R.id.tv_timing);//时间定时器
        tvTiming.setOnClickListener(this);
        tv_current_time = flContent.findViewById(R.id.tv_current_time);
        tv_total_time = flContent.findViewById(R.id.tv_total_time);
        music_seekbar = flContent.findViewById(R.id.music_seekbar);
        music_seekbar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        music_progress = flContent.findViewById(R.id.music_progress);
        tvShare = flContent.findViewById(R.id.tv_share);
        tvShare.setOnClickListener(this);
        ivList = flContent.findViewById(R.id.iv_list);
        ivList.setOnClickListener(this);
        tvSpeed = findViewById(R.id.tv_speed);
        tvSpeed.setOnClickListener(this);
        ivImg = findViewById(R.id.iv_img);
        mCardView = findViewById(R.id.card_view);
        int width = 0;
        if (UtilPixelTransfrom.getScreenHeight(this) > 1000 && UtilPixelTransfrom.getScreenHeight(this) <= 1500) {
            width = UtilPixelTransfrom.dip2px(this, 150);
        } else if (UtilPixelTransfrom.getScreenHeight(this) > 1500 && UtilPixelTransfrom.getScreenHeight(this) <= 1900) {
            width = UtilPixelTransfrom.dip2px(this, 200);
        } else {
            width = UtilPixelTransfrom.dip2px(this, 250);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, (int) (width / 1.52f));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin = UtilPixelTransfrom.dip2px(this, 80);
//        params.setMargins(0, 20,0,0);
        mCardView.setLayoutParams(params);
        ivAdImg = findViewById(R.id.iv_ad_img);
        tvAdTitle = findViewById(R.id.tv_ad_title);
        ivAdClose = findViewById(R.id.iv_ad_close);
        ivAdClose.setOnClickListener(this);
        ivAdClose.setColorFilter(0xffffffff);
        rlAdLayout = findViewById(R.id.rl_ad_layout);
        tvSetting = findViewById(R.id.tv_setting);
        tvSetting.setOnClickListener(this);

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("938728263") //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setImageAcceptedSize(width, (int) (width / 1.52f))//这个参数设置即可，不影响模板广告的size
                .build();
        mTTAdNative = TTAdSdk.getAdManager().createAdNative(this);
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> list) {
                if (list != null && !list.isEmpty()) {
                    rlAdLayout.setVisibility(View.VISIBLE);
                    TTFeedAd ad = list.get(0);
                    UtilGlide.loadImg(mActivity, ad.getImageList().get(0).getImageUrl(), ivAdImg);
                    Log.d("ad", "url=====" + ad.getIcon().getImageUrl());
                    tvAdTitle.setText(ad.getDescription());
                    //可以被点击的view, 也可以把convertView放进来意味item可被点击
                    List<View> clickViewList = new ArrayList<>();
                    clickViewList.add(rlAdLayout);
                    List<View> creativeViewList = new ArrayList<>();
                    creativeViewList.add(rlAdLayout);
                    ad.registerViewForInteraction(rlAdLayout, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, TTNativeAd ad) {

                        }

                        @Override
                        public void onAdCreativeClick(View view, TTNativeAd ad) {
                            rlAdLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdShow(TTNativeAd ad) {

                        }


                    });


                    switch (ad.getInteractionType()) {
                        case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                            //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
                            ad.setActivityForDownloadApp(mActivity);
                            break;

                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        /****************统计*******************/
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(bookId));
        MobclickAgent.onEvent(this, "BOOK_ID", map);
        /****************统计******************/
    }


    private void loadingPlayData() {
        if (UtilNetStatus.isHasConnection(this)) {
            DBListenHistory mHistory = MusicDBController.getLastDBListenHistoryByBookId(bookId);
            if (mHistory != null) {
                int position = mHistory.getPosition();
                page = (position - 1) / 50 + 1;
                getData();
            } else {
                page = 1;
                getData();
            }
        } else {
            playQueue = DownloadController.queryDataAsc(bookId, "4");
            if (playQueue != null && !playQueue.isEmpty()) {
                DBChapter chapter = playQueue.get(0);
                if (isPlay) {
                    DBListenHistory history = MusicDBController.getLastDBListenHistoryByBookId(bookId);
                    Bundle bundle = new Bundle();
                    if (history != null) {
                        int currentIndex = -1;
                        for (int i = 0; i < playQueue.size(); i++) {
                            if (TextUtils.equals(history.getChapterId(), playQueue.get(i).getChapterId())) {
                                currentIndex = i;
                                break;
                            }
                        }
                        if (currentIndex == -1) {
                            bundle.putParcelable("vo", chapter);
                        } else {
                            bundle.putParcelable("vo", UtilListener.dBListenHistoryToDBChapter(history));
                        }
                    } else {
                        bundle.putParcelable("vo", chapter);
                    }
                    bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) playQueue);
                    MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().playFromSearch("aaa", bundle);
                }
            } else {
//                showEmptyErrorLayout("没有相关播放内容", R.mipmap.empty_player);
            }
        }
    }


    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        map.put("page", String.valueOf(page));
        map.put("size", "50");
        if (TokenManager.isLogin(mActivity)) {
            map.put("uid", TokenManager.getUid(mActivity));
        }
        BaseObserver observer = new BaseObserver<BaseResult<ChapterResult>>(mActivity, BaseObserver.MODEL_ALL) {
            @Override
            public void success(BaseResult<ChapterResult> data) {
                super.success(data);
                if (data != null && data.getData() != null && data.getData().getList() != null && !data.getData().getList().isEmpty()) {
                    playQueue = data.getData().getList();
                    DBChapter chapter = data.getData().getList().get(0);
                    UtilGlide.loadImg(mActivity, chapter.getBookImage(), ivImg);
                    if (isPlay) {
                        DBListenHistory history = MusicDBController.getLastDBListenHistoryByBookId(bookId);
                        Bundle bundle = new Bundle();
                        if (history != null) {
                            int currentIndex = 0;
                            for (int i = 0; i < data.getData().getList().size(); i++) {
                                if (TextUtils.equals(history.getChapterId(), data.getData().getList().get(i).getChapterId())) {
                                    currentIndex = i;
                                    break;
                                }
                            }
                            if (history.getTotal() != 0 && history.getDuration() * 100 / history.getTotal() < 95) {
                                bundle.putParcelable("vo", UtilListener.dBListenHistoryToDBChapter(history));
                            } else {
                                if (currentIndex < data.getData().getList().size() - 1) {
                                    DBChapter dbChapter = data.getData().getList().get(currentIndex + 1);
                                    if (TextUtils.isEmpty(dbChapter.getUrl())) {
                                        program_number.setText(dbChapter.getTitle());
                                        showToast("此章节收费");
                                        return;
                                    } else {
                                        bundle.putParcelable("vo", dbChapter);
                                    }
                                } else {
                                    page++;
                                    getData();
                                }
                            }
                            bundle.putParcelable("vo", UtilListener.dBListenHistoryToDBChapter(history));
                        } else {
                            bundle.putParcelable("vo", chapter);
                        }
                        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data.getData().getList());
                        MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().playFromSearch("aaa", bundle);
                    }
                }
            }


            @Override
            public void error(BaseResult<ChapterResult> value, Throwable e) {
                super.error(value, e);
            }
        };
        mActivity.mDisposable.add(observer);
        UtilRetrofit.getInstance().create(HttpService.class).chapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookId = bundle.getString("bookId");
            isPlay = bundle.getBoolean("isPlay", false);
        }

    }

    @Override
    protected boolean showActionBar() {
        return false;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //上一章
            case R.id.iv_previous: {
                MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().skipToPrevious();
            }
            break;
            //下一章
            case R.id.iv_next: {
                MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().skipToNext();
            }
            break;
            case R.id.iv_play:
                if (getPlaybackStateCompat() != null && getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().pause();
                } else if (getPlaybackStateCompat() != null && getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PAUSED) {
                    MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().play();
                } else {
                    DBListenHistory history = MusicDBController.getLastDBListenHistoryByBookId(bookId);
                    Bundle bundle = new Bundle();
                    if (history != null) {
                        int currentIndex = 0;
                        for (int i = 0; i < playQueue.size(); i++) {
                            if (TextUtils.equals(history.getChapterId(), playQueue.get(i).getChapterId())) {
                                currentIndex = i;
                                break;
                            }
                        }
                        if (history.getTotal() != 0 && history.getDuration() * 100 / history.getTotal() < 95) {
                            bundle.putParcelable("vo", UtilListener.dBListenHistoryToDBChapter(history));
                        } else {
                            if (currentIndex < playQueue.size() - 1) {
                                DBChapter dbChapter = playQueue.get(currentIndex + 1);
                                if (TextUtils.isEmpty(dbChapter.getUrl())) {
                                    showToast("此章节收费");
                                    return;
                                } else {
                                    bundle.putParcelable("vo", dbChapter);
                                }
                            } else {
                                page++;
                                getData();
                            }
                        }
                        bundle.putParcelable("vo", UtilListener.dBListenHistoryToDBChapter(history));
                    } else {
                        bundle.putParcelable("vo", playQueue.get(0));
                    }
                    bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) playQueue);
                    MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().playFromSearch("aaa", bundle);
                }
                break;
            case R.id.tv_timing: {
                if (getPlaybackStateCompat() == null) {
                    showToast("请先收听书籍");
                    return;
                }
                if (getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_STOPPED) {
                    showToast("请先收听书籍");
                    return;
                }
                TimeSettingDialog dialog = new TimeSettingDialog(this);
                dialog.setListener(new TimeSettingDialog.SettingTimingCallBackListener() {
                    @Override
                    public void callback(int timing) {
                        if (timing == -1) {
                            MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().sendCustomAction(MusicService.MUSIC_TIMER_STOP, null);
                        } else {
                            Log.d("aaa", "timer------" + timing);
                            Bundle bundle = new Bundle();
                            bundle.putInt("timer", timing);
                            MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().sendCustomAction(MusicService.MUSIC_TIMER_START, bundle);
                        }
                    }
                });
                dialog.show();
            }
            break;

            case R.id.tv_share:
                if (playQueue == null || playQueue.isEmpty()) {
                    showToast("数据加载中，请稍后");
                    return;
                }
                ShareDialog shareDialog = new ShareDialog(mActivity);
                shareDialog.setImageUrl(playQueue.get(0).getBookImage());
                shareDialog.setBookname(playQueue.get(0).getBookTitle());
                shareDialog.setUrl("http://www.tingshijie.com/Weixin2/bookshow/id/" + bookId);
                shareDialog.show();
                break;

            case R.id.iv_close:
                UtilIntent.finishDIYBottomToTop(mActivity);
                break;

            case R.id.iv_list: {
                PlayListDialog mDialog = new PlayListDialog(mActivity);
                mDialog.setData(bookId);
                mDialog.show();
            }
            break;

            case R.id.tv_speed:
                SpeedDialog dialog = new SpeedDialog(mActivity);
                dialog.setListener(new SpeedDialog.SpeedCallBackListener() {
                    @Override
                    public void speed(float speed) {
                        switch (AppData.speedType) {
                            case 1:
                                tvSpeed.setText("1X倍速");
                                break;

                            case 2:
                                tvSpeed.setText("1.25X倍速");
                                break;

                            case 3:
                                tvSpeed.setText("1.5X倍速");
                                break;
                            case 4:
                                tvSpeed.setText("1.75X倍速");
                                break;

                            case 5:
                                tvSpeed.setText("2X倍速");
                                break;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putFloat("speed", speed);
                        MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().sendCustomAction(MusicService.MUSIC_SPEED, bundle);
                    }
                });
                dialog.show();
                break;

            case R.id.play_name:
                Bundle bundle = new Bundle();
                bundle.putString("bookId", bookId);
                mActivity.intent(BookDetailsActivity.class, bundle);
                break;

            case R.id.iv_ad_close:
                rlAdLayout.setVisibility(View.GONE);
                break;

            case R.id.tv_setting:
                SettingSecondDialog secondDialog = new SettingSecondDialog(mActivity);
                secondDialog.show();
                break;
            default:
                break;
        }
    }

    public void play(boolean b) {
        if (!UtilNetStatus.isWifiConnection() && UtilNetStatus.isHasConnection(this)) {
            showToast("非wifi下请注意流量");
        }
        if (b) {
            Bundle bundle = new Bundle();
            bundle.putString("bookId", bookId);
            bundle.putInt("position", position);
            MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().playFromSearch("aaa", bundle);
        } else {
            MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().pause();
        }
    }

    private void timerProgress(int time) {
        Log.d("aaa", "timerProgress========" + time);
        if (time > 60 * 60) {
            int hours = time / 60 / 60;
            int minute = (time - hours * 60 * 60) / 60;
            int second = time - hours * 60 * 60 - minute * 60;
            tvTiming.setText(String.format("%02d:%02d:%02d", hours, minute, second));
        } else if (time > 60 && time <= 60 * 60) {
            int minute = time / 60;
            int second = time % 60;
            tvTiming.setText(String.format("%02d:%02d", minute, second));
        } else {
            tvTiming.setText(String.format("%02d:%02d", 00, time));
        }
    }


    /**
     * TODO 实现监听Seekbar的类 ---------------------------------------------------
     */
    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            tv_current_time.setText(UtilDate
                    .transformToTimeStr(seekBar.getProgress()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().seekTo(seekBar.getProgress());
            scheduleSeekbarUpdate();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        UtilIntent.finishDIYBottomToTop(mActivity);
    }


    private void updateProgress() {
        if (getPlaybackStateCompat() == null) {
            return;
        }
        long currentPosition = getPlaybackStateCompat().getPosition();
        if (getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PLAYING) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaControllerCompat.
            long timeDelta = SystemClock.elapsedRealtime() -
                    getPlaybackStateCompat().getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * getPlaybackStateCompat().getPlaybackSpeed();
        }
        music_seekbar.setProgress((int) currentPosition);
    }


    private void stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }


    private void scheduleSeekbarUpdate() {
        stopSeekbarUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void stopTimerUpdate() {
        if (mTimerScheduleFuture != null) {
            mTimerScheduleFuture.cancel(false);
        }
    }

    private void scheduleTimerUpdate() {
        stopTimerUpdate();
        if (!mExecutorService.isShutdown()) {
            mTimerScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            timer--;

                            mHandler.post(mUpdateTimerTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void updateTimer() {
        if (timer > 0) {
            timerProgress(timer);
        } else {
            tvTiming.setText("定时");
        }
    }


    @Override
    protected void notifyServiceConnected() {
        super.notifyServiceConnected();

        if (getPlaybackStateCompat() != null) {

            Bundle bundle = getPlaybackStateCompat().getExtras();
            if (bundle != null) {
                String currentPlayBookId = bundle.getString("bookId", "-1");
                if (TextUtils.isEmpty(bookId)) {
                    bookId = currentPlayBookId;
                }
                if (!TextUtils.equals(currentPlayBookId, bookId)) {
                    loadingPlayData();
                } else {
                    String bookImage = bundle.getString("bookImage");
                    String bookTitle = bundle.getString("bookTitle");
                    String chapterTitle = bundle.getString("chapterTitle");
                    float speed = getPlaybackStateCompat().getPlaybackSpeed();
                    switch (String.valueOf(speed)) {
                        case "1.0":
                            tvSpeed.setText("X1倍速");
                            AppData.speedType = 1;
                            break;
                        case "1.25":
                            tvSpeed.setText("X1.25倍速");
                            AppData.speedType = 2;
                            break;
                        case "1.5":
                            tvSpeed.setText("X1.5倍速");
                            AppData.speedType = 3;
                            break;
                        case "1.75":
                            tvSpeed.setText("X1.75倍速");
                            AppData.speedType = 4;
                            break;
                        case "2.0":
                            tvSpeed.setText("X2倍速");
                            AppData.speedType = 5;
                            break;
                    }
                    play_name.setText(bookTitle);
                    program_number.setText(chapterTitle);
                    UtilGlide.loadImg(mActivity, bookImage, ivImg);
                    timer = bundle.getInt("timer");
                    if (timer <= 0) {
                        tvTiming.setText("定时");
                    } else {
                        updateTimer();
                        scheduleTimerUpdate();
                    }
                    long duration = bundle.getLong("totalTime");
                    if (duration > 0) {
                        tv_total_time.setText(UtilDate.format(duration, "mm:ss"));
                        music_seekbar.setMax((int) duration);
                    }
                    if (getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PLAYING) {
                        Log.d("aaa", "notifyServiceConnected==========STATE_PLAYING");
                        playQueue = bundle.getParcelableArrayList("playQueue");
                        updateProgress();
                        ivPlay.setImageResource(R.drawable.vector_play);
                        scheduleSeekbarUpdate();
                    } else if (getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_PAUSED) {
                        Log.d("aaa", "notifyServiceConnected==========STATE_PAUSED");
                        playQueue = bundle.getParcelableArrayList("playQueue");
                        notifyPause();
                        updateProgress();
                    } else if (getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_STOPPED) {
                        Log.d("aaa", "notifyServiceConnected==========STATE_STOPPED");
                        loadingPlayData();
                    } else if (getPlaybackStateCompat().getState() == PlaybackStateCompat.STATE_ERROR) {
                        Log.d("aaa", "notifyServiceConnected==========STATE_ERROR");
                        loadingPlayData();
                    }
                }
            } else {
                Log.d("aaa", "notifyServiceConnected==========Bundle为空");
                loadingPlayData();
            }
        } else {
            Log.d("aaa", "notifyServiceConnected==========没有播放状态");
            notifyStop();
            loadingPlayData();
        }
    }


    @Override
    protected void notifyRewinding(String bookId, String bookTitle, String chapterTitle, String bookImage) {
        super.notifyRewinding(bookId, bookTitle, chapterTitle, bookImage);
        music_progress.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.GONE);
        music_seekbar.setProgress(0);
        tv_current_time.setText("--:--");
        tv_total_time.setText("--:--");
        stopSeekbarUpdate();
        this.bookId = bookId;
        play_name.setText(bookTitle);
        program_number.setText(chapterTitle);
    }

    @Override
    protected void notifyLoading() {
        super.notifyLoading();
        music_progress.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.GONE);
        stopSeekbarUpdate();
    }


    @Override
    protected void notifyPlay(String bookId, String bookTitle, String chapterTitle, String bookImage, long duration) {
        super.notifyPlay(bookId, bookTitle, chapterTitle, bookImage, duration);
        Log.d("aaa", "通知播放");
        isPlay = false;
        this.bookId = bookId;

        play_name.setText(bookTitle);
        program_number.setText(chapterTitle);
        tv_total_time.setText(UtilDate.format(duration, "mm:ss"));
        ivPlay.setVisibility(View.VISIBLE);
        ivPlay.setImageResource(R.drawable.vector_play);

//        long currentPosition = getPlaybackStateCompat().getPosition();
//        if (currentPosition == 0) {
//            tv_current_time.setText("00:00");
//        }
        music_seekbar.setMax((int) duration);
        Log.d("aaa", "duration----------" + duration);
        scheduleSeekbarUpdate();
    }

    @Override
    protected void notifyPause() {
        super.notifyPause();
        ivPlay.setImageResource(R.drawable.vector_pause);
        stopSeekbarUpdate();
    }

    @Override
    protected void notifyStop() {
        super.notifyStop();
        ivPlay.setImageResource(R.drawable.vector_pause);
        music_seekbar.setProgress(0);
        tv_current_time.setText("--:--");
        tv_total_time.setText("--:--");
        stopSeekbarUpdate();
        timer = -1;
        tvTiming.setText("定时");
        stopTimerUpdate();
    }


    @Override
    protected void notifyTimer(int time) {
        super.notifyTimer(time);
        Log.d("aaa", "timerProgress========" + time);
        this.timer = time;
        updateTimer();
        scheduleTimerUpdate();
    }

    @Override
    protected void notifyTimerStop() {
        super.notifyTimerStop();
        stopTimerUpdate();
        timer = -1;
        tvTiming.setText("定时");
    }
}
