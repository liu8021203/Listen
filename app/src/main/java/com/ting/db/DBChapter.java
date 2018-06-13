package com.ting.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by liu on 2017/7/25.
 */
@Entity
public class DBChapter implements Parcelable {
    @Id
    private Long id;

    @NotNull
    private int bookId;
    private int chapterId;
    private String chapterTitle;
    private String chapterUrl;
    private Long rate;
    private Long size = 0L;
    private Long completesize = 0L;
    //0：下载错误，1：开始下载， 2：下载进行中，3：下载暂停， 4：下载完成
    private Integer state;
    private String bookName;
    private String bookUrl;
    private String host;
    private Integer sort;
    private Integer position;
    @Generated(hash = 1696386409)
    public DBChapter(Long id, int bookId, int chapterId, String chapterTitle,
            String chapterUrl, Long rate, Long size, Long completesize,
            Integer state, String bookName, String bookUrl, String host,
            Integer sort, Integer position) {
        this.id = id;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
        this.chapterUrl = chapterUrl;
        this.rate = rate;
        this.size = size;
        this.completesize = completesize;
        this.state = state;
        this.bookName = bookName;
        this.bookUrl = bookUrl;
        this.host = host;
        this.sort = sort;
        this.position = position;
    }
    @Generated(hash = 1028277234)
    public DBChapter() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getBookId() {
        return this.bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public int getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }
    public String getChapterTitle() {
        return this.chapterTitle;
    }
    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }
    public String getChapterUrl() {
        return this.chapterUrl;
    }
    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }
    public Long getRate() {
        return this.rate;
    }
    public void setRate(Long rate) {
        this.rate = rate;
    }
    public Long getSize() {
        return this.size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public Long getCompletesize() {
        return this.completesize;
    }
    public void setCompletesize(Long completesize) {
        this.completesize = completesize;
    }
    public Integer getState() {
        return this.state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookUrl() {
        return this.bookUrl;
    }
    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }
    public String getHost() {
        return this.host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public Integer getSort() {
        return this.sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getPosition() {
        return this.position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeInt(this.bookId);
        dest.writeInt(this.chapterId);
        dest.writeString(this.chapterTitle);
        dest.writeString(this.chapterUrl);
        dest.writeValue(this.rate);
        dest.writeValue(this.size);
        dest.writeValue(this.completesize);
        dest.writeValue(this.state);
        dest.writeString(this.bookName);
        dest.writeString(this.bookUrl);
        dest.writeString(this.host);
        dest.writeValue(this.sort);
        dest.writeValue(this.position);
    }

    protected DBChapter(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.bookId = in.readInt();
        this.chapterId = in.readInt();
        this.chapterTitle = in.readString();
        this.chapterUrl = in.readString();
        this.rate = (Long) in.readValue(Long.class.getClassLoader());
        this.size = (Long) in.readValue(Long.class.getClassLoader());
        this.completesize = (Long) in.readValue(Long.class.getClassLoader());
        this.state = (Integer) in.readValue(Integer.class.getClassLoader());
        this.bookName = in.readString();
        this.bookUrl = in.readString();
        this.host = in.readString();
        this.sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.position = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<DBChapter> CREATOR = new Parcelable.Creator<DBChapter>() {
        @Override
        public DBChapter createFromParcel(Parcel source) {
            return new DBChapter(source);
        }

        @Override
        public DBChapter[] newArray(int size) {
            return new DBChapter[size];
        }
    };
}
