package com.ting.bean;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/22.
 * 推荐
 */
public class Recommend extends BaseParam {
    private int id;//作品ID
    private String thumb;//作品封面URL
    private String title;//作品标题
    private String author;//作者
    private String category;//分配名称
    private int lastUpdate;//最后更新时间戳
    private float diffcult;//难度
    private boolean status;//状态true连播，false完结

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

    public float getDiffcult() {
        return diffcult;
    }

    public void setDiffcult(float diffcult) {
        this.diffcult = diffcult;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
