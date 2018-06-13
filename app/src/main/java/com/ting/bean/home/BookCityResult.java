package com.ting.bean.home;

import com.ting.bean.BaseResult;
import com.ting.bean.Slider;

import java.util.List;

/**
 * Created by liu on 16/6/28.
 */
public class BookCityResult extends BaseResult{
    private List<Slider> slider;
    private List<BookCityVO> hotlist;
    private List<BookCityVO> lastUpdate;
    private BookCityCategory categorylist;

    public List<Slider> getSlider() {
        return slider;
    }

    public void setSlider(List<Slider> slider) {
        this.slider = slider;
    }

    public List<BookCityVO> getHotlist() {
        return hotlist;
    }

    public void setHotlist(List<BookCityVO> hotlist) {
        this.hotlist = hotlist;
    }

    public List<BookCityVO> getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(List<BookCityVO> lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public BookCityCategory getCategorylist() {
        return categorylist;
    }

    public void setCategorylist(BookCityCategory categorylist) {
        this.categorylist = categorylist;
    }
}
