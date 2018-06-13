package com.ting.bean.myself;

/**
 * Created by gengjiajia on 15/9/29.
 * 我的粉丝历时读书
 */
public class MySeeHistory {
    private int bookID;//历史图书ID
    private String thumb;//历史图书URL
    private String title;//历史图书标题;

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

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
}
