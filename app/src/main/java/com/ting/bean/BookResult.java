package com.ting.bean;

import com.ting.bean.vo.BookDataVO;
import com.ting.bean.vo.CardVO;
import com.ting.bean.vo.CommentDataVO;
import com.ting.bean.vo.HostInfoVO;

import java.util.List;

public class BookResult {
    private CommentDataVO commentData;
    private BookDataVO bookData;
    private HostInfoVO hostInfo;
    private List<CardVO> cardData;


    public CommentDataVO getCommentData() {
        return commentData;
    }

    public void setCommentData(CommentDataVO commentData) {
        this.commentData = commentData;
    }

    public BookDataVO getBookData() {
        return bookData;
    }

    public void setBookData(BookDataVO bookData) {
        this.bookData = bookData;
    }

    public HostInfoVO getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(HostInfoVO hostInfo) {
        this.hostInfo = hostInfo;
    }


    public List<CardVO> getCardData() {
        return cardData;
    }

    public void setCardData(List<CardVO> cardData) {
        this.cardData = cardData;
    }
}
