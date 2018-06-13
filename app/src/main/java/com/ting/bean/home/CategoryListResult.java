package com.ting.bean.home;

import java.util.List;

/**
 * Created by liu on 16/7/28.
 */
public class CategoryListResult {
    private int lenght;
    private int page;
    private int count;
    private List<CategoryListVO> data;

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

    public List<CategoryListVO> getData() {
        return data;
    }

    public void setData(List<CategoryListVO> data) {
        this.data = data;
    }
}
