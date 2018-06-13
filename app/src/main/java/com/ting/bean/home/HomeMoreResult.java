package com.ting.bean.home;

import com.ting.bean.BaseResult;
import com.ting.bean.BookInfo;

import java.util.List;

/**
 * Created by gengjiajia on 15/11/2.
 * 首页更多返回结果集
 */
public class HomeMoreResult extends BaseResult {
    private int cacheDate;

    private int lenght;
    private List<BookInfo> data;

    public int getCacheDate() {
        return cacheDate;
    }

    public void setCacheDate(int cacheDate) {
        this.cacheDate = cacheDate;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public List<BookInfo> getData() {
        return data;
    }

    public void setData(List<BookInfo> data) {
        this.data = data;
    }
}
