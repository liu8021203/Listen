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
import com.ting.bean.play.PlayingVO;
import com.ting.common.AppData;
import com.ting.common.TokenManager;
import com.ting.common.http.HttpService;
import com.ting.db.DBChapter;
import com.ting.db.DBListenHistory;
import com.ting.bean.play.PlayListVO;
import com.ting.play.adapter.PlayListAdapter;
import com.ting.play.controller.MusicDBController;
import com.ting.bean.play.PlayResult;
import com.ting.play.receiver.MyRemoteControlEventReceiver;
import com.ting.play.state.MusicNotification;
import com.ting.util.UtilGson;
import com.ting.util.UtilRetrofit;
import com.ting.util.UtilSPutil;
import com.ting.util.UtilStr;
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
    private String url = null;
    private String currentURL = null;
    private Long time; // 播放进度
    private int msg;// 播放控制消息
    //书籍ID
    private int bookId = -1;
    //章节ID
    private int cid = -1;
    //书籍标题
    private String bookname = null;
    //章节名称
    private String cateTitle;
    //主播名
    private String host;
    //书籍图片
    private String pic;
    //播放集合
    public static PlayListVO vOs;
    private int total = 0;
    public static MusicNotification notification;

    private NotificationReceiver notificationReceiver;

    //排序方式
    private String sort = "asc";
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

    private ComponentName mMediaButtonReceiverComponent;
    //是否正常关闭
    private boolean isNormalDestory = false;

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
        notification = new MusicNotification(this);
        mMediaButtonReceiverComponent = new ComponentName(this, MyRemoteControlEventReceiver.class);
    }

    /* 启动service时执行的方法 */
    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {

        /* 得到从startService传来的动作，后是默认参数，这里是我自定义的常量 */
        if (intent == null) {
            record(false);
            isNormalDestory = true;
            MusicService.this.stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        if (mState == State.Preparing) {
            return super.onStartCommand(intent, flags, startId);
        }
        msg = intent.getIntExtra("MSG", 0); // 播放信息
        switch (msg) {
            //播放
            case PLAY_MSG: {
                url = intent.getStringExtra("url");
                bookId = intent.getIntExtra("bookid", -1);
                cid = intent.getIntExtra("cid", -1);
                sort = intent.getStringExtra("sort");
                cateTitle = intent.getStringExtra("cateTitle");
                bookname = intent.getStringExtra("bookName");
                host = intent.getStringExtra("host");
                pic = intent.getStringExtra("pic");
                UtilSPutil.getInstance(this).setInt("bookid", bookId);
                if (intent.getParcelableExtra("vos") != null) {
                    vOs = intent.getParcelableExtra("vos");
                }
                if (url != null) {
                    play();
                } else {
                    notifyError();
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
                if (vOs.getData() != null) {
                    int position = vOs.getData().get(0).getPosition();
                    int page = position / 50 + 1;
                    if (page == 1) {
                        if (currentPosition == 0) {
                            Toast.makeText(this, "前面没有更多章节", Toast.LENGTH_SHORT).show();
                        } else {
                            PlayingVO vo = vOs.getData().get(currentPosition - 1);
                            if (UtilStr.isEmpty(vo.getUrl())) {
                                Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                            } else {
                                cid = vo.getId();
                                url = vo.getUrl();
                                cateTitle = vo.getTitle();
                                play();
                            }
                        }
                    } else {
                        if (currentPosition == 0) {
                            Toast.makeText(this, "前面没有更多章节", Toast.LENGTH_SHORT).show();
                        } else {
                            PlayingVO vo = vOs.getData().get(currentPosition - 1);
                            if (UtilStr.isEmpty(vo.getUrl())) {
                                Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                            } else {
                                cid = vo.getId();
                                url = vo.getUrl();
                                cateTitle = vo.getTitle();
                                play();
                            }
                        }
                    }
                } else {
                    if (currentPosition == 0) {
                        Toast.makeText(this, "前面没有更多章节", Toast.LENGTH_SHORT).show();
                    } else {
                        DBChapter vo = vOs.getOfflineData().get(currentPosition - 1);
                        cid = vo.getChapterId();
                        url = vo.getChapterUrl();
                        cateTitle = vo.getChapterTitle();
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
                    if (vOs.getData() != null && !vOs.getData().isEmpty()) {
                        PlayingVO vo = vOs.getData().get(0);
                        if (UtilStr.isEmpty(vo.getUrl())) {
                            Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                        } else {
                            cid = vo.getId();
                            url = vo.getUrl();
                            cateTitle = vo.getTitle();
                            play();
                        }
                    } else {
                        DBChapter chapter = vOs.getOfflineData().get(0);
                        cid = chapter.getChapterId();
                        url = chapter.getChapterUrl();
                        cateTitle = chapter.getChapterTitle();
                        play();
                    }
                } else {
                    if (vOs.getData() != null && !vOs.getData().isEmpty()) {
                        if (currentPosition < vOs.getData().size() - 1) {
                            PlayingVO vo = vOs.getData().get(currentPosition + 1);
                            if (UtilStr.isEmpty(vo.getUrl())) {
                                Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                            } else {
                                cid = vo.getId();
                                url = vo.getUrl();
                                cateTitle = vo.getTitle();
                                play();
                            }
                        } else {
                            Toast.makeText(MusicService.this, "后面没有更多章节", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (currentPosition < vOs.getOfflineData().size() - 1) {
                            DBChapter chapter = vOs.getOfflineData().get(currentPosition + 1);
                            cid = chapter.getChapterId();
                            url = chapter.getChapterUrl();
                            cateTitle = chapter.getChapterTitle();
                            play();
                        } else {
                            Toast.makeText(MusicService.this, "后面没有更多章节", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            //跳转
            case SEEKTO_MSG: {
                time = intent.getLongExtra("seekTime", 0);
                seekType = intent.getIntExtra("seekType", 1);
                if (seekType == 0) {
                    url = intent.getStringExtra("url");
                    bookId = intent.getIntExtra("bookid", -1);
                    cid = intent.getIntExtra("cid", -1);
                    sort = intent.getStringExtra("sort");
                    cateTitle = intent.getStringExtra("cateTitle");
                    bookname = intent.getStringExtra("bookName");
                    host = intent.getStringExtra("host");
                    pic = intent.getStringExtra("pic");
                    UtilSPutil.getInstance(this).setInt("bookid", bookId);
                    if (intent.getParcelableExtra("vos") != null) {
                        vOs = intent.getParcelableExtra("vos");
                    }
                }
                if (seekType == 0) {
                    play();
                } else {
                    if (mExoPlayer != null) {
                        mExoPlayer.seekTo(time);
                        mExoPlayer.setPlayWhenReady(true);
                    }
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
                                record(false);
                                isNormalDestory = true;
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
        mPlayOnFocusGain = true;
        tryToGetAudioFocus();
        registerAudioNoisyReceiver();
        String localFile = AppData.FILE_PATH + bookId + "/" + bookId + "" + cid + ".mp3";
        File file = new File(localFile);
        if (file.exists()) {
            url = localFile;
        }
        boolean mediaHasChanged = !TextUtils.equals(url, currentURL);
        if (mediaHasChanged) {
            currentURL = url;
        }
        if (mediaHasChanged || mExoPlayer == null) {
            releaseResources(false);
            //判断是否本地播放
            if (!TextUtils.isEmpty(currentURL)) {
                currentURL = currentURL.replaceAll(" ", "%20"); // Escape spaces for URLs
            }
            if (mExoPlayer == null) {
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(), new DefaultLoadControl());
                mExoPlayer.addListener(mEventListener);
            }
            final AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(CONTENT_TYPE_MUSIC).setUsage(USAGE_MEDIA).build();
            mExoPlayer.setAudioAttributes(audioAttributes);
            DataSource.Factory factory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "listen"));
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            Log.d("aaa", "播放链接===========" + currentURL);
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentURL), factory, extractorsFactory, null, null);
            mExoPlayer.prepare(mediaSource);
            mWifiLock.acquire();
        }
        configurePlayerState();
        if (mControlClient == null) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            intent.setComponent(mMediaButtonReceiverComponent);
            mControlClient = new RemoteControlClient(
                    PendingIntent.getBroadcast(this /*context*/,
                            0 /*requestCode, ignored*/, intent /*intent*/, 0 /*flags*/));
            mAudioManager.registerRemoteControlClient(mControlClient);
            mAudioManager.registerMediaButtonEventReceiver(mMediaButtonReceiverComponent);
            mControlClient.setOnGetPlaybackPositionListener(new RemoteControlClient.OnGetPlaybackPositionListener() {
                @Override
                public long onGetPlaybackPosition() {
                    if (mExoPlayer == null) {
                        return 0;
                    } else {
                        return mExoPlayer.getCurrentPosition();
                    }
                }
            });
            mControlClient.setPlaybackPositionUpdateListener(new RemoteControlClient.OnPlaybackPositionUpdateListener() {
                @Override
                public void onPlaybackPositionUpdate(long newPositionMs) {

                }
            });
        }

        mControlClient.setPlaybackState(
                RemoteControlClient.PLAYSTATE_PLAYING);

        mControlClient.setTransportControlFlags(
                RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                        RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                        RemoteControlClient.FLAG_KEY_MEDIA_NEXT |
                        RemoteControlClient.FLAG_KEY_MEDIA_STOP);


        // Update the remote controls
        mControlClient.editMetadata(true)
                .putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, host)
                .putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, bookname)
                .putString(MediaMetadataRetriever.METADATA_KEY_TITLE, cateTitle)
                .putLong(MediaMetadataRetriever.METADATA_KEY_DURATION,
                        mExoPlayer.getDuration())
                .apply();
    }

    /**
     * 暂停
     */
    private void pause() {
        if (mControlClient != null) {
            mControlClient.setPlaybackState(
                    RemoteControlClient.PLAYSTATE_PAUSED);
        }
        record(false);
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
        Log.d("aaa", "onDestroy");
        if(!isNormalDestory) {
            record(false);
        }
        errorRelease();
        if (notificationReceiver != null) {
            this.unregisterReceiver(notificationReceiver);
        }
        Intent intent = new Intent();
        intent.setAction(MUSIC_DESTORY);
        sendBroadcast(intent);
        super.onDestroy();
    }

    /**
     * 某种异常错误释放资源
     */
    public void errorRelease() {
        mState = State.Stopped;
        if (mControlClient != null) {
            mControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_STOPPED);
        }
        notifyComplete();
        sendPauseStatus();
        giveUpAudioFocus();
        stopForeground(true);
        unregisterAudioNoisyReceiver();
        AppData.playKey = null;
        AppData.currPlayBookId = -1;
        AppData.isPlaying = false;
        releaseResources(true);
        if (tTimer != null) {
            tTimer.cancel();
            tTimer = null;
        }
    }

    public void record(boolean isComplete) {
        if (!TextUtils.isEmpty(url) && getCurrPlayPostion() != -1 && vOs != null) {
            MusicDBController controller = new MusicDBController();
            DBListenHistory vo = new DBListenHistory();
            vo.setBookid(bookId);
            vo.setChapter_name(cateTitle);
            vo.setCid(cid);
            vo.setUrl(url);
            vo.setDate(System.currentTimeMillis());

            //在线播放记忆位置
            if (vOs.getData() != null && !vOs.getData().isEmpty()) {
                int position = getCurrPlayPostion();
                Log.d("aaa", "postion========" + position + "=========");
                if (position != -1 && position != -2) {
                    vo.setPosition(vOs.getData().get(getCurrPlayPostion()).getPosition());
                } else {
                    vo.setPosition(0);
                }
            }
            //离线播放记忆位置
            else {
                int position = getCurrPlayPostion();
                if (position != -1 && position != -2) {
                    vo.setPosition(vOs.getOfflineData().get(getCurrPlayPostion()).getPosition());
                } else {
                    vo.setPosition(0);
                }
            }
            vo.setBookname(bookname);
            vo.setHost(host);
            vo.setPic(pic);
            //播放完成
            if (isComplete) {
                vo.setDuration(0L);
                vo.setTotal(0L);
            } else {
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
        if (mAudioFocus != AudioFocus.Focused && AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)) {
            mAudioFocus = AudioFocus.Focused;
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
//        stopForeground(true);
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
        Intent intent = new Intent();
        intent.putExtra("bookid", bookId);
        intent.putExtra("cid", cid);
        AppData.playKey = bookId + "-" + cid;
        AppData.isPlaying = false;
        intent.setAction(MUSIC_LOADING);
        sendBroadcast(intent);
    }

    /**
     * 通知播放
     */
    private void notifyStartPlay() {
        Log.d("aaa", "通知播放");
        seekType = 1;
        mState = State.Playing;
        Intent intent = new Intent();
        intent.putExtra("TotalTime", mExoPlayer.getDuration());
        intent.putExtra("bookid", bookId);
        intent.putExtra("cid", cid);
        intent.putExtra("cateTitle", cateTitle);
        intent.setAction(MUSIC_PLAY);
        sendBroadcast(intent);
        startForeground(NOTIFICATION_ID, notification.getNotification());
        notification.notifyInit(bookId, bookname, cateTitle, pic);
        AppData.isPlaying = true;
        AppData.playKey = bookId + "-" + cid;
        AppData.currPlayBookId = bookId;
        updateProgress();
    }

    /**
     * 通知暂停
     */
    private void notifyPause() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent();
        intent.putExtra("bookid", bookId);
        intent.setAction(MUSIC_PAUSE);
        sendBroadcast(intent);
        notification.notifyPause();
        AppData.isPlaying = false;
    }


    protected void notifyComplete() {
        Log.d("aaa--MusicService", "播放完成");
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent();
        intent.putExtra("bookid", bookId);
        notification.notifyCancel();
        intent.setAction(MUSIC_COMPLETE);
        sendBroadcast(intent);
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
        AppData.currPlayBookId = -1;
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
                intent.putExtra("bookid", bookId);
                intent.putExtra("cateTitle", cateTitle);
                intent.putExtra("bookName", bookname);
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
        intent.putExtra("bookid", bookId);
        intent.setAction(MUSIC_BUFFER_START);
        sendBroadcast(intent);
    }

    //通知完成缓存
    private void notifyBuffEnd() {
        mState = State.Playing;
        Intent intent = new Intent();
        intent.putExtra("bookid", bookId);
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
                    AppData.currPlayBookId = -1;
                    record(false);
                    sendPauseStatus();
                    isNormalDestory = true;
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
        if (vOs.getData() != null && !vOs.getData().isEmpty()) {
            List<PlayingVO> data = vOs.getData();
            if (data == null) {
                return -1;
            } else {
                for (int i = 0; i < data.size(); i++) {
                    if (cid == data.get(i).getId()) {
                        return i;
                    }
                }
                return -2;
            }
        } else {
            List<DBChapter> data = vOs.getOfflineData();
            if (data == null) {
                return -1;
            } else {
                for (int i = 0; i < data.size(); i++) {
                    if (cid == data.get(i).getChapterId()) {
                        return i;
                    }
                }
                return -2;
            }
        }
    }

    /**
     * 播放完成
     */
    private void playCompletion() {
        //播放完成
        msg = PLAY_MSG;
        if (vOs != null && ((vOs.getData() != null && vOs.getData().size() > 0) || (vOs.getOfflineData() != null && vOs.getOfflineData().size() > 0))) {
            int index = getCurrPlayPostion();
            if (index != -1) {
                //在线逻辑
                if (vOs.getData() != null && !vOs.getData().isEmpty()) {
                    if (index == -2) {
                        PlayingVO vo = vOs.getData().get(0);
                        if (UtilStr.isEmpty(vo.getUrl())) {
                            record(true);
                            Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                            isNormalDestory = true;
                            MusicService.this.stopSelf();
                        } else {
                            cid = vo.getId();
                            url = vo.getUrl();
                            cateTitle = vo.getTitle();
                            play();
                        }
                    } else {
                        if (index < vOs.getData().size() - 1) {
                            PlayingVO vo = vOs.getData().get(index + 1);
                            if (UtilStr.isEmpty(vo.getUrl())) {
                                record(true);
                                Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                                isNormalDestory = true;
                                MusicService.this.stopSelf();
                            } else {
                                Log.d("service", "下一集");
                                cid = vo.getId();
                                url = vo.getUrl();
                                cateTitle = vo.getTitle();
                                play();
                            }
                        } else {
                            List<PlayingVO> data = vOs.getData();
                            int position = data.get(data.size() - 1).getPosition();
                            int page = position / 50 + 2;
                            loadData(page);
                        }
                    }
                }
                //离线逻辑
                else {
                    if (index == -2) {
                        DBChapter vo = vOs.getOfflineData().get(0);
                        cid = vo.getChapterId();
                        url = vo.getChapterUrl();
                        cateTitle = vo.getChapterTitle();
                        play();
                    } else {
                        if (index < vOs.getOfflineData().size() - 1) {
                            DBChapter vo = vOs.getOfflineData().get(index + 1);
                            cid = vo.getChapterId();
                            url = vo.getChapterUrl();
                            cateTitle = vo.getChapterTitle();
                            play();
                        } else {
                            Toast.makeText(MusicService.this, "播放完成", Toast.LENGTH_SHORT).show();
                            record(true);
                            isNormalDestory = true;
                            MusicService.this.stopSelf();
                        }
                    }
                }
            }
        } else {
            Toast.makeText(MusicService.this, "播放完成", Toast.LENGTH_SHORT).show();
            record(true);
            isNormalDestory = true;
            MusicService.this.stopSelf();
        }
    }

    /**
     * 加载数据
     *
     * @param page
     */
    private void loadData(final int pages) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pages + "");
        map.put("type", "works");
        map.put("count", "50");
        map.put("bookID", bookId + "");
        if (TokenManager.isLogin(this)) {
            map.put("uid", TokenManager.getUid(this));
        }
        map.put("sort", sort);
        BaseObserver baseObserver = new BaseObserver<PlayResult>() {
            @Override
            public void success(PlayResult data) {
                super.success(data);
                if (data.getData() != null && data.getData().getData() != null && data.getData().getData().size() > 0) {
                    int page = data.getData().getPage();
                    int count = data.getData().getCount();
                    int length = data.getData().getLenght();
                    for (int i = 0; i < data.getData().getData().size(); i++) {
                        int position = -1;
                        if (sort.equals("asc")) {
                            position = (page - 1) * count + i;
                        } else {
                            position = length - ((page - 1) * count + i);
                        }
                        data.getData().getData().get(i).setPosition(position);
                    }
                    if(pages != page) {
                        vOs.getData().addAll(data.getData().getData());
                        Log.d("aaa", "请求网络");
                        notifyDataUpdate(vOs);
                        int index = getCurrPlayPostion();
                        if (index != -1) {
                            if (index < vOs.getData().size() - 1) {
                                PlayingVO vo = vOs.getData().get(index + 1);
                                if (UtilStr.isEmpty(vo.getUrl())) {
                                    Toast.makeText(MusicService.this, "此章节收费", Toast.LENGTH_SHORT).show();
                                    record(true);
                                    isNormalDestory = true;
                                    MusicService.this.stopSelf();
                                } else {
                                    cid = vo.getId();
                                    url = vo.getUrl();
                                    cateTitle = vo.getTitle();
                                    play();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void error() {
                super.error();
                Toast.makeText(MusicService.this, "最后一章", Toast.LENGTH_SHORT).show();
                record(true);
                isNormalDestory = true;
                MusicService.this.stopSelf();
            }
        };
        UtilRetrofit.getInstance().create(HttpService.class).getPlayerList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(baseObserver);
    }

    private void updateProgress() {
        Log.d("bbb", mExoPlayer.getCurrentPosition() + "");
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
                case ExoPlayer.STATE_IDLE:
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    notifyLoading();
                    break;
                case ExoPlayer.STATE_READY:
                    if (playWhenReady) {
                        if (msg == SEEKTO_MSG) {
                            if (mExoPlayer != null) {
                                mExoPlayer.seekTo(time);
                            }
                            msg = PLAY_MSG;
                        }
                        record(false);
                        notifyStartPlay();
                    }
                    break;
                case ExoPlayer.STATE_ENDED:
                    notifyComplete();
                    if (playWhenReady) {
                        playCompletion();
                    } else {

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
                    record(false);
                    isNormalDestory = true;
                    MusicService.this.stopSelf();
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    what = error.getRendererException().getMessage();
                    record(false);
                    isNormalDestory = true;
                    MusicService.this.stopSelf();
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    what = error.getUnexpectedException().getMessage();
                    record(false);
                    isNormalDestory = true;
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
