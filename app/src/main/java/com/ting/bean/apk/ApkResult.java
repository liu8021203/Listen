package com.ting.bean.apk;

import com.ting.bean.BaseResult;

/**
 * Created by liu on 16/9/26.
 */

public class ApkResult extends BaseResult{
    private ApkBean data;

    public ApkBean getData() {
        return data;
    }

    public void setData(ApkBean data) {
        this.data = data;
    }
}
