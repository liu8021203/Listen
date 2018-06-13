package com.ting.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.ting.bean.play.PlayingVO;
import com.ting.db.DBChapter;

import java.io.File;

/**
 * Created by liu on 15/10/21.
 */
public class UtilListener {


    public static DBChapter PlayingVOToBook(PlayingVO vo, int bookid, String bookname, String hostname, String imgUrl)
    {
        DBChapter tempVO = new DBChapter();
        tempVO.setBookId(bookid);
        tempVO.setChapterId(vo.getId());
        tempVO.setChapterTitle(vo.getTitle());
        tempVO.setChapterUrl(vo.getUrl());
        tempVO.setBookName(bookname);
        tempVO.setHost(hostname);
        tempVO.setBookUrl(imgUrl);
        tempVO.setPosition(vo.getPosition());
        return tempVO;
    }


    public static String getHotScore(int score)
    {
        String temp = "";
        if(score >= 10000)
        {
             double d = score / 10000;
             temp = UtilFloat.getValue(1, d) + "万";
        }
        else {
            temp = String.valueOf(score);
        }
        return temp;
    }


    public static Uri getUriForFile(Context context, File file) {
        if (file == null || context == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.ting.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }



}
