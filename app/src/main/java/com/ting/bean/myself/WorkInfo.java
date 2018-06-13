package com.ting.bean.myself;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/29.
 * 我的作品信息
 */
public class WorkInfo extends BaseParam {
    private int id;//作品ID
    private String title;//作品的标题
    private String thumb;//作品的封面URL
    private int listened;//收听人数
    private int viewd;//人气
    private int lastUpdate;//最后更新时间戳

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getListened() {
        return listened;
    }

    public void setListened(int listened) {
        this.listened = listened;
    }

    public int getViewd() {
        return viewd;
    }

    public void setViewd(int viewd) {
        this.viewd = viewd;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
