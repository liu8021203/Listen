package com.ting.bean.play;

import com.ting.bean.BaseResult;

/**
 * Created by liu on 15/11/9.
 */
public class PayResult extends BaseResult{
    private PayVO blistinfo;

    public PayVO getBlistinfo() {
        return blistinfo;
    }

    public void setBlistinfo(PayVO blistinfo) {
        this.blistinfo = blistinfo;
    }
}
