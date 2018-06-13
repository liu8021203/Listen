package com.ting.bean.anchor;

import com.ting.bean.base.BaseParam;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/22.
 * 粉丝
 */
public class Fans extends BaseParam {
    private int lenght;//粉丝数量
    private int page;//当前所在页数
    private int count;//当前每页条数
    private List<FansMess> data;//粉丝数组
    public int getLenght() {
        return lenght;
    }

    public List<FansMess> getData() {
        return data;
    }

    public void setData(List<FansMess> data) {
        this.data = data;
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
