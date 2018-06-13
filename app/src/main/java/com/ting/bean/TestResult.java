package com.ting.bean;

import java.util.List;

/**
 * Created by liu on 15/9/15.
 */
public class TestResult extends BaseResult{
    private Integer cacheDate;
    private List<Slider> slider;
    private List<BookInfo> lastLargess;
    private List<BookInfo> lastUpdate;

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
