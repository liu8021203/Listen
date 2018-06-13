package com.ting.bean.myself;

import com.ting.bean.BaseResult;
import com.ting.bean.myself.DouPayBean;

import java.util.List;

/**
 * Created by gengjiajia on 15/9/29.
 * 我的豆豆结果集
 */
public class MyDouResult extends BaseResult {
    private int balance;//用户余额
    private List<DouPayBean> type;//充值选项

    public List<DouPayBean> getType() {
        return type;
    }

    public void setType(List<DouPayBean> type) {
        this.type = type;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
