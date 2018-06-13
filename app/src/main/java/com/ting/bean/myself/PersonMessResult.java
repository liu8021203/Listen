package com.ting.bean.myself;

import com.ting.bean.BaseResult;

/**
 * Created by gengjiajia on 15/9/29.
 * 个人信息结果集
 */
public class PersonMessResult extends BaseResult{
    private String thumb;//用户头像URL
    private String name;//用户名称
    private int vipLevel;//用户VIP等级
    private int level;//用户等级
    private boolean isSignIn;//今日是否签到
    private String signature;//用户签名
    private int sexual;//用户性别1为男2为女
    private int type;//0为听迷1为主播;
    private String nickname;//昵称
    private int expire;//查询用户的VIP过期时间,UNIX时间戳
    private int applystatus;//成为主播


    public int getApplystatus() {
        return applystatus;
    }

    public void setApplystatus(int applystatus) {
        this.applystatus = applystatus;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getExpire() {
        return expire;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSexual() {
        return sexual;
    }

    public void setSexual(int sexual) {
        this.sexual = sexual;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setIsSignIn(boolean isSignIn) {
        this.isSignIn = isSignIn;
    }
}
