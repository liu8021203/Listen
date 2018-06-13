package com.ting.bean.play;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gengjiajia on 15/10/8.
 * 评论详情
 */
public class CommentDate implements Serializable{

    /**
     * id : 111121
     * thumb : http://www.tingshijie.com/Uploads/20160701/1467345010.
     * name : 张强强
     * time : 1467368112
     * string : 1210
     * uid : 29582
     * usertype : 0
     * rankname : 声音爱好者
     * vip : false
     */

    private int id;
    private String thumb;
    private String name;
    private long time;
    private String string;
    private String uid;
    //0是普通用户，1是主播用户
    private int usertype;
    private String rankname;
    private boolean vip;
    private String reply_num;
    private List<ReplyVO> reply_list;

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

    public long getTime() {
        return time;
    }


    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public String getRankname() {
        return rankname;
    }

    public void setRankname(String rankname) {
        this.rankname = rankname;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getReply_num() {
        return reply_num;
    }

    public void setReply_num(String reply_num) {
        this.reply_num = reply_num;
    }

    public List<ReplyVO> getReply_list() {
        return reply_list;
    }

    public void setReply_list(List<ReplyVO> reply_list) {
        this.reply_list = reply_list;
    }
}
