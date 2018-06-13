package com.ting.bean.play;

import java.io.Serializable;

/**
 * Created by liu on 2017/6/24.
 */

public class ReplyVO implements Serializable{

    /**
     * id : 114912
     * username : 乖乖虎
     * content : 请问如何充值 <a href="www.tingshijie.com/icenter-7.html">@听世界工作室</a>:该作品评论暂时关闭，催更，需要联系主播的欢迎加入刺儿粉丝4群，群号码：483047192。收听，充值，兑换会员中有问题的请联系客服:769991514 主播目前本月比较忙，请耐心等待
     * target_id : 7014
     * createtime : 1490708732
     * tp_ding : 0
     * tp_cai : 0
     * parentid : 113093
     * ip : 43.227.137.22
     * istop : 0
     */

    private String id;
    private String username;
    private String content;
    private String target_id;
    private String createtime;
    private String tp_ding;
    private String tp_cai;
    private String parentid;
    private String ip;
    private String istop;
    private String thumb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTp_ding() {
        return tp_ding;
    }

    public void setTp_ding(String tp_ding) {
        this.tp_ding = tp_ding;
    }

    public String getTp_cai() {
        return tp_cai;
    }

    public void setTp_cai(String tp_cai) {
        this.tp_cai = tp_cai;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIstop() {
        return istop;
    }

    public void setIstop(String istop) {
        this.istop = istop;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
