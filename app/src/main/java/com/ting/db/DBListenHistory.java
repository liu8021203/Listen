package com.ting.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liu on 2017/7/25.
 */

@Entity(indexes = {
        @Index(value = "bookid, cid", unique = true)
})
public class DBListenHistory {
    @Id
    private Long id;

    @NotNull
    private Long duration;   //时间
    private Long total;         //总时间
    private Integer bookid;     //书籍id
    private Integer cid;        //章节id
    private Integer position;   //章节序号
    private String bookname;    //书籍名称
    private String chapter_name;//章节名称
    private Integer rank;       //人气
    private String host;        //主播
    private String pic;         //图片
    private String url;         //播放url
    private Long date;          //插入时间

    @Generated(hash = 770431242)
    public DBListenHistory(Long id, @NotNull Long duration, Long total,
                           Integer bookid, Integer cid, Integer position, String bookname,
                           String chapter_name, Integer rank, String host, String pic, String url,
                           Long date) {
        this.id = id;
        this.duration = duration;
        this.total = total;
        this.bookid = bookid;
        this.cid = cid;
        this.position = position;
        this.bookname = bookname;
        this.chapter_name = chapter_name;
        this.rank = rank;
        this.host = host;
        this.pic = pic;
        this.url = url;
        this.date = date;
    }

    @Generated(hash = 812535113)
    public DBListenHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDuration() {
        return this.duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getBookid() {
        return this.bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public Integer getCid() {
        return this.cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getBookname() {
        return this.bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getChapter_name() {
        return this.chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public Integer getRank() {
        return this.rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDate() {
        return this.date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

}
