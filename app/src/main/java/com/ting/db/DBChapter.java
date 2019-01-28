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
    private String bookId;
    private String chapterId;
    private String title;
    private String url;
    private int price;
    private Long size = 0L;
    private Long completeSize = 0L;
    private String time;
    //0：下载错误，1：开始下载， 2：下载进行中，3：下载暂停， 4：下载完成
    private Integer state;
    private String bookTitle;
    private String bookImage;
    private String bookHost;
    private int bookPrice;
    private Integer position;
    @Generated(hash = 2050317807)
    public DBChapter(Long id, @NotNull String bookId, String chapterId,
            String title, String url, int price, Long size, Long completeSize,
            String time, Integer state, String bookTitle, String bookImage,
            String bookHost, int bookPrice, Integer position) {
        this.id = id;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.title = title;
        this.url = url;
        this.price = price;
        this.size = size;
        this.completeSize = completeSize;
        this.time = time;
        this.state = state;
        this.bookTitle = bookTitle;
        this.bookImage = bookImage;
        this.bookHost = bookHost;
        this.bookPrice = bookPrice;
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
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public Long getSize() {
        return this.size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public Long getCompleteSize() {
        return this.completeSize;
    }
    public void setCompleteSize(Long completeSize) {
        this.completeSize = completeSize;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public Integer getState() {
        return this.state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getBookTitle() {
        return this.bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public String getBookImage() {
        return this.bookImage;
    }
    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
    public String getBookHost() {
        return this.bookHost;
    }
    public void setBookHost(String bookHost) {
        this.bookHost = bookHost;
    }
    public int getBookPrice() {
        return this.bookPrice;
    }
    public void setBookPrice(int bookPrice) {
        this.bookPrice = bookPrice;
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
        dest.writeString(this.bookId);
        dest.writeString(this.chapterId);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeInt(this.price);
        dest.writeValue(this.size);
        dest.writeValue(this.completeSize);
        dest.writeString(this.time);
        dest.writeValue(this.state);
        dest.writeString(this.bookTitle);
        dest.writeString(this.bookImage);
        dest.writeString(this.bookHost);
        dest.writeInt(this.bookPrice);
        dest.writeValue(this.position);
    }

    protected DBChapter(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.bookId = in.readString();
        this.chapterId = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.price = in.readInt();
        this.size = (Long) in.readValue(Long.class.getClassLoader());
        this.completeSize = (Long) in.readValue(Long.class.getClassLoader());
        this.time = in.readString();
        this.state = (Integer) in.readValue(Integer.class.getClassLoader());
        this.bookTitle = in.readString();
        this.bookImage = in.readString();
        this.bookHost = in.readString();
        this.bookPrice = in.readInt();
        this.position = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<DBChapter> CREATOR = new Creator<DBChapter>() {
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
