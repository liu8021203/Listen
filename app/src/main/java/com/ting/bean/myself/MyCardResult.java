package com.ting.bean.myself;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by gengjiajia on 16/8/6.
 * 我的听书卡返回结果集
 */
public class MyCardResult extends BaseResult {
    private String lenght;
    private int page;
    private int count;


    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
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

    private List<MyCardBean> data;

    public List<MyCardBean> getData() {
        return data;
    }

    public void setData(List<MyCardBean> data) {
        this.data = data;
    }
}
