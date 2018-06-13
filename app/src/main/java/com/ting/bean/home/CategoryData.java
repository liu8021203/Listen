package com.ting.bean.home;

import com.ting.bean.home.BookCityVO;

import java.util.List;

/**
 * Created by liu on 16/7/28.
 */
public class CategoryData {
    private List<BookCityVO> data;
    private int catid;

    public List<BookCityVO> getData() {
        return data;
    }

    public void setData(List<BookCityVO> data) {
        this.data = data;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }
}
