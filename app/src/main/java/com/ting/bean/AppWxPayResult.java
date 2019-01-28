package com.ting.bean;

import com.google.gson.annotations.SerializedName;

public class AppWxPayResult {


    /**
     * appid : wx65ef305f9d0ebdec
     * noncestr : EXzMc86isQoAXffm
     * package : Sign=WXPay
     * partnerid : 1279609001
     * prepayid : wx09152144406773714fb7dd7b1858488222
     * timestamp : 1539069704
     * sign : F2B0170AC1BD145CB5C8833625F0726F
     */

    private String appid;
    private String noncestr;
    private String partnerid;
    private String prepayid;
    private int timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }


    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
