package com.ting.bean.vo;

public class BuyChapterWayVO {
    //0：单集购买    1：全本购买   2：听书卡购买
    private int type;
    private String chapterId;
    private String bookId;
    private String cardId;
    private String desc;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
