package com.ting.bean.anchor;

import com.ting.bean.BaseResult;
import com.ting.bean.Work;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/22.
 * 主播信息返回结果集
 */
public class AnchorMessResult extends BaseResult {
    private String thumb;//主播头像URL
    private String name;//主播名称
    private String declaration;//主播心情语
    private int viewd;//主播人气
    private boolean followed;// 制定的用户是否关注
    private Work works;//主播作品
    private Fans fans;//粉丝
    private Reward reward;//打赏排名
    private String rank_id;
    private String rankname;//主播等级名称
    private List<ListenBookVO> tingshuka;


    public String getRankname() {
        return rankname;
    }

    public void setRankname(String rankname) {
        this.rankname = rankname;
    }

    public String getRank_id() {
        return rank_id;
    }

    public void setRank_id(String rank_id) {
        this.rank_id = rank_id;
    }

    public List<ListenBookVO> getTingshuka() {
        return tingshuka;
    }

    public void setTingshuka(List<ListenBookVO> tingshuka) {
        this.tingshuka = tingshuka;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Fans getFans() {
        return fans;
    }

    public void setFans(Fans fans) {
        this.fans = fans;
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

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public int getViewd() {
        return viewd;
    }

    public void setViewd(int viewd) {
        this.viewd = viewd;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public Work getWorks() {
        return works;
    }

    public void setWorks(Work works) {
        this.works = works;
    }
}
