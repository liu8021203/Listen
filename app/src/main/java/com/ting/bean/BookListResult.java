package com.ting.bean;

import com.ting.bean.vo.BookVO;

import java.util.List;

public class BookListResult {
    private int page;
    private int size;
    private int count;
    private List<BookVO> list;

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

    public List<BookVO> getList() {
        return list;
    }

    public void setList(List<BookVO> list) {
        this.list = list;
    }
}
