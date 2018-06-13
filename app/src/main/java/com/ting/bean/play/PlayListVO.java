package com.ting.bean.play;

import android.os.Parcel;
import android.os.Parcelable;

import com.ting.db.DBChapter;

import java.util.List;

/**
 * Created by liu on 15/11/3.
 */
public class PlayListVO implements Parcelable {

    private List<PlayingVO> data;
    private List<DBChapter> offlineData;

    public List<PlayingVO> getData() {
        return data;
    }

    public void setData(List<PlayingVO> data) {
        this.data = data;
    }

    public List<DBChapter> getOfflineData() {
        return offlineData;
    }

    public void setOfflineData(List<DBChapter> offlineData) {
        this.offlineData = offlineData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
        dest.writeTypedList(this.offlineData);
    }

    public PlayListVO() {
    }

    protected PlayListVO(Parcel in) {
        this.data = in.createTypedArrayList(PlayingVO.CREATOR);
        this.offlineData = in.createTypedArrayList(DBChapter.CREATOR);
    }

    public static final Creator<PlayListVO> CREATOR = new Creator<PlayListVO>() {
        @Override
        public PlayListVO createFromParcel(Parcel source) {
            return new PlayListVO(source);
        }

        @Override
        public PlayListVO[] newArray(int size) {
            return new PlayListVO[size];
        }
    };
}
