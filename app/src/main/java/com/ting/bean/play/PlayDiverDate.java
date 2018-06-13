package com.ting.bean.play;

import com.ting.bean.base.BaseParam;

import java.util.List;

/**
 * Created by gengjiajia on 15/10/8.
 * 分集数组
 */
public class PlayDiverDate extends BaseParam {
    private int lenght;//分级数量
    private int page;//分级当前页数
    private int count;//分页每页数量
    private List<PlayingVO> data;//分集详情;

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

    public List<PlayingVO> getData() {
        return data;
    }

    public void setData(List<PlayingVO> data) {
        this.data = data;
    }
}
