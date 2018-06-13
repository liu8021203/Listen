package com.ting.play.state;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ting.common.AppData;
import com.ting.play.PlayActivity;
import com.ting.R;

public class MusicNotification {
    private static final String CHANNEL_ID = "com.ting.MUSIC_CHANNEL_ID";
    private NotificationManager manager;
    private Notification notification;
    private PendingIntent pendingIntent;
    private RemoteViews smallRemoteViews;
    private PendingIntent playPendingIntent;
    private Context context;
    private NotificationCompat.Builder mBuilder;
    private Intent notificationIntent;
    public final static String PAUSE_MSG = "com.listen.notification.pause";
    public final static String PLAY_MSG = "com.listen.notification.play";
    public final static String CLOSE_MSG = "com.listen.notification.close";
    public final static String SHOW_PLAY_MSG = "com.listen.notification.showPlayerView";
    private String pic = "";

    public MusicNotification(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        this.notification = new Notification();
        initNotification();
    }


    @SuppressLint("NewApi")
    private void initNotification() {
        System.out.println("initNotification执行了");
        smallRemoteViews = new RemoteViews(context.getPackageName(),
                R.layout.play_music_notification_small);
        //播放intent
        Intent playIntent = new Intent();
        playIntent.setAction(PLAY_MSG);
        playPendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 0, playIntent, 0);
        smallRemoteViews.setOnClickPendingIntent(R.id.play_notification_play,
                playPendingIntent);


        //暂停intent
        Intent pauseIntent = new Intent();
        pauseIntent.setAction(PAUSE_MSG);
        playPendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 0, pauseIntent, 0);
        smallRemoteViews.setOnClickPendingIntent(R.id.play_notification_pause,
                playPendingIntent);

        //关闭intent
        Intent closeIntent = new Intent();
        closeIntent.setAction(CLOSE_MSG);
        playPendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 0, closeIntent, 0);
        smallRemoteViews.setOnClickPendingIntent(R.id.play_notification_close,
                playPendingIntent);

        //item点击Intent
        notificationIntent = new Intent(context, PlayActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("NOTIFICATION_MSG", SHOW_PLAY_MSG);
        pendingIntent = PendingIntent.getActivity(context, 120,
                notificationIntent, 0);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContent(smallRemoteViews);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setCategory(Notification.CATEGORY_SERVICE);
        mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        notification = mBuilder.setContentTitle("some string")
                .setContentText("Slide down on note to expand").setSmallIcon(R.mipmap.icon).build();
        notification.flags = Notification.FLAG_NO_CLEAR;

    }


    public Notification getNotification() {
        return notification;
    }


    public void notifyInit(int bookId, String bookName, String cateName, String pic){
        Log.d("aaa", "notifyInit");
        notification.contentView.setTextViewText(
                R.id.play_notification_album_name, bookName);
        notification.contentView.setTextViewText(
                R.id.tv_cate_name, cateName);
        if(pic != null && !this.pic.equals(pic)) {
            Glide.with(context).asBitmap().load(pic).into(new SimpleTarget<Bitmap>() {
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
        if (AppData.playKey != null) {
            notification.contentView.setViewVisibility(R.id.play_notification_play, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.play_notification_pause, View.GONE);
            manager.notify(120, notification);
        } else {
            notifyCancel();
        }
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
