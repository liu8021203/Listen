package com.ting.bean.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class ChapterListVO implements Parcelable {


    /**
     * chapterId : 897762
     * position : 1
     * title : 001
     * price : 0
     * time : 00:00
     * url : http://2016.tingshijie.com/2016-05-12/HSXNM/001.mp3
     * uploadDate : 2017.06.22
     * bookTitle : 混世小农民刘忙-高音质版
     * bookId : 5117
     * bookImage : http://h.tingshijie.com/20140123/1390449745.jpg
     * bookHost : 刘忙
     */

    private String chapterId;
    private int position;
    private String title;
    private String price;
    private String time;
    private String url;
    private String uploadDate;
    private String bookTitle;
    private String bookId;
    private String bookImage;
    private String bookHost;

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookHost() {
        return bookHost;
    }

    public void setBookHost(String bookHost) {
        this.bookHost = bookHost;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chapterId);
        dest.writeInt(this.position);
        dest.writeString(this.title);
        dest.writeString(this.price);
        dest.writeString(this.time);
        dest.writeString(this.url);
        dest.writeString(this.uploadDate);
        dest.writeString(this.bookTitle);
        dest.writeString(this.bookId);
        dest.writeString(this.bookImage);
        dest.writeString(this.bookHost);
    }

    public ChapterListVO() {
    }

    protected ChapterListVO(Parcel in) {
        this.chapterId = in.readString();
        this.position = in.readInt();
        this.title = in.readString();
        this.price = in.readString();
        this.time = in.readString();
        this.url = in.readString();
        this.uploadDate = in.readString();
        this.bookTitle = in.readString();
        this.bookId = in.readString();
        this.bookImage = in.readString();
        this.bookHost = in.readString();
    }

    public static final Creator<ChapterListVO> CREATOR = new Creator<ChapterListVO>() {
        @Override
        public ChapterListVO createFromParcel(Parcel source) {
            return new ChapterListVO(source);
        }

        @Override
        public ChapterListVO[] newArray(int size) {
            return new ChapterListVO[size];
        }
    };
}
