package com.ting.bean.myself;

import com.ting.bean.BaseResult;
import com.ting.bean.myself.MySeeInfo;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/29.
 * 我的关注结果集合
 */
public class MySeeResult extends BaseResult {
    private int lenght;
    private int page;
    private int count;
    private List<MySeeInfo> data;


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


    public List<MySeeInfo> getData() {
        return data;
    }

    public void setData(List<MySeeInfo> data) {
        this.data = data;
    }
}
