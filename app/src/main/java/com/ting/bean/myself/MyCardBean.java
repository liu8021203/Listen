package com.ting.bean.myself;

/**
 * Created by gengjiajia on 16/8/6.
 * 我的听书卡返回结果集
 */
public class MyCardBean {

    /**
     * username : inditest
     * thumb : http://test.test.com/Uploads/201605/57426ccc14e30.jpg
     * createtime : 2016.06.26
     * expire_time : 2018.09.23
     * type : 月
     */
    private String zhubo_uid;
    private String username;
    private String thumb;
    private String createtime;
    private String expire_time;
    private String type;

    public String getZhubo_uid() {
        return zhubo_uid;
    }

    public void setZhubo_uid(String zhubo_uid) {
        this.zhubo_uid = zhubo_uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
