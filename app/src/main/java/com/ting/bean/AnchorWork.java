package com.ting.bean;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/22.
 * 主播作品
 */
public class AnchorWork extends BaseParam {
    private int id;//作品 ID
    private String thumb;//作品封面URL
    private String title;//作品标题
    private int listened;//收听数量
    private int lastUpdate;//最后更新时间

    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getListened() {
        return listened;
    }

    public void setListened(int listened) {
        this.listened = listened;
    }
}
