package com.ting.bean;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/21.
 * 热门主播信息
 */
public class Data extends BaseParam {
    private String id;//主播ID
    private String name;//主播名称
    private String thumb;//主播封面
    private boolean isFollowed;//请求用户是否关注
    private String history;
    private int focusFans;//粉丝关注数量
    private int worksCount;//作品数量
    private String  groupname;//主播等级

    public void setHistory(String history) {
        this.history = history;
    }

    public int getFocusFans() {
        return focusFans;
    }

    public void setFocusFans(int focusFans) {
        this.focusFans = focusFans;
    }

    public int getWorksCount() {
        return worksCount;
    }

    public void setWorksCount(int worksCount) {
        this.worksCount = worksCount;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getHistory() {
        return history;
    }

    public void String(String history) {
        this.history = history;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }


}
