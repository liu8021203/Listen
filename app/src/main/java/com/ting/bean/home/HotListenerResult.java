package com.ting.bean.home;

import com.ting.bean.BaseResult;
import com.ting.bean.BookInfo;
import com.ting.bean.Slider;

import java.util.List;

/**
 * Created by liu on 15/9/15.
 * 热听排行结果集
 */
public class HotListenerResult extends BaseResult {
    private Integer cacheDate;
    private List<Slider> slider;//首页轮播图
    private List<BookInfo> lastLargess;//最新打赏
    private List<BookInfo> lastUpdate;//最近更新

    public Integer getCacheDate() {
        return cacheDate;
    }

    public void setCacheDate(Integer cacheDate) {
        this.cacheDate = cacheDate;
    }

    public List<Slider> getSlider() {
        return slider;
    }

    public void setSlider(List<Slider> slider) {
        this.slider = slider;
    }

    public List<BookInfo> getLastLargess() {
        return lastLargess;
    }

    public void setLastLargess(List<BookInfo> lastLargess) {
        this.lastLargess = lastLargess;
    }

    public List<BookInfo> getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(List<BookInfo> lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
