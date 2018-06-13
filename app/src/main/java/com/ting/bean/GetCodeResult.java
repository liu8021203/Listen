package com.ting.bean;

import com.ting.bean.BaseResult;

/**
 * Created by gengjiajia on 15/9/17.
 * 获得验证码结果集
 */
public class GetCodeResult extends BaseResult {
    private String captcha;//验证码

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
