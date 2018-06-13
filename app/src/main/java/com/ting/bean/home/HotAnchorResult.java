package com.ting.bean.home;

import com.ting.bean.BaseResult;
import com.ting.bean.Data;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/21.
 * 热门主播返回结果集
 */
public class HotAnchorResult extends BaseResult {
    private List<Data> activedata;
    private List<Data> hotdata;

    public List<Data> getHotdata() {
        return hotdata;
    }

    public void setHotdata(List<Data> hotdata) {
        this.hotdata = hotdata;
    }

    public List<Data> getActivedata() {
        return activedata;
    }

    public void setActivedata(List<Data> activedata) {
        this.activedata = activedata;
    }
}
