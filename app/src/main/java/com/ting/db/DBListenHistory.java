package com.ting.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liu on 2017/7/25.
 */

@Entity
public class DBListenHistory {
    @Id
    private Long id;

    @NotNull
    private Long duration;   //时间
    private Long total;         //总时间
    private String bookId;     //书籍id
    private String chapterId;        //章节id
    private Integer position;   //章节序号
    private String bookTitle;    //书籍名称
    private String chapterTitle;//章节名称
    private String bookHost;        //主播
    private String bookImage;         //图片
    private String chapterUrl;         //播放url
    private Long systemTime;     //插入时间
    @Generated(hash = 1878333219)
    public DBListenHistory(Long id, @NotNull Long duration, Long total,
            String bookId, String chapterId, Integer position, String bookTitle,
            String chapterTitle, String bookHost, String bookImage,
            String chapterUrl, Long systemTime) {
        this.id = id;
        this.duration = duration;
        this.total = total;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.position = position;
        this.bookTitle = bookTitle;
        this.chapterTitle = chapterTitle;
        this.bookHost = bookHost;
        this.bookImage = bookImage;
        this.chapterUrl = chapterUrl;
        this.systemTime = systemTime;
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
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
    public Integer getPosition() {
        return this.position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }
    public String getBookTitle() {
        return this.bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public String getChapterTitle() {
        return this.chapterTitle;
    }
    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }
    public String getBookHost() {
        return this.bookHost;
    }
    public void setBookHost(String bookHost) {
        this.bookHost = bookHost;
    }
    public String getBookImage() {
        return this.bookImage;
    }
    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
    public String getChapterUrl() {
        return this.chapterUrl;
    }
    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }
    public Long getSystemTime() {
        return this.systemTime;
    }
    public void setSystemTime(Long systemTime) {
        this.systemTime = systemTime;
    }

    
    

}
