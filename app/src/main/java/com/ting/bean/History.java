package com.ting.bean;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/21.
 */
public class History extends BaseParam {
    private String thumb;//最新图书URL
    private String title;//最新图书标题
    private int bookID;//最新图书ID

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
}
