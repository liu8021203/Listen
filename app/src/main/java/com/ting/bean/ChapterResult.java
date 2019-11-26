package com.ting.bean;

import com.ting.bean.vo.CardListVO;
import com.ting.bean.vo.CardVO;
import com.ting.bean.vo.ChapterListVO;
import com.ting.db.DBChapter;

import java.util.List;

public class ChapterResult {
    private int pageNo;
    private int pageSize;
    private int count;
    private int bookDownloadStatus;
    private List<DBChapter> list;
    private List<CardVO> cardData;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DBChapter> getList() {
        return list;
    }

    public void setList(List<DBChapter> list) {
        this.list = list;
    }

    public List<CardVO> getCardData() {
        return cardData;
    }

    public void setCardData(List<CardVO> cardData) {
        this.cardData = cardData;
    }

    public int getBookDownloadStatus() {
        return bookDownloadStatus;
    }

    public void setBookDownloadStatus(int bookDownloadStatus) {
        this.bookDownloadStatus = bookDownloadStatus;
    }
}
