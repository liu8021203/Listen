package com.ting.bean.search;

/**
 * Created by gengjiajia on 15/10/15.
 * 搜索信息属性
 */
public class SearchMess {
    private int id;//作品ID
    private String title;//作品的标题
    private String thumb;//作品的封面URL
    private int count;//最新的章数
    private int viewd;//人气

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getViewd() {
        return viewd;
    }

    public void setViewd(int viewd) {
        this.viewd = viewd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
