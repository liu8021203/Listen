package com.ting.bean;

import com.ting.bean.vo.ExpenseVO;

import java.util.List;

public class ExpenseResult {
    private int page;
    private int size;
    private int count;
    private List<ExpenseVO> list;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ExpenseVO> getList() {
        return list;
    }

    public void setList(List<ExpenseVO> list) {
        this.list = list;
    }
}
