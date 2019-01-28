package com.ting.bean;

import com.ting.bean.vo.BookVO;
import com.ting.bean.vo.HostVO;

import java.util.List;

public class AppSearchResult {

    private List<HostVO> hostData;
    private List<BookVO> bookData;

    public List<HostVO> getHostData() {
        return hostData;
    }

    public void setHostData(List<HostVO> hostData) {
        this.hostData = hostData;
    }

    public List<BookVO> getBookData() {
        return bookData;
    }

    public void setBookData(List<BookVO> bookData) {
        this.bookData = bookData;
    }
}
