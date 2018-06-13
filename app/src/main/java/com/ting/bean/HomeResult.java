package com.ting.bean;

import com.ting.bean.home.BookListVO;
import com.ting.bean.home.HotAnchorVO;
import com.ting.bean.home.SpecialVO;
import com.ting.bean.home.SuperMarketVO;

import java.util.List;

/**
 * Created by liu on 2017/7/25.
 */

public class HomeResult extends BaseResult{
    private List<Slider> slider;
    private List<SuperMarketVO> supermarket;
    private List<HotAnchorVO> hotauthor;
    private BookListVO booklist;
    private List<SpecialVO> special;

    public List<Slider> getSlider() {
        return slider;
    }

    public void setSlider(List<Slider> slider) {
        this.slider = slider;
    }

    public List<SuperMarketVO> getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(List<SuperMarketVO> supermarket) {
        this.supermarket = supermarket;
    }

    public List<HotAnchorVO> getHotauthor() {
        return hotauthor;
    }

    public void setHotauthor(List<HotAnchorVO> hotauthor) {
        this.hotauthor = hotauthor;
    }

    public BookListVO getBooklist() {
        return booklist;
    }

    public void setBooklist(BookListVO booklist) {
        this.booklist = booklist;
    }

    public List<SpecialVO> getSpecial() {
        return special;
    }

    public void setSpecial(List<SpecialVO> special) {
        this.special = special;
    }
}
