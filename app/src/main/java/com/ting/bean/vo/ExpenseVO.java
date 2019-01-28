package com.ting.bean.vo;

public class ExpenseVO {
    /**
     * price : 100
     * type : 6
     * date : 2018-10-10 16:48:36
     * orderDesc : 充值:5元100听豆
     */

    private String price;
    private int type;
    private String date;
    private String orderDesc;
    private String orderExpense;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderExpense() {
        return orderExpense;
    }

    public void setOrderExpense(String orderExpense) {
        this.orderExpense = orderExpense;
    }
}
