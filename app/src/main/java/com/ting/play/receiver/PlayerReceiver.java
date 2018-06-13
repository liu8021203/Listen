package com.ting.play.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ting.bean.play.PlayListVO;
import com.ting.bean.play.PlayingVO;
import com.ting.play.PlayActivity;
import com.ting.play.service.MusicService;
import com.ting.play.subview.PlayListSubView;

import java.util.ArrayList;

/**
 * Created by liu on 2017/12/11.
 */

public class PlayerReceiver extends BroadcastReceiver{

    private PlayActivity mActivity;

    public PlayerReceiver(PlayActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int bookid = intent.getIntExtra("bookid", -1);
        switch (action){
            case MusicService.MUSIC_LOADING:
                if(mActivity != null){
                    mActivity.loadingMusic(bookid);
                }
                break;

            case MusicService.MUSIC_PLAY:{
                if(mActivity != null) {
                    long duration = intent.getLongExtra("TotalTime", -1);
                    int cid = intent.getIntExtra("cid", -1);
                    String title = intent.getStringExtra("cateTitle");
                    mActivity.initPlay(duration, bookid, cid, title);
                }
            }
                break;

            case MusicService.MUSIC_PAUSE:{
                if(mActivity != null){
                    mActivity.pauseNotify(bookid);
                }
            }
                break;

            case MusicService.MUSIC_PROGRESS: {
                if (mActivity != null) {
                    long currTime = intent.getLongExtra("currentTime", -1);
                    long totalTime = intent.getLongExtra("totalTime", -1);
                    String title = intent.getStringExtra("cateTitle");
                    String bookName = intent.getStringExtra("bookName");
                    mActivity.updateTime(currTime, totalTime, title, bookName);
                }
            }
                break;

            case MusicService.MUSIC_COMPLETE:{
                if(mActivity != null){
                    mActivity.playComplete(bookid);
                }
            }
                break;
            case MusicService.MUSIC_ERROR: {
                if(mActivity != null){
                    mActivity.error(bookid);
                }
            }
                break;

            case MusicService.MUSIC_BUFFER_START: {
                if(mActivity != null){
                    mActivity.buffStart(bookid);
                }
            }
                break;

            case MusicService.MUSIC_BUFFER_END:{
                if(mActivity != null){
                    mActivity.buffEnd(bookid);
                }
            }
                break;

            case MusicService.MUSIC_DATA_UPDATE:{
                if(mActivity != null){
                    PlayListVO vo = intent.getParcelableExtra("data");
//                    mActivity.updateData(bookid, (ArrayList<PlayingVO>) vo.getData());
                }
            }
                break;

            case MusicService.TIMER_COMPLETE:
                if(mActivity != null){
                    mActivity.timerComplete();
                }
                break;

            case MusicService.TIMER_PROGRESS:
                if(mActivity != null){
                    int timing = intent.getIntExtra("timing", -1);
                    mActivity.timerProgress(timing);
                }
                break;

            case MusicService.MUSIC_DESTORY:
                if(mActivity != null){
                    mActivity.destory();
                }
                break;
        }
    }
}
