package com.ting.bean.home;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by liu on 2017/8/3.
 */

public class HomeHotAnchorResult extends BaseResult{
    private int lenght;
    private int page;
    private int count;
    private List<HotAnchorVO> data;

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

    public List<HotAnchorVO> getData() {
        return data;
    }

    public void setData(List<HotAnchorVO> data) {
        this.data = data;
    }
}
