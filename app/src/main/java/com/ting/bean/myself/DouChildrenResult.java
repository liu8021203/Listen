package com.ting.bean.myself;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by gengjiajia on 16/8/6.
 * 听豆明细返回结果集合
 */
public class DouChildrenResult extends BaseResult {


    /**
     * lenght : 3
     * page : 1
     * count : 10
     */

    private int lenght;
    private int page;
    private int count;
    private List<DouChildrenBean> data;

    public List<DouChildrenBean> getData() {
        return data;
    }

    public void setData(List<DouChildrenBean> data) {
        this.data = data;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
