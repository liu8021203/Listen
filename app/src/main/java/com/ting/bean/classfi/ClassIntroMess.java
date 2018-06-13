package com.ting.bean.classfi;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/24.
 * 分类之详情信息
 */
public class ClassIntroMess extends BaseParam {
    private int id;//作品ID
    private  String title;//作品的标题
    private String thumb;//作品的封面URL
    private int viewd;//人气
    private String author;//作者
    private String category;//分配名称
    private int lastUpdate;//最后更新集数

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

    public int getViewd() {
        return viewd;
    }

    public void setViewd(int viewd) {
        this.viewd = viewd;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
