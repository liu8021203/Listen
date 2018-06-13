package com.ting.bean.anchor;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/22.
 * 粉丝信息
 */
public class FansMess extends BaseParam {
    private int id;//粉丝ID
    private String thumb;//粉丝头像URL
    private String name;//粉丝名称
    private boolean isFriend;//请求用户与该用户是否好友
    private FansHistoryVO history;//该用户访问历史

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public FansHistoryVO getHistory() {
        return history;
    }

    public void setHistory(FansHistoryVO history) {
        this.history = history;
    }
}
