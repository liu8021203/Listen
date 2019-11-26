package com.ting.play.state;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ting.play.PlayActivity;
import com.ting.R;
import com.ting.play.service.MusicService;

public class MusicNotification {
    private static final String CHANNEL_ID = "com.tingshijie";
    private NotificationManager manager;
    private Notification notification;
    private PendingIntent pendingIntent;
    private RemoteViews smallRemoteViews;
    private MusicService mService;
    private NotificationCompat.Builder mBuilder;
    private Intent notificationIntent;
    private NotificationChannel channel;
    private String pic = "";

    public MusicNotification(MusicService context) {
        this.mService = context;
        this.manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        this.notification = new Notification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, "com.ting", NotificationManager.IMPORTANCE_LOW);
            channel.setBypassDnd(true);    //设置绕过免打扰模式
            channel.canBypassDnd();       //检测是否绕过免打扰模式
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);//设置在锁屏界面上显示这条通知
            channel.setDescription("听世界听书");
            channel.setLightColor(Color.GREEN);
            channel.setName("听世界");
            channel.setShowBadge(false);
            channel.setVibrationPattern(null);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
        initNotification();
    }


    @SuppressLint("NewApi")
    private void initNotification() {
        Log.d("aaa", "initNotification执行了");
        smallRemoteViews = new RemoteViews(mService.getPackageName(),
                R.layout.play_music_notification_small);

        //播放PendingIntent
        PendingIntent playPendingIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(mService,
                PlaybackStateCompat.ACTION_PLAY);
        smallRemoteViews.setOnClickPendingIntent(R.id.play_notification_play,
                playPendingIntent);

        //暂停PendingIntent
        PendingIntent pausePendingIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(mService,
                PlaybackStateCompat.ACTION_PAUSE);
        smallRemoteViews.setOnClickPendingIntent(R.id.play_notification_pause,
                pausePendingIntent);

        //关闭PendingIntent
        PendingIntent closePlayPendingIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(mService,
                PlaybackStateCompat.ACTION_STOP);

        smallRemoteViews.setOnClickPendingIntent(R.id.play_notification_close,
                closePlayPendingIntent);

        //item点击Intent
        notificationIntent = new Intent(mService, PlayActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(mService, 120,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder = new NotificationCompat.Builder(mService, CHANNEL_ID);
        mBuilder.setCustomContentView(smallRemoteViews);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        mBuilder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mService.getSessionToken())
                // Add a cancel button
                .setShowCancelButton(true)
                .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(mService,
                        PlaybackStateCompat.ACTION_STOP)));
        notification = mBuilder.setContentTitle("some string")
                .setContentText("Slide down on note to expand").setSmallIcon(R.mipmap.icon).build();
        notification.flags = Notification.FLAG_NO_CLEAR;

    }


    public Notification getNotification() {
        return notification;
    }


    public void notifyInit(String bookName, String cateName, String pic) {
        Log.d("aaa", "MusicNotification==============notifyInit");
        notification.contentView.setTextViewText(
                R.id.play_notification_album_name, bookName);
        notification.contentView.setTextViewText(
                R.id.tv_cate_name, cateName);
        if (pic != null && !this.pic.equals(pic)) {
            Glide.with(mService).asBitmap().load(pic).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    notification.contentView.setImageViewBitmap(R.id.play_notification_cover_image, resource);
                    notification.contentView.setViewVisibility(R.id.play_notification_play, View.GONE);
                    notification.contentView.setViewVisibility(R.id.play_notification_pause, View.VISIBLE);
                    manager.notify(120, notification);
                }
            });
            this.pic = pic;
        }
        notification.contentView.setViewVisibility(R.id.play_notification_play, View.GONE);
        notification.contentView.setViewVisibility(R.id.play_notification_pause, View.VISIBLE);
        manager.notify(120, notification);
    }


    //暂停状态
    @SuppressLint("NewApi")
    public void notifyPause() {
        Log.d("aaa---notification", "notifyPause");
        notification.contentView.setViewVisibility(R.id.play_notification_play, View.VISIBLE);
        notification.contentView.setViewVisibility(R.id.play_notification_pause, View.GONE);
        manager.notify(120, notification);
    }

    @SuppressLint("NewApi")
    public void notifyResume() {
        Log.d("aaa", "notifyResume");
        notification.contentView.setViewVisibility(R.id.play_notification_play, View.GONE);
        notification.contentView.setViewVisibility(R.id.play_notification_pause, View.VISIBLE);
        manager.notify(120, notification);


    }

    public void notifyCancel() {
        Log.d("aaa", "notifyCancel");
        if (manager != null) {
            manager.cancel(120);
        }
    }
}
