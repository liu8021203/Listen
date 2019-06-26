package com.ting.base;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import com.ting.play.service.MusicService;

import java.util.List;

public abstract class PlayerBaseActivity extends BaseActivity{
    private final String TAG = getClass().getSimpleName();

    private MediaBrowserCompat mMediaBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaBrowser = new MediaBrowserCompat(this, new ComponentName(this, MusicService.class), mConnectionCallback, null);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("aaa", "MediaBrowserCompat------connect");
        if (mMediaBrowser != null) {
            mMediaBrowser.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MediaControllerCompat controllerCompat = MediaControllerCompat.getMediaController(PlayerBaseActivity.this);
        if (controllerCompat != null) {
            controllerCompat.unregisterCallback(mCallback);
        }
        if (mMediaBrowser != null) {
            Log.d("aaa", "MediaBrowserCompat------disconnect");
            mMediaBrowser.disconnect();
        }

    }


    protected void notifyServiceConnected() {

    }

    /**
     * 返回播放状态
     *
     * @return
     */
    public PlaybackStateCompat getPlaybackStateCompat() {
        if (MediaControllerCompat.getMediaController(mActivity) != null) {
            return MediaControllerCompat.getMediaController(PlayerBaseActivity.this).getPlaybackState();
        }
        return null;
    }

    private MediaBrowserCompat.ConnectionCallback mConnectionCallback = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            super.onConnected();
            Log.d("aaa", "MediaSession-----链接成功------" + TAG);
            MediaControllerCompat mMediaControllerCompat = null;
            try {
                mMediaControllerCompat = new MediaControllerCompat(PlayerBaseActivity.this, mMediaBrowser.getSessionToken());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mMediaControllerCompat.registerCallback(mCallback);
            MediaControllerCompat.setMediaController(PlayerBaseActivity.this, mMediaControllerCompat);
            notifyServiceConnected();
        }

        @Override
        public void onConnectionSuspended() {
            super.onConnectionSuspended();
            Log.d("aaa", "MediaSession-----链接断开");
        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
            Log.d("aaa", "MediaSession-----链接失败");
        }
    };

    private MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
        }

        @Override
        public void onExtrasChanged(Bundle extras) {
            super.onExtrasChanged(extras);
            String model = extras.getString("model");
            if(TextUtils.equals(model, MusicService.MUSIC_TIMER_START)){
                int timer = extras.getInt("timer");
                notifyTimer(timer);
            }else if(TextUtils.equals(model, MusicService.MUSIC_TIMER_STOP)){
                notifyTimerStop();
            }
        }
    };


    private void updatePlaybackState(PlaybackStateCompat state) {
        if (state == null) {
            return;
        }
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_REWINDING: {
                Bundle bundle = state.getExtras();
                String chapterTitle = bundle.getString("chapterTitle");
                String bookTitle = bundle.getString("bookTitle");
                String bookId = bundle.getString("bookId");
                String bookImage = bundle.getString("bookImage");
                notifyRewinding(bookId, bookTitle, chapterTitle, bookImage);
            }
            break;

            case PlaybackStateCompat.STATE_PLAYING: {
                Bundle bundle = state.getExtras();
                long duration = bundle.getLong("totalTime");
                String chapterTitle = bundle.getString("chapterTitle");
                String bookTitle = bundle.getString("bookTitle");
                String bookId = bundle.getString("bookId");
                String bookImage = bundle.getString("bookImage");
                notifyPlay(bookId, bookTitle, chapterTitle, bookImage, duration);
            }
            break;
            case PlaybackStateCompat.STATE_PAUSED:
                notifyPause();
                break;
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:

                notifyStop();

                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                notifyLoading();
                break;

            case PlaybackStateCompat.STATE_ERROR:
                notifyStop();
                break;


            default:
//                LogHelper.d(TAG, "Unhandled state ", state.getState());
        }


    }

    /**
     * 通知准备
     */
    protected void notifyRewinding(String bookId, String bookTitle, String chapterTitle, String bookImage){
        Log.d("aaa", TAG + "------notifyRewinding");
    }

    /**
     * 通知加载中
     */
    protected void notifyLoading() {
        Log.d("aaa", TAG + "------notifyLoading");
    }

    /**
     * 通知播放
     *
     * @param bookTitle
     * @param chapterTitle
     * @param bookImage
     * @param duration
     */
    protected void notifyPlay(String bookId, String bookTitle, String chapterTitle, String bookImage, long duration) {
        Log.d("aaa", TAG + "------notifyPlay");
    }

    /**
     * 通知暂停
     */
    protected void notifyPause() {
        Log.d("aaa", TAG + "------notifyPause");
    }

    /**
     * 通知进度条更新
     *
     * @param total
     * @param progress
     */
    protected void notifyProgress(long total, long progress) {
    }

    /**
     * 通知停止
     */
    protected void notifyStop() {
        Log.d("aaa", TAG + "------notifyStop");
    }

    /**
     * 定时器变化
     */
    protected void notifyTimer(int time) {
        Log.d("aaa", TAG + "------notifyTimer-----" + time);
    }

    /**
     * 定时器停止
     */
    protected void notifyTimerStop() {
        Log.d("aaa", TAG + "------notifyTimerStop");
    }
}
