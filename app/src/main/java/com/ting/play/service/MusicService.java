package com.ting.play.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Util;
import com.ting.base.BaseObserver;
import com.ting.base.BaseToast;
import com.ting.bean.BaseResult;
import com.ting.bean.ChapterResult;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.download.DownloadController;
import com.ting.play.AESDataSourceFactory;
import com.ting.play.controller.MusicDBController;
import com.ting.play.state.MusicNotification;
import com.ting.util.UtilListener;
import com.ting.util.UtilMD5Encryption;
import com.ting.util.UtilRetrofit;
import com.ting.welcome.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC;
import static com.google.android.exoplayer2.C.USAGE_MEDIA;

/**
 *
 */
public class MusicService extends MediaBrowserServiceCompat {
    public static final String MEDIA_ID_ROOT = "__ROOT__";

    //定时器开启
    public static final String MUSIC_TIMER_START = "MUSIC_TIMER_START";
    //定时器停止
    public static final String MUSIC_TIMER_STOP = "MUSIC_TIMER_STOP";

    //倍速播放
    public static final String MUSIC_SPEED = "MUSIC_SPEED";
    //定时器
    public static final String TIMER_PROGRESS = "TIMER_PROGRESS";
    //定时完成
    public static final String TIMER_COMPLETE = "TIMER_COMPLETE";
    //定时器开启
    public static final String TIMER_START = "TIMER_START";
    //定时器关闭close
    public static final String TIMER_CLOSE = "TIMER_CLOSE";

    // The volume we set the media player to when we lose audio focus, but are
    // allowed to reduce the volume instead of stopping playback.
    public static final float VOLUME_DUCK = 0.2f;
    // The volume we set the media player when we have audio focus.
    public static final float VOLUME_NORMAL = 1.0f;

    public static final int NOTIFICATION_ID = 120;

    public static final int PLAY_MSG = 2;// 开始播放
    public static final int PAUSE_MSG = 3;// 暂停播放
    public static final int PLAY_PREVIOUS = 4; //上一章
    public static final int PLAY_NEXT = 5;     //下一章
    public static final int SEEKTO_MSG = 6;// 继续播放
    public static final int TIME_START = 7; //开启定时
    public static final int TIME_STOP = 8; //关闭定时
    public static final int SPEED = 9; //倍速播放
    public static final int SEEKTO_PLAY_MSG = 10;  //播放
    public static final int PLAY_SEEK_TIME_MSG = 12;       //滑动到指定位置播放

    private ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();
    private boolean mAudioNoisyReceiverRegistered = false;
    private boolean mNotificationReceiverRegistered = false;
    private boolean mPlayOnFocusGain;


    private final IntentFilter mAudioNoisyIntentFilter =
            new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private final BroadcastReceiver mAudioNoisyReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                        pause();
                    }
                }
            };

    private IntentFilter filter = null;
    private RemoteControlClient mControlClient;

    enum State {
        Retrieving,
        Stopped,    // media player is stopped and not prepared to play
        Preparing,  // media player is preparing...
        Playing,    // playback active (media player ready!). (but the media player may actually be
        // paused in this state if we don't have audio focus. But we stay in this state
        // so that we know we have to resume playback once we get focus back)
        Paused      // playback paused (media player ready!)
    }

    private State mState = State.Retrieving;

    enum AudioFocus {
        NoFocusNoDuck,    // we don't have audio focus, and can't duck
        NoFocusCanDuck,   // we don't have focus, but can play at a low volume ("ducking")
        Focused           // we have full audio focus
    }

    /**
     * 音频状态
     */
    private int audioStatus = -1;

    AudioFocus mAudioFocus = AudioFocus.NoFocusNoDuck;

    private SimpleExoPlayer mExoPlayer; // 媒体播放器对象
    private String currentURL = null;
    private Long time; // 播放进度
    private int msg;// 播放控制消息
    //书籍ID
    private String bookId = null;
    //章节ID
    private String chapterId = null;
    //播放集合
    private ArrayList<DBChapter> playQueue;
    private MusicNotification notification;
    //定时器
    private Timer tTimer;
    //wifi锁
    private WifiManager.WifiLock mWifiLock;
    //音频管理
    private AudioManager mAudioManager;

    private MusicDBController mController;
    private DownloadController mDownloadController;
    public static MusicService service = null;

    public static MusicService getInstance() {
        return service;
    }

    //0:未播放进行滑动，1：在播放状态下进行滑动
    private int seekType = 0;
    //定时时间
    private int timer = 0;

    private DBChapter currentPlayVO = null;

    private ComponentName mMediaButtonReceiverComponent;

    private MediaSessionCompat mSessionCompat;
    private PlaybackStateCompat mPlaybackStateCompat;


    //当前播放完毕, 停止播放  true是停止， false是不停止
    private boolean currentPlayComplete = false;

    private float speed = 1.0f;


    @Override
    public void onCreate() {
        super.onCreate();
        mWifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        notification = new MusicNotification(this);
        mSessionCompat = new MediaSessionCompat(this, this.getClass().getSimpleName());
        mSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSessionCompat.setCallback(mCallback);
        setSessionToken(mSessionCompat.getSessionToken());
        mController = new MusicDBController();
        mDownloadController = new DownloadController();
    }

    /* 启动service时执行的方法 */
    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        Log.d("aaa", "MusicService-------onStartCommand");
        MediaButtonReceiver.handleIntent(mSessionCompat, intent);
        return START_NOT_STICKY;
    }


    /**
     * 播放
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void play() {
        Log.d("aaa", "play----------播放");
        if (currentPlayVO == null) {
            return;
        }
        String url = currentPlayVO.getUrl();
        mPlayOnFocusGain = true;
        tryToGetAudioFocus();
        registerAudioNoisyReceiver();
        DBChapter chapter = DownloadController.queryDBChapter(currentPlayVO.getBookId(), currentPlayVO.getChapterId());
        if (chapter != null && chapter.getState() == 4) {
            String localFile = AppData.FILE_PATH + currentPlayVO.getBookId() + "/" + UtilMD5Encryption.getMd5Value(currentPlayVO.getChapterId()) + ".tsj";
            File file = new File(localFile);
            if (file.exists()) {
                url = localFile;
            }
        }
        url = url.replaceAll(" ", "%20");
        boolean mediaHasChanged = !TextUtils.equals(url, currentURL);
        if (mediaHasChanged) {
            currentURL = url;
        }
        if (mediaHasChanged || mExoPlayer == null) {
            releaseResources(false);
            if (mExoPlayer == null) {
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(), new DefaultLoadControl());
                mExoPlayer.addListener(mEventListener);
            }
            final AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(CONTENT_TYPE_MUSIC).setUsage(USAGE_MEDIA).build();
            mExoPlayer.setAudioAttributes(audioAttributes);
            AESDataSourceFactory factory = new AESDataSourceFactory(this, Util.getUserAgent(this, "listen"));
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            Log.d("aaa", "播放链接===========" + currentURL);
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentURL), factory, extractorsFactory, null, null);
            mExoPlayer.prepare(mediaSource);
            mWifiLock.acquire();
        }
        configurePlayerState();
    }

    /**
     * 暂停
     */
    private void pause() {
        mState = State.Paused;
        if (currentPlayVO != null) {
            record();
        }
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
        releaseResources(false);
        unregisterAudioNoisyReceiver();
        stopForeground(false);
        notifyPause();
    }


    @Override
    public void onDestroy() {
        Log.d("aaa", "MusicService--------onDestroy");
        super.onDestroy();

    }


    public void record() {
        if (currentPlayVO != null) {
            DBListenHistory vo = new DBListenHistory();
            vo.setBookId(currentPlayVO.getBookId());
            vo.setChapterTitle(currentPlayVO.getTitle());
            vo.setChapterId(currentPlayVO.getChapterId());
            vo.setChapterUrl(currentPlayVO.getUrl());
            vo.setPosition(currentPlayVO.getPosition());
            vo.setBookTitle(currentPlayVO.getBookTitle());
            vo.setBookHost(currentPlayVO.getBookHost());
            vo.setBookImage(currentPlayVO.getBookImage());
            vo.setSystemTime(System.currentTimeMillis());
            //播放完成
            if (mExoPlayer != null && mExoPlayer.getCurrentPosition() > 0) {
                vo.setDuration(mExoPlayer.getCurrentPosition());
            } else {
                vo.setDuration(0L);
            }
            if (mExoPlayer != null && mExoPlayer.getDuration() > 0) {
                vo.setTotal(mExoPlayer.getDuration());
            } else {
                vo.setTotal(0L);
            }
            mController.insert(vo);
        }
    }

    /**
     * 尝试获取音频焦点
     */
    private void tryToGetAudioFocus() {
        int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AudioFocus.Focused;
        } else {
            mAudioFocus = AudioFocus.NoFocusNoDuck;
        }
    }

    /**
     * 放弃音频焦点
     */
    private void giveUpAudioFocus() {
        if (mAudioManager.abandonAudioFocus(mAudioFocusChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AudioFocus.NoFocusNoDuck;
        }
    }


    /**
     * 加载数据
     *
     * @param model 1:  主动上一章， 2：主动下一章  3：被动下一章
     */
    private void loadData(final int model, final int pages) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", String.valueOf(pages));
        map.put("size", "50");
        map.put("bookId", currentPlayVO.getBookId());
        if (TokenManager.isLogin(this)) {
            map.put("uid", TokenManager.getUid(this));
        }
        BaseObserver baseObserver = new BaseObserver<BaseResult<ChapterResult>>() {


            @Override
            public void success(BaseResult<ChapterResult> data) {
                super.success(data);
                Log.d("aaa", "请求网络");
                ChapterResult result = data.getData();
                if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                    if (model == 1) {
                        playQueue.addAll(0, result.getList());
                        DBChapter vo = result.getList().get(result.getList().size() - 1);
                        if (TextUtils.isEmpty(vo.getUrl())) {
                            BaseToast.makeText(MusicService.this, "此章节收费").show();
                            musicStop();
                            MusicService.this.stopSelf();
                        } else {
                            currentPlayVO = vo;
                            play();
                        }
                    } else {
                        int startPosition = result.getList().get(0).getPosition();
                        int endPosition = result.getList().get(result.getList().size() - 1).getPosition();
                        if (currentPlayVO.getPosition() < startPosition) {
                            MusicService.this.playQueue.addAll(result.getList());
                            DBChapter vo = result.getList().get(0);
                            if (TextUtils.isEmpty(vo.getUrl())) {
                                BaseToast.makeText(MusicService.this, "此章节收费").show();
                                musicStop();
                                MusicService.this.stopSelf();
                            } else {
                                currentPlayVO = vo;
                                play();
                            }
                        } else if (currentPlayVO.getPosition() >= endPosition) {
                            BaseToast.makeText(MusicService.this, "后面没有更多章节").show();
                            musicStop();
                            MusicService.this.stopSelf();
                        } else {
                            playQueue = (ArrayList<DBChapter>) result.getList();
                            int index = getCurrPlayPostion();
                            DBChapter vo = playQueue.get(index + 1);
                            if (TextUtils.isEmpty(vo.getUrl())) {
                                Log.d("aaa", "playCompletion======此章节收费");
                                BaseToast.makeText(MusicService.this, "此章节收费").show();
                                musicStop();
                                MusicService.this.stopSelf();
                            } else {
                                Log.d("aaa", "下一集");
                                currentPlayVO = vo;
                                play();
                            }
                        }
                    }
                } else {
                    if (model == 1) {
                        BaseToast.makeText(MusicService.this, "前面没有更多章节").show();
                    } else if (model == 2) {
                        BaseToast.makeText(MusicService.this, "后面没有更多章节").show();
                    } else {
                        BaseToast.makeText(MusicService.this, "后面没有更多章节").show();
                        musicStop();
                        MusicService.this.stopSelf();
                    }
                }
            }

            @Override
            public void error(BaseResult<ChapterResult> value, Throwable e) {
                super.error(value, e);
                BaseToast.makeText(MusicService.this, "最后一章").show();
                musicStop();
                MusicService.this.stopSelf();
            }

        };
        UtilRetrofit.getInstance().create(HttpService.class).chapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }


    /**
     * 释放资源
     *
     * @param b
     */
    private void releaseResources(boolean b) {
        if (b && mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer.removeListener(mEventListener);
            mExoPlayer = null;
            mPlayOnFocusGain = false;
        }
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }


    private final AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d("aaa--MusicService", "mAudioFocusChangeListener=========" + focusChange);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mAudioFocus = AudioFocus.Focused;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                    mAudioFocus = AudioFocus.NoFocusCanDuck;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // Lost audio focus, but will gain it back (shortly), so note whether
                    // playback should resume
                    mAudioFocus = AudioFocus.NoFocusNoDuck;
                    mPlayOnFocusGain = mExoPlayer != null && mExoPlayer.getPlayWhenReady();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    // Lost audio focus, probably "permanently"
                    mAudioFocus = AudioFocus.NoFocusNoDuck;
                    break;
            }

            if (mExoPlayer != null) {
                // Update the player state based on the change
                configurePlayerState();
            }
        }
    };

    /**
     * 配置播放状态
     */
    private void configurePlayerState() {
        Log.d("aaa", "MusicService------configurePlayerState-------" + mAudioNoisyReceiverRegistered);
        if (mAudioFocus == AudioFocus.NoFocusNoDuck) {
            pause();
        } else {
            registerAudioNoisyReceiver();
            if (mAudioFocus == AudioFocus.NoFocusCanDuck) {
                mExoPlayer.setVolume(VOLUME_DUCK);
            } else {
                mExoPlayer.setVolume(VOLUME_NORMAL);
            }
            if (mPlayOnFocusGain) {
                mState = State.Playing;
                mExoPlayer.setPlayWhenReady(true);
                mPlayOnFocusGain = false;
            }
        }

    }


    private void registerAudioNoisyReceiver() {
        if (!mAudioNoisyReceiverRegistered) {
            mAudioNoisyReceiverRegistered = true;
            registerReceiver(mAudioNoisyReceiver, mAudioNoisyIntentFilter);
        }
    }

    /**
     * 取消
     */
    private void unregisterAudioNoisyReceiver() {
        if (mAudioNoisyReceiverRegistered) {
            mAudioNoisyReceiverRegistered = false;
            unregisterReceiver(mAudioNoisyReceiver);
        }
    }


    /**
     * 通知加载
     */
    private void notifyLoading() {
        Log.d("aaa", "notifyLoading------通知加载");
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        Bundle bundle = new Bundle();
        bundle.putString("chapterTitle", currentPlayVO.getTitle());
        bundle.putString("bookId", currentPlayVO.getBookId());
        bundle.putString("bookTitle", currentPlayVO.getBookTitle());
        bundle.putString("bookImage", currentPlayVO.getBookImage());
        bundle.putInt("timer", timer);
        playbackstateBuilder.setState(PlaybackStateCompat.STATE_REWINDING, mExoPlayer.getCurrentPosition(), 1.0f, SystemClock.elapsedRealtime());
        mSessionCompat.setPlaybackState(playbackstateBuilder.build());
    }

    /**
     * 通知播放
     */
    private void notifyStartPlay() {
        Log.d("aaa", "通知播放");
        if (mState != State.Paused) {
            Intent intent = new Intent(getApplicationContext(), MusicService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            startForeground(NOTIFICATION_ID, notification.getNotification());
        }
        mState = State.Playing;
        mSessionCompat.setActive(true);
        record();
        notification.notifyInit(currentPlayVO.getBookTitle(), currentPlayVO.getTitle(), currentPlayVO.getBookImage());
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        Bundle bundle = new Bundle();
        bundle.putLong("totalTime", mExoPlayer.getDuration());
        bundle.putString("chapterTitle", currentPlayVO.getTitle());
        bundle.putString("chapterId", currentPlayVO.getChapterId());
        bundle.putString("bookId", currentPlayVO.getBookId());
        bundle.putString("bookTitle", currentPlayVO.getBookTitle());
        bundle.putString("bookImage", currentPlayVO.getBookImage());
        bundle.putParcelableArrayList("playQueue", playQueue);
        bundle.putInt("timer", timer);
        playbackstateBuilder.setExtras(bundle);
        playbackstateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), speed, SystemClock.elapsedRealtime());
        mSessionCompat.setPlaybackState(playbackstateBuilder.build());
    }

    /**
     * 通知暂停
     */
    private void notifyPause() {
        if (mExoPlayer == null) {
            mState = State.Stopped;
            notifyStop();
            return;
        }
        notification.notifyPause();
        Bundle bundle = new Bundle();
        mState = State.Paused;
        bundle.putLong("totalTime", mExoPlayer.getDuration());
        bundle.putString("chapterTitle", currentPlayVO.getTitle());
        bundle.putString("chapterId", currentPlayVO.getChapterId());
        bundle.putString("bookId", currentPlayVO.getBookId());
        bundle.putString("bookTitle", currentPlayVO.getBookTitle());
        bundle.putString("bookImage", currentPlayVO.getBookImage());
        bundle.putParcelableArrayList("playQueue", playQueue);
        bundle.putInt("timer", timer);
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        playbackstateBuilder.setExtras(bundle);
        playbackstateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), speed, SystemClock.elapsedRealtime());
        mSessionCompat.setPlaybackState(playbackstateBuilder.build());
    }

    /**
     * 通知停止播放
     */
    protected void notifyStop() {
        Log.d("aaa", "MusicService--------播放完成");
        mState = State.Stopped;
        mSessionCompat.setActive(false);
        Bundle bundle = new Bundle();
        if (mExoPlayer != null) {
            bundle.putLong("totalTime", mExoPlayer.getDuration());
        }
        bundle.putString("chapterTitle", currentPlayVO.getTitle());
        bundle.putString("bookId", currentPlayVO.getBookId());
        bundle.putString("bookImage", currentPlayVO.getBookImage());
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        playbackstateBuilder.setExtras(bundle);
        playbackstateBuilder.setState(PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.STATE_NONE, speed);
        mSessionCompat.setPlaybackState(playbackstateBuilder.build());
    }


    /**
     * Music 关闭
     */
    private void musicStop() {
        mState = State.Stopped;
        giveUpAudioFocus();
        stopForeground(true);
        unregisterAudioNoisyReceiver();
        releaseResources(true);
        if (tTimer != null) {
            tTimer.cancel();
            tTimer = null;
        }
        notifyStop();
    }


    //通知开始缓存
    private void notifyBuffStart() {
        mState = State.Preparing;

    }

    //通知完成缓存
    private void notifyBuffEnd() {
        Log.d("aaa", "notifyBuffEnd------通知完成加载");
        mState = State.Playing;
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        Bundle bundle = new Bundle();
        bundle.putLong("totalTime", mExoPlayer.getDuration());
        bundle.putString("chapterTitle", currentPlayVO.getTitle());
        bundle.putString("chapterId", currentPlayVO.getChapterId());
        bundle.putString("bookId", currentPlayVO.getBookId());
        bundle.putString("bookTitle", currentPlayVO.getBookTitle());
        bundle.putString("bookImage", currentPlayVO.getBookImage());
        bundle.putParcelableArrayList("playQueue", playQueue);
        bundle.putInt("timer", timer);
        playbackstateBuilder.setExtras(bundle);
        playbackstateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), speed, SystemClock.elapsedRealtime());
        mSessionCompat.setPlaybackState(playbackstateBuilder.build());
    }


    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {

        return new BrowserRoot(MEDIA_ID_ROOT, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    /**
     * 获取当前播放的位置
     *
     * @return -1：没有相关数据   -2：当前播放url不在播放队列中
     */
    private int getCurrPlayPostion() {
        if (playQueue != null && !playQueue.isEmpty()) {
            for (int i = 0; i < playQueue.size(); i++) {
                if (TextUtils.equals(currentPlayVO.getChapterId(), playQueue.get(i).getChapterId())) {
                    return i;
                }
            }
            return -2;
        } else {
            return -1;
        }
    }


    /**
     * 播放完成
     */
    private void playCompletion() {
        //播放完成
        msg = PLAY_MSG;
        currentURL = null;
        record();
        if (!currentPlayComplete) {
            if (playQueue != null && !playQueue.isEmpty()) {
                int index = getCurrPlayPostion();
                Log.d("aaa", "playCompletion======" + index + "=======" + playQueue.size());
                if (index == -2) {
                    DBChapter vo = playQueue.get(0);
                    if (TextUtils.isEmpty(vo.getUrl())) {
                        BaseToast.makeText(MusicService.this, "此章节收费").show();
                        musicStop();
                        MusicService.this.stopSelf();
                    } else {
                        currentPlayVO = vo;
                        play();
                    }
                } else {
                    if (index < playQueue.size() - 1) {
                        DBChapter vo = playQueue.get(index + 1);
                        if (TextUtils.isEmpty(vo.getUrl())) {
                            Log.d("aaa", "playCompletion======此章节收费");
                            BaseToast.makeText(MusicService.this, "此章节收费").show();
                            musicStop();
                            MusicService.this.stopSelf();
                        } else {
                            Log.d("aaa", "下一集");
                            currentPlayVO = vo;
                            play();
                        }
                    } else {
                        int page = currentPlayVO.getPosition() / 50 + 1;
                        loadData(3, page);
                    }
                }
            } else {
                Log.d("aaa", "播放列表为空");
                BaseToast.makeText(MusicService.this, "播放完成").show();
                musicStop();
            }
        } else {
            Log.d("aaa", "定时该章节播放完毕关闭播放器");
            currentPlayComplete = false;
            BaseToast.makeText(MusicService.this, "播放完成").show();
            musicStop();
        }
    }


    private final class ExoPlayerEventListener implements ExoPlayer.EventListener {



        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.d("aaa", "onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.d("aaa", "onLoadingChanged ---- " + isLoading);
            if (isLoading) {
                notifyBuffStart();
            } else {
                notifyBuffEnd();
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d("aaa", "onPlayerStateChanged ------- " + playbackState + "--------------" + playWhenReady);
            switch (playbackState) {
                case Player.STATE_IDLE:
                    break;
                case Player.STATE_BUFFERING:
//                    notifyLoading();
                    break;
                case Player.STATE_READY:
                    if (playWhenReady) {
                        if (msg == PLAY_SEEK_TIME_MSG) {
                            if (mExoPlayer != null) {
                                mExoPlayer.seekTo(time);
                            }
                            msg = PLAY_MSG;
                        }
                        notifyStartPlay();
                    }
                    break;
                case Player.STATE_ENDED:
                    if (playWhenReady) {
                        playCompletion();
                    } else {
                        notifyPause();
                    }
                    break;

            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            Log.d("aaa", "onRepeatModeChanged");
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            Log.d("aaa", "onShuffleModeEnabledChanged");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            final String what;
            currentURL = null;
            switch (error.type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    what = error.getSourceException().getMessage();
                    record();
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    what = error.getRendererException().getMessage();
                    record();
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    what = error.getUnexpectedException().getMessage();
                    record();
                    break;
                default:
                    what = "Unknown: " + error;
            }
            musicStop();
            MusicService.this.stopSelf();
            Log.d("aaa", "onPlayerError------" + what + "------" + mWifiLock.isHeld() + "--------" + error.type);
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.d("aaa", "onPositionDiscontinuity");
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.d("aaa", "onPlaybackParametersChanged");
        }

        @Override
        public void onSeekProcessed() {
            Log.d("aaa", "onSeekProcessed");
        }
    }


    private MediaSessionCompat.Callback mCallback = new MediaSessionCompat.Callback() {

        @Override
        public void onPlay() {
            super.onPlay();
            msg = PLAY_MSG;
            if (mExoPlayer != null) {
                mExoPlayer.setPlayWhenReady(true);
            }
        }


        @Override
        public void onStop() {
            super.onStop();
            musicStop();
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
            super.onPlayFromSearch(query, extras);
            Log.d("aaa", "播放搜索内容");
            extras.setClassLoader(getClass().getClassLoader());
            msg = PLAY_MSG;
            currentPlayVO = extras.getParcelable("vo");
            ArrayList<DBChapter> list = extras.getParcelableArrayList("data");
            if (list != null && !list.isEmpty()) {
                playQueue = list;
            }
            DBListenHistory history = MusicDBController.getListenHistoryByChapterId(currentPlayVO.getChapterId());
            if (history != null) {
                if (history.getTotal() != 0 && history.getDuration() * 100 / history.getTotal() < 95) {
                    msg = PLAY_SEEK_TIME_MSG;
                    time = history.getDuration();
                    currentPlayVO = UtilListener.dBListenHistoryToDBChapter(history);
                    play();
                } else {
                    if (!TextUtils.isEmpty(currentPlayVO.getUrl())) {
                        play();
                    }
                }
            } else {
                if (!TextUtils.isEmpty(currentPlayVO.getUrl())) {
                    play();
                }
            }

        }


        @Override
        public void onPause() {
            super.onPause();
            msg = PAUSE_MSG;
            pause();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            time = pos;
            Log.d("aaa", "onSeekTo-----" + pos);
            if (mExoPlayer != null) {
                mExoPlayer.seekTo(pos);
            }
        }


        @Override
        public void onPrepare() {
            super.onPrepare();
        }


        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            int currentPosition = getCurrPlayPostion();
            if (currentPosition == -1 && currentPosition == -2) {
                BaseToast.makeText(MusicService.this, "后面没有更多章节").show();
                return;
            }
            if (playQueue != null && !playQueue.isEmpty()) {
                if (currentPosition < playQueue.size() - 1) {
                    DBChapter vo = playQueue.get(currentPosition + 1);
                    if (TextUtils.isEmpty(vo.getUrl())) {
                        BaseToast.makeText(MusicService.this, "下一章节收费").show();
                    } else {
                        record();
                        currentPlayVO = vo;
                        play();
                    }
                } else {
                    if (currentPlayVO.getPosition() % 50 == 0) {
                        record();
                        loadData(2, (currentPlayVO.getPosition() - 1) / 50 + 2);
                    } else {
                        BaseToast.makeText(MusicService.this, "后面没有更多章节").show();
                    }
                }
            } else {
                BaseToast.makeText(MusicService.this, "后面没有更多章节").show();
            }
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            int currentPosition = getCurrPlayPostion();
            if (currentPosition == -1 || currentPosition == -2) {
                BaseToast.makeText(MusicService.this, "前面没有更多章节").show();
                return;
            }
            if (playQueue != null && !playQueue.isEmpty()) {
                if (currentPosition == 0) {
                    if (currentPlayVO.getPosition() == 1) {
                        BaseToast.makeText(MusicService.this, "前面没有更多章节").show();
                    } else {
                        if ((currentPlayVO.getPosition() - 1) % 50 == 0) {
                            record();
                            loadData(1, (currentPlayVO.getPosition() - 1) / 50);
                        } else {
                            BaseToast.makeText(MusicService.this, "前面没有更多章节").show();
                        }
                    }
                } else {
                    DBChapter vo = playQueue.get(currentPosition - 1);
                    if (TextUtils.isEmpty(vo.getUrl())) {
                        BaseToast.makeText(MusicService.this, "前一章节收费").show();
                    } else {
                        record();
                        currentPlayVO = vo;
                        play();
                    }
                }
            }
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
            if (TextUtils.equals(action, MUSIC_SPEED)) {
                speed = extras.getFloat("speed", 1.0f);
                if (mExoPlayer != null) {
                    PlaybackParameters playbackParameters = new PlaybackParameters(speed, 1.0F);
                    mExoPlayer.setPlaybackParameters(playbackParameters);
                    if (mState == State.Playing) {
                        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
                        Bundle bundle = new Bundle();
                        bundle.putLong("totalTime", mExoPlayer.getDuration());
                        bundle.putString("chapterTitle", currentPlayVO.getTitle());
                        bundle.putString("bookId", currentPlayVO.getBookId());
                        bundle.putString("bookTitle", currentPlayVO.getBookTitle());
                        bundle.putString("bookImage", currentPlayVO.getBookImage());
                        bundle.putString("chapterId", currentPlayVO.getChapterId());
                        bundle.putParcelableArrayList("playQueue", (ArrayList<? extends Parcelable>) playQueue);
                        bundle.putInt("timer", timer);
                        playbackstateBuilder.setExtras(bundle);
                        playbackstateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), speed, SystemClock.elapsedRealtime());
                        mSessionCompat.setPlaybackState(playbackstateBuilder.build());
                    }
                }
            } else if (TextUtils.equals(action, MUSIC_TIMER_START)) {
                timer = extras.getInt("timer", -1);
                if (timer == 0) {
                    currentPlayComplete = true;
                    timer = (int) ((mExoPlayer.getDuration() - mExoPlayer.getCurrentPosition()) / 1000 + 10);
                }
                if (tTimer != null) {
                    tTimer.cancel();
                    tTimer = null;
                }
                Bundle bundle = new Bundle();
                bundle.putString("model", MUSIC_TIMER_START);
                bundle.putInt("timer", timer);
                mSessionCompat.setExtras(bundle);
                if (timer != -1) {
                    tTimer = new Timer();
                    tTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (timer <= 1) {
                                tTimer.cancel();
                                tTimer = null;
                                record();
                                musicStop();
                            } else {
                                timer--;
                                mSessionCompat.getController().getPlaybackState().getExtras().putInt("timer", timer);
                            }

                        }
                    }, 0, 1000);
                }
            } else if (TextUtils.equals(action, MUSIC_TIMER_STOP)) {
                currentPlayComplete = false;
                if (tTimer != null) {
                    tTimer.cancel();
                    tTimer = null;
                }
                timer = -1;
                Bundle bundle = new Bundle();
                bundle.putString("model", MUSIC_TIMER_STOP);
                mSessionCompat.setExtras(bundle);
            }
        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            KeyEvent event = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_STOP && event.getAction() == KeyEvent.ACTION_DOWN) {
                mState = State.Stopped;
                record();
                musicStop();
                MusicService.this.stopSelf();
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PAUSE && event.getAction() == KeyEvent.ACTION_DOWN) {
                record();
                pause();
                notifyPause();
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY && event.getAction() == KeyEvent.ACTION_DOWN) {
                record();
                if (mExoPlayer != null) {
                    mExoPlayer.setPlayWhenReady(true);
                } else {
                    play();
                }
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (mState == State.Playing) {
                    pause();
                } else {
                    if (mExoPlayer != null) {
                        mExoPlayer.setPlayWhenReady(true);
                    } else {
                        play();
                    }
                }
            }
            return super.onMediaButtonEvent(mediaButtonEvent);
        }
    };


}
