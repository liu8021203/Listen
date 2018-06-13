package com.ting.bean.anchor;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liu on 16/8/5.
 */
public class ListenBookVO {

    /**
     * id : 65
     * username : 刺ル
     * createtime : 1467339467
     * price : 20000
     * default : 1
     * url : http://www.tingshijie.com/index.php?s=User/Broadcast/redeemVIP&vipID=65
     * tips : 我会努力更新的！感谢各位的支持
     * type : 年卡
     * yuanprice : 20000
     * title : 刺儿听书
     * thumb : http://www.tingshijie.com/Uploads/201607/5783a4139a372.jpg
     * timelimit : 0
     * timelimitval : 1470837005
     * timeoutcancel : 0
     * zhekou : 10
     * nums : 1
     */

    private String id;
    private String username;
    private String createtime;
    private String price;
    @SerializedName("default")
    private String defaultX;
    private String url;
    private String tips;
    private String type;
    private String yuanprice;
    private String title;
    private String thumb;
    private String timelimit;
    private String timelimitval;
    private String timeoutcancel;
    private String zhekou;
    private String nums;

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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(String defaultX) {
        this.defaultX = defaultX;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYuanprice() {
        return yuanprice;
    }

    public void setYuanprice(String yuanprice) {
        this.yuanprice = yuanprice;
    }

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

    public String getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(String timelimit) {
        this.timelimit = timelimit;
    }

    public String getTimelimitval() {
        return timelimitval;
    }

    public void setTimelimitval(String timelimitval) {
        this.timelimitval = timelimitval;
    }

    public String getTimeoutcancel() {
        return timeoutcancel;
    }

    public void setTimeoutcancel(String timeoutcancel) {
        this.timeoutcancel = timeoutcancel;
    }

    public String getZhekou() {
        return zhekou;
    }

    public void setZhekou(String zhekou) {
        this.zhekou = zhekou;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }
}
