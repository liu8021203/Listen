package com.ting.play.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.ting.base.BaseActivity;
import com.ting.bean.play.PlayingVO;
import com.ting.play.BookDetailsActivity;
import com.ting.bean.play.PlayListVO;
import com.ting.play.service.MusicService;
import com.ting.util.UtilLog;
import com.ting.util.UtilStr;

import java.util.ArrayList;

import javax.crypto.Mac;

/**
 * MusicController 执行针对MusicService的操作，并接收MusicService返回的状态
 * initMusicReceiver 使MusicController开始接收广播
 * UnRegisterReceiver 注销广播
 * addListener(name,IPlayerStatus) 可添加状态接收接口
 * deleteListener(name) 可删除添加的接口
 * clearListener() 清空所有的接口
 *
 * @author Young
 */
public class MusicController {

    private Context mContext;


    public MusicController(Context context) {
        this.mContext = context;
    }


    /**
     * 播放
     *
     * @param bookid    书籍ID
     * @param cid       章节ID
     * @param url       章节url
     * @param cateTitle 章节名称
     * @param bookName  书籍名称
     * @param host      主播名称
     * @param pic       图片url
     * @param vo        播放集合
     * @param sort      排序
     * @param type      0：在线播放，1：离线播放
     */
    public void play(int bookid, int cid, String url, String cateTitle, String bookName, String host, String pic, PlayListVO vo, String sort, int type) {
        if (UtilStr.isEmpty(url)) {
            Toast.makeText(mContext, "数据有误，请联系客服", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent playIntent = new Intent(mContext, MusicService.class);
            playIntent.putExtra("MSG", MusicService.PLAY_MSG);
            playIntent.putExtra("url", url);
            playIntent.putExtra("bookid", bookid);
            playIntent.putExtra("cid", cid);
            playIntent.putExtra("cateTitle", cateTitle);
            playIntent.putExtra("vos", vo);
            playIntent.putExtra("sort", sort);
            playIntent.putExtra("type", type);
            playIntent.putExtra("bookName", bookName);
            playIntent.putExtra("host", host);
            playIntent.putExtra("pic", pic);
            mContext.startService(playIntent);
        }
    }

    /**
     * 拖动播放
     *
     * @param seekTime  时间
     * @param bookid    书籍ID
     * @param cid       章节ID
     * @param url       章节url
     * @param cateTitle 章节名称
     * @param bookName  书籍名称
     * @param host      主播名称
     * @param pic       图片url
     * @param vos       播放集合
     * @param sort      排序
     * @param type      0：在线播放，1：离线播放
     * @param seekType  0初始化播放  1拖动播放
     */
    public void play(Long seekTime, int bookid, int cid, String url, String cateTitle, String bookName, String host, String pic, PlayListVO vos, String sort, int type, int seekType) {
        if (UtilStr.isEmpty(url) && seekType == 0) {
            UtilLog.LogD("播放链接为空");
            return;
        } else {
            Intent playIntent = new Intent(mContext, MusicService.class);
            playIntent.putExtra("MSG", MusicService.SEEKTO_MSG);
            playIntent.putExtra("url", url);
            playIntent.putExtra("seekTime", seekTime);
            playIntent.putExtra("bookid", bookid);
            playIntent.putExtra("cid", cid);
            playIntent.putExtra("vos", vos);
            playIntent.putExtra("sort", sort);
            playIntent.putExtra("type", type);
            playIntent.putExtra("seekType", seekType);
            playIntent.putExtra("bookName", bookName);
            playIntent.putExtra("cateTitle", cateTitle);
            playIntent.putExtra("host", host);
            playIntent.putExtra("pic", pic);
            mContext.startService(playIntent);
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        Intent pauseIntent = new Intent(mContext, MusicService.class);
        pauseIntent.putExtra("MSG", MusicService.PAUSE_MSG);
        mContext.startService(pauseIntent);
    }

    /**
     * 上一章
     */
    public void previous(){
        Intent pauseIntent = new Intent(mContext, MusicService.class);
        pauseIntent.putExtra("MSG", MusicService.PLAY_PREVIOUS);
        mContext.startService(pauseIntent);
    }

    /**
     * 下一章
     */
    public void next(){
        Intent pauseIntent = new Intent(mContext, MusicService.class);
        pauseIntent.putExtra("MSG", MusicService.PLAY_NEXT);
        mContext.startService(pauseIntent);
    }

    public void seekTo(Long seekTime){
        Intent intent = new Intent(mContext, MusicService.class);
        intent.putExtra("MSG", MusicService.SEEKTO_MSG);
        intent.putExtra("seekTime", seekTime);
        mContext.startService(intent);
    }

    /**
     * 开启定时
     *
     * @param time
     */
    public void timeStart(int time) {
        Intent playIntent = new Intent(mContext, MusicService.class);
        playIntent.putExtra("MSG", MusicService.TIME_START);
        playIntent.putExtra("time", time);
        mContext.startService(playIntent);
    }

    /**
     * 停止定时
     */
    public void timeStop() {
        Intent playIntent = new Intent(mContext, MusicService.class);
        playIntent.putExtra("MSG", MusicService.TIME_STOP);
        mContext.startService(playIntent);
    }

}
