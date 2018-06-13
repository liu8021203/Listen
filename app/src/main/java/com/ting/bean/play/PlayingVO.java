package com.ting.bean.play;

import android.os.Parcel;
import android.os.Parcelable;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/10/8.
 * 分集详情
 */
public class PlayingVO implements Parcelable {
    private int id;//分集的ID
    private String title;//分集标题
    private String url;//分集获取音频URL;
    private int price;
    private int position;
    private int download;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeInt(this.price);
        dest.writeInt(this.position);
        dest.writeInt(this.download);
    }

    public PlayingVO() {
    }

    protected PlayingVO(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.price = in.readInt();
        this.position = in.readInt();
        this.download = in.readInt();
    }

    public static final Creator<PlayingVO> CREATOR = new Creator<PlayingVO>() {
        @Override
        public PlayingVO createFromParcel(Parcel source) {
            return new PlayingVO(source);
        }

        @Override
        public PlayingVO[] newArray(int size) {
            return new PlayingVO[size];
        }
    };
}
