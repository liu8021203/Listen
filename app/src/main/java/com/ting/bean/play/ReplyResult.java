package com.ting.bean.play;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by liu on 2017/6/27.
 */

public class ReplyResult extends BaseResult{
    private int lenght;
    private int page;
    private int count;
    private List<ReplyVO> data;

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

    public List<ReplyVO> getData() {
        return data;
    }

    public void setData(List<ReplyVO> data) {
        this.data = data;
    }
}
