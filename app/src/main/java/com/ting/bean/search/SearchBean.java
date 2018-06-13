package com.ting.bean.search;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/10/15.
 * 搜索结果集属性
 */
public class SearchBean extends BaseParam{
    private int bookID;//作品ID
    private String title;//作品的标题

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return bookID;
    }

    public void setId(int id) {
        this.bookID = id;
    }
}
