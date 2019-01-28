package com.ting.play.service;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.RemoteControlClient;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ting.base.BaseObserver;
import com.ting.base.BaseToast;
import com.ting.bean.BaseResult;
import com.ting.bean.ChapterResult;
import com.ting.bean.play.PlayingVO;
import com.ting.bean.vo.ChapterListVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.bean.play.PlayListVO;
import com.ting.play.AESDataSourceFactory;
import com.ting.play.adapter.PlayListAdapter;
import com.ting.play.controller.MusicDBController;
import com.ting.bean.play.PlayResult;
import com.ting.play.receiver.MyRemoteControlEventReceiver;
import com.ting.play.state.MusicNotification;
import com.ting.util.UtilGson;
import com.ting.util.UtilMD5Encryption;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSPutil;
import com.ting.util.UtilStr;
import com.ting.view.MyToast;
import com.ting.welcome.MainActivity;

import java.io.File;
import java.io.IOException;
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
 * MusicService 2014/7/31
 *
 * @author Young
 */
public class MusicService extends Service {
    private static final String TAG = "MusicService";
    //播放界面加载中
    public static final String MUSIC_LOADING = "MUSIC_LOADING";
    //播放界面播放
    public static final String MUSIC_PLAY = "MUSIC_PLAY";
    //播放界面进度条更新
    public static final String MUSIC_PROGRESS = "MUSIC_PROGRESS";
    //播放暂停
    public static final String MUSIC_PAUSE = "MUSIC_PAUSE";
    //播放界面播放完成
    public static final String MUSIC_COMPLETE = "MUSIC_COMPLETE";
    //播放错误
    public static final String MUSIC_ERROR = "MUSIC_ERROR";
    //加载中
    public static final String MUSIC_BUFFER_START = "MUSIC_BUFFER_START";
    //加载完成
    public static final String MUSIC_BUFFER_END = "MUSIC_BUFFER_END";
    //通知数据更新
    public static final String MUSIC_DATA_UPDATE = "MUSIC_DATA_UPDATE";
    //
    public static final String MUSIC_DESTORY = "MUSIC_DESTORY";
    //定时器
    public static final String TIMER_PROGRESS = "TIMER_PROGRESS";
    //定时完成
    public static final String TIMER_COMPLETE = "TIMER_COMPLETE";

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

    private ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();
    private boolean mAudioNoisyReceiverRegistered;
    private boolean mPlayOnFocusGain;


    private boolean mExoPlayerNullIsStopped = false;
    private final IntentFilter mAudioNoisyIntentFilter =
            new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private final BroadcastReceiver mAudioNoisyReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {

                    }
                }
            };


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
    private ArrayList<DBChapter> data;
    public static MusicNotification notification;
    private NotificationReceiver notificationReceiver;
    //定时器
    private Timer tTimer;
    //wifi锁
    private WifiManager.WifiLock mWifiLock;
    //音频管理
    private AudioManager mAudioManager;


    public static MusicService service = null;

    public static MusicService getInstance() {
        return service;
    }

    //0:未播放进行滑动，1：在播放状态下进行滑动
    private int seekType = 0;
    //定时时间
    private int timimg = 0;

    private DBChapter currentPlayVO = null;

    private ComponentName mMediaButtonReceiverComponent;


    //当前播放完毕, 停止播放  true是停止， false是不停止
    private boolean currentPlayComplete = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mWifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 通知状态
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicNotification.PAUSE_MSG);
        filter.addAction(MusicNotification.PLAY_MSG);
        filter.addAction(MusicNotification.CLOSE_MSG);
        filter.addAction(MusicNotification.SHOW_PLAY_MSG);
        registerReceiver(notificationReceiver, filter);
        notification = new MusicNotification(getApplicationContext());
        mMediaButtonReceiverComponent = new ComponentName(this, MyRemoteControlEventReceiver.class);
    }

    /* 启动service时执行的方法 */
    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {

        /* 得到从startService传来的动作，后是默认参数，这里是我自定义的常量 */
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        if (mState == State.Preparing) {
            return super.onStartCommand(intent, flags, startId);
        }
        Bundle bundle = intent.getExtras();
        msg = bundle.getInt("MSG", 0);
        switch (msg) {
            //播放
            case PLAY_MSG: {
                if (currentPlayVO != null) {
                    record();
                }
                currentPlayVO = bundle.getParcelable("vo");
                if (bundle.getParcelableArrayList("data") != null) {
                    data = bundle.getParcelableArrayList("data");
                }
                if (currentPlayVO != null) {
                    play();
                }
            }
            break;
            //暂停
            case PAUSE_MSG: {
                pause();
            }
            break;
            //上一章
            case PLAY_PREVIOUS: {
                //当前播放的位置
                int currentPosition = getCurrPlayPostion();
                if (currentPosition == -1 || currentPosition == -2) {
                    Toast.makeText(this, "前面没有更多章节", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (currentPosition == 0) {
                    Toast.makeText(this, "前面没有更多章节", Toast.LENGTH_SHORT).show();
                } else {
                    DBChapter vo = data.get(currentPosition - 1);
                    if (UtilStr.isEmpty(vo.getUrl())) {
                        Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                    } else {
                        if (currentPlayVO != null) {
                            record();
                        }
                        currentPlayVO = vo;
                        play();
                    }
                }
            }
            break;
            //下一章
            case PLAY_NEXT: {
                int currentPosition = getCurrPlayPostion();
                if (currentPosition == -1) {
                    Toast.makeText(MusicService.this, "后面没有更多章节", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (currentPosition == -2) {
                    if (data != null && !data.isEmpty()) {
                        DBChapter vo = data.get(0);
                        if (UtilStr.isEmpty(vo.getUrl())) {
                            Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                        } else {
                            if (currentPlayVO != null) {
                                record();
                            }
                            currentPlayVO = vo;
                            play();
                        }
                    }
                } else {
                    if (data != null && !data.isEmpty()) {
                        if (currentPosition < data.size() - 1) {
                            DBChapter vo = data.get(currentPosition + 1);
                            if (UtilStr.isEmpty(vo.getUrl())) {
                                Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                            } else {
                                if (currentPlayVO != null) {
                                    record();
                                }
                                currentPlayVO = vo;
                                play();
                            }
                        } else {
                            Toast.makeText(MusicService.this, "后面没有更多章节", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            //跳转
            case SEEKTO_MSG: {
                time = bundle.getLong("seekTime", 0);
                if (mExoPlayer != null) {
                    mExoPlayer.seekTo(time);
                    mExoPlayer.setPlayWhenReady(true);
                }
            }
            break;

            case SEEKTO_PLAY_MSG: {
                time = bundle.getLong("seekTime", 0);
                if (currentPlayVO != null) {
                    record();
                }
                currentPlayVO = bundle.getParcelable("vo");
                if (bundle.getParcelableArrayList("data") != null) {
                    data = bundle.getParcelableArrayList("data");
                }
                if (currentPlayVO != null) {
                    play();
                }
            }
            break;

            case TIME_START: {
                timimg = intent.getIntExtra("time", -1);
                if (tTimer != null) {
                    tTimer.cancel();
                    tTimer = null;
                }
                if (timimg != -1) {
                    tTimer = new Timer();
                    tTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (timimg == 0) {
                                tTimer.cancel();
                                tTimer = null;
                                notifyTimerComplete();
                                AppData.ifTimeSetting = false;
                                AppData.shutDownTimer = 0;
                                record();
                                notifyPlayStop();
                                MusicService.this.stopSelf();
                            } else {
                                timimg--;
                                notifyTimerProgress();
                            }

                        }
                    }, 0, 1000);
                }
            }
            break;

            case TIME_STOP: {
                if (tTimer != null) {
                    tTimer.cancel();
                    tTimer = null;
                }
            }
            break;

            case SPEED:
                float speed = intent.getFloatExtra("speed", 1.0f);
                if (mExoPlayer != null) {
                    PlaybackParameters playbackParameters = new PlaybackParameters(speed, 1.0F);
                    mExoPlayer.setPlaybackParameters(playbackParameters);
                }
                break;

            default:
                notifyError();
                break;
        }
        return START_NOT_STICKY;
    }

    /**
     * 播放
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void play() {
        Log.d("aaa", "play----------播放");
        String url = currentPlayVO.getUrl();
        mPlayOnFocusGain = true;
        tryToGetAudioFocus();
        registerAudioNoisyReceiver();
        String localFile = AppData.FILE_PATH + currentPlayVO.getBookId() + "/" + UtilMD5Encryption.getMd5Value(currentPlayVO.getChapterId()) + ".tsj";
        File file = new File(localFile);
        if (file.exists()) {
            url = localFile;
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
        record();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
        releaseResources(false);
        unregisterAudioNoisyReceiver();
//        stopForeground(false);
        notifyPause();
    }


    @Override
    public void onDestroy() {
        Log.d("aaa", "onDestroy");
        super.onDestroy();
        errorRelease();
    }

    /**
     * 某种异常错误释放资源
     */
    public void errorRelease() {
        mState = State.Stopped;
        currentPlayVO = null;
        if (notificationReceiver != null) {
            this.unregisterReceiver(notificationReceiver);
        }
        sendPauseStatus();
        giveUpAudioFocus();
        stopForeground(true);
        unregisterAudioNoisyReceiver();
        releaseResources(true);
        if (tTimer != null) {
            tTimer.cancel();
            tTimer = null;
        }
        AppData.playKey = null;
        AppData.isPlaying = false;
        AppData.loadingKey = null;
    }

    public void record() {
        if (currentPlayVO != null) {
            MusicDBController controller = new MusicDBController();
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
            controller.insert(vo);
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
     * 释放资源
     *
     * @param b
     */
    private void releaseResources(boolean b) {
        if (b && mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer.removeListener(mEventListener);
            mExoPlayer = null;
            mExoPlayerNullIsStopped = true;
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
                    mAudioFocus = AudioFocus.NoFocusCanDuck;
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
                mExoPlayer.setPlayWhenReady(true);
                mPlayOnFocusGain = false;
            }
        }

    }


    private void registerAudioNoisyReceiver() {
        if (!mAudioNoisyReceiverRegistered) {
            registerReceiver(mAudioNoisyReceiver, mAudioNoisyIntentFilter);
            mAudioNoisyReceiverRegistered = true;
        }
    }

    private void unregisterAudioNoisyReceiver() {
        if (mAudioNoisyReceiverRegistered) {
            unregisterReceiver(mAudioNoisyReceiver);
            mAudioNoisyReceiverRegistered = false;
        }
    }


    /**
     * 通知加载
     */
    private void notifyLoading() {
        AppData.loadingKey = currentPlayVO.getChapterId();
        Intent intent = new Intent();
        intent.setAction(MUSIC_LOADING);
        sendBroadcast(intent);
    }

    /**
     * 通知播放
     */
    private void notifyStartPlay() {
        Log.d("aaa", "通知播放");
        if(mState != State.Paused) {
            startForeground(NOTIFICATION_ID, notification.getNotification());
        }
        mState = State.Playing;
        record();
        notification.notifyInit(currentPlayVO.getBookTitle(), currentPlayVO.getTitle(), currentPlayVO.getBookImage());
        Intent intent = new Intent();
        intent.putExtra("totalTime", mExoPlayer.getDuration());
        intent.putExtra("chapterTitle", currentPlayVO.getTitle());
        intent.setAction(MUSIC_PLAY);
        sendBroadcast(intent);
        AppData.loadingKey = null;
        AppData.isPlaying = true;
        AppData.playKey = currentPlayVO.getChapterId();
        updateProgress();
    }

    /**
     * 通知暂停
     */
    private void notifyPause() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent();
        intent.putExtra("bookId", currentPlayVO.getBookId());
        intent.setAction(MUSIC_PAUSE);
        sendBroadcast(intent);
        notification.notifyPause();
        AppData.loadingKey = null;
        AppData.isPlaying = false;
    }


    protected void notifyPlayStop() {
        Log.d("aaa", "MusicService--------播放完成");
        mState = State.Stopped;
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent();
        notification.notifyCancel();
        intent.setAction(MUSIC_COMPLETE);
        sendBroadcast(intent);
        AppData.loadingKey = null;
        AppData.isPlaying = false;
        AppData.playKey = null;
    }

    protected void notifyDataUpdate(PlayListVO vo) {
        Log.d("aaa", "通知数据更新");
        Intent intent = new Intent();
        intent.putExtra("bookid", bookId);
        intent.putExtra("data", vo);
        intent.setAction(MUSIC_DATA_UPDATE);
        sendBroadcast(intent);
    }

    /**
     * 通知定时结束
     */
    protected void notifyTimerComplete() {
        Intent intent = new Intent();
        intent.setAction(TIMER_COMPLETE);
        sendBroadcast(intent);
    }

    protected void notifyTimerProgress() {
        Intent intent = new Intent();
        intent.setAction(TIMER_PROGRESS);
        intent.putExtra("timing", timimg);
        sendBroadcast(intent);
    }


    /**
     * 错误通知
     */
    private void notifyError() {
        mState = State.Stopped;
        AppData.playKey = null;
        AppData.loadingKey = null;
        AppData.isPlaying = false;
        Intent intent = new Intent();
        intent.putExtra("bookid", bookId);
        intent.setAction(MUSIC_ERROR);
        sendBroadcast(intent);

    }

    /**
     * 通知进度条更新
     */
    private void notifyProgress() {
        if (mExoPlayer != null) {
            try {
                Intent intent = new Intent();
                intent.putExtra("currentTime", mExoPlayer.getCurrentPosition());
                intent.putExtra("totalTime", mExoPlayer.getDuration());
                intent.putExtra("chapterTitle", currentPlayVO.getTitle());
                intent.putExtra("bookTitle", currentPlayVO.getBookTitle());
                intent.setAction(MUSIC_PROGRESS);
                sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //通知开始缓存
    private void notifyBuffStart() {
        mState = State.Preparing;
        Intent intent = new Intent();
        intent.setAction(MUSIC_BUFFER_START);
        sendBroadcast(intent);
    }

    //通知完成缓存
    private void notifyBuffEnd() {
        mState = State.Playing;
        Intent intent = new Intent();
        intent.setAction(MUSIC_BUFFER_END);
        sendBroadcast(intent);
    }


    /**
     * 发送播放状态
     */
    private void sendPlaySatus() {
        Intent intent = new Intent();
        intent.setAction(MainActivity.MAIN_PLAY);
        sendBroadcast(intent);
    }


    private void sendPauseStatus() {
        Intent intent = new Intent();
        intent.setAction(MainActivity.MAIN_PAUSE);
        sendBroadcast(intent);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    /*
     * 接受notification发来的广播控制播放器
     */
    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("aaa", "action==========" + action);
            switch (action) {
                case MusicNotification.PLAY_MSG:
                    play();
                    sendPlaySatus();
                    break;
                case MusicNotification.PAUSE_MSG:
                    pause();
                    sendPauseStatus();
                    break;
                case MusicNotification.CLOSE_MSG:
                    record();
                    notifyPlayStop();
                    sendPauseStatus();
                    MusicService.this.stopSelf();
                    break;


            }

        }
    }


    /**
     * 获取当前播放的位置
     *
     * @return -1：没有相关数据   -2：当前播放url不在播放队列中
     */
    private int getCurrPlayPostion() {
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                if(TextUtils.equals(currentPlayVO.getChapterId(), data.get(i).getChapterId())){
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
        if (!currentPlayComplete) {
            if (data != null && !data.isEmpty()) {
                int index = getCurrPlayPostion();
                if (index == -2) {
                    DBChapter vo = data.get(0);
                    if (TextUtils.isEmpty(vo.getUrl())) {
                        BaseToast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                        MusicService.this.stopSelf();
                    } else {
                        currentPlayVO = vo;
                        play();
                    }
                } else {
                    if (index < data.size() - 1) {
                        DBChapter vo = data.get(index + 1);
                        if (TextUtils.isEmpty(vo.getUrl())) {
                            BaseToast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                            MusicService.this.stopSelf();
                        } else {
                            Log.d("service", "下一集");
                            currentPlayVO = vo;
                            play();
                        }
                    } else {
                        int position = (data.get(index).getPosition() - 1) / 50 + 2;
                        int page = position / 50 + 2;
                        loadData(page);
                    }
                }
            } else {
                BaseToast.makeText(MusicService.this, "播放完成", Toast.LENGTH_SHORT).show();
                MusicService.this.stopSelf();
            }
        }
    }

    /**
     * 加载数据
     *
     * @param pages
     */
    private void loadData(final int pages) {
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
                ChapterResult result = data.getData();
                Log.d("aaa", "请求网络");
                if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                    DBChapter vo = result.getList().get(0);
                    if (TextUtils.isEmpty(vo.getUrl())) {
                        BaseToast.makeText(MusicService.this, "此章节收费").show();
                        MusicService.this.stopSelf();
                    } else {
                        currentPlayVO = vo;
                        play();
                    }
                    MusicService.this.data.addAll(result.getList());
                } else {
                    BaseToast.makeText(MusicService.this, "最后一章").show();
                    MusicService.this.stopSelf();
                }
            }


            @Override
            public void error(BaseResult<ChapterResult> value, Throwable e) {
                super.error(value, e);
                BaseToast.makeText(MusicService.this, "最后一章").show();
                MusicService.this.stopSelf();
            }
        };
        UtilRetrofit.getInstance().create(HttpService.class).chapter(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    private void updateProgress() {
        handler.removeCallbacksAndMessages(null);
        notifyProgress();
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateProgress();
        }
    };

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }


    private final class ExoPlayerEventListener implements ExoPlayer.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            Log.d("aaa", "onTimelineChanged");
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
                    notifyLoading();
                    break;
                case Player.STATE_READY:
                    if (playWhenReady) {
                        if (msg == SEEKTO_PLAY_MSG) {
                            if (mExoPlayer != null) {
                                mExoPlayer.seekTo(time);
                            }
                            record();
                            msg = PLAY_MSG;
                        }
                        notifyStartPlay();
                    }
                    break;
                case Player.STATE_ENDED:
                    record();
                    notifyPlayStop();
                    if (playWhenReady) {
                        playCompletion();
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
            notifyError();
            switch (error.type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    what = error.getSourceException().getMessage();
                    record();
                    MusicService.this.stopSelf();
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    what = error.getRendererException().getMessage();
                    record();
                    MusicService.this.stopSelf();
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    what = error.getUnexpectedException().getMessage();
                    record();
                    MusicService.this.stopSelf();
                    break;
                default:
                    what = "Unknown: " + error;
            }
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

}
