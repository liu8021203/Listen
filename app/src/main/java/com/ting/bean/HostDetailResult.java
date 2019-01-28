package com.ting.bean;

import com.ting.bean.vo.BookVO;
import com.ting.bean.vo.GiftDetailVO;
import com.ting.bean.vo.HostVO;

import java.util.List;

public class HostDetailResult {
    private HostVO hostData;
    private List<BookVO> bookData;
    private List<GiftDetailVO> giftData;


    public HostVO getHostData() {
        return hostData;
    }

    public void setHostData(HostVO hostData) {
        this.hostData = hostData;
    }

    public List<BookVO> getBookData() {
        return bookData;
    }

    public void setBookData(List<BookVO> bookData) {
        this.bookData = bookData;
    }

    public List<GiftDetailVO> getGiftData() {
        return giftData;
    }

    public void setGiftData(List<GiftDetailVO> giftData) {
        this.giftData = giftData;
    }
}
