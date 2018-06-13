package com.ting.bean;

import com.ting.bean.base.BaseParam;

/**
 * Created by liu on 15/9/16.
 */
public class BookInfo extends BaseParam {
    private Integer id;
    private String thumb;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
