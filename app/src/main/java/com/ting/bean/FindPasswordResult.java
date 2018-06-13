package com.ting.bean;

import com.ting.bean.BaseResult;

/**
 * Created by gengjiajia on 15/10/12.
 * 找回密码获取验证码结果集
 */
public class FindPasswordResult extends BaseResult {
    private String captcha;
    private int uid;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
