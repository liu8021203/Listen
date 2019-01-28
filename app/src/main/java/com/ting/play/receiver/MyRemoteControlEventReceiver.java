package com.ting.play.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.ting.common.AppData;
import com.ting.db.DBListenHistory;
import com.ting.play.controller.MusicController;
import com.ting.play.controller.MusicDBController;
import com.ting.play.service.MusicService;

/**
 * Created by liu on 2017/12/6.
 */

public class MyRemoteControlEventReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("aaa", intent.getAction());
        KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(
                Intent.EXTRA_KEY_EVENT);
        if(keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_UP) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
//                    Log.d("aaa", "hook!");
//                    if(AppData.isPlaying) {
//                        Intent intentService = new Intent(context, MusicService.class);
//                        intentService.putExtra("MSG", MusicService.PAUSE_MSG);
//                        context.startService(intentService);
//                        Log.d("aaa", "PLAY_PAUSE!-----暂停");
//                    }else{
//                        MusicDBController dbController = new MusicDBController();
//                        DBListenHistory mHistory = dbController.getBookIdData(String.valueOf(AppData.currPlayBookId));
//                        MusicController musicController = new MusicController(context);
//                        musicController.play(AppData.currPlayBookId, mHistory.getCid(), mHistory.getUrl(), mHistory.getChapter_name(), mHistory.getBookname(), mHistory.getHost(), mHistory.getPic(), null, "asc", 0);
//                        Log.d("aaa", "PLAY_PAUSE!------播放");
//                    }
                    break;

                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE: {
//                    Log.d("aaa", "PLAY_PAUSE!");
//                    if(AppData.isPlaying) {
//                        Intent intentService = new Intent(context, MusicService.class);
//                        intentService.putExtra("MSG", MusicService.PAUSE_MSG);
//                        context.startService(intentService);
//                        Log.d("aaa", "PLAY_PAUSE!-----暂停");
//                    }else{
//                        MusicDBController dbController = new MusicDBController();
//                        DBListenHistory mHistory = dbController.getBookIdData(String.valueOf(AppData.currPlayBookId));
//                        MusicController musicController = new MusicController(context);
//                        musicController.play(AppData.currPlayBookId, mHistory.getCid(), mHistory.getUrl(), mHistory.getChapter_name(), mHistory.getBookname(), mHistory.getHost(), mHistory.getPic(), null, "asc", 0);
//                        Log.d("aaa", "PLAY_PAUSE!------播放");
//                    }
                }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY: {
                    Log.d("aaa", "PLAY!");

                }
                    break;

                case KeyEvent.KEYCODE_MEDIA_PAUSE: {
                    Log.d("aaa", "PAUSE!");
                }
                    break;

                case KeyEvent.KEYCODE_MEDIA_STOP:
                    Log.d("aaa", "STOP");
                    break;

                case KeyEvent.KEYCODE_MEDIA_NEXT: {
                    Log.d("aaa", "NEXT");
                    Intent intentService = new Intent(context, MusicService.class);
                    intentService.putExtra("MSG", MusicService.PLAY_NEXT);
                    context.startService(intentService);
                }
                    break;

                case KeyEvent.KEYCODE_MEDIA_PREVIOUS: {
                    Log.d("aaa", "PREVIOUS");
                    Intent intentService = new Intent(context, MusicService.class);
                    intentService.putExtra("MSG", MusicService.PLAY_PREVIOUS);
                    context.startService(intentService);
                }
                    break;
            }
        }
        abortBroadcast();
    }
}
