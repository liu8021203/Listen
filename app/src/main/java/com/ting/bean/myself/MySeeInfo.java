package com.ting.bean.myself;

import com.ting.bean.base.BaseParam;

/**
 * Created by gengjiajia on 15/9/29.
 */
public class MySeeInfo extends BaseParam {
    private int id;//粉丝ID
    private String thumb;//粉丝头像URL
    private String name;//粉丝名称
    //    private boolean isFriend;//请求用户与该用户是否好友;
    private String tips;//该用户访问历史
    private String rankname;
    private SeeAnchorInfo lastupdate;

    public SeeAnchorInfo getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(SeeAnchorInfo lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getRankname() {
        return rankname;
    }

    public void setRankname(String rankname) {
        this.rankname = rankname;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
