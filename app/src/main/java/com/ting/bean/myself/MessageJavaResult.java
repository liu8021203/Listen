package com.ting.bean.myself;

import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by liu on 2018/2/1.
 */

public class MessageJavaResult extends BaseResult{
    private int lenght;
    private int page;
    private int count;
    private List<MessageJavaVO> data;

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

    public List<MessageJavaVO> getData() {
        return data;
    }

    public void setData(List<MessageJavaVO> data) {
        this.data = data;
    }
}
