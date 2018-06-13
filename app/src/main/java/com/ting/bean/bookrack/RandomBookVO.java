package com.ting.bean.bookrack;

/**
 * Created by liu on 2017/12/8.
 */

public class RandomBookVO {

    /**
     * title : 特种兵在都市|流氓艳遇记
     * author : 夜十三
     * thumb : http://h.tingshijie.com/20151216/1450247146.jpg
     * hit : 6154632
     * jishu : 1580
     * update_status : 0
     * lastUpdatetime : 1509245578
     */
    private int bookid;
    private String title;
    private String author;
    private String thumb;
    private String hit;
    private String jishu;
    private String update_status;
    private String lastUpdatetime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getJishu() {
        return jishu;
    }

    public void setJishu(String jishu) {
        this.jishu = jishu;
    }

    public String getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(String update_status) {
        this.update_status = update_status;
    }

    public String getLastUpdatetime() {
        return lastUpdatetime;
    }

    public void setLastUpdatetime(String lastUpdatetime) {
        this.lastUpdatetime = lastUpdatetime;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }
}
