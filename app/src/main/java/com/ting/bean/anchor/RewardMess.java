package com.ting.bean.anchor;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/22.
 * 打赏排名信息
 */
public class RewardMess extends BaseParam {


    private int bookID;
    private String title;
    private int id;
    private String thumb;//头像
    private String name;//用户名
    private int rankQueue;//排名
    private int number;//听豆数

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRankQueue() {
        return rankQueue;
    }

    public void setRankQueue(int rankQueue) {
        this.rankQueue = rankQueue;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
