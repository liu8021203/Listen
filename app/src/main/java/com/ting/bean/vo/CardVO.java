package com.ting.bean.vo;

public class CardVO {


    /**
     * id : 8
     * cardPrice : 100
     * cardMonth : 1
     * cardDesc : 1元1个月
     */

    private String id;
    private String cardPrice;
    private String cardMonth;
    private String cardDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(String cardPrice) {
        this.cardPrice = cardPrice;
    }

    public String getCardMonth() {
        return cardMonth;
    }

    public void setCardMonth(String cardMonth) {
        this.cardMonth = cardMonth;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }
}
