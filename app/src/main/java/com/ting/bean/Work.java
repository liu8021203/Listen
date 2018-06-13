package com.ting.bean;

import com.ting.bean.base.BaseParam;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/22.
 */
public class Work extends BaseParam {
    private int lenght;//作品数量
    private int page;//当前所在页数
    private int count;//每页条数
    private List<AnchorWork> data;//作品数组

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

    public List<AnchorWork> getData() {
        return data;
    }

    public void setData(List<AnchorWork> data) {
        this.data = data;
    }
}
